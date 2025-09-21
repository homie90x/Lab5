import java.util.*;
//Harley and Mahathir

//References:
//https://www.geeksforgeeks.org/dsa/stable-marriage-problem/
//https://builtin.com/articles/gale-shapley-algorithm
//https://en.wikipedia.org/wiki/Gale-Shapley_algorithm

public class Matching {

        public static int[] satisfactoryPair(int[][] progPref, int[][] compPref){
                int n = progPref.length;

                int[] progMatch = new int[n];
                Arrays.fill(progMatch, -1);
                int[] compMatch = new int[n];
                Arrays.fill(compMatch, -1);
                //Indicates the company/programmer match (initialized as unmatched)

                boolean progFree[] = new boolean[n];
                Arrays.fill(progFree,true);
                //Indicates that the programmer is free

                int[] nextApplication = new int[n];
                Arrays.fill(nextApplication, 0);
                //Keeping track of which programmer is applying next

                int freeCounter = n;
                //counter keeps track of how many programmers we have iterated through

                while (freeCounter > 0) {
                        for (int prog = 0; prog < n; prog++){
                                if (!progFree[prog]){
                                        continue;
                                //if the current programmer is not free, move on to the next iteration        
                                }
                                else {
                                        int comp = progPref[prog][nextApplication[prog]];
                                        //sets the company variable equal to the next preference of the current programmer
                                        nextApplication[prog]++;
                                        //Increase application position for next iteration
                                
                                        if (compMatch[comp] == -1){
                                        //if the company is free

                                                compMatch[comp] = prog;
                                                progMatch[prog] = comp;
                                                //Matching the programmer and company to each other in the array

                                                progFree[prog] = false;
                                                //programmer is no longer free
                                                freeCounter--;
                                                //now we have one less programmer to pair with a company
                                        }
                                        else {
                                        //goes into the comparison sequence if a company is already occupied
                                                int oldProg = compMatch[comp];
                                                if (compPrefersP1overP2(compPref[comp], prog, oldProg)){
                                                //if P1 is preferred over P2
                                                        compMatch[comp] = prog;
                                                        progMatch[prog] = comp;
                                                        //makes match switch to new programmer

                                                        progFree[prog] = false;
                                                        progFree[oldProg] = true;
                                                        progMatch[oldProg] = -1;
                                                        //frees old programmer

                                                }
                                        }
                                }
                        }

                }

                return progMatch;
        }

        public static boolean compPrefersP1overP2(int[] compPref, int P1, int P2){
                int n = compPref.length;
                for (int i = 0; i < n; i++){
                        if (compPref[i] == P1)
                                return true;
                        //P1 is preferred over P2
                        if (compPref[i] == P2)
                                return false;
                        //P2 is preferred over P1
                }
                return false;
                //closes out the method
        }

        public static void main(){
                
        }

}