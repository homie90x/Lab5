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
                                        continue;}
                                //if the current programmer is not free, move on to the next iteration    

                                while (nextApplication[prog] < n && (progPref[prog][nextApplication[prog]] - 1) < 0) {
                                        nextApplication[prog]++;} //makes sure that all programmers will have a match if possible
                                        //goes through all available companies to see if something works
                                
                                if (nextApplication[prog] >= n) {
                                        progFree[prog] = false; //indicates that there's no suitable match for this programmer
                                        freeCounter--; 
                                        continue;}

                                int comp = progPref[prog][nextApplication[prog]];
                                 //sets the company variable equal to the next preference of the current programmer

                                nextApplication[prog]++;
                                //Increase application position for next iteration
                                
                                if (compMatch[comp-1] == -1){
                                //if the company is free

                                        compMatch[comp-1] = prog;
                                        progMatch[prog] = comp;
                                        //Matching the programmer and company to each other in the array

                                        progFree[prog] = false;
                                        //programmer is no longer free
                                        freeCounter--;
                                        //now we have one less programmer to pair with a company
                                        }
                                else {
                                //goes into the comparison sequence if a company is already occupied
                                        int oldProg = compMatch[comp-1];
                                        if (compPrefersP1overP2(compPref[comp-1], prog, oldProg)){
                                        //if P1 is preferred over P2
                                                compMatch[comp -1] = prog;
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
                        return progMatch;
                }
                

        public static boolean compPrefersP1overP2(int[] compPref, int P1, int P2){
                int n = compPref.length;
                for (int i = 0; i < n; i++){
                        if (compPref[i] == P1+1)
                                return true;
                        //P1 is preferred over P2
                        if (compPref[i] == P2+1)
                                return false;
                        //P2 is preferred over P1
                }
                return false;
                //closes out the method
        }

        public static void main(String[] args){

                System.out.println("ProgMatch returns an array with the coordinating company matches to each programmer (elements 1 to 5) \n");

               int[][] progPref1=  {
                {5, 1, 4, 2, 3},
                {4, 5, 2, 1, 3},
                {4, 2, 3, 5, 1},
                {3, 2, 4, 1, 5},
                {1, 4, 2, 3, 5}};

                int[][] compPref1= {
                {2, 5, 1, 3, 4},
                {1, 2, 3, 4, 5},
                {5, 3, 2, 1, 4},
                {1, 3, 2, 4, 5},
                {2, 3, 5, 4, 1}};

                System.out.println("ProgMatch (Canvas Example): " + Arrays.toString(satisfactoryPair(progPref1, compPref1)) + "\n");
                
                int[][] progPref2=  {
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};

                int[][] compPref2= { 
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5},
                {1, 2, 3, 4, 5}};

                System.out.println("EDGECASE: ProgMatch (Programmers and Companies have same preferences): " + Arrays.toString(satisfactoryPair(progPref2, compPref2)) + "\n");

                int[][] progPref3=  {
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1}};

                int[][] compPref3= { 
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1},
                {5, 4, 3, 2, 1}};

                System.out.println("EDGECASE: ProgMatch (Programmers and Companies have same preferences REVERSED): " + Arrays.toString(satisfactoryPair(progPref3, compPref3)) + "\n");

                int[][] progPref4 = {
                {1, 0, 3, 0, 5},
                {0, 2, 0, 4, 5},
                {1, 2, 3, 0, 0},
                {0, 0, 3, 4, 5},
                {1, 2, 0, 4, 0}};


                int[][] compPref4 = {
                {1, 2, 3, 4, 5},
                {0, 2, 3, 4, 5},
                {1, 0, 3, 4, 5},
                {1, 2, 3, 0, 5},
                {1, 2, 0, 4, 5}};

                System.out.println("EDGECASE: ProgMatch (Some Preferences aren't listed): " + Arrays.toString(satisfactoryPair(progPref4, compPref4)) + "\n");
        } 
}