package enigma;
import java.util.List;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Kevin Li
 */
class Permutation {
    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycless notation.  Characters in the
     *  alphabet that are not included in any cycless map to themselves.
     *  Whitespace is ignored.*/
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        this.cycless = new ArrayList<String>();
        for (String cy : cycles.split("\\(")) {
            if (!cy.trim().equals("") && !cy.contains(")")) {
                throw error("Improper format of a cycle.");
            }
            cy = cy.replaceAll("\\)", "").trim();
            for (char c : cy.toCharArray()) {
                if (!alphabet.contains(c)) {
                    throw error("Improper character in a cycle.");
                }
            }
            if (!cy.equals("") || cy.length() > 0) {
                this.cycless.add(cy.toUpperCase());
            }
        }
    }

    /** Add the cycless c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (String cy : cycle.split("\\(")) {
            cy = cy.replaceAll("\\)", "").trim();
            if (!cy.equals("") || cy.length() > 0) {
                this.cycless.add(cy.toUpperCase());
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return alphabet().toInt((char) permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return alphabet().toInt((char) invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (checkMap()) {
            return p;
        } else {
            for (String cy : cycless) {
                if (cy.indexOf(p) != -1 && _alphabet.contains(
                        cy.charAt((cycless.indexOf(p) + 1) % cy.length()))) {
                    char a = cy.charAt((cy.indexOf(p) + 1) % cy.length());
                    return a;
                }
            }
            return p;
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (checkMap()) {
            return c;
        } else {
            for (String cy : cycless) {
                int total = (cy.indexOf(c) - 1) % cy.length();
                if ((cy.indexOf(c) - 1) % cy.length() < 0) {
                    total += cy.length();
                }
                if (cy.indexOf(c) != -1 && (cy.length() == 1
                        || _alphabet.contains(cy.charAt(total)))) {
                    char a = cy.charAt(total);
                    return a;
                }
            }
            return c;
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }
    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int i = 0;
        for (String cy : cycless) {
            if (cy.length() == 1) {
                return false;
            }
            i += cy.length();
        }
        return i == alphabet().size();
    }
    /** Checks if cycles maps to itself or is empty.
     * @return boolean */
    boolean checkMap() {
        for (String cy : cycless) {
            if (cy.length() > 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** List of all the cycles. Each cycless is separated in the list*/
    private List<String> cycless;
}
