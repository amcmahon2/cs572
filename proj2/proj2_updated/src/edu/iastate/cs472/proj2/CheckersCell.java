package edu.iastate.cs472.proj2;

import java.util.Objects;

/**
 * represents a cell on the checkers board, identified by its row and column.
 * his class is used to track the location of pieces on the board. it overrides 
 * Object's .equals() and .hashCode() to compare cells based on their row and column 
 * values
 * 
 * @author Andrew McMahon
 */
public class CheckersCell{
    
    /**
     * row of the cell on the board
     */
    public int row;
    
    /**
     * column of the cell on the board
     */
    public int col;

    /**
     * constructs a new CheckersCell with the specified row and column.
     * 
     * @param row the row of the cell on the board
     * @param col the column of the cell on the board
     */
    CheckersCell(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * compares this CheckersCell with another object for equality. two cells are considered
     * equal if they have the same row and column.
     * 
     * @param obj, the object to compare with
     * @return true if the two cells have the same row and column, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj){
        	return true; 
        }
        if (obj == null || getClass() != obj.getClass()){
        	return false; 
        }
        if(((CheckersCell) obj).row == row){
        	if(((CheckersCell) obj).col == this.col) {
        		return true;
        	}
        }
        return false;
    }

    /**
     * returns the hash code for this CheckersCell. the hash code is generated based on 
     * the row and column values of the cell.
     * 
     * @return the hash code of this cell
     */
    @Override
    public int hashCode(){
        return Objects.hash(row, col);
    }
}
