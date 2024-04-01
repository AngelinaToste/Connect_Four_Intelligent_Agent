/* CSCI 384- Project 1,  Angelina Toste 
    purpose: create an intelligent game agent to play a two-agent game of Connect Four
    Rules of the game: 
        - game board: 6 rows x 7 columns
        - tiles inserted from the top 7 columns only, they sink to the lowest position when placed on the board
        - winner determined by having pieces in one of the goal positions horizontal, diagonal, or vertical (4 pieces)
    Define a State:
        - initial state is the empty board  Board[42] = {0,0,0....} 
        - future states an array of the 42 spaces, 0 meaning no piece there, 1 meaning player 1's piece, and 2 meaning player 2's piece.
        - goal positions
            mathematical way to determine it:
                horizontal (in a single row): pos[i], pos[i+1], pos [i+2], pos[i+3]
                vertical (in a single column): pos[i], pos[i+7], pos[i+14], pos[i+21]
                diagonal: pos[i], pos[i+8], pos[i+16], pos[i+24]
            
         
Functionality needed in the program
- GUI interface for the Board
- ability to click a column or column number to insert a new tile
- function to insert tile: this function will check the column from the bottom up to find the first zero space and insert tile there
- function for goal evaluation after a tile has been inserted
- minimax algorithm for the computer to determine the next best available move also using alpha beta pruning
    + additionally use heuristic function to determine the best choice based on defense or offense move

            { 0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
            }
*/

/*
 * List of functionalities to achieve today:
 * X- make game board
 * X- make tiles
 * X- make button/ way of user putting tiles in the columns on the board
 * X- reflect the user's moves in the array
 * - use the intelligent agent to choose the next move 
 *      (there are always 7 possible options on each turn-- unless a column is completely full)
 * - win validation
 * - also include tie validation
 * 
 */

import javax.swing.*;
import java.util.*;

import java.awt.*;

import java.awt.event.*; 





public class gameBoard extends JFrame
{
    Color tileColor = Color.BLACK;
    int whoseTurn = 0, turnCounter = 0;
    //int[] CPUMove = CPUTurn(whoseTurn);
    boolean playerMoveMade = false, startPressed = false;
    int userTurnCounter = 0, CPUTurnCounter = 0;

    JFrame self;

    // tracking occupied spaces on board
    int[] occupiedSpaces0 = {0,0,0,0,0,0};
    int[] occupiedSpaces1 = {0,0,0,0,0,0};
    int[] occupiedSpaces2 = {0,0,0,0,0,0};
    int[] occupiedSpaces3 = {0,0,0,0,0,0};
    int[] occupiedSpaces4 = {0,0,0,0,0,0};
    int[] occupiedSpaces5 = {0,0,0,0,0,0};
    int[] occupiedSpaces6 = {0,0,0,0,0,0};

    public gameBoard ()
    {

        /* ---------------------------------------------------------GUI SETUP--------------------------------------------- */
        setTitle("Connect Four Intelligent Agent");
        setSize(800, 700);
        setLayout(null);
        JLabel PlayerTurnMessage = new JLabel("");
        PlayerTurnMessage.setBounds( 20, 10, 400, 20);
        JButton c0 = new JButton("0"), c1 = new JButton("1"), c2 = new JButton("2"), 
                c3 = new JButton("3"), c4 = new JButton("4"), c5 = new JButton("5"),
                c6 = new JButton("6"), start = new JButton("Start Game!"), quitGame = new JButton("Quit Game"),
                r0c0 = new JButton(), r0c1 = new JButton(), r0c2 = new JButton(), r0c3 = new JButton(), r0c4 = new JButton(), r0c5 = new JButton(), r0c6 = new JButton(),
                r1c0 = new JButton(), r1c1 = new JButton(), r1c2 = new JButton(), r1c3 = new JButton(), r1c4 = new JButton(), r1c5 = new JButton(), r1c6 = new JButton(),
                r2c0 = new JButton(), r2c1 = new JButton(), r2c2 = new JButton(), r2c3 = new JButton(), r2c4 = new JButton(), r2c5 = new JButton(), r2c6 = new JButton(),
                r3c0 = new JButton(), r3c1 = new JButton(), r3c2 = new JButton(), r3c3 = new JButton(), r3c4 = new JButton(), r3c5 = new JButton(), r3c6 = new JButton(),
                r4c0 = new JButton(), r4c1 = new JButton(), r4c2 = new JButton(), r4c3 = new JButton(), r4c4 = new JButton(), r4c5 = new JButton(), r4c6 = new JButton(),
                r5c0 = new JButton(), r5c1 = new JButton(), r5c2 = new JButton(), r5c3 = new JButton(), r5c4 = new JButton(), r5c5 = new JButton(), r5c6 = new JButton();
        
        int startingX = 75, bWidth = 50, bHeight = 20, spacing = 65;
        c0.setBounds(startingX, 50, bWidth, bHeight);
        c1.setBounds(startingX + (spacing), 50, bWidth, bHeight);
        c2.setBounds(startingX + (2 * spacing), 50, bWidth, bHeight);
        c3.setBounds(startingX + (3 * spacing), 50, bWidth, bHeight);
        c4.setBounds(startingX + (4 * spacing), 50, bWidth, bHeight);
        c5.setBounds(startingX + (5 * spacing), 50, bWidth, bHeight);
        c6.setBounds(startingX + (6 * spacing), 50, bWidth, bHeight);
        start.setBounds (570, 100, 200, 300);
        quitGame.setBounds (570, 400, 200, 200);
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        quitGame.setFont(new Font("Arial", Font.PLAIN, 20));

        //organizing buttons in each column
        JButton[] column0 = {r0c0, r1c0, r2c0, r3c0, r4c0, r5c0};
        JButton[] column1 = {r0c1, r1c1, r2c1, r3c1, r4c1, r5c1};
        JButton[] column2 = {r0c2, r1c2, r2c2, r3c2, r4c2, r5c2};
        JButton[] column3 = {r0c3, r1c3, r2c3, r3c3, r4c3, r5c3};
        JButton[] column4 = {r0c4, r1c4, r2c4, r3c4, r4c4, r5c4};
        JButton[] column5 = {r0c5, r1c5, r2c5, r3c5, r4c5, r5c5};
        JButton[] column6 = {r0c6, r1c6, r2c6, r3c6, r4c6, r5c6};

        JButton[][] columns = {column0, column1, column2, column3, column4, column5, column6};

        // when the window is closed, the program will exit
        addWindowListener(new WindowAdapter() { 
            @Override
            public void windowClosing(WindowEvent e) 
            { 
                System.exit(0); 
            } 
        }); 


        int xSpacing = 65, ySpacing = 80;
    

        // need to use unclickable buttons to represent the blank spaces where the tiles are
        
        r0c0.setBounds(80 + (65 * 0), 110 + (80 * 0 ), 50, 50);
        r1c0.setBounds(80 + (xSpacing * 0), 110 + (ySpacing * 1 ), 50, 50);
        r2c0.setBounds(80 + (xSpacing * 0), 110 + (ySpacing * 2 ), 50, 50);
        r3c0.setBounds(80 + (xSpacing * 0), 110 + (ySpacing * 3 ), 50, 50);
        r4c0.setBounds(80 + (xSpacing * 0), 110 + (ySpacing * 4 ), 50, 50);
        r5c0.setBounds(80 + (xSpacing * 0), 110 + (ySpacing * 5 ), 50, 50);
        
        r0c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 0 ), 50, 50);
        r1c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 1 ), 50, 50);
        r2c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 2 ), 50, 50);
        r3c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 3 ), 50, 50);
        r4c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 4 ), 50, 50);
        r5c1.setBounds(80 + (xSpacing * 1), 110 + (ySpacing * 5 ), 50, 50);

        r0c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 0 ), 50, 50);
        r1c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 1 ), 50, 50);
        r2c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 2 ), 50, 50);
        r3c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 3 ), 50, 50);
        r4c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 4 ), 50, 50);
        r5c2.setBounds(80 + (xSpacing * 2), 110 + (ySpacing * 5 ), 50, 50);

        r0c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 0 ), 50, 50);
        r1c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 1 ), 50, 50);
        r2c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 2 ), 50, 50);
        r3c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 3 ), 50, 50);
        r4c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 4 ), 50, 50);
        r5c3.setBounds(80 + (xSpacing * 3), 110 + (ySpacing * 5 ), 50, 50);

        r0c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 0 ), 50, 50);
        r1c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 1 ), 50, 50);
        r2c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 2 ), 50, 50);
        r3c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 3 ), 50, 50);
        r4c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 4 ), 50, 50);
        r5c4.setBounds(80 + (xSpacing * 4), 110 + (ySpacing * 5 ), 50, 50);

        r0c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 0 ), 50, 50);
        r1c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 1 ), 50, 50);
        r2c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 2 ), 50, 50);
        r3c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 3 ), 50, 50);
        r4c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 4 ), 50, 50);
        r5c5.setBounds(80 + (xSpacing * 5), 110 + (ySpacing * 5 ), 50, 50);

        r0c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 0 ), 50, 50);
        r1c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 1 ), 50, 50);
        r2c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 2 ), 50, 50);
        r3c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 3 ), 50, 50);
        r4c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 4 ), 50, 50);
        r5c6.setBounds(80 + (xSpacing * 6), 110 + (ySpacing * 5 ), 50, 50);


        /*----------------------- BUTTON ACTION LISTENER SETUP  --------------------------------------------------------------- */

        start.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                /*
                 * choose a random number to determine who will go first
                 * display who will go first and their color
                 * 
                 * player1 -user - red
                 * player2- cpu - yellow
                 */

                 Random startingPlayer = new Random();
                 whoseTurn = startingPlayer.nextInt(2);

                 tileColor = Color.YELLOW;

                 if (whoseTurn == 1)
                 {
                    PlayerTurnMessage.setText("Player 2 (CPU) Goes First- YELLOW");

                    //CPU goes first
                    CPUTurn(columns, PlayerTurnMessage);
                    
                    PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");

                 }
                 else
                 {
                    PlayerTurnMessage.setText("Player 1 (YOU) Goes First- RED (Select a column to start your turn)");

                 }
                
            }

        });
        
         
        c0.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(0, columns, PlayerTurnMessage);  

                
                
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");

                CPUTurn(columns, PlayerTurnMessage);
                
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(1, columns, PlayerTurnMessage); 

                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(2, columns, PlayerTurnMessage); 

                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(3, columns, PlayerTurnMessage); 

                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(4, columns, PlayerTurnMessage); 

                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c5.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(5, columns, PlayerTurnMessage); 

                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;
            }

        });
        c6.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                playerMove(6, columns, PlayerTurnMessage); 
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
                
                CPUTurn(columns, PlayerTurnMessage);
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
                playerMoveMade = false;

            }

        });

        quitGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // allow the user to exit the game
                System.exit(0); 

            }

        });
        


        add(c0);
        add(start);
        add(c1);
        add(c2);
        add(c3);
        add(c4);
        add(c5);
        add(c6);
        add(quitGame);

        add(r0c0);
        add(r1c0);
        add(r2c0);
        add(r3c0);
        add(r4c0);
        add(r5c0);

        add(r0c1);
        add(r1c1);
        add(r2c1);
        add(r3c1);
        add(r4c1);
        add(r5c1);

        add(r0c2);
        add(r1c2);
        add(r2c2);
        add(r3c2);
        add(r4c2);
        add(r5c2);


        add(r0c3);
        add(r1c3);
        add(r2c3);
        add(r3c3);
        add(r4c3);
        add(r5c3);

        add(r0c4);
        add(r1c4);
        add(r2c4);
        add(r3c4);
        add(r4c4);
        add(r5c4);

        add(r0c5);
        add(r1c5);
        add(r2c5);
        add(r3c5);
        add(r4c5);
        add(r5c5);

        add(r0c6);
        add(r1c6);
        add(r2c6);
        add(r3c6);
        add(r4c6);
        add(r5c6);
        add(PlayerTurnMessage);

        setVisible(true);



    }

    // make a function to determine the results of the CPU's turn
    public void CPUTurn( JButton columns[][], JLabel PlayerTurnMessage )
    {
        
        int[] move = new int[2]; // format is {row, column}

            // choose a random column
            Random randCol = new Random();
            move[1] = randCol.nextInt(7);
            
            // array of the column arrays

            int[][] occupiedSpacesN = {occupiedSpaces0, occupiedSpaces1, occupiedSpaces2, occupiedSpaces3, occupiedSpaces4, occupiedSpaces5, occupiedSpaces6};
           
            // choose an available row from the specified column
            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[move[1]][r] == 0)
                { // if the lowest row in the column is empty
                    // assign it to whose turn it is
                    // in this case, the column buttons are only for use of player 1 (the human player)

                    // find the correct column array and set the button in the correct row to yellow
                    

                    columns[move[1]][r].setBackground(Color.YELLOW);

                    // set the occupied space to 2 to represent player 2's piece
                    occupiedSpacesN[move[1]][r] = 2;


                    break;
                }
            }
            playerMoveMade = false;
            CPUTurnCounter++;
             
            if  (CPUTurnCounter > 3 )
            {
                isWin(2, occupiedSpacesN);
            }
            
    }


    public void playerMove(int colNum, JButton columns[][], JLabel PlayerTurnMessage)
    {
        if (!playerMoveMade)
        {
            //paint the lowest possible token in this column a specific color (depending on whose turn it is)
            /*
            * - find the lowest token
            * - check the availability of other tokens in the column. 
            *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
            *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
            */
            // set the new tile color to red for the user
            tileColor = Color.RED;
            int[][] occupiedSpacesN = {occupiedSpaces0, occupiedSpaces1, occupiedSpaces2, occupiedSpaces3, occupiedSpaces4, occupiedSpaces5, occupiedSpaces6};

            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[colNum][r] == 0)
                { // if the lowest row in the column is empty
                    // assign it to whose turn it is
                    // in this case, the column buttons are only for use of player 1 (the human player)

                    occupiedSpacesN[colNum][r] = 1;
                    columns[colNum][r].setBackground(tileColor);
                    columns[colNum][r].repaint();
                    //invokeAndWait();
                    playerMoveMade = true;
                    break;
                }
            }

            userTurnCounter++;
             
            if  ( userTurnCounter > 3)
            {
                isWin( 1, occupiedSpacesN);
                //System.out.println("1");
                //displayWinner(PlayerTurnMessage, 1);
                //PlayerTurnMessage.setText("Player 1 (YOU) WINS");
            }
            
            // issue warning "this column is full, choose another."

            
        }

    }

    public Boolean isWin( int playerNum, int occupiedSpacesN[][])
    { //int tilesPlayed,
        // purpose: check if there are more than 4 tiles played by each player, check for col row and diag rows of 4
        // parameters 
            //tilesPlayed - num of tiles played to determine if there are enough tiles for 4 to be in a row
            //playerNum - the player that is being checked for a win
        // return  boolean true if a win exists
        Boolean isWin = false;

        //if (tilesPlayed > 3)
        //{
        int inACol = 0; // counter to determine how many of the same tile are consecutive in a column ex r0c0 r1c0 c2c0 r3c0
        // check for connect 4 in each column

        for(int c = 0; c < 8; c++) // for number of columns
        {
            for (int r = 0; r < 7; r++) // for number of rows in a column
            {
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
                        JOptionPane.showMessageDialog(self, "WINNER IS PLAYER " + playerNum + "!\nClick 'Start Game' to play again or 'Exit Game' to close the game.");
                        break;

                    }
                
                }
                else 
                {
                    // reset inACol
                    inACol = 0;
                }
            }
        }

        //}
       // else
        //{
            //isWin = false;
        //}

        return isWin;
    }


    public static void main (String[] args)
    {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gameBoard game = new gameBoard();
                game.self = game;
                game.setVisible(true);
              
            }
          });
    }  
}
