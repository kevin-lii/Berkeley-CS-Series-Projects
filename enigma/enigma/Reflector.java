package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Kevin Li
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }
    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position", posn);
        }
    }
    @Override
    boolean reflecting() {
        return true;
    }
    @Override
    int convertBackward(int e) {
        throw error("You cannot convert backwards", e);
    }


}
