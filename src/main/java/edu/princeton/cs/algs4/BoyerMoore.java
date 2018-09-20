/******************************************************************************
 *  Compilation:  javac BoyerMoore.java
 *  Execution:    java BoyerMoore pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  bad-character rule part of the Boyer-Moore algorithm.
 *  (does not implement the strong good suffix rule)
 *
 *  % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:               abracadabra
 *
 *  % java BoyerMoore rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:         rab
 *
 *  % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad 
 *  pattern:                                   bcara
 *
 *  % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

/**
 *  The {@code BoyerMoore} class finds the first occurrence of a pattern string
 *  in a text string.
 *  <p>
 *  This implementation uses the Boyer-Moore algorithm (with the bad-character
 *  rule, but not the strong good suffix rule).
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BoyerMoore {
    private final int R;     // the radix
    private int[] right;     // the bad-character skip array

    private char[] pattern;  // store the pattern as a character array
    private String pat;      // or as a string

    /**
     * Preprocesses the pattern string.
     *
     * @param pat the pattern string
     */
    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        // position of rightmost occurrence of c in the pattern
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pat.length(); j++)
            right[pat.charAt(j)] = j;
    }

    /**
     * Preprocesses the pattern string.
     *
     * @param pattern the pattern string
     * @param R the alphabet size
     */
    public BoyerMoore(char[] pattern, int R) {
        this.R = R;
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++)
            this.pattern[j] = pattern[j];

        // position of rightmost occurrence of c in the pattern
        right = new int[R];
        for (int c = 0; c < R; c++)
            right[c] = -1;
        for (int j = 0; j < pattern.length; j++)
            right[pattern[j]] = j;
    }

    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  txt the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
    public int search(String txt) {
        int m = pat.length();
        int n = txt.length();
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m-1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i+j)) {
                    skip = Math.max(1, j - right[txt.charAt(i+j)]);
                    break;
                }
            }
            if (skip == 0) return i;    // found
        }
        return n;                       // not found
    }


    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
    public int search(char[] text) {
        int m = pattern.length;
        int n = text.length;
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m-1; j >= 0; j--) {
                if (pattern[j] != text[i+j]) {
                    skip = Math.max(1, j - right[text[i+j]]);
                    break;
                }
            }
            if (skip == 0) return i;    // found
        }
        return n;                       // not found
    }

    public BoyerMoore(){
        this.R = 256;
    }

    private int bmBc[];
    private int suffix[];
    private int bmGs[];
    private void preBmBc(String pattern){
        bmBc = new int[R];
        int m = pattern.length();

        for (int i = 0; i < R; i++)
            bmBc[i] = m;

        for (int i = 0; i < m - 1; i++)
            bmBc[pattern.charAt(i)] = m - i - 1;
    }

    private void preSuffix(String pattern){
        int m = pattern.length();
        suffix = new int[m];

        int i = m - 1;
        int f = i, g = i;

        suffix[m - 1] = m;
        for (i = m - 2; i >= 0; i--) {
            if (i > g && suffix[i + m - 1 - f] < i - g)
                suffix[i] = suffix[i + m - 1 - f];
            else {
                f = g = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(m - 1 - i + g))
                    g--;

                suffix[i] = i - g;
            }
        }
    }

    private void preBmGs(String pattern){
        preSuffix(pattern);
        int m = pattern.length();
        bmGs = new int[m];

        for (int i = 0; i < m; i++)
            bmGs[i] = m;

        for (int i = m - 1; i >= 0; i--)
            if (suffix[i] == i + 1)
                for (int j = 0; j < m - 1 - i; j++)
                    if (bmGs[j] == m)
                        bmGs[j] = m - 1 - i;

        for (int i = 0; i <= m - 2; i++)
            bmGs[m - 1 - suffix[i]] = m - 1 - i;
    }

    private int max(int a, int b){
        return (a > b) ? a : b;
    }

    private int bmOld(String pattern, String text){
        /* Pre-processing */
        preBmBc(pattern);
        preBmGs(pattern);

        /* Searching */
        int j = 0, i = 0;
        int n = text.length();
        int m = pattern.length();
        while (j <= n - m) {
            for (i = m - 1; i >= 0 && pattern.charAt(i) == text.charAt(i + j); i--);

            if (i < 0) {
                // match success.
                System.out.println("j = " + j);
                // j += bmGs[0];
                return j;
            } else
                j += max(bmGs[i], bmBc[text.charAt(i + j)] - m + 1 + i);
        }

        return n;
    }

    private int bm(String pattern, String text){
        /* Pre-processing */
        preBmBc(pattern);
        preBmGs(pattern);

        /* Searching */
        int m = pattern.length();
        int n = text.length();

        int j = 0;
        int i = m -1;

        while (j <= n - m && i >= 0) {
            if (pattern.charAt(i) == text.charAt(i + j)) {
                i--;
            } else {
                j += max(bmGs[i], bmBc[text.charAt(i + j)] - m + 1 + i);
                i = m - 1;
            }
        }

        if (i < 0) return j;
        return n;
    }

    private int bruteForce(String pattern, String text){
        int m = pattern.length();
        int n = text.length();

        int j = 0;
        int i = m - 1;

        while (j <= n - m && i >= 0){
            if (pattern.charAt(i) == text.charAt(i + j)) {
                i--;
            } else {
                j++;
                i = m - 1;
            }
        }

        if (i < 0) return j;
        return n;
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text    = txt.toCharArray();

        BoyerMoore boyermoore1 = new BoyerMoore(pat);
        BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
        BoyerMoore boyerMoore3 = new BoyerMoore();
        BoyerMoore boyerMoore4 = new BoyerMoore();
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);
        int offset3 = boyerMoore3.bm(pat, txt);
        int offset4 = boyerMoore4.bruteForce(pat, txt);

        // print results
        StdOut.println("text:    " + txt);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset3; i++)
            StdOut.print(" ");
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset4; i++)
            StdOut.print(" ");
        StdOut.println(pat);
    }
}


/******************************************************************************
 *  Copyright 2002-2018, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
