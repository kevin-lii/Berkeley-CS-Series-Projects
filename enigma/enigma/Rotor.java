package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Kevin Li
 */
class Rotor {
    /** My name. */
    private final String _name;
    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;
    /** The setting position of the rotor.*/
    private int position;
    /** Determines if the rotor has advanced.*/
    private boolean advanced;
    /** Determines if rotor is on notch.*/
    private boolean onNotch;
    /** Constructor of rotor.
     * @param name name of rotor
     * @param perm The permutation of the rotor*/
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        position = 0;
        advanced = false;
        onNotch = false;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return position;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        position = _permutation.wrap(posn);
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        set(alphabet().toInt(cposn));
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int total = (permutation().permute(p + setting()) - setting()) % size();
        if (total < 0) {
            total += size();
        }
        return total;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int total = (permutation().invert(e + setting()) - setting()) % size();
        if (total < 0) {
            total += size();
        }
        return total;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing.*/
    void advance() {
    }
    /**Gets the variable advanced.
     * @return boolean*/
    boolean advanced() {
        return advanced;
    }
    /**Sets the variable advanced. */
    /** @param advance if rotor advances*/
    void setAdvanced(boolean advance) {
        this.advanced = advance;
    }
    /**Gets the variable onNotch.*/
     /** @return boolean*/
    boolean isOnNotch() {
        return onNotch;
    }
    /**Sets the variable onNotch. */
     /** @param onnotch if rotor is on notch*/
    void setOnNotch(boolean onnotch) {
        this.onNotch = onnotch;
    }
    @Override
    public String toString() {
        return "Rotor " + _name;
    }


}
