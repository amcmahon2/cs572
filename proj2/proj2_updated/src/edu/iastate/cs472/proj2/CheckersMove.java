package edu.iastate.cs472.proj2;

import java.util.ArrayList;

/**
 * @author Andrew McMahon
 */

/**
 * A CheckersMove object represents a move in the game of Checkers.
 * It holds the row and column of the piece that is to be moved
 * and the row and column of the square to which it is to be moved.
 * (This class makes no guarantee that the move is legal.)
 *
 * It represents an action in the game of Checkers.
 * There may be a single move or multiple jumps in an action.
 * It holds a sequence of the rows and columns of the piece
 * that is to be moved, for example:
 * a single move: (2, 0) -> (3, 1)
 * a sequnce of jumps: (2, 0) -> (4, 2) -> (6, 0)
 *
 */
public class CheckersMove{
    
    ArrayList<Integer> rows = new ArrayList<Integer>();
    ArrayList<Integer> cols = new ArrayList<Integer>();
    
    CheckersMove(int r1, int c1, int r2, int c2){
        // Constructor, a single move from
        //(r1, c1) to (r2, c2)
        
        // move's start
        rows.add(r1);
        cols.add(c1);
        
        // move's destination
        rows.add(r2);
        cols.add(c2);
    }
    
    CheckersMove() {
        // Constructor, create an empty move
    }
    
    boolean isJump(){
        // Test whether this move is a jump.  It is assumed that
        // the move is legal.  In a jump, the piece moves two
        // rows.  (In a regular move, it only moves one row.)
    	if (rows.size() < 2) {
    		//edge case if board is not 8x8 (believe the board is always 8x8, though
            throw new IllegalStateException();
        }
        return (rows.get(0) - rows.get(1) == 2 || rows.get(0) - rows.get(1) == -2);
    }
    
    
    void addMove(int r, int c){
        // add another move (continuous jump), which goes from
        // (last ele in rows, last ele in cols) to (r, c)
        rows.add(r);
        cols.add(c);
    }
    
    /**
     * creates a new clone of the current CheckersMove object. the new CheckersMove 
     * will have the same rows and columns as the original.
     * 
     * @return a new CheckersMove object which is a clone of the current one
     */
    @Override
    public CheckersMove clone(){
        CheckersMove move = new CheckersMove();
        move.rows.addAll(this.rows);
        move.cols.addAll(this.cols);
        return move;
    }

    /**
     * returns the most recent move represented by the current CheckersMove object.
     * this method creates and returns a CheckersCell for the last row and column in 
     * the rows and columns lists, respectively. if there are no moves, it returns null.
     * 
     * @return the most recent move as a CheckersCell, or null if no moves are recorded
     */
    public CheckersCell recentMove(){
        if (rows.isEmpty() || cols.isEmpty()){
            return null;
        }
        return new CheckersCell(rows.get(rows.size() - 1), cols.get(cols.size() - 1));
    }

    /**
     * Returns the first move recorded by the current CheckersMove object.
     * This method creates and returns a CheckersCell for the first row and column 
     * in the rows and columns lists, respectively. If there are no moves, it returns null.
     * 
     * @return the first move as a CheckersCell, or null if no moves are recorded
     */
    public CheckersCell firstMove(){
        if (rows.isEmpty() || cols.isEmpty()){
            return null;
        }
        //return new CheckersCell(rows.get(0), cols.get(0));
        return new CheckersCell(rows.get(rows.size()-1), cols.get(cols.size()-1));
    }

    

    /**
     * for debugging purposes, used to concatenate a sequence of moves
     * 
     * @return the string of moves
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Move sequence: ");
        for (int i = 0; i < rows.size(); i++) {
            sb.append("(").append(rows.get(i)).append(", ").append(cols.get(i)).append(")");
            if (i < rows.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }
    
}  // end class CheckersMove.
