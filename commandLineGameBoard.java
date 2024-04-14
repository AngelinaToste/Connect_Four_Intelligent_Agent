// Angelina Toste
// Connect Four Intelligent Agent Command Line Game Board
import java.util.*;

import javax.swing.*;
import java.util.*;

import java.awt.*;

import java.awt.event.*; 

public class commandLineGameBoard extends JFrame implements ActionListener
{

    private static final int ROWSIZE = 6;
    private static final int COLSIZE = 7;
    private static final int MAX_VALUE = 100;  
    private static final int MIN_VALUE = -100; 

    boolean playerMoveMade;
    int userTurnCounter = 0, CPUTurnCounter = 0;
    int userPoints = 100, CPUPoints = -100;
    int[] lastMove = {7, 7};
    int[] CPULastMove = {7, 7};

    
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
            // make the cpus first move random 
            if (CPUTurnCounter == 0)
            {
                int[] move = new int[2]; // format is {row, column}

                // choose a random column
                Random randCol = new Random();
                move[1] = randCol.nextInt(7);
           
                // choose an available row from the specified column
                for ( int r = 5; r >= 0; r--)
                {
                    if (occupiedSpacesN[move[1]][r] == 0)
                    { // if the lowest row in the column is empty
                        // assign it to whose turn it is
                        // in this case, the column buttons are only for use of player 1 (the human player)

                        // set the occupied space to 2 to represent player 2's piece
                        occupiedSpacesN[move[1]][r] = 2;
                        System.out.println("row "+ move[0] + " col " + move[1]);
                        lastMove[0] = r;
                        lastMove[1] = move[1];
                        CPULastMove[0] = r;
                        CPULastMove[1] = move[1];
                        playerMoveMade = false;
                        CPUTurnCounter++;
                        break;
                    }
                }
            }
            else
            {
                int depth = ROWSIZE * COLSIZE;

                int[] move = nextMove(occupiedSpacesN, depth, MIN_VALUE, MAX_VALUE, false);

                occupiedSpacesN[move[1]][move[0]] = 2;
                lastMove[0] = move[0];
                lastMove[1] = move[1];
                CPULastMove[0] = move[0];
                CPULastMove[1] = move[1];

                System.out.println("row "+ move[0] + " col " + move[1]);
                playerMoveMade = false;
            
                CPUTurnCounter ++;

            }
            
            if  ( CPUTurnCounter > 3 && isWin( false, occupiedSpacesN, lastMove))
            {
                System.out.println("done");
                System.exit(0);
            }
            
            
        }   
          
        return CPUTurnCounter;  
    }
    public int[] nextMove(int[][] state, int depth, int alpha, int beta, boolean isMaximizingPlayer) // returns an integer array of row, col, and the score
    {
        // use this to return an array for the move 
        int bestScore = MAX_VALUE; //unreachably low score to maximize against
        
        
        // collection of possible columns to choose
        int [] actions = ACTIONS(state, CPULastMove, isMaximizingPlayer);
        int[] address = {7, 7, 7};
        //int[][] possibleAddress = new int[actions.length][3]; //will contain the row and column of the best square to select
        //int addressCapacity = 0;
        //iterate over the available columns to place a tile

        for (int a = 0; a < actions.length; a++)
        {
            if (actions[a] < 7)
            {

                //insert the playerNum into the specified column
                for ( int r = 5; r >= 0; r--)
                {
                        if (state[actions[a]][r] == 0)
                        {
                            //int score = AlphaBeta(state, depth, alpha, beta, isMaxixingPlayer, lastMove);
                            int score = AlphaBeta(state, depth, alpha, beta, isMaximizingPlayer, lastMove); //call the minimax on each empty score for the opponent, who is minimizing
                            //System.out.println(score);
                            state[actions[a]][r] = 2;

                            if(score < bestScore) // old score < bestScore
                            {
                                //System.out.println(score);
                                bestScore = score;
                                address[0] = r;
                                address[1] = actions[a];
                                address[2] =  score; // row, column, score
                               
                            }
                            state[actions[a]][r] = 0;
                            break;
                        }
                }
            }
        }

        if (address[0] == 7)
        {
            System.out.println("Address is 7");
        }

        return address;
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
            else if ( inACol == 0 && (state[lastMove[1]][r] == playerNum))
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
        // FIXME why is it not affected by the inital placement of it's last move.
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
    // still issues with predictability.
    // add in some randomness, especially with CPU's first move, randomness can also be added in to determining the best move
    // may help resolve any ties with multiple moves with the same best heuristic
    int AlphaBeta(int[][] state, int depth, int alpha, int beta, boolean isMaximizingPlayer, int[] lastMove)
    {
        
        int score = 0;
        if (isMaximizingPlayer)
        {
            score= heuristicScore(state, lastMove, isMaximizingPlayer);
        }
        else
        {
            score= heuristicScore(state, CPULastMove, isMaximizingPlayer);
        }

        if (depth == 0 || score == MAX_VALUE || score == MIN_VALUE)  
        {return score; }
        else if (depth == 37) 
        {return score; }
        if (isMaximizingPlayer) 
        {  
            int maxScore = MIN_VALUE;  

            // for each a in game.ACTIONS(state) do
            int [] actions = ACTIONS(state, lastMove, isMaximizingPlayer);

            for (int a = 0; a < actions.length; a++)
            {
                if (actions[a] < 7)
                {

                    //insert a 1 into the specified column
                    for ( int r = 5; r >= 0; r--)
                    {
                        if (state[actions[a]][r] == 0)
                        {
                            state[actions[a]][r] = 1;

                            score = AlphaBeta(state, depth - 1, alpha, beta, false, lastMove);
                            maxScore = Math.max(maxScore, score);
                            alpha = Math.max(alpha, maxScore);
                            state[actions[a]][r] = 0; // reset the change made
                            
                            if (beta <= alpha){ break; }
                        }
                    }
                }

            }
            //System.out.println("algo end");
            return maxScore;  
        }

        else 
        {  
            int minScore = MAX_VALUE;  

            // for each a in game.ACTIONS(state) do
            int [] actions = ACTIONS(state, CPULastMove, isMaximizingPlayer);

            for (int a = 0; a < actions.length; a++)
            {
                if (actions[a] < 7)
                {

                    //insert a 2 into the specified column
                    for ( int r = 5; r >= 0; r--)
                    {
                        if (state[actions[a]][r] == 0)
                        {
                            state[actions[a]][r] = 2;
                            
                            score = AlphaBeta(state, depth - 1, alpha, beta, true, lastMove);
                            minScore = Math.min(minScore, score);
                            beta = Math.min(beta, minScore);
                            state[actions[a]][r] = 0; // reset the change made
                            
                            if (beta <= alpha){ break; }
                            
                        }
                    }
                }

            }
            //System.out.println("algo end");
            return minScore;  
        }  
    } 

    int[] ACTIONS(int[][] occupiedSpacesN, int[] lastMove, boolean isMaximizingPlayer)
    { 
        int playerNum = 0;
        if (isMaximizingPlayer){playerNum = 1;}
        else{playerNum = 2;}
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

            }
        }

        // attempt to reorder the actions array to prioritize moves at or close to the column that the last move was in
        int temp, x = 0, y = 1, z = 2; //openSpaces = 0, blocked = 0;
        int[] localMoves = {7,7,7};

        if ((lastMove[1] >= 1) && (lastMove[1] <= 5))
        {
            localMoves[0] = lastMove[1];
            localMoves[1] =  (lastMove[1]-1);
            localMoves[2] = (lastMove[1]+1);  // moves to prioritize if they are available
        }
        else if (lastMove[1] == 0)
        {
            localMoves[0] = lastMove[1];
            localMoves[1] =  (lastMove[1]+1);
            localMoves[2] = (lastMove[1]+2);  // moves to prioritize if they are available
        }
        else if (lastMove[1] == 6)
        {
            localMoves[0] = lastMove[1];
            localMoves[1] =  (lastMove[1]-1);
            localMoves[2] = (lastMove[1]-2);  // moves to prioritize if they are available
        }

        

        int[][] openAndBlocked  = {isBlocked (playerNum, localMoves[0], occupiedSpacesN), isBlocked (playerNum, localMoves[1], occupiedSpacesN), isBlocked (playerNum, localMoves[2], occupiedSpacesN)};

        int[] localMoveOrder = {7, 7, 7};


        for (int t = 0; t < 3; t++)
        {
            // if a move is blocked
            if ((openAndBlocked[t][0] < 4) && (openAndBlocked[t][1] == 1))
            {
                localMoveOrder[t] = availableActions.length-(t+1);
            }
            
        }

        for (int t = 0; t < 3; t++)
        {
            // if a move is blocked
            if ((localMoveOrder[t] == 7) && (localMoveOrder[0] != 0) && (localMoveOrder[1] != 0) && (localMoveOrder[2] != 0))
            {
                localMoveOrder[t] = 0;
            }
            else if ((localMoveOrder[t] == 7) && (localMoveOrder[0] != 1) && (localMoveOrder[1] != 1) && (localMoveOrder[2] != 1))
            {
                localMoveOrder[t] = 1;
            }
            else if ((localMoveOrder[t] == 7) && (localMoveOrder[0] != 2) && (localMoveOrder[1] != 2) && (localMoveOrder[2] != 2))
            {
                localMoveOrder[t] = 2;
            }
            
        }

        x = localMoveOrder[0];
        y = localMoveOrder[1];
        z = localMoveOrder[2];

        // orders the columns based on the ordering provided through checking for blocks.

        for (int a = 0; a < availableActions.length; a++)
        {
            if (availableActions[a] == (lastMove[1] - 1))
            {
                // swap the current action with the one in the second position in the array
                temp = availableActions[y];
                availableActions[y] = availableActions[a];
                availableActions[a] = temp;
            }
            else if (availableActions[a] == lastMove[1])
            {
                // swap the current action with the one in the first position in the array
                temp = availableActions[x];
                availableActions[x] = availableActions[a];
                availableActions[a] = temp;
            }
            else if (availableActions[a] == (lastMove[1] + 1))
            {
                // swap the current action with the one in the second position in the array
                temp = availableActions[z];
                availableActions[z] = availableActions[a];
                availableActions[a] = temp;
            }
            
        }

        //JOptionPane.showMessageDialog(self,availableActions[0]+" "+ availableActions[1]+" "+availableActions[2]+" "+availableActions[3]+" "+ availableActions[4] +" "+availableActions[5]+" "+ availableActions[6]);
        return availableActions;

    }

    int[] isBlocked (int playerNum, int column, int occupiedSpacesN[][])
    {
        int openSpaces = 0, blocked = 0;
        // if there is no more room in the column-- set the x, y, z to different values, (prioritize y and z)
        for ( int r = 0; r < 6; r++)
        {
            if (occupiedSpacesN[column][r] == 0)
            {
                openSpaces++;
            }
            else if (occupiedSpacesN[column][r] != playerNum)
            {
               blocked++;
               break;
            }
            

        }

        int[] openAndBlocked = {openSpaces, blocked};
        //JOptionPane.showMessageDialog(self, openSpaces);
        return openAndBlocked;
        
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

