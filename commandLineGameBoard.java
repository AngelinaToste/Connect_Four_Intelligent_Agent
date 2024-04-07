import java.util.*;

public class commandLineGameBoard 
{

    private static final int ROWSIZE = 6;
    private static final int COLSIZE = 7;
    private static final int MAX_VALUE = 100;  
    private static final int MIN_VALUE = -100; 

    boolean playerMoveMade;
    int userTurnCounter = 0, CPUTurnCounter = 0;
    int userPoints = 100, CPUPoints = -100;
    int[] lastMove = {7, 7};
    //private static final int MAX_VALUE = 100

    
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

            userTurnCounter = playerMove(columnChoice, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove);
            displayBoard(occupiedSpacesN);
            playerMoveMade = true;

            System.out.println("Player 2 (CPU) Chooses a Column");
            CPUTurnCounter = CPUTurn(occupiedSpacesN, CPUTurnCounter, lastMove);
            displayBoard(occupiedSpacesN);
            

        }
        


    }

    public int CPUTurn( int[][] occupiedSpacesN, int CPUTurnCounter, int[] lastMove)
    {
        if (playerMoveMade)
        {
            int depth = ROWSIZE * COLSIZE;

            int[] utilityMovePair = AlphaBeta(occupiedSpacesN, depth, MIN_VALUE, MAX_VALUE, false, lastMove);

            int move = utilityMovePair[1];

            System.out.println(utilityMovePair[0]+" "+ utilityMovePair[1]);

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
                    lastMove[0] = r;
                    lastMove[1] = move;


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
             
            if  (CPUTurnCounter > 3 && isWin(false, occupiedSpacesN, lastMove))
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

    public int playerMove(int colNum, int[][] occupiedSpacesN, boolean playerMoveMade, int userTurnCounter, int[] lastMove)
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
                    lastMove[0] = r;
                    lastMove[1] = colNum;
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
             
        if  ( userTurnCounter > 3 && isWin( true, occupiedSpacesN, lastMove))
        {
            System.out.println("done");
            System.exit(0);
                //clearBoard(occupiedSpacesN);
                //playerMoveMade =  false;
        }

        return userTurnCounter;
            
    }

    public Boolean isWin(boolean isMaximizingPlayer, int[][] state, int[] lastMove) 
    {
        
        
        //int tilesPlayed,
        // purpose: check if there are more than 4 tiles played by each player, check for col row and diag rows of 4
        // parameters 
            //tilesPlayed - num of tiles played to determine if there are enough tiles for 4 to be in a row
            //playerNum - the player that is being checked for a win
        // return  boolean true if a win exists
        Boolean isWin = false;

        int score = 0, playerNum, inACol = 0, inARow = 0, inADecDiagonal = 0, inAnIncDiagonal = 0;

        if (isMaximizingPlayer){playerNum = 1;}
        else {playerNum = 2;}

        // check the COLUMN that the lastMove is in, reward points accordingly
        for (int r = 5; r >= 0; r--)
        {
            if (state[lastMove[1]][r] == 0)
            {// don't look through a column if the lowest index is a 0
                // reset inACol
                inACol = 0;
                break;
            } 
            else if ( inACol == 0 && state[lastMove[1]][r] == playerNum) 
            {
                inACol++;
            }
            else if ( inACol < 4 && state[lastMove[1]][r] == playerNum)
            {
                inACol++;
                if (inACol == 4)
                {
                    isWin = true;
                    displayBoard(state);
                    System.out.println("COLUMN WINNER IS PLAYER " + playerNum + " wining move: row " + r + " column" + lastMove[1]);
                    if ( playerNum == 1) {return isWin;} // max value
                    else if ( playerNum == 2) {return isWin;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inACol)
                    {
                        case 2: 
                            //score = score + 20;
                            break;
                        case 3:
                            //score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inACol)
                    {
                        case 2: 
                            //score = score - 20;
                            break;
                        case 3:
                            //score = score - 30;
                            break;
                    } 
                }
                
                
                // reset inACol
                inACol = 0;
            }
        }

        // check the ROW that the lastMove is in, reward points accordingly
        for (int c = 0; c < 7; c++)
        {

            if ( inARow == 0 && state[c][lastMove[0]] == playerNum) 
            {
                inARow++;
            }
            else if ( inARow < 4 && state[c][lastMove[0]] == playerNum)
            {
                inARow++;
                if (inARow == 4)
                {
                    isWin = true;
                    displayBoard(state);
                    System.out.println("ROW WINNER IS PLAYER " + playerNum + " wining move: row " + lastMove[0] + " column" + c);
                    if ( playerNum == 1) {return isWin;} // max value
                    else if ( playerNum == 2) {return isWin;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inARow)
                    {
                        case 2: 
                            //score = score + 20;
                            break;
                        case 3:
                            //score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inARow)
                    {
                        case 2: 
                            //score = score - 20;
                            break;
                        case 3:
                            //score = score - 30;
                            break;
                    } 
                }
                
                
                // reset inACol
                inARow = 0;
            }
        }
        
        
        // check the DECREASING DIAGONAL that the lastMove is in, reward points accordingly

        int[] startingPosition = new int[2];

        int row = lastMove[0], col = lastMove[1], dif = row-col;

        
        switch(dif)
        {
            case 0:
                startingPosition[0] = 0;
                startingPosition[1] = 0;
                break;
            case 1:
                startingPosition[0] = 1;
                startingPosition[1] = 0;
                break;
            case -1:
                startingPosition[0] = 0;
                startingPosition[1] = 1;
                break;
            case 2:
                startingPosition[0] = 2;
                startingPosition[1] = 0;
                break;
            case -2:
                startingPosition[0] = 0;
                startingPosition[1] = 2;
                break;
            case -3:
                startingPosition[0] = 0;
                startingPosition[1] = 3;
                break;
        }

        for (int c = startingPosition[1], r = startingPosition[0]; c < 7 && r < 6; c++, r++) // c for number of columns (0-6) and r for number of rows in a column (0-5)
        {
            if ( inADecDiagonal == 0 && state[c][r] == playerNum)
            {
                inADecDiagonal++;
            }
            else if ( inARow < 4 && state[c][r] == playerNum)
            {
                inADecDiagonal++;
                if (inADecDiagonal == 4)
                {
                    isWin = true; 
                    displayBoard(state);
                    System.out.println("DECREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
                    if ( playerNum == 1) {return isWin;} // max value
                    else if ( playerNum == 2) {return isWin;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inADecDiagonal)
                    {
                        case 2: 
                            //score = score + 20;
                            break;
                        case 3:
                            //score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inADecDiagonal)
                    {
                        case 2: 
                            //score = score - 20;
                            break;
                        case 3:
                            //score = score - 30;
                            break;
                    } 
                }

                // reset inADiagonal
                inADecDiagonal = 0;
            }
        }

        // check the INCREASING DIAGONAL that the lastMove is in, reward points accordingly

        int[] startingPosition2 = new int[2];

        int row2 = lastMove[0], col2 = lastMove[1], sum = row2 + col2;

        
        switch(sum)
        {
            case 3:
                startingPosition2[0] = 3;
                startingPosition2[1] = 0;
                break;
            case 4:
                startingPosition2[0] = 4;
                startingPosition2[1] = 0;
                break;
            case 5:
                startingPosition2[0] = 5;
                startingPosition2[1] = 0;
                break;
            case 6:
                startingPosition2[0] = 5;
                startingPosition2[1] = 1;
                break;
            case 7:
                startingPosition2[0] = 5;
                startingPosition2[1] = 2;
                break;
            case 8:
                startingPosition2[0] = 5;
                startingPosition2[1] = 3;
                break;

        }

        for (int c = startingPosition2[1], r = startingPosition2[0]; c < 7 && r >= 0; c++, r--) // c for number of columns (0-6) and r for number of rows in a column (0-5)
        {
            if ( inAnIncDiagonal == 0 && state[c][r] == playerNum)
            {
                inAnIncDiagonal++;
            }
            else if ( inARow < 4 && state[c][r] == playerNum)
            {
                inAnIncDiagonal++;
                if (inAnIncDiagonal == 4)
                {
                    isWin = true; 
                    displayBoard(state);
                    System.out.println("INCREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
                    if ( playerNum == 1) {return isWin;} // max value
                    else if ( playerNum == 2) {return isWin;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inAnIncDiagonal)
                    {
                        case 2: 
                            score = score + 20;
                            break;
                        case 3:
                            score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inAnIncDiagonal)
                    {
                        case 2: 
                            score = score - 20;
                            break;
                        case 3:
                            score = score - 30;
                            break;
                    } 
                }
                // reset inADiagonal
                inAnIncDiagonal = 0;
            }
        
        }

        return isWin;
    }

    private static int heuristicScore(int[][] state, int[] lastMove, boolean isMaximizingPlayer)
    {
        //System.out.println("heuristic start");
        // point system
        // 2 point for block 2 in a row
        // 3 point for block 3 in a row
        // 2 point for 2 in a row
        // 3 points for 3 in a row

        // may want to save most recent move and check based on that.

        int score = 0, playerNum, inACol = 0, inARow = 0, inADecDiagonal = 0, inAnIncDiagonal = 0;

        if (isMaximizingPlayer){playerNum = 1;}
        else {playerNum = 2;}

        // check the COLUMN that the lastMove is in, reward points accordingly
        for (int r = 5; r >= 0; r--)
        {
            if (state[lastMove[1]][r] == 0)
            {// don't look through a column if the lowest index is a 0
                // reset inACol
                inACol = 0;
                break;
            } 
            else if ( inACol == 0 && state[lastMove[1]][r] == playerNum) 
            {
                inACol++;
            }
            else if ( inACol < 4 && state[lastMove[1]][r] == playerNum)
            {
                inACol++;
                if (inACol == 4)
                {
                    if ( playerNum == 1) {return 100;} // max value
                    else if ( playerNum == 2) {return -100;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inACol)
                    {
                        case 2: 
                            score = score + 20;
                            break;
                        case 3:
                            score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inACol)
                    {
                        case 2: 
                            score = score - 20;
                            break;
                        case 3:
                            score = score - 30;
                            break;
                    } 
                }
                
                
                // reset inACol
                inACol = 0;
            }
        }

        // check the ROW that the lastMove is in, reward points accordingly
        for (int c = 0; c < 7; c++)
        {

            if ( inARow == 0 && state[c][lastMove[0]] == playerNum) 
            {
                inARow++;
            }
            else if ( inARow < 4 && state[c][lastMove[0]] == playerNum)
            {
                inARow++;
                if (inARow == 4)
                {
                    if ( playerNum == 1) {return 100;} // max value
                    else if ( playerNum == 2) {return -100;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inARow)
                    {
                        case 2: 
                            score = score + 20;
                            break;
                        case 3:
                            score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inARow)
                    {
                        case 2: 
                            score = score - 20;
                            break;
                        case 3:
                            score = score - 30;
                            break;
                    } 
                }
                
                
                // reset inACol
                inARow = 0;
            }
        }
        
        
        // check the DECREASING DIAGONAL that the lastMove is in, reward points accordingly

        int[] startingPosition = new int[2];

        int row = lastMove[0], col = lastMove[1], dif = row-col;

        
        switch(dif)
        {
            case 0:
                startingPosition[0] = 0;
                startingPosition[1] = 0;
                break;
            case 1:
                startingPosition[0] = 1;
                startingPosition[1] = 0;
                break;
            case -1:
                startingPosition[0] = 0;
                startingPosition[1] = 1;
                break;
            case 2:
                startingPosition[0] = 2;
                startingPosition[1] = 0;
                break;
            case -2:
                startingPosition[0] = 0;
                startingPosition[1] = 2;
                break;
            case -3:
                startingPosition[0] = 0;
                startingPosition[1] = 3;
                break;
        }

        for (int c = startingPosition[1], r = startingPosition[0]; c < 7 && r < 6; c++, r++) // c for number of columns (0-6) and r for number of rows in a column (0-5)
        {
            if ( inADecDiagonal == 0 && state[c][r] == playerNum)
            {
                inADecDiagonal++;
            }
            else if ( inARow < 4 && state[c][r] == playerNum)
            {
                inADecDiagonal++;
                if (inADecDiagonal == 4)
                {
                    //isWin = true; 
                    //displayBoard(state);
                    if ( playerNum == 1) {return 100;} // max value
                    else if ( playerNum == 2) {return -100;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inADecDiagonal)
                    {
                        case 2: 
                            score = score + 20;
                            break;
                        case 3:
                            score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inADecDiagonal)
                    {
                        case 2: 
                            score = score - 20;
                            break;
                        case 3:
                            score = score - 30;
                            break;
                    } 
                }

                // reset inADiagonal
                inADecDiagonal = 0;
            }
        }

        // check the DECREASING DIAGONAL that the lastMove is in, reward points accordingly

        int[] startingPosition2 = new int[2];

        int row2 = lastMove[0], col2 = lastMove[1], sum = row2 + col2;

        
        switch(sum)
        {
            case 3:
                startingPosition2[0] = 3;
                startingPosition2[1] = 0;
                break;
            case 4:
                startingPosition2[0] = 4;
                startingPosition2[1] = 0;
                break;
            case 5:
                startingPosition2[0] = 5;
                startingPosition2[1] = 0;
                break;
            case 6:
                startingPosition2[0] = 5;
                startingPosition2[1] = 1;
                break;
            case 7:
                startingPosition2[0] = 5;
                startingPosition2[1] = 2;
                break;
            case 8:
                startingPosition2[0] = 5;
                startingPosition2[1] = 3;
                break;

        }

        for (int c = startingPosition2[1], r = startingPosition2[0]; c < 7 && r >= 0; c++, r--) // c for number of columns (0-6) and r for number of rows in a column (0-5)
        {
            if ( inAnIncDiagonal == 0 && state[c][r] == playerNum)
            {
                inAnIncDiagonal++;
            }
            else if ( inARow < 4 && state[c][r] == playerNum)
            {
                inAnIncDiagonal++;
                if (inAnIncDiagonal == 4)
                {
                    //isWin = true; 
                    //displayBoard(state);
                    if ( playerNum == 1) {return 100;} // max value
                    else if ( playerNum == 2) {return -100;} // max value
                    break;
                }
            
            }
            else 
            {
                if (playerNum == 1)
                {
                    switch(inAnIncDiagonal)
                    {
                        case 2: 
                            score = score + 20;
                            break;
                        case 3:
                            score = score + 30;
                            break;
                    } 
                }
                else
                {
                    switch(inAnIncDiagonal)
                    {
                        case 2: 
                            score = score - 20;
                            break;
                        case 3:
                            score = score - 30;
                            break;
                    } 
                }
                // reset inADiagonal
                inAnIncDiagonal = 0;
            }
        
        }
        //System.out.println("heuristic end");
        return score;

    }

    /* --------------------------------------------------THE ALGORITHM------------------------------------------------------------------- */

    //issues with the return values of this function. how is the score being used ?
    int[] AlphaBeta(int[][] state, int depth, int alpha, int beta, boolean isMaximizingPlayer, int[] lastMove)
    {
        //System.out.println("algo start");
        int[] utilityMovePair = new int[2]; //(value, move) //FIXME doing nothing with this
        int score = heuristicScore(state, lastMove, isMaximizingPlayer);  //FIXME doing nothing with this
        utilityMovePair[0] = score; // added

        if (depth == 0 || score == MAX_VALUE || score == MIN_VALUE)  
        {return utilityMovePair; } //? need to return an int array
        else if (depth == 30) 
        {return utilityMovePair; }//? need to return an int array

        if (isMaximizingPlayer) 
        {  
            int maxScore = MIN_VALUE;  
            int[] maxScoreMove = new int[2]; //FIXME doing nothing with this

            // for each a in game.ACTIONS(state) do
            int [] actions = ACTIONS(state);

            for (int a = 0; a < actions.length; a++)
            {
                if (actions[a] < 7)
                {
                    int rowValue = 0;
                    //insert a 1 into the specified column
                    for ( int r = 5; r >= 0; r--)
                    {
                        if (state[actions[a]][r] == 0)
                        {
                            state[actions[a]][r] = 1;
                            rowValue = r;
                            break;
                        }
                    }
                    //set maxScore to the max of maxscore vs the next depth for the min player
                    int[] minScoreMove = AlphaBeta(state, depth - 1, alpha, beta, false, lastMove);
                    maxScore = Math.max(maxScore, minScoreMove[0]);
                    maxScoreMove[0] = maxScore;  //added
                    // set alpha to the maximum of alpha vs the maxScore
                    alpha = Math.max(alpha, maxScore);

                    //reset the changed value to 0
                    state[actions[a]][rowValue] = 0;
                    maxScoreMove[1] = actions[a];//added
                    if (beta <= alpha) {break;}
                }

            }
            //System.out.println("algo end");
            return maxScoreMove;  
        }

        else 
        {  
            int minScore = MAX_VALUE;  
            int[] minScoreMove = new int[2]; // FIXME doing nothing with this

            // for each a in game.ACTIONS(state) do
            int [] actions = ACTIONS(state);

            for (int a = 0; a < actions.length; a++)
            {
                if (actions[a] < 7)
                {
                    int rowValue = 0;
                    //insert a 2 into the specified column
                    for ( int r = 5; r >= 0; r--)
                    {
                        if (state[actions[a]][r] == 0)
                        {
                            state[actions[a]][r] = 2;
                            rowValue = r;
                            break;
                        }
                    }
                    //set minScore to the min of minscore vs the next depth for the max player
                    int[] maxScoreMove = AlphaBeta(state, depth - 1, alpha, beta, true, lastMove);
                    minScore = Math.min(minScore, maxScoreMove[0]);  
                    minScoreMove[0] = minScore; //added
                    // set beta to the minimum of alpha vs the minScore
                    beta = Math.min(beta, minScore);

                    //reset the changed value to 0
                    state[actions[a]][rowValue] = 0;
                    minScoreMove[1] = actions[a]; //added
                    if (beta <= alpha) {break;}
                }

            }
            //System.out.println("algo end");
            return minScoreMove;  
        }  
    } 

    int[] ACTIONS(int[][] occupiedSpacesN)
    { 
        //System.out.println("Actions");
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
                //else if (r == 0)
                //{
                  //  r = 5;
                //}
            }
        }
        //System.out.println("Actions end");
        //for (int x = 0; x < availableActions.length; x++)
        //{
            //System.out.print(availableActions[x]);
        //}
        //System.out.println();
        return availableActions;

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
