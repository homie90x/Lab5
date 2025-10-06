/*
 * Matching.java
 * 
 * Authors: Harley and Mahathir
 *
 * References:
 * - https://www.geeksforgeeks.org/dsa/stable-marriage-problem/
 * - https://builtin.com/articles/gale-shapley-algorithm
 * - https://en.wikipedia.org/wiki/Gale–Shapley_algorithm
 *
 * This program solves the programmer-company pairing problem. It ensures that no pair (P1, C2) prefers each 
 * other over their current partners, i.e., the matching is satisfactory (stable).
 */

import java.util.*;

public class Matching {

    /**
     * @param progPref preference matrix of programmers
     * @param compPref preference matrix of companies
     * @return array progMatch where progMatch[i] = company matched with programmer i
     */
    public static int[] satisfactoryPair(int[][] progPref, int[][] compPref) {
        int n = progPref.length;
        int[] progMatch = new int[n];
        int[] compMatch = new int[n];
        boolean[] freeProg = new boolean[n];
        int[] nextProposal = new int[n];

        Arrays.fill(progMatch, -1);
        Arrays.fill(compMatch, -1);
        Arrays.fill(freeProg, true);
        Arrays.fill(nextProposal, 0);

        // Precompute company rankings for O(1) comparisons
        int[][] compRank = new int[n][n];
        for (int c = 0; c < n; c++) {
            for (int rank = 0; rank < n; rank++) {
                compRank[c][normalize(compPref[c][rank], n)] = rank;
            }
        }

        int freeCount = n;
        while (freeCount > 0) {
            // Find a free programmer
            int p = -1;
            for (int i = 0; i < n; i++) {
                if (freeProg[i]) {
                    p = i;
                    break;
                }
            }

            // Programmer proposes to companies in order
            while (nextProposal[p] < n && freeProg[p]) {
                int c = normalize(progPref[p][nextProposal[p]++], n);

                if (compMatch[c] == -1) {
                    // Company is free → match them
                    progMatch[p] = c;
                    compMatch[c] = p;
                    freeProg[p] = false;
                    freeCount--;
                } else {
                    int current = compMatch[c];
                    if (compRank[c][p] < compRank[c][current]) {
                        // Company prefers new programmer → switch
                        progMatch[p] = c;
                        compMatch[c] = p;
                        freeProg[p] = false;
                        freeProg[current] = true;
                        progMatch[current] = -1;
                    }
                }
            }

            // If programmer exhausted all options
            if (nextProposal[p] >= n && freeProg[p]) {
                freeProg[p] = false;
                freeCount--;
            }
        }

        return progMatch;
    }

    /**
     * Normalizes preference values (handles both 0-based and 1-based inputs).
     */
    private static int normalize(int raw, int n) {
        return (raw > 0 && raw <= n) ? raw - 1 : raw;
    }

    /**
     * Checks if a given matching is stable.
     */
    public static boolean isStable(int[] progMatch, int[][] progPref, int[][] compPref) {
        int n = progMatch.length;

        // Precompute company rankings
        int[][] compRank = new int[n][n];
        for (int c = 0; c < n; c++) {
            for (int rank = 0; rank < n; rank++) {
                compRank[c][normalize(compPref[c][rank], n)] = rank;
            }
        }

        // Check all possible pairs for instability
        for (int p1 = 0; p1 < n; p1++) {
            int c1 = progMatch[p1];
            for (int c2 = 0; c2 < n; c2++) {
                if (c2 == c1) continue;

                // If programmer prefers c2 over c1
                if (prefers(progPref[p1], c2, c1)) {
                    int p2 = -1;
                    for (int i = 0; i < n; i++) {
                        if (progMatch[i] == c2) {
                            p2 = i;
                            break;
                        }
                    }
                    // If c2 prefers p1 over p2 → instability
                    if (p2 == -1 || compRank[c2][p1] < compRank[c2][p2]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

     //Checks if programmer prefers company 'a' over 'b'.
     
    private static boolean prefers(int[] pref, int a, int b) {
        for (int c : pref) {
            c = normalize(c, pref.length);
            if (c == a) return true;
            if (c == b) return false;
        }
        return false;
    }

     // Runs several test cases to demonstrate algorithm correctness.

    public static void main(String[] args) {
        // Test Case 1: Provided example
        int[][] progPref1 = {
            {5, 1, 4, 2, 3},
            {4, 5, 2, 1, 3},
            {4, 2, 3, 5, 1},
            {3, 2, 4, 1, 5},
            {1, 4, 2, 3, 5}
        };

        int[][] compPref1 = {
            {2, 5, 1, 3, 4},
            {1, 2, 3, 4, 5},
            {5, 3, 2, 1, 4},
            {1, 3, 2, 4, 5},
            {2, 3, 5, 4, 1}
        };

        runTest("Test Case 1", progPref1, compPref1);

        // Edge Case 2: All programmers and companies have identical preferences
        int[][] progPref2 = {
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5},
            {1, 2, 3, 4, 5}
        };
        int[][] compPref2 = progPref2;
        runTest("Edge Case 2 (identical preferences)", progPref2, compPref2);

        // Edge Case 3: Reverse preferences
        int[][] progPref3 = {
            {5, 4, 3, 2, 1},
            {5, 4, 3, 2, 1},
            {5, 4, 3, 2, 1},
            {5, 4, 3, 2, 1},
            {5, 4, 3, 2, 1}
        };
        int[][] compPref3 = progPref3;
        runTest("Edge Case 3 (reverse preferences)", progPref3, compPref3);

        // Edge Case 4: Partial / missing preferences (skipped entries)
        int[][] progPref4 = {
            {1, 0, 3, 0, 5},
            {0, 2, 0, 4, 5},
            {1, 2, 3, 0, 0},
            {0, 0, 3, 4, 5},
            {1, 2, 0, 4, 0}
        };

        int[][] compPref4 = {
            {1, 2, 3, 4, 5},
            {0, 2, 3, 4, 5},
            {1, 0, 3, 4, 5},
            {1, 2, 3, 0, 5},
            {1, 2, 0, 4, 5}
        };

        runTest("Edge Case 4 (incomplete preferences)", progPref4, compPref4);

        
    }

    private static void runTest(String name, int[][] progPref, int[][] compPref) {
        System.out.println("\n--- " + name + " ---");
        int[] matches = satisfactoryPair(progPref, compPref);
        for (int i = 0; i < matches.length; i++) {
            System.out.println("Programmer " + (i + 1) + " -> Company " + (matches[i] + 1));
        }
        System.out.println("Stable? " + isStable(matches, progPref, compPref));
    }
}
