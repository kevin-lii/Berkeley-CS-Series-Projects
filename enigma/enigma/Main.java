package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Kevin Li
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine enigma = readConfig();
        while (_input.hasNextLine()) {
            String command = _input.nextLine();
            if (command.indexOf("*") != -1) {
                int index = command.indexOf('(');
                int remove = command.indexOf('*');
                if (index != -1) {
                    String plugs = command.substring(index);
                    Permutation plugBoard = new Permutation(plugs, _alphabet);
                    enigma.setPlugboard(plugBoard);
                    command = command.substring(0, index - 1);
                }
                command = command.substring(remove + 1);
                String[] rotors = command.trim().split("\\s+");
                enigma.insertRotors(
                        Arrays.copyOfRange(rotors, 0, rotors.length - 1));
                char[] array = rotors[rotors.length - 1].toCharArray();
                String set = "";
                for (char c : array) {
                    set += _alphabet.toChar((_alphabet.toInt(c) + dif)
                            % _alphabet.size());
                }
                enigma.setRotors(set);
            } else {
                if ((enigma.getRotorsUsed().equals(null)
                        || enigma.getRotorsUsed().size() == 0)
                        && command.trim().equals("")) {
                    continue;
                }
                if (enigma.getRotorsUsed().equals(null)
                        || enigma.getRotorsUsed().size() == 0) {
                    throw error("Must start with a setting line");
                }
                String line = enigma.convert(
                        command.replaceAll("\\s+", "").trim());
                printMessageLine(line);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int pawls = 0;
            int numRotor = 0;
            List<Rotor> rotors = new ArrayList<Rotor>();
            if (_config.hasNext()) {
                String alphabet = _config.nextLine().trim();
                if (alphabet.indexOf("-") != -1) {
                    String[] string = alphabet.split("\\-");
                    for (String s : string) {
                        for (char c : s.toCharArray()) {
                            if (!Character.isLetter(c) && !Character.isDigit(c)
                                    && !Character.isAlphabetic(c)) {
                                throw error("Alphabet is not valid");
                            }
                        }
                    }
                    _alphabet = new CharacterRange(alphabet.charAt(0),
                            alphabet.charAt(alphabet.length() - 1));
                } else {
                    int min = Integer.MAX_VALUE;
                    int max = Integer.MIN_VALUE;
                    char first = alphabet.charAt(0);
                    for (char a : alphabet.toCharArray()) {
                        if (!Character.isLetter(a) && !Character.isDigit(a)
                                && !Character.isAlphabetic(a)) {
                            throw error("Alphabet is not valid");
                        }
                        if ((int) a < min) {
                            min = (int) a;
                        }
                        if ((int) a > max) {
                            max = (int) a;
                        }
                    }
                    _alphabet = new CharacterRange((char) min, (char) max);
                    dif = (int) first - (int) min;
                }
            }
            if (_config.hasNext()) {
                numRotor = _config.nextInt();
                pawls = _config.nextInt();
                if (numRotor <= pawls) {
                    throw error("Rotors must be greater than pawls");
                }
            }
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotor, pawls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("NumRotors and pawls must be integers");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String nextLine = _config.nextLine();
            if (nextLine.trim().equals("")) {
                nextLine = _config.nextLine();
            }
            int index = nextLine.indexOf('(');
            String subString1 = nextLine.substring(0, index - 1);
            String subString2 = nextLine.substring(index);
            String[] rotorDet = subString1.trim().split("\\s+");
            if (rotorDet.length < 2) {
                throw error("Rotor is improperly formatted");
            }
            switch (rotorDet[1].charAt(0)) {
            case 'M':
                if (rotorDet[1].length() == 1) {
                    return new MovingRotor(rotorDet[0],
                            new Permutation(subString2, _alphabet), "");
                }
                String notch = "";
                for (char c : rotorDet[1].substring(1).toCharArray()) {
                    notch += _alphabet.toChar((_alphabet.toInt(c) + dif)
                            % _alphabet.size());
                }
                return new MovingRotor(rotorDet[0], new Permutation(
                        subString2, _alphabet), notch);
            case 'N':
                return new FixedRotor(rotorDet[0],
                        new Permutation(subString2, _alphabet));
            case 'R':
                if (_config.hasNext("\\(\\p{Print}\\p{Print}\\)")) {
                    subString2 += " " + _config.nextLine().trim();
                }
                return new Reflector(rotorDet[0],
                        new Permutation(subString2, _alphabet));
            default:
                throw error("NOT A VALID ROTOR TYPE");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String line = "";
        String part = "";
        char[] chars = msg.toCharArray();
        for (int i = 0; i < msg.length();) {
            for (int j = 0; j < 5 && i < msg.length(); j++, i++) {
                part += chars[i];
            }
            line += part + " ";
            part = "";
        }
        _output.print(line + "\n");
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** Difference in length. */
    private int dif = 0;
}
