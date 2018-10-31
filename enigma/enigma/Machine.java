package enigma;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Kevin Li
 */
class Machine {
    /** Number of rotors.*/
    private int _numRotors;
    /** Number of pawls.*/
    private int _pawls;
    /** Map that stores the name of the rotors to the rotor itself.*/
    private Map<String, Rotor> rotorss;
    /** List that stores the rotors used in the machine.*/
    private List<Rotor> rotorsUsed;
    /**Plugboard of the machine.*/
    private Permutation _plugboard;
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        this._numRotors = numRotors;
        this._pawls = pawls;
        rotorss = new HashMap<>();
        for (Rotor rotor : allRotors) {
            rotorss.put(rotor.name().toUpperCase(), rotor);
        }
        _plugboard = new Permutation("", _alphabet);
        rotorsUsed = new ArrayList<>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length > 0) {
            rotorsUsed.clear();
            int moving = 0;
            for (String rotor : rotors) {
                rotor = rotor.toUpperCase();
                if (!this.rotorss.containsKey(rotor)) {
                    throw error("Rotor does not exist");
                }
                if (rotorsUsed.contains(rotor)) {
                    throw error("Rotors cannot be used more than once");
                }
                if (this.rotorss.get(rotor).rotates()) {
                    moving += 1;
                }
                rotorsUsed.add(this.rotorss.get(rotor));
            }
            if (!rotorsUsed.get(0).reflecting()) {
                throw error("First rotor must be a reflector");
            }
            if (_numRotors != rotorsUsed.size()) {
                throw error("NumRotor doesn't match the # of rotors inserted");
            }
            if (numPawls() != moving) {
                throw error("Pawls doesn't match the # of moving rotors");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Setting is not of correct length");
        }
        char[] chars = setting.toCharArray();
        for (int i = 1; i < _numRotors; i++) {
            if (!_alphabet.contains(chars[i - 1])) {
                throw error("Setting contains invalid value");
            }
            rotorsUsed.get(i).set(chars[i - 1]);
        }
    }
    /**Getter of the list rotorUsed.
     * @return List<Rotor>*/
    List<Rotor> getRotorsUsed() {
        return rotorsUsed;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        c = _plugboard.permute(c);
        for (int i = rotorsUsed.size() - 1; i >= 0; i--) {
            rotorsUsed.get(i).atNotch();
        }
        if (rotorsUsed.get(rotorsUsed.size() - 1).rotates()) {
            rotorsUsed.get(rotorsUsed.size() - 1).advance();
        }
        for (int i = rotorsUsed.size() - 1; i >= 0; i--) {
            if (i - 1 >= numRotors() - numPawls()
                    && rotorsUsed.get(i).isOnNotch()
                    && rotorsUsed.get(i).advanced()
                    && rotorsUsed.get(i - 1).rotates()) {
                rotorsUsed.get(i - 1).advance();
            } else if (i - 1 >= numRotors() - numPawls()
                    && rotorsUsed.get(i).isOnNotch()
                    && rotorsUsed.get(i - 1).rotates()) {
                rotorsUsed.get(i - 1).advance();
                rotorsUsed.get(i).advance();
            }

            c = rotorsUsed.get(i).convertForward(c);
        }
        for (int i = 1; i < rotorsUsed.size(); i++) {
            c = rotorsUsed.get(i).convertBackward(c);
            rotorsUsed.get(i).setAdvanced(false);
        }
        c = _plugboard.invert(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.toUpperCase();
        String message = "";
        for (char partMsg : msg.toCharArray()) {
            if (!_alphabet.contains(partMsg)) {
                throw error("Value is not part of alphabet");
            }
            message += _alphabet.toChar(convert(_alphabet.toInt(partMsg)));
        }
        return message;
    }
    /**Returns the complete setting of the machine. TESTING PURPOSES*/
    /** @return String*/
    String getSetting() {
        String setting = "";
        for (Rotor r : rotorsUsed) {
            setting += _alphabet.toChar(r.setting());
        }
        return setting;
    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

}
