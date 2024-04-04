import java.util.*;

//import javax.swing.JOptionPane;

//import javax.swing.JButton;
//import javax.swing.JLabel;
////import javax.swing.JOptionPane;

//import javafx.scene.paint.Color;

public class commandLineGameBoard {

    boolean playerMoveMade;

    
    commandLineGameBoard()
    {
        
    }
    
    
    void startGame(int[][] occupiedSpacesN)
    {
        int userTurnCounter = 0, CPUTurnCounter = 0;
        
        playerMoveMade = false;
        System.out.println("Starting a Connect Four Game");

        //print the board
        displayBoard(occupiedSpacesN);

        while (userTurnCounter + CPUTurnCounter < 42)
        {
            // ask the user to choose a value
            System.out.println("Player 1 (YOU) Chooses a Column, 0-6");
            Scanner keyboard = new Scanner(System.in);

            int columnChoice = keyboard.nextInt();

            playerMove(columnChoice, occupiedSpacesN, playerMoveMade, userTurnCounter);
            displayBoard(occupiedSpacesN);
            playerMoveMade = true;

            System.out.println("Player 2 (CPU) Chooses a Column");
            CPUTurn(occupiedSpacesN, CPUTurnCounter);
            displayBoard(occupiedSpacesN);
            

        }
        


    }

    public void CPUTurn( int[][] occupiedSpacesN, int CPUTurnCounter)
    {
        if (playerMoveMade)
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


                    break;
                }
                else if (r == 0)
                {
                    System.out.println("Choosing a new column1");
                    move[1] = randCol.nextInt(7);
                }
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

    public void playerMove(int colNum, int[][] occupiedSpacesN, boolean playerMoveMade, int userTurnCounter)
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

        //FIXME? is there an issue with column checking or are threads causing it to trigger too early?
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

        commandLineGameBoard Connect4 = new commandLineGameBoard();
        Connect4.startGame(occupiedSpacesN);
    }

}
