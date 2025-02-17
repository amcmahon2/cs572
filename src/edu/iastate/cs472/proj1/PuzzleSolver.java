package edu.iastate.cs472.proj1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
/**
 *  
 * @author Andrew McMahon
 *
 */

public class PuzzleSolver{
	/**
	 *  Read an initial state from the input file.  Solve the eight puzzle three times:
	 *  
	 *      1) The first solution allows single moves only and uses the heuristic based on the number
	 *         of mismatched tiles. 
	 *         
	 *      2) The second solution also allows single moves only but uses the heuristic based on the 
	 *         Manhattan distance. 
	 *         
	 *      3) The third solution allows single and double moves and uses the admissible heuristic 
	 *         designed by yourself.  
	 *         
	 *  Each solution is printed out as a sequence of states, generated by single/double moves, from the 
	 *  initial state to the goal state. If no solution exists, report it.  
	 * 
	 * @param args
	 * @throws FileNotFoundException if the input file does not exist 
	 * @throws IllegalArgumentException if the initial state from the file is not in the correct format
	 */
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException{	
		// Read an initial board configuration from a file.  
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter file name:");
		String path = scan.next();
		State init = new State(path);
		
		// Call EightPuzzle.solve8puzzle() to solve the puzzle. 
		String returner = EightPuzzle.solve8Puzzle(init);
		System.out.println(returner);
		
		// You may make it interactive by repeatedly accepting puzzle files and print out 
		// solutions.  (No extra credit but good for debugging and for the user.)
		String fileNames[] = new String[] {"8Puzzle.txt"}; 
	}

}
