package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/** The suite of all JUnit tests for the enigma package.
 *  @author Kevin Li
 */
public class UnitTest {
    public Machine createMachine() {
        Alphabet alphabet = new CharacterRange('A', 'Z');
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                        + "(IV) (JZ) (S)", alphabet), "C"));
        rotors.add(new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                        + "(GR) (NT) (A) (Q)", alphabet), "A"));
        rotors.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", alphabet), "E"));
        rotors.add(new FixedRotor("IV",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                        + "(HIX)", alphabet)));
        rotors.add(new MovingRotor("IVV", new Permutation("(AEPLIYWC"
                + "OXMRFZBSTGJQNH)(DV) (KU)", alphabet), "J"));
        rotors.add(new Reflector("V", new Permutation("(AE) (BN) (CK) "
                + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
                alphabet)));
        return new Machine(alphabet, 5, 3, rotors);
    }

    public Machine createAMachine() {
        Alphabet alphabet = new CharacterRange('A', 'Z');
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                        + "(DFG) (IV) (JZ) (S)", alphabet), "C"));
        rotors.add(new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                        + "(BJ) (GR) (NT) (A) (Q)", alphabet), "C"));
        rotors.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", alphabet), "C"));
        rotors.add(new FixedRotor("IV",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                        + "(HIX)", alphabet)));
        rotors.add(new Reflector("V", new Permutation("(AE) (BN) (CK) "
                + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
                alphabet)));
        return new Machine(alphabet, 5, 3, rotors);
    }

    public Machine createBigMachine() {
        Alphabet alphabet = new CharacterRange('A', 'Z');
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(new MovingRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                        + "(DFG) (IV) (JZ) (S)", alphabet), "C"));
        rotors.add(new MovingRotor("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) "
                        + "(BJ) (GR) (NT) (A) (Q)", alphabet), "C"));
        rotors.add(new MovingRotor("III",
                new Permutation("(ABDHPEJT) "
                        + "(CFLVMZOYQIRWUKXSG) (N)", alphabet), "C"));
        rotors.add(new FixedRotor("IV",
                new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
                        + "(HIX)", alphabet)));
        rotors.add(new Reflector("V", new Permutation("(AE) (BN) (CK) "
                + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
                alphabet)));
        rotors.add(new MovingRotor("VI",
                new Permutation("(AJQDVLEOZWIYTS) "
                        + "(CGMNHFUX) (BPRK) ", alphabet), "C"));
        rotors.add(new MovingRotor("VII",
                new Permutation("(AFLSETWUNDHOZVICQ) (BKJ) "
                        + "(GXY) (MPR)", alphabet), "C"));
        rotors.add(new MovingRotor("VIII",
                new Permutation("(ANOUPFRIMBZTLW"
                        + "KSVEGCJYDHXQ)", alphabet), "C"));
        return new Machine(alphabet, 8, 6, rotors);
    }

    public Machine createSpecMachine() {
        Alphabet alphabet = new CharacterRange('A', 'C');
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(new MovingRotor("I", new Permutation("", alphabet), "C"));
        rotors.add(new MovingRotor("II", new Permutation("", alphabet), "C"));
        rotors.add(new MovingRotor("III", new Permutation("", alphabet), "C"));
        rotors.add(new FixedRotor("IV", new Permutation("", alphabet)));
        rotors.add(new Reflector("V", new Permutation("", alphabet)));
        return new Machine(alphabet, 5, 3, rotors);
    }

    public Machine createBadMachine() {
        Alphabet alphabet = new CharacterRange('A', 'Z');
        ArrayList<Rotor> rotors = new ArrayList<>();
        rotors.add(new FixedRotor("I",
                new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) "
                        + "(IV) (JZ) (S)", alphabet)));
        rotors.add(new Reflector("II",
                new Permutation("(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) "
                        + "(GR) (NT) (A) (Q)", alphabet)));
        return new Machine(alphabet, 2, 1, rotors);
    }

    @Test
    /** Tests the edge case (most rotors on notches) with 8 rotors.
     * Also tests if getSetting works properly. */
    public void test8rotor() {
        Machine machine = createBigMachine();
        machine.insertRotors(
                new String[]{"V", "IV", "III", "II", "I", "VI", "VII", "VIII"});
        machine.setRotors("AACCCCA");
        machine.convert("e");
        assertEquals("AABDDDDB", machine.getSetting());
        assertEquals('B', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(7).setting()));
        assertEquals('D', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(6).setting()));
        assertEquals('D', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(5).setting()));
        assertEquals('D', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(4).setting()));
        assertEquals('D', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(3).setting()));
        assertEquals('B', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(2).setting()));
        assertEquals('A', machine.getRotorsUsed().get(3).alphabet()
                .toChar(machine.getRotorsUsed().get(1).setting()));
    }

    @Test
    /** Test if rotors rotate properly and whether all the
     * rotors will advance if all the rotors are on a notch.*/
    public void testEdgeCaseAllRotate() {
        Machine enigma = createMachine();
        enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
        enigma.setRotors("AEAC");
        assertEquals("VRTSENKAJP",
                enigma.convert("HelloWorld"));
        enigma.setRotors("AFBD");
        assertEquals("RTSENKAJP",
                enigma.convert("elloWorld"));
        enigma.setRotors("AFBE");
        assertEquals("TSENKAJP",
                enigma.convert("lloWorld"));

        enigma.setRotors("AEAB");
        assertEquals("PLARCYGEPU",
                enigma.convert("HelloWorld"));
        enigma.setRotors("AFBC");
        assertEquals("LARCYGEPU",
                enigma.convert("elloWorld"));
        enigma.setRotors("AFCD");
        assertEquals("ARCYGEPU",
                enigma.convert("lloWorld"));

        enigma.insertRotors(new String[] {"V", "IV", "III", "IVV", "I"});
        enigma.setRotors("AXJR");
        assertEquals("AAXJR", enigma.getSetting());

        enigma.convert("a");
        assertEquals("AAYKS", enigma.getSetting());
    }

    @Test
    /** Test if rotors rotate properly using a slightly
     * modified version of the four rotor example in the specs.*/
    public void test5Rotors() {
        Machine enigma = createAMachine();
        enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
        enigma.setRotors("AABC");
        assertEquals("XZEIHCZMG",
                enigma.convert("elloWorld"));
        enigma.setRotors("AACD");
        assertEquals("ZEIHCZMG",
                enigma.convert("lloWorld"));

        enigma.setRotors("AACA");
        assertEquals("APEGJJOR",
                enigma.convert("lloWorld"));
        enigma.setRotors("AACB");
        assertEquals("PEGJJOR",
                enigma.convert("loWorld"));

        enigma.setRotors("ABBC");
        assertEquals("RAOHKYBDYH",
                enigma.convert("HelloWorld"));
        enigma.setRotors("ABCD");
        assertEquals("AOHKYBDYH",
                enigma.convert("elloWorld"));
        enigma.setRotors("ACDE");
        assertEquals("OHKYBDYH",
                enigma.convert("lloWorld"));

        enigma.setRotors("ABCA");
        assertEquals("PSELREKQU",
                enigma.convert("elloWorld"));
        enigma.setRotors("ACDB");
        assertEquals("SELREKQU",
                enigma.convert("lloWorld"));
        enigma.setRotors("ACDC");
        assertEquals("ELREKQU",
                enigma.convert("loWorld"));

        enigma.setRotors("ACAC");
        assertEquals("KFYEAATBP",
                enigma.convert("elloWorld"));
        enigma.setRotors("ACBD");
        assertEquals("FYEAATBP",
                enigma.convert("lloWorld"));

        enigma.setRotors("ACBC");
        assertEquals("DGDTXFVDVE",
                enigma.convert("HelloWorld"));
        enigma.setRotors("ACCD");
        assertEquals("GDTXFVDVE",
                enigma.convert("elloWorld"));

        enigma.setRotors("ACCA");
        assertEquals("DDVYKUTVEC",
                enigma.convert("HelloWorld"));
        enigma.setRotors("ADDB");
        assertEquals("DVYKUTVEC",
                enigma.convert("elloWorld"));
    }

    @Test
    /** Tests if all the error commands work properly inside of machine */
    public void errors() {
        Machine enigma = createAMachine();
        try {
            enigma.insertRotors(new String[] {"IV", "III", "II", "I"});
        } catch (EnigmaException e) {
            assertEquals("First rotor must be a reflector", e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
            enigma.setRotors("AAA");
        } catch (EnigmaException e) {
            assertEquals("Setting is not of correct length", e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "I", "III", "II", "I"});
            enigma.setRotors("AAAA");
        } catch (EnigmaException e) {
            assertEquals("Pawls doesn't match the # of moving rotors",
                    e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "X", "III", "II", "I"});
            enigma.setRotors("AAAA");
        } catch (EnigmaException e) {
            assertEquals("Rotor does not exist", e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
            enigma.setRotors("AAAA");
            enigma.convert("Hel8oWorld");
        } catch (EnigmaException e) {
            assertEquals("Value is not part of alphabet", e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
            enigma.setRotors("AAA8");
        } catch (EnigmaException e) {
            assertEquals("Setting contains invalid value", e.getMessage());
        }
        try {
            enigma.insertRotors(new String[] {"V", "III", "II", "I"});
            enigma.setRotors("AAA8");
        } catch (EnigmaException e) {
            assertEquals("NumRotor doesn't match the "
                    + "# of rotors inserted", e.getMessage());
        }
        try {
            Machine badMachine = createBadMachine();
            badMachine.insertRotors(new String[] {"II", "I"});
        } catch (EnigmaException e) {
            assertEquals("Pawls doesn't match "
                    + "the # of moving rotors", e.getMessage());
        }

    }

    @Test
    /** Checks if the machine functions as shown in the spec.*/
    public void createSpecTest() {
        Machine enigma = createSpecMachine();
        enigma.insertRotors(new String[] {"V", "IV", "III", "II", "I"});
        enigma.setRotors("AAAB");
        assertEquals("AAAAB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAAAC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAABA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAABB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAABC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAACA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABAB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABAC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABBA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABBB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABBC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AABCA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACAB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACAC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACBA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACBB", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACBC", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AACCA", enigma.getSetting());
        enigma.convert("a");
        assertEquals("AAAAB", enigma.getSetting());
    }

    public Machine createLabMachine() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        Rotor[] machineRotors = {one, two, three, four};
        return new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
    }

    @Test
    public void testDoubleStep() {
        Machine mach = createLabMachine();
        String[] rotors = {"R1", "R2", "R3", "R4"};
        String setting = "AAA";
        mach.insertRotors(rotors);
        mach.setRotors(setting);
        assertEquals("AAAA", mach.getSetting());
        mach.convert("aaaa");
        assertEquals("AABA", mach.getSetting());
        mach.convert("aa");
        assertEquals("AABC", mach.getSetting());
        mach.convert('a');
        assertEquals("AACD", mach.getSetting());
        mach.convert('a');
        assertEquals("ABDA", mach.getSetting());
        mach.convert("aaaa");
        assertEquals("ABAA", mach.getSetting());
        mach.convert('a');
        assertEquals("ABAB", mach.getSetting());
        mach.convert('a');
        assertEquals("ABAC", mach.getSetting());
        mach.convert('a');
        assertEquals("ABBD", mach.getSetting());
        mach.convert('a');
        assertEquals("ABBA", mach.getSetting());
        mach.convert('a');
        assertEquals("ABBB", mach.getSetting());
        mach.convert('a');
        assertEquals("ABBC", mach.getSetting());
        mach.convert('a');
        assertEquals("ABCD", mach.getSetting());
        mach.convert('a');
        assertEquals("ACDA", mach.getSetting());
        mach.convert('a');
        assertEquals("ACDB", mach.getSetting());
        mach.convert('a');
        assertEquals("ACDC", mach.getSetting());
        mach.convert('a');
        assertEquals("ACAD", mach.getSetting());
        mach.convert('a');
        assertEquals("ACAA", mach.getSetting());
        mach.convert('a');
        assertEquals("ACAB", mach.getSetting());
        mach.convert('a');
        assertEquals("ACAC", mach.getSetting());
        mach.convert('a');
        assertEquals("ACBD", mach.getSetting());
        mach.convert('a');
        assertEquals("ACBA", mach.getSetting());
        mach.convert('a');
        assertEquals("ACBB", mach.getSetting());
        mach.convert('a');
        assertEquals("ACBC", mach.getSetting());
        mach.convert('a');
        assertEquals("ACCD", mach.getSetting());
        mach.convert('a');
        assertEquals("ADDA", mach.getSetting());
    }

    public Machine createHardDefaultHardMachine() {
        Alphabet alphabet = new CharacterRange('A', 'Z');
        List<Rotor> rotors = new ArrayList<>();
        rotors.add(new MovingRotor("V",
                new Permutation("(AVOLDRWFIUQ)"
                        + "(BZKSMNHYC) (EGTJPX)", alphabet), "Z"));
        rotors.add(new MovingRotor("VI",
                new Permutation("(AJQDVLEOZWIYTS) "
                        + "(CGMNHFUX) (BPRK)", alphabet), "ZM"));
        rotors.add(new MovingRotor("VII",
                new Permutation("(ANOUPFRIMBZTL"
                        + "WKSVEGCJYDHXQ) ", alphabet), "ZM"));
        rotors.add(new MovingRotor("VIII",
                new Permutation("(AFLSETWUNDHOZVICQ) "
                + "(BKJ) (GXY) (MPR)", alphabet), "ZM"));
        rotors.add(new Reflector("B", new Permutation("(AE) (BN) (CK) "
                + "(DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)",
                alphabet)));
        return new Machine(alphabet, 5, 4, rotors);
    }

    @Test
    /** Test that the number of pawls (3 vs. 4) effects the setting and
     * the result.*/
    public void testHardDefault() {
        Machine mHard = createHardDefaultHardMachine();
        mHard.insertRotors(new String[] {"B", "V", "VI", "VII", "VIII"});
        mHard.setRotors("MMMM");
        mHard.setPlugboard(new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                mHard.getRotorsUsed().get(1).alphabet()));
        assertEquals("AMMMM", mHard.getSetting());
        mHard.convert("F");
        assertEquals("ANNNN", mHard.getSetting());
        assertEquals("B", mHard.convert("R"));
        assertEquals("ANNNO", mHard.getSetting());
        assertEquals("M", mHard.convert("O"));

        mHard.setRotors("ZZZZ");
        assertEquals("AZZZZ", mHard.getSetting());
        mHard.convert("F");
        assertEquals("AAAAA", mHard.getSetting());

        mHard.setRotors("ZYYZ");
        assertEquals("AZYYZ", mHard.getSetting());
        mHard.convert("F");
        assertEquals("AZYZA", mHard.getSetting());

    }

    @Test
    /** Tests that results and settings are the same when first
     * rotor is a fixed rotor */
    public void weirdTest() {
        Machine m = createAMachine();
        m.insertRotors(new String[] {"V", "I", "II", "III", "IV"});
        m.setRotors("AAAA");
        assertEquals("AAAAA", m.getSetting());
        assertEquals("B", m.convert("A"));
        assertEquals("AAAAA", m.getSetting());
        assertEquals("B", m.convert("A"));
    }

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }
}
