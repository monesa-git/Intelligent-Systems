import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author monesa, aparajitha, chandan, nour
 *
 * Solving N-queens problem by different hill climbing approach
 */

public class N_QueensAlgorithm {

    char board[][];
    int queensArray[];
    int boardCounter;
    int rrCounter;
    /**
     * shuffleBoard function uses different hillClimbing algorithm to solve the N-queen problem
     *
     * @param numberOfQueens
     * @param algorithm
     * @return
     */
    public int shuffleBoard(int numberOfQueens, int algorithm) {
        boardCounter = 0;

        board = new char[numberOfQueens][numberOfQueens];
        queensArray = new int[numberOfQueens];
        Random rand = new Random();
        for(int i=0; i<numberOfQueens; i++) {
            for(int j=0; j<numberOfQueens; j++) {
                board[i][j] = '*';
            }
        }
        for(int i=0; i<numberOfQueens; i++) {
            int randomNumber = rand.nextInt(numberOfQueens);
            board[randomNumber][i] = 'Q';
            queensArray[i] = randomNumber;
        }
        printQueensBoard(numberOfQueens);

        //Trying Hill Climbing without Sideways
        if(algorithm == 1) {
            int presentHeuristic = calulateHeuristicForBoard(numberOfQueens);
            System.out.println("The heuristic value of the original board is : "+presentHeuristic);
            int currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
            if(currentHeuristic == 0) {
                System.out.println("Solution found...");
                return 1;
            }else {
                System.out.println("Soulution Not found...");
                return 0;
            }
        }

        //Trying Hill climbing with Sideways
        if(algorithm == 2) {
            int presentHeuristic = calulateHeuristicForBoard(numberOfQueens);
            System.out.println("The heuristic value of the original board is : "+presentHeuristic);
            int currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
            while(currentHeuristic < presentHeuristic) {
                presentHeuristic = currentHeuristic;
                currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
                if(currentHeuristic == 0) {
                    break;
                }
            }
            int index =0 ;
            while(currentHeuristic == presentHeuristic && index < numberOfQueens) {
                index++;
                currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
                if(currentHeuristic == 0) {
                    break;
                }
            }
            if(currentHeuristic == 0) {
                System.out.println("Solution found...");
                return 1;
            }else {
                System.out.println("Solution Not found...");
                return 0;
            }
        }

        //Trying random restart without sideways
        if(algorithm == 3) {
            int presentHeuristic = calulateHeuristicForBoard(numberOfQueens);
            System.out.println("The heuristic value of the original board is : "+presentHeuristic);
            int currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
            if(currentHeuristic == 0) {
                System.out.println("Solution found");
                printQueensBoard(numberOfQueens);
                return 1;
            } else {
                System.out.println("Randomly restarting...");
                rrCounter++;
                return shuffleBoard(numberOfQueens,algorithm);
            }
        }

        //Trying random restart with sideways
        if(algorithm == 4) {
            int presentHeuristic = calulateHeuristicForBoard(numberOfQueens);
            System.out.println("The heuristic value of the original board is : "+presentHeuristic);
            int currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
            while(currentHeuristic < presentHeuristic) {
                presentHeuristic = currentHeuristic;
                currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
                if(currentHeuristic == 0) {
                    break;
                }
            }
            int index =0 ;
            while(currentHeuristic == presentHeuristic && index < numberOfQueens) {
                index++;
                currentHeuristic = hillClimbing(numberOfQueens, presentHeuristic);
                if(currentHeuristic == 0) {
                    break;
                }
            }
            if(currentHeuristic == 0) {
                System.out.println("Solution found");
                printQueensBoard(numberOfQueens);
                return 1;
            } else {
                System.out.println("Randomly restarting...");
                rrCounter++;
                return shuffleBoard(numberOfQueens,algorithm);
            }
        }
        return -1;
    }

    /**
     * Prints the current board
     *
     * @param numberOfQueens
     */
    public void printQueensBoard(int numberOfQueens) {
        for(int i=0; i<numberOfQueens; i++) {
            for(int j=0; j<numberOfQueens; j++) {
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
    }

    /**
     * hillClimbing function calculates the best successor and returns its heuristic value.
     *
     * @param numberOfQueens
     * @param presentHeuristic
     * @return
     */
    public int hillClimbing(int numberOfQueens, int presentHeuristic){
        for(int i=0; i<numberOfQueens; i++) {
            board[queensArray[i]][i] = '*';
            int originalQueen = queensArray[i];
            boolean inside = false;
            int heuristic = 999;
            int bestColumn = 0;
            for(int j=0; j<numberOfQueens; j++) {
                if(j != queensArray[i]) {
                    queensArray[i] = j;
                    board[j][i] = 'Q';
                    heuristic = calulateHeuristicForBoard(numberOfQueens);
                    if(heuristic <= presentHeuristic) {
                        System.out.println("Better board found at column "+ i +" with heuristic : "+heuristic);
                        printQueensBoard(numberOfQueens);
                        inside = true;
                        presentHeuristic = heuristic;
                        bestColumn = j;
                        boardCounter++;
                        board[j][i] = '*';
                    }else {
                        board[j][i] = '*';
                    }
                }
            }
            if(inside) {
                queensArray[i] = bestColumn;
                board[queensArray[i]][i] = 'Q';
            }else {
                queensArray[i] = originalQueen;
                board[queensArray[i]][i] = 'Q';
            }
        }
        return presentHeuristic;
    }

    /**
     * calulateHeuristicForBoard calculates the heuristic value for the current board.
     *
     * @param numberOfQueens
     * @return
     */
    public int calulateHeuristicForBoard(int numberOfQueens) {
        int numberOfAttacks = 0;

        for(int i=0; i < numberOfQueens; i++) {
            //CalculatingDiagonalAttack

            //Going backwards and finding out if there are attack
            if(queensArray[i]>0) {
                int row = queensArray[i];
                int col = i;
                while(row > 0) {
                    if(col >= numberOfQueens-1) {
                        break;
                    }
                    if(board[--row][++col] == 'Q') {
                        numberOfAttacks++;
                    }
                }
            }
            //Going forwards and finding out if there are attack
            int row = queensArray[i];
            int col = i;
            while(row < numberOfQueens-1) {
                col = col+1;
                if(col >= numberOfQueens) {
                    break;
                }
                if(board[++row][col] == 'Q') {
                    numberOfAttacks++;
                }
            }
            row = queensArray[i];
            col = i;
            while(col < numberOfQueens - 1) {
                //Calculating Horizontal Attack
                if(board[row][++col] == 'Q') {
                    numberOfAttacks++;
                }
            }
        }

        return numberOfAttacks;
    }

    /**
     * Main function for which get the input from the user and calls the shuffle board function.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("N-Queens Problem using hill climbing approach");
        System.out.println("Enter the number of quuens");
        Scanner sc = new Scanner(System.in);
        int numberOfQueens = sc.nextInt();
        System.out.println("Enter the number of trials you want to want to try the algorithms");
        int numberOfTrails = sc.nextInt();

        //Hill climbing without sideways
        int successAttemptsForHC = 0;
        int successHC;
        int successTotal = 0;
        int failureTotal = 0;
        for(int i=0; i<numberOfTrails; i++) {
            System.out.println("#Trail "+(i+1));
            N_QueensAlgorithm nqa = new N_QueensAlgorithm();
            successHC = nqa.shuffleBoard(numberOfQueens, 1);
            if(successHC == 1) {
                successTotal = successTotal + nqa.boardCounter;
                successAttemptsForHC++;
            }else{
                failureTotal =  failureTotal + nqa.boardCounter;
            }
        }

        int avgHCSuccess = 0;
        if(successAttemptsForHC>0) {
            avgHCSuccess = successTotal/successAttemptsForHC;
        }

        int avgHCFailure = 0;
        if(successAttemptsForHC != numberOfTrails) {
            avgHCFailure = failureTotal/(numberOfTrails - successAttemptsForHC);
        }

//      Rate of success and rate of failure - HC
        float RateSuccessHCWithoutSideways = (float) successAttemptsForHC/numberOfTrails;
        float RateFailureHCWithoutSideways = (float) (numberOfTrails - successAttemptsForHC)/numberOfTrails;

        //Hill Climbing with sideways
        int successAttemptsForSideways = 0;
        int successSideways;
        successTotal = 0;
        failureTotal = 0;
        for(int i=0; i<numberOfTrails; i++) {
            System.out.println("#Trail "+(i+1));
            N_QueensAlgorithm nqa = new N_QueensAlgorithm();
            successSideways = nqa.shuffleBoard(numberOfQueens, 2);
            if(successSideways == 1) {
                successTotal = successTotal + nqa.boardCounter;
                successAttemptsForSideways++;
            }else {
                failureTotal =  failureTotal + nqa.boardCounter;
            }
        }
        int avgHillSidewaysSuccess = 0;
        if(successAttemptsForSideways>0) {
            avgHillSidewaysSuccess= successTotal/successAttemptsForSideways;
        }

        int avgHillSidewaysFailure = 0;
        if(successAttemptsForSideways != numberOfTrails) {
            avgHillSidewaysFailure =  failureTotal/(numberOfTrails - successAttemptsForSideways);
        }

//      Rate of success and rate of failure - HC with Sideways
        float RateSuccessHCWithSideways = (float) successAttemptsForSideways/numberOfTrails;
        float RateFailureHCWithSideways = (float) (numberOfTrails - successAttemptsForSideways)/numberOfTrails;

       
        //Hill Climbing Random Restart without sideways
        int successAttemptsRRWSS = 0;
        int successRRWSS ;
        successTotal = 0;
        failureTotal = 0;
        int randomRestartAverage = 0;

        N_QueensAlgorithm nqa = new N_QueensAlgorithm();
        nqa.rrCounter = 0;
        for(int i=0; i<numberOfTrails; i++) {
            System.out.println("#Trail "+(i+1));
            successRRWSS = nqa.shuffleBoard(numberOfQueens, 3);
            if(successRRWSS == 1) {
                successTotal = successTotal + nqa.boardCounter;
                successAttemptsRRWSS++;
                randomRestartAverage = randomRestartAverage + nqa.rrCounter;
            }else {
                failureTotal =  failureTotal + nqa.boardCounter;
            }
        }

        int avgHillRandomSuccess = 0;
        if(successAttemptsRRWSS>0) {
            avgHillRandomSuccess =  successTotal/successAttemptsRRWSS;
        }
        
        randomRestartAverage = randomRestartAverage/numberOfTrails;

        int avgHillRandomFailure = 0;
        if(successAttemptsRRWSS != numberOfTrails) {
            avgHillRandomFailure =  failureTotal/(numberOfTrails - successAttemptsRRWSS);
        }

//      Rate of success and rate of failure - Random Restart Without Sideways
        float RateSuccessHCRestartWithoutSideways = (float) successAttemptsRRWSS/numberOfTrails;
        float RateFailureHCRestartWithoutSideways = (float) (numberOfTrails - successAttemptsRRWSS)/numberOfTrails;

        //Hill Climbing Random Restart with sideways
        int successAttemptsForHCRR = 0;
        int successHCRR;
        int randomRestartAverageWSS = 0;
        successTotal = 0;
        failureTotal = 0;
        nqa = new N_QueensAlgorithm();
        nqa.rrCounter = 0;
        for(int i=0; i<numberOfTrails; i++) {
            System.out.println("#Trail "+(i+1));
            successHCRR = nqa.shuffleBoard(numberOfQueens, 4);
            if(successHCRR == 1) {
                successTotal = successTotal + nqa.boardCounter;
                successAttemptsForHCRR++;
                randomRestartAverageWSS = randomRestartAverageWSS + nqa.rrCounter;
            }else {
                failureTotal =  failureTotal + nqa.boardCounter;
            }
        }
        
        randomRestartAverageWSS = randomRestartAverageWSS/numberOfTrails;

        int avgHillRandomSidewaysSuccess =  0;
        if(successAttemptsForHCRR>0) {
            avgHillRandomSidewaysSuccess =  successTotal/successAttemptsForHCRR;
        }

        int avgHillRandomSidewaysFailure = 0;
        if(successAttemptsForHCRR != numberOfTrails) {
            avgHillRandomSidewaysFailure =  failureTotal/(numberOfTrails - successAttemptsForHCRR);
        }

//      Rate of success and rate of failure - Random Restart With Sideways
        float RateSuccessHCRestartWithSideways = (float) successAttemptsForHCRR/numberOfTrails;
        float RateFailureHCRestartWithSideways = (float) (numberOfTrails - successAttemptsForHCRR)/numberOfTrails;

        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("Hill Climbing without sideways- Out of "+numberOfTrails+" trails, for "+successAttemptsForHC+ " solution is found");
        System.out.println("Rate of success: " + String.format("%.2f", RateSuccessHCWithoutSideways*100) + "%");
        System.out.println("Rate of failure: " + String.format("%.2f", RateFailureHCWithoutSideways*100) + "%");
        System.out.println("The average number of steps when it succeeds - "+avgHCSuccess);
        System.out.println("The average number of steps when it fails - "+avgHCFailure);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("Hill Climbing with sideways - Out of "+numberOfTrails+" trails, for "+successAttemptsForSideways+ " solution is found");
        System.out.println("Rate of success: " + String.format("%.2f", RateSuccessHCWithSideways*100) + "%");
        System.out.println("Rate of failure: " + String.format("%.2f", RateFailureHCWithSideways*100) + "%");
        System.out.println("The average number of steps when it succeeds - "+avgHillSidewaysSuccess);
        System.out.println("The average number of steps when it fails - "+avgHillSidewaysFailure);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("Random Restart Hill Climbing without sideways - Out of "+numberOfTrails+" trails, for "+successAttemptsRRWSS+ " solution is found");
        System.out.println("Rate of success: " + String.format("%.2f", RateSuccessHCRestartWithoutSideways*100) + "%");
        System.out.println("Rate of failure: " + String.format("%.2f", RateFailureHCRestartWithoutSideways*100) + "%");
        System.out.println("The average number of steps when it succeeds - "+avgHillRandomSuccess);
        System.out.println("The average number of steps when it fails - "+avgHillRandomFailure);
        System.out.println("The average number of times it restarts - "+randomRestartAverage);
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.println("Random Restart Hill Climbing with sideways - Out of "+numberOfTrails+" trails, for "+successAttemptsForHCRR+ " solution is found");
        System.out.println("Rate of success: " + String.format("%.2f", RateSuccessHCRestartWithSideways*100) + "%");
        System.out.println("Rate of failure: " + String.format("%.2f", RateFailureHCRestartWithSideways*100) + "%");
        System.out.println("The average number of steps when it succeeds - "+avgHillRandomSidewaysSuccess);
        System.out.println("The average number of steps when it fails - "+avgHillRandomSidewaysFailure);
        System.out.println("The average number of times it restarts - "+randomRestartAverageWSS);
        System.out.println("------------------------------------------------------------------------------------------------------------------");

    }
}
