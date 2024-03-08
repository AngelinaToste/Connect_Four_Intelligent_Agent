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
            
            { 0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0, 0,
            }
Functionality needed in the program
- GUI interface for the Board
- ability to click a column or column number to insert a new tile
- function to insert tile: this function will check the column from the bottom up to find the first zero space and insert tile there
- function for goal evaluation after a tile has been inserted
- minimax algorithm for the computer to determine the next best available move also using alpha beta pruning
    + additionally use heuristic function to determine the best choice based on defense or offense move

*/

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event;
import java.util.regex.*;

