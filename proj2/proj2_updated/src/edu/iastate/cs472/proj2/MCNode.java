package edu.iastate.cs472.proj2;

import java.util.*;
import java.math.*;
/**
 * 
 * @author Andrew McMahon
 *
 * @param <E>
 */
public class MCNode<E>{
	protected E state;    
    protected CheckersMove lastMove;
    protected MCNode<E> parent;
    protected List<MCNode<E>> children;   
    protected double wins; 
    protected double visits;                
    protected static final double C = Math.sqrt(2);
    protected int player;
 
  
    /**
     * Constructor for the root node.
     */
    public MCNode(E s){
        state = s;
        lastMove = null;
        parent = null;
        children = new ArrayList<MCNode<E>>();
        wins = 0.0;
        visits = 0.0;
    }
    
    public double UCB(){
        if(visits == 0){
            return Double.POSITIVE_INFINITY;
        }
        return (wins / visits + C * Math.sqrt(Math.log(parent.visits)/visits));
    }
}
