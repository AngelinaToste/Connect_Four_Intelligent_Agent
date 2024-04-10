import java.util.*;

public class commandLineGameBoardRevised 
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

    
    commandLineGameBoardRevised()
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
                         
                            state[actions[a]][r] = 2;

                            if(score < bestScore)
                            {
                                bestScore = score;
                                address[0] = r;
                                address[1] = actions[a];
                                address[2] =  score; // row, column, score
                                //possibleAddress[addressCapacity][2] =  score; // row, column, score
                                //possibleAddress[addressCapacity][0] = actions[a];
                                //possibleAddress[addressCapacity][1] = r;
                                //possibleAddress[addressCapacity][2] =  score; // row, column, score
                                //addressCapacity++;
                            }
                            state[actions[a]][r] = 0;
                            break;
                        }
                }
            }
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

        commandLineGameBoardRevised Connect4 = new commandLineGameBoardRevised();
        Connect4.startGame(occupiedSpacesN);
    }

}

