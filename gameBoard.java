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


public class gameBoard extends JFrame implements ActionListener
{
    private static final int ROWSIZE = 6;
    private static final int COLSIZE = 7;
    private static final int MAX_VALUE = 100;  
    private static final int MIN_VALUE = -100; 

    
    int userTurnCounter = 0, CPUTurnCounter = 0, userPoints = 100, CPUPoints = -100, whoseTurn = 0, turnCounter = 0;
    int[] lastMove = {7, 7};
    int[] CPULastMove = {7, 7};
    Color tileColor = Color.BLACK;
 
    boolean playerMoveMade, startPressed = false, isCleared = false;
    JFrame self;

    // tracking occupied spaces on board
    int[] occupiedSpaces0 = {0,0,0,0,0,0};
    int[] occupiedSpaces1 = {0,0,0,0,0,0};
    int[] occupiedSpaces2 = {0,0,0,0,0,0};
    int[] occupiedSpaces3 = {0,0,0,0,0,0};
    int[] occupiedSpaces4 = {0,0,0,0,0,0};
    int[] occupiedSpaces5 = {0,0,0,0,0,0};
    int[] occupiedSpaces6 = {0,0,0,0,0,0};

    int[][] occupiedSpacesN = {occupiedSpaces0, occupiedSpaces1, occupiedSpaces2, occupiedSpaces3, occupiedSpaces4, occupiedSpaces5, occupiedSpaces6};

    
    JLabel PlayerTurnMessage = new JLabel("Click the 'Start Game!' button to begin a game.");
    
    JButton c0 = new JButton("0"), c1 = new JButton("1"), c2 = new JButton("2"), 
            c3 = new JButton("3"), c4 = new JButton("4"), c5 = new JButton("5"),
            c6 = new JButton("6"), start = new JButton("Start Game!"), quitGame = new JButton("Quit Game"),
            r0c0 = new JButton(), r0c1 = new JButton(), r0c2 = new JButton(), r0c3 = new JButton(), r0c4 = new JButton(), r0c5 = new JButton(), r0c6 = new JButton(),
            r1c0 = new JButton(), r1c1 = new JButton(), r1c2 = new JButton(), r1c3 = new JButton(), r1c4 = new JButton(), r1c5 = new JButton(), r1c6 = new JButton(),
            r2c0 = new JButton(), r2c1 = new JButton(), r2c2 = new JButton(), r2c3 = new JButton(), r2c4 = new JButton(), r2c5 = new JButton(), r2c6 = new JButton(),
            r3c0 = new JButton(), r3c1 = new JButton(), r3c2 = new JButton(), r3c3 = new JButton(), r3c4 = new JButton(), r3c5 = new JButton(), r3c6 = new JButton(),
            r4c0 = new JButton(), r4c1 = new JButton(), r4c2 = new JButton(), r4c3 = new JButton(), r4c4 = new JButton(), r4c5 = new JButton(), r4c6 = new JButton(),
            r5c0 = new JButton(), r5c1 = new JButton(), r5c2 = new JButton(), r5c3 = new JButton(), r5c4 = new JButton(), r5c5 = new JButton(), r5c6 = new JButton();
    
    //organizing buttons in each column
    JButton[] column0 = {r0c0, r1c0, r2c0, r3c0, r4c0, r5c0};
    JButton[] column1 = {r0c1, r1c1, r2c1, r3c1, r4c1, r5c1};
    JButton[] column2 = {r0c2, r1c2, r2c2, r3c2, r4c2, r5c2};
    JButton[] column3 = {r0c3, r1c3, r2c3, r3c3, r4c3, r5c3};
    JButton[] column4 = {r0c4, r1c4, r2c4, r3c4, r4c4, r5c4};
    JButton[] column5 = {r0c5, r1c5, r2c5, r3c5, r4c5, r5c5};
    JButton[] column6 = {r0c6, r1c6, r2c6, r3c6, r4c6, r5c6};

    JButton[][] columns = {column0, column1, column2, column3, column4, column5, column6};

    public gameBoard ()
    {
        setTitle("Connect Four Intelligent Agent");
        setSize(800, 700);
        setLayout(null);
        PlayerTurnMessage.setBounds( 20, 10, 400, 20);
        /* ---------------------------------------------------------GUI SETUP--------------------------------------------- */
        
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
        start.addActionListener(this);
        quitGame.addActionListener(this);
        c0.addActionListener(this);
        c1.addActionListener(this);
        c2.addActionListener(this);
        c3.addActionListener(this);
        c4.addActionListener(this);
        c5.addActionListener(this);
        c6.addActionListener(this);
        quitGame.addActionListener(this);

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

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getActionCommand();

        if (source.equals("Start Game!"))
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
                playerMoveMade = true;
                PlayerTurnMessage.setText("Player 2 (CPU) Goes First- YELLOW");

                //CPU goes first
                CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes First- RED (Select a column to start your turn)");

            }
        }
        else if (source.equals("0"))
        {
            userTurnCounter = playerMove(0, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("1"))
        {
            userTurnCounter = playerMove(1, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("2"))
        {
            userTurnCounter = playerMove(2, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("3"))
        {
            userTurnCounter = playerMove(3, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );

            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("4"))
        {
            userTurnCounter = playerMove(4, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("5"))
        {
            userTurnCounter = playerMove(5, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("6"))
        {
            userTurnCounter = playerMove(6, columns, occupiedSpacesN, playerMoveMade, userTurnCounter, lastMove); 
            playerMoveMade = true;
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 2 (CPU) Goes Next- YELLOW");
            }
            
            
            CPUTurnCounter = CPUTurn(columns, occupiedSpacesN, CPUTurnCounter, CPULastMove );
            if(isCleared)
            {
                PlayerTurnMessage.setText("Click the 'Start Game!' button to begin a game."); 
                isCleared = false;
            }
            else
            {
                PlayerTurnMessage.setText("Player 1 (YOU) Goes Next- RED (Select a column to start your turn)");
            }
            playerMoveMade = false;
        }
        else if (source.equals("Quit Game"))
        {
            // allow the user to exit the game
            System.exit(0); 
        }
    }

    public int CPUTurn( JButton columns[][], int[][] occupiedSpacesN, int CPUTurnCounter, int[] lastMove)
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
                        columns[move[1]][r].setBackground(Color.YELLOW);
                        JOptionPane.showMessageDialog(self,"row "+ r + " col " + move[1]);
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

                // find the correct column array and set the button in the correct row to yellow
                columns[move[1]][move[0]].setBackground(Color.YELLOW);

                lastMove[0] = move[0];
                lastMove[1] = move[1];
                CPULastMove[0] = move[0];
                CPULastMove[1] = move[1];

                JOptionPane.showMessageDialog(self,"row "+ move[0] + " col " + move[1]);
                playerMoveMade = false;
            
                CPUTurnCounter ++;

            }
            
            if  ( CPUTurnCounter > 3 && isWin( false, occupiedSpacesN, lastMove))
            {
                JOptionPane.showMessageDialog(self,"done");
                System.exit(0);

                //TODO, also consider clearing the board?? although may not need to have a restart game option
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
                         
                            state[actions[a]][r] = 2; //FIXME

                            if(score < bestScore)
                            {
                                bestScore = score;
                                address[0] = r;
                                address[1] = actions[a];
                                address[2] =  score; // row, column, score
                               
                            }
                            state[actions[a]][r] = 0; //FIXME
                            break;
                        }
                }
            }
        }

        return address;
    }

    public int playerMove(int colNum, JButton columns[][], int[][] occupiedSpacesN, boolean playerMoveMade, int userTurnCounter, int[] lastMove)
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

            for ( int r = 5; r >= 0; r--)
            {
                if (occupiedSpacesN[colNum][r] == 0)
                { // if the lowest row in the column is empty
                    // assign it to whose turn it is
                    // in this case, the column buttons are only for use of player 1 (the human player)

                    occupiedSpacesN[colNum][r] = 1;
                    columns[colNum][r].setBackground(tileColor);
                    columns[colNum][r].repaint();
                    lastMove[0] = r;
                    lastMove[1] = colNum;

                    playerMoveMade = true;
                    break;
                }
                if ( r == 0 && occupiedSpacesN[colNum][r] > 0)
                {
                    JOptionPane.showMessageDialog(self, "This column is full. Please choose another.");
                    r = 5;
                }
    
            }
        }

        userTurnCounter++;
             
        if  ( userTurnCounter > 3 && isWin( true, occupiedSpacesN, lastMove))
        {
            JOptionPane.showMessageDialog(self, "done");
            System.exit(0);
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
                    //displayBoard(state);
                    JOptionPane.showMessageDialog(self, "COLUMN WINNER IS PLAYER " + playerNum + " wining move: row " + r + " column" + lastMove[1]);
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
                    //displayBoard(state);
                    JOptionPane.showMessageDialog(self, "ROW WINNER IS PLAYER " + playerNum + " wining move: row " + lastMove[0] + " column" + c);
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
                    //displayBoard(state);
                    JOptionPane.showMessageDialog(self, "DECREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
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
                    //displayBoard(state);
                    JOptionPane.showMessageDialog(self, "INCREASING DIAGONAL WINNER IS PLAYER "  + playerNum + " wining move: row " + r + " column" + c);
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
                            state[actions[a]][r] = 1; //FIXME

                            score = AlphaBeta(state, depth - 1, alpha, beta, false, lastMove);
                            maxScore = Math.max(maxScore, score);
                            alpha = Math.max(alpha, maxScore);
                            state[actions[a]][r] = 0; // reset the change made  //FIXME
                            
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
                            state[actions[a]][r] = 2; //FIXME
                            
                            score = AlphaBeta(state, depth - 1, alpha, beta, true, lastMove);
                            minScore = Math.min(minScore, score);
                            beta = Math.min(beta, minScore);
                            state[actions[a]][r] = 0; // reset the change made //FIXME
                            
                            if (beta <= alpha){ break; }
                            break;
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

        int[] localMoves = {lastMove[1], (lastMove[1]-1), (lastMove[1]+1)};  // moves to prioritize if they are available

        int[][] openAndBlocked  = {isBlocked (playerNum, localMoves[0]), isBlocked (playerNum, localMoves[1]), isBlocked (playerNum, localMoves[2])};

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
                localMoveOrder[t] = 1;
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

    int[] isBlocked (int playerNum, int column)
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
