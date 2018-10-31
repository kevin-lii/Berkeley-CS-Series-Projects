package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Kevin Li
 */
class MovingRotor extends Rotor {
    /** Stores the rotor's notch(es).*/
    private String notch;

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this.notch = notches.toUpperCase();
    }
    @Override
    boolean rotates() {
        return true;
    }
    @Override
    boolean atNotch() {
        setOnNotch(notch.indexOf(alphabet().toChar(
                permutation().wrap(setting()))) != -1 && rotates());
        return isOnNotch();
    }
    @Override
    void advance() {
        setAdvanced(rotates());
        set(setting() + 1);
    }
}
