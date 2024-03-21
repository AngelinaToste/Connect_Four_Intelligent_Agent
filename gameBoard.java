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
 * - make tiles
 * X- make button/ way of user putting tiles in the columns on the board
 * - reflect the user's moves in the array
 * - use the intelligent agent to choose the next move 
 *      (there are always 7 possible options on each turn-- unless a column is completely full)
 * - 
 */


 
//import java.io.*;
import javax.swing.*;
import java.util.*;
//import javafx.scene.shape.Circle;

//import java.awt.Graphics2D;
import java.awt.*;
//import java.util.regex.*;
import java.awt.event.*; 




public class gameBoard extends JFrame
{
    Color tileColor = Color.BLACK;
    int whoseTurn = 0, turnCounter = 0;
    int[] CPUMove = CPUTurn(whoseTurn);
    boolean playerMoveMade = false, startPressed = false;

    public gameBoard ()
    {
        //create and display gui
        setTitle("Connect Four Intelligent Agent");
        setSize(800, 700);
        setLayout(null);
        setVisible(true);
        JButton c0 = new JButton("0"), c1 = new JButton("1"), c2 = new JButton("2"), 
                c3 = new JButton("3"), c4 = new JButton("4"), c5 = new JButton("5"),
                c6 = new JButton("6"), start = new JButton("Start Game!"), quitGame = new JButton("Quit Game");
        
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


        JPanel drawingPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
        
                g.drawRect(50, 100, 500, 500);
                g.setColor(Color.BLUE);
                g.fillRect(50, 100, 500, 500);
                g.setColor(tileColor);

                int xSpacing = 65, ySpacing = 80;

                for (int r = 0; r < 6; r++)
                {
                    for(int c = 0; c < 7; c++)
                    {
                        g.drawOval(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                        g.fillOval(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    }
                }
            }
        };

        start.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.YELLOW;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }
                
            }

        });
        
         
        c0.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }
                
            }

        });
        c1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }
            }

        });
        c2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }

            }

        });
        c3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }

            }

        });
        c4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }

            }

        });
        c5.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                    whoseTurn++;
                }
            }

        });
        c6.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!playerMoveMade)
                {
                    //paint the lowest possible token in this column a specific color (depending on whose turn it is)
                    /*
                    * 
                    * - find the lowest token
                    * - check the availability of other tokens in the column. 
                    *      > this can be done by either checking color or by storing the color values in an 1D array. such as {0,0,0,1,2,1}
                    *      > in the example above, 1 would mean the color for player 1 and 2 would mean the color for player 2
                    */
                    // change the color and trigger a repaint of the specific area
                    tileColor = Color.RED;
                    int xSpacing = 65, ySpacing = 80, c=0, r=5;
                    drawingPanel.repaint(80 + (xSpacing * c), 110 + (ySpacing * r), 50, 50);
                    playerMoveMade = true;
                }

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
        

        // make the drawing panel the same size as the window
        drawingPanel.setBounds(0, 0, 600, 700);
        add(drawingPanel);

        add(c0);
        add(start);
        add(c1);
        add(c2);
        add(c3);
        add(c4);
        add(c5);
        add(c6);
        add(quitGame);

        // need to check for whose turn it is (needs to be CPU turn) and if the user has clicked a column button
        //if ()



        







    }

    // make a function to determine the results of the CPU's turn
    public int[] CPUTurn( int turnCounter)
    {
        int[] move = new int[2];

        if (turnCounter == 0)
        {
            Random randRow = new Random(), randCol = new Random();
                
            move[0] = randRow.nextInt(6);
                
            move[1] = randCol.nextInt(7);

        }
        else
        {
            Random randRow = new Random(), randCol = new Random();
                
            move[0] = randRow.nextInt(6);
                
            move[1] = randCol.nextInt(7);

        }

        return move;
    }

        

    public static void main (String[] args)
    {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              new gameBoard().setVisible(true);
            }
          });
    }  
}
