package edu.iastate.cs472.proj1;

import java.io.FileNotFoundException;

/**
 *  
 * @author Andrew McMahon
 *
 */

public class EightPuzzle{
	/**
	 * This static method solves an 8-puzzle with a given initial state using three heuristics. The 
	 * first two, allowing single moves only, compare the board configuration with the goal configuration 
	 * by the number of mismatched tiles, and by the Manhattan distance, respectively.  The third 
	 * heuristic, designed by yourself, allows double moves and must be also admissible.  The goal 
	 * configuration set for all puzzles is
	 * 
	 * 			1 2 3
	 * 			8   4
	 * 			7 6 5
	 * 
	 * @param s0
	 * @return a string specified in the javadoc below
	 */
	public static String solve8Puzzle(State s0){
		// 1) If there exists no solution, return a message that starts with "No solution 
		//    exists for the following initial state:" and follows with a blank line and 
		//    then what would be the output from a call s0.toString(). See the end of 
		//    Section 6 in the project description for an example. 
	    if (!s0.solvable()){
	        return "No solution exists for the following initial state:" + "\n" + s0.toString();
	    }
	    StringBuilder result = new StringBuilder();
		// 2) Otherwise, solve the puzzle with the three heuristics.  The two solutions generated by
		//    the first two heuristics may be different but must have the same length for optimality. 
	    for(int x = 0; x < 3; x++){
	    	Heuristic curr;	    
	    	String heuristicString = "";
	    	Heuristic h[] = {Heuristic.TileMismatch, Heuristic.ManhattanDist, Heuristic.DoubleMoveHeuristic }; 
	    	if(x == 0){
	    		curr = h[0];
	    		heuristicString = "TileMismatch";
	    	}
	    	else if(x == 1){
	    		curr = h[1];
	    		heuristicString = "ManhattanDist";
	    	}
	    	else{
	    		curr = h[2];	
	    		heuristicString = "DoubleMoveHeuristic";
	    	}
	    	
	    	// 3) Combine the three solution strings into one that would print out in the 
	    	//    output format specified in Section 6 of the project description.
	        String solution = AStar(s0, curr);
	    	if(solution.substring(0,2).equals("No")){
	    		return "No solution found.";
	    	}
	    	String[] thisMoves = solution.split("\n");

		    //check if the first two solutions are of the same length (optimality check)
		    if(thisMoves[0].length() != thisMoves[1].length()){
		        return "The first two heuristics did not produce the same length solutions.";
		    }

		    //count the moves, excluding the final state
	        int moveCount = thisMoves.length - 1;
	        result.append(moveCount).append(" moves in total (heuristic: ").append(heuristicString).append(")\n");
	        for(String move : thisMoves){
	            result.append(move).append("\n");
	        }
	        
	        //formatting
	        result.append("\n");  
	    }
	    return result.toString();
	}

	
	/**
	 * This method implements the A* algorithm to solve the 8-puzzle with an input initial state s0. 
	 * The algorithm implementation is described in Section 3 of the project description. 
	 * 
	 * Precondition: the puzzle is solvable with the initial state s0.
	 * 
	 * @param s0  initial state
	 * @param h   heuristic 
	 * @return    solution string 
	 */
	public static String AStar(State s0, Heuristic h){
		// Initialize the two lists used by the algorithm. 
		OrderedStateList OPEN = new OrderedStateList(h, true); 
		OrderedStateList CLOSE = new OrderedStateList(h, false);
		
		//set starting heuristic, reset numMoves to be safe, and add inital state s0 to open list
		State.heu = h;
		s0.numMoves = 0;
	    OPEN.addState(s0);
		
	    // Implement the algorithm described in Section 3 to solve the puzzle. 
		// Once a goal state s is reached, call solutionPath(s) and return the solution string. 
			
		//A* loop
	    int count = 0;
	    while(OPEN.size() != 0){
	    	count++;
	        State currentState = OPEN.remove();
	        CLOSE.addState(currentState);
	        
	        if(currentState.isGoalState()){
	            return solutionPath(currentState);
	        }
	        
	        //generate successor states
	        for(Move move : Move.values()){
	            try{
	            	//initial successor state
	                State successor = currentState.successorState(move);
	                successor.numMoves = currentState.numMoves + 1;
	                
	                //calculate cost (changes heuristic if needed on the fly)
	                int cost = successor.cost();
	                
	                //add successor to OPEN list if it's not in CLOSED or OPEN
	                if((OPEN.findState(successor) != successor) && (CLOSE.findState(successor) != successor)){
	                	OPEN.addState(successor);
	                }
	                
	            } 
	            catch(IllegalArgumentException e){
	                //invalid move, can ignore
	            }
	        }
	    }
    return "No solution found";					
	}
	
	
	
	/**
	 * From a goal state, follow the predecessor link to trace all the way back to the initial state. 
	 * Meanwhile, generate a string to represent board configurations in the reverse order, with 
	 * the initial configuration appearing first. Between every two consecutive configurations 
	 * is the move that causes their transition. A blank line separates a move and a configuration.  
	 * In the string, the sequence is preceded by the total number of moves and a blank line. 
	 * 
	 * See Section 6 in the projection description for an example. 
	 * 
	 * Call the toString() method of the State class. 
	 * 
	 * @param goal
	 * @return
	 */
	private static String solutionPath(State goal){
		StringBuilder sol = new StringBuilder();
		//set to input for first iteration
	    State currentState = goal;

	    //trace all the way back to initial state, noting the board state at each state
	    while (currentState != null) {	    	
	    	sol.insert(0, currentState.toString() + "\n");
	        currentState = currentState.previous;
	    }
	    return sol.toString().trim();
	}	
}
