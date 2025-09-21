import java.util.*;

//References:
//https://www.geeksforgeeks.org/dsa/stable-marriage-problem/

public class Matching {

        public static int[] satisfactoryPair(int n, int[][] progPref, int[][] compPref){

                int[] progMatch = new int[n];
                Arrays.fill(progMatch, -1);
                int[] compMatch = new int[n];
                Arrays.fill(compMatch, -1);
                //Indicates the company/programmer match (initialized as unmatched)

                boolean progFree[] = new boolean[n];
                Arrays.fill(progFree, true);
                boolean compFree[] = new boolean[n];
                Arrays.fill(compFree,true);
                //Indicates that the company/programmer is free

                int[] nextApplication = new int[n];
                Arrays.fill(nextApplication, 0);
                //Keeping track of which programmer is applying to which company

                return progMatch;
        }

        public static void main(){
                
        }

}