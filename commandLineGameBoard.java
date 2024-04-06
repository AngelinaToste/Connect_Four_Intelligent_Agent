import java.util.*;

//import javax.swing.JOptionPane;

//1import javax.swing.JOptionPane;

//import javax.swing.JOptionPane;

//import javax.swing.JButton;
//import javax.swing.JLabel;
////import javax.swing.JOptionPane;

//import javafx.scene.paint.Color;

public class commandLineGameBoard 
{

    boolean playerMoveMade;
    int userTurnCounter = 0, CPUTurnCounter = 0;

    
    commandLineGameBoard()
    {
        
    }
    
    
    void startGame(int[][] occupiedSpacesN)
    {
        
        
        playerMoveMade = false;
        System.out.println("Starting a Connect Four Game");

        //print the board
        displayBoard(occupiedSpacesN);

        while ((userTurnCounter + CPUTurnCounter) < 42)
        {
            // ask the user to choose a value
            System.out.println("Player 1 (YOU) Chooses a Column, 0-6");
            Scanner keyboard = new Scanner(System.in);

            int columnChoice = keyboard.nextInt();

            userTurnCounter = playerMove(columnChoice, occupiedSpacesN, playerMoveMade, userTurnCounter);
            displayBoard(occupiedSpacesN);
            playerMoveMade = true;

            System.out.println("Player 2 (CPU) Chooses a Column");
            CPUTurnCounter = CPUTurn(occupiedSpacesN, CPUTurnCounter);
            displayBoard(occupiedSpacesN);
            

        }
        


    }

    public int CPUTurn( int[][] occupiedSpacesN, int CPUTurnCounter)
    {
        if (playerMoveMade)
        {

            int move = AlphaBetaSearch(occupiedSpacesN);

           
            //int[] move = new int[2]; // format is {row, column}

            // choose a random column
            //Random randCol = new Random();
            //move[1] = randCol.nextInt(7);
           
            // choose an available row from the specified column
            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[move][r] == 0)
                { // if the lowest row in the column is empty
                    // assign it to whose turn it is
                    // in this case, the column buttons are only for use of player 1 (the human player)

                    // set the occupied space to 2 to represent player 2's piece
                    occupiedSpacesN[move][r] = 2;


                    break;
                }
                /* there should already be checking in place to find all possible actions. dont need to check if something is possible, we know
                else if (r == 0)
                {
                    System.out.println("Choosing a new column!");
                    move[1] = randCol.nextInt(7);
                    r = 5;
                }
                */
            }

            playerMoveMade = false;
            CPUTurnCounter++;
             
            if  (CPUTurnCounter > 3 && isWin(2, occupiedSpacesN))
            {
                System.out.println("done");
                System.exit(0);
                //clearBoard(occupiedSpacesN, columns);
            }
            
        }   
          
        return CPUTurnCounter;  
    }
    public void displayBoard(int[][] occupiedSpacesN)
    {
        //print the board

        for (int r = 0; r < 6; r++)
        {
            for (int c = 0; c < 7; c++)
            {

                System.out.print( " " + occupiedSpacesN[c][r] + " ");
                
            }
            System.out.println();
        }
        System.out.println();
    }

    public int playerMove(int colNum, int[][] occupiedSpacesN, boolean playerMoveMade, int userTurnCounter)
    {
        if (!playerMoveMade)
        {

            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[colNum][r] == 0)
                { // if the lowest row in the column is empty
                    // assign it to whose turn it is
                    // in this case, the column buttons are only for use of player 1 (the human player)

                    occupiedSpacesN[colNum][r] = 1;
                    //invokeAndWait();
                    playerMoveMade = true;
                    break;
                }
                if ( r == 0 && occupiedSpacesN[colNum][r] > 0)
                {
                    System.out.println("This column is full. Please choose another.");
                    r = 5;
                }
    
            }
        }

        userTurnCounter++;
             
        if  ( userTurnCounter > 3 && isWin( 1, occupiedSpacesN))
        {
            System.out.println("done");
            System.exit(0);
                //clearBoard(occupiedSpacesN);
                //playerMoveMade =  false;
        }

        return userTurnCounter;
            
    }

    public Boolean isWin(int playerNum, int[][] occupiedSpacesN)
    {
        //int tilesPlayed,
        // purpose: check if there are more than 4 tiles played by each player, check for col row and diag rows of 4
        // parameters 
            //tilesPlayed - num of tiles played to determine if there are enough tiles for 4 to be in a row
            //playerNum - the player that is being checked for a win
        // return  boolean true if a win exists
        Boolean isWin = false;

        /*------------------------------CHECK IF WIN IS IN A COLUMN----------------------------------------------------- */
        int inACol = 0; // counter to determine how many of the same tile are consecutive in a column ex r0c0 r1c0 c2c0 r3c0
        // check for connect 4 in each column

        for(int c = 0; c < 7; c++) // for number of columns (0-6)
        {
            for (int r = 5; r >= 0; r--) // for number of rows in a column (0-5)
            { // starts from bottom of col to top because all of the pieces go to the lowest possible place in a column
                // potentially slightly more effecient, start with area with most tiles (bottom) first
                if ( inACol == 0 && occupiedSpacesN[c][r] == playerNum) 
                {
                    inACol++;
                }
                else if ( inACol < 4 && occupiedSpacesN[c][r] == playerNum)
                {
                    inACol++;
                    if (inACol == 4)
                    {
                        isWin = true;
                        displayBoard(occupiedSpacesN);
                        System.out.println("COLUMN WINNER IS PLAYER " + playerNum + " wining move: row " + r + " column" + c);
                        
                        return isWin;

                    }
                
                }
                else 
                {
                    // reset inACol
                    inACol = 0;
                }
            }
        }

        /*------------------------------CHECK IF WIN IS IN A ROW----------------------------------------------------- */
        int inARow = 0;
        for(int r = 5; r >= 0; r--) // for number of rows in a column (0-5)
        {
            for (int c = 0; c < 7; c++) // for number of columns (0-6)
            {
                if ( inARow == 0 && occupiedSpacesN[c][r] == playerNum)
                {
                    inARow++;
                }
                else if ( inARow < 4 && occupiedSpacesN[c][r] == playerNum)
                {
                    inARow++;
                    if (inARow == 4)
                    {
                        isWin = true; 
                        displayBoard(occupiedSpacesN);
                        System.out.println("ROW WINNER IS PLAYER " + playerNum + " wining move: row " + r + " column" + c);
                        break;

                    }
                
                }
                else 
                {
                    // reset inARow
                    inARow = 0;
                }
            }
        }
        
        /*------------------------------CHECK IF WIN IS IN AN INCREASING DIAGONAL----------------------------------------------------- */
        int inAnIncDiagonal = 0;
        // all possible increasing diagonal starting positions
        int allPossibleIncDiag[][] = {{3, 0},{4, 0},{5, 0},{5, 1},{5, 2},{5, 3}};

        for (int i = 0; i < allPossibleIncDiag.length; i++)
        {

            for (int c = allPossibleIncDiag[i][1], r = allPossibleIncDiag[i][0]; c < 7 && r >= 0; c++, r--) // c for number of columns (0-6) and r for number of rows in a column (0-5)
            {
                if ( inAnIncDiagonal == 0 && occupiedSpacesN[c][r] == playerNum)
                {
                    inAnIncDiagonal++;
                }
                else if ( inARow < 4 && occupiedSpacesN[c][r] == playerNum)
                {
                    inAnIncDiagonal++;
                    if (inAnIncDiagonal == 4)
                    {
                        isWin = true; 
                        displayBoard(occupiedSpacesN);
                        System.out.println("INCREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
                        break;
                    }
                
                }
                else 
                {
                    // reset inADiagonal
                    inAnIncDiagonal = 0;
                }
            }
        }

        /*------------------------------CHECK IF WIN IS IN A DECREASING DIAGONAL----------------------------------------------------- */

        // all possible decreasing diagonal starting positions
        int allPossibleDecDiag[][] = {{0, 0},{0, 1},{0, 2},{0, 3},{1, 0},{2, 0}};
        int inADecDiagonal = 0;

        for (int i = 0; i < allPossibleDecDiag.length; i++)
        {

            for (int c = allPossibleDecDiag[i][1], r = allPossibleDecDiag[i][0]; c < 7 && r < 6; c++, r++) // c for number of columns (0-6) and r for number of rows in a column (0-5)
            {
                if ( inADecDiagonal == 0 && occupiedSpacesN[c][r] == playerNum)
                {
                    inADecDiagonal++;
                }
                else if ( inARow < 4 && occupiedSpacesN[c][r] == playerNum)
                {
                    inADecDiagonal++;
                    if (inADecDiagonal == 4)
                    {
                        isWin = true; 
                        displayBoard(occupiedSpacesN);
                        System.out.println("DECREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
                        break;
                    }
                
                }
                else 
                {
                    // reset inADiagonal
                    inADecDiagonal = 0;
                }
            }
        }
        return isWin;
    }

    /* --------------------------------------------------THE ALGORITHM------------------------------------------------------------------- */
    // current issues:
    // results needs to be fixed so that the array is a copy and does not make reference to the original
    // alpha beta search needs to return something!
    // need to comme up with a way to evaluate the results


 /*
function minimax (node, depth, maximizingPlayer)
- if a node is the starting node or it is a winning move then return the heuristic value of the move along with the move itself

- if the player is the maximizing player
	--set value = -infinity
	--for each possible action resulting from the node
		---value = max(value, minimax(child (action) , depth − 1, FALSE)) // find the min value for the child node and compare it to the value set, choose the larger value
		--- return the value (return the pair of the evaluation and the value)
- else if the player is the minimizing player
	-- set the value =  infinity
	-- for each possible action resulting from the node
		--- value = min(value, minimax(child (action), depth − 1, TRUE)) // find the min value from the child/ action node and compare it to the value that was assigned initially, choose the smaller number
		--- return the value (the pair of evaluation and the value of the action)
*/
    

    
    int AlphaBetaSearch(int[][] state)
    {
        int[] utilityMovePair = {0,0}; //(value, move)

        //int player = playerNum;

        utilityMovePair = MAXValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE);
        
        return utilityMovePair[1]; // return move

    }

    int[] MAXValue(int [][] state, int alpha, int beta)
    {
        int[] utilityMovePair = {0,0};
        if ( (CPUTurnCounter >= 4) && isWin(2, state))
        {
            return null;
           // return UTILITY(), null 
        }

        // asssign v to negative infinity
        int v = Integer.MIN_VALUE, move = 0;
        
        // for each a in game.ACTIONS(state) do
        int [] actions = ACTIONS(state);

        for (int a = 0; a < actions.length; a++)
        {
            // for each possible action, find the minimum value action pair using the min function
            int[] minPair = MINValue(RESULT(state, actions[a]), alpha, beta);
            int v2 = minPair[0];
            //a2 = minPair[1]; // double check if this is needed

            // if the value is less than the current v value, assign the old value move pair the the new value move pair
            if (actions[a] < 7)
            {
                if (v2 > v)
                {
                    v = v2;
                    move = actions[a];
                    alpha = Math.max(alpha, v);
                }
                if (v >= beta)
                {
                
                    utilityMovePair[0] = v;
                    utilityMovePair[1] = move;
                    return utilityMovePair;
                }

            }
            
        }
        utilityMovePair[0] = v;
        utilityMovePair[1] = move;
        return utilityMovePair;
    }

    int[] MINValue(int[][] state, int alpha, int beta)
    {
        int[] utilityMovePair = {0,0};
        if ((CPUTurnCounter >= 4) && isWin(2, state))
        {
           return null;
        }
        
        // asssign v to infinity
        int v = Integer.MAX_VALUE;

        // actions is the array of possible columns to move to
        int [] actions = ACTIONS(state);
        int move = 0;

        for (int a = 0; a < actions.length; a++)
        {
            // for each possible action, find the minimum value action pair using the min function
            int[] maxPair = MAXValue(RESULT(state, actions[a]), alpha, beta);
            int v2 = maxPair[0];
            //a2 = maxPair[1]; is this needed

            // if the value is less than the current v value, assign the old value move pair the the new value move pair
            if (actions[a] < 7)
            {
                if (v2 < v)
                {
                    v = v2;
                    move = actions[a];
                    beta = Math.min(beta, v);
                }
                if (v <= alpha)
                {
                
                    utilityMovePair[0] = v;
                    utilityMovePair[1] = move;
                    return utilityMovePair;
                }

            }  
        }
        utilityMovePair[0] = v;
        utilityMovePair[1] = move;
        return utilityMovePair;
    }

    int[] ACTIONS(int[][] occupiedSpacesN)
    {
        int[] availableActions = {7, 7, 7, 7, 7, 7, 7}; // 7 will act as a sentinel since it is an int but not a valid column number
        int actionCounter = 0;
        // returns a list of the possible actions during a given state, these will be listed as available column numbers

        // choose an available row from the specified column
        for (int c = 0; c < 7; c++) // check each column that has an available spot
        {
            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[c][r] == 0)
                { 
                    availableActions[actionCounter] = c;
                    actionCounter++;
                    break;
                }
                else if (r == 0)
                {
                    r = 5;
                }
            }
        }
        return availableActions;

    }

    int[][] RESULT(int[][] state, int action)
    {
        // take the given action and place it in the given array

        // defines the state resulting from taing action a in state s
    
        // make a copy of the state called the result state so it can be modified to see the effect of a particular move in the current state
        int[][] resultState = new int[7][6];

        for (int c = 0; c < 7; c++)
        {
            int[] stateColumn = state[c];
            System.arraycopy(stateColumn, 0, resultState[c], 0, 6);
        }

        for ( int r = 5; r >= 0; r--)
        {
            if (resultState[action][r] == 0)
            { // if the lowest row in the column is empty
                // assign it to whose turn it is
                // in this case, the column buttons are only for use of player 1 (the human player)

                // set the occupied space to 2 to represent player 2's piece
                resultState[action][r] = 2;
                break;
            }
        }

        //playerMoveMade = false;
        //CPUTurnCounter++;

        return resultState;
    }

    public static void main(String[] args)
    {
        //int userTurnCounter = 0, CPUTurnCounter = 0;
        // tracking occupied spaces on board
        int[] occupiedSpaces0 = {0,0,0,0,0,0};
        int[] occupiedSpaces1 = {0,0,0,0,0,0};
        int[] occupiedSpaces2 = {0,0,0,0,0,0};
        int[] occupiedSpaces3 = {0,0,0,0,0,0};
        int[] occupiedSpaces4 = {0,0,0,0,0,0};
        int[] occupiedSpaces5 = {0,0,0,0,0,0};
        int[] occupiedSpaces6 = {0,0,0,0,0,0};

        int[][] occupiedSpacesN = {occupiedSpaces0, occupiedSpaces1, occupiedSpaces2, occupiedSpaces3, occupiedSpaces4, occupiedSpaces5, occupiedSpaces6};

        commandLineGameBoard Connect4 = new commandLineGameBoard();
        Connect4.startGame(occupiedSpacesN);
    }

}
