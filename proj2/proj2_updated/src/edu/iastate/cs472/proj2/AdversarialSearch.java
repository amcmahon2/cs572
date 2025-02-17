package edu.iastate.cs472.proj2;

/**
 * 
 * @author Andrew McMahon
 *
 */

/**
 * This class is to be extended by the class MonteCarloTreeSearch.
 */
public abstract class AdversarialSearch{
    protected CheckersData currentState;

    // An instance of this class will be created in the Checkers.Board
    // It would be better to keep the default constructor.

    protected void setCheckersData(CheckersData currentState){
        this.currentState = currentState;
    }
    
    /** 
     * 
     * @return an array of valid moves
     */
    protected CheckersMove[] legalMoves(){
        return currentState != null ? currentState.getLegalMoves(currentState.RED) : null;
    }
	
    /**
     * Return a move returned from the Monte Carlo tree search.
     * 
     * @param legalMoves
     * @return CheckersMove 
     */
    public abstract CheckersMove makeMove(CheckersMove[] legalMoves);
}
