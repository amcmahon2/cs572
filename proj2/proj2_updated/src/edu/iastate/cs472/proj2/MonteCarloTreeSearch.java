package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * 
 * @author Andrew McMahon
 *
 */
public class MonteCarloTreeSearch extends AdversarialSearch {

	private int[][] board;
	private CheckersData game;
    private static final double EXPLORATION_PARAM = Math.sqrt(2);
    private Random random = new Random();

    /**
     * Executes the Monte Carlo Tree Search and returns the best move.
     *
     * @param legalMoves All possible moves for the agent at the current step.
     * @return The best move to make.
     */
    public CheckersMove makeMove(CheckersMove[] legalMoves){
        if (legalMoves == null || legalMoves.length == 0) { 
            return null;
        }
        this.game = currentState;
        MCNode<CheckersData> root = new MCNode<>(game);
        root.player = CheckersData.RED;
        root.children = new ArrayList<MCNode<CheckersData>>();
        
        //expand from root node, to be used below with MCTS
        expand(root, legalMoves);
        
        //perform MCTS iterations
        for (int i = 0; i < 1000; i++){
            MCNode<CheckersData> selectedNode = select(root);
            //MCNode<CheckersData> expandedNode = expand(selectedNode);
            expand(selectedNode);
            int simulationScore = simulate(selectedNode);
            backpropagate(selectedNode, simulationScore);
        }

        MCNode<CheckersData> high = new MCNode<CheckersData>(new CheckersData());
        high.wins = Integer.MIN_VALUE;
        for(MCNode<CheckersData> child : root.children){
            if(child.wins >= high.wins){
            	high = child;
            }
        }
        System.out.println(board);
        System.out.println();
        return high.lastMove;
    }

    /**
     * expands node curr to find all legal moves
     * 
     * NOTE: overloaded, this one takes the node curr and generates legal moves using CheckersData getLegalMoves()makeMove, whereas the other
     * uses a given list of legal moves and doesn't require generation.
     * 
     * 
     * @param curr, node which move generation begins with
     */
    private void expand(MCNode<CheckersData> curr){
    	int currPlayer = (curr.player == CheckersData.BLACK ? CheckersData.RED : CheckersData.BLACK);
        for(CheckersMove move: curr.state.getLegalMoves(currPlayer)) {
            CheckersData temp = new CheckersData(currentState.board);

        	//CheckersData temp = new CheckersData(currentState.board);
        	//copy current board to temporary one to save state of current
        	
            temp.makeMove(move);
            MCNode<CheckersData> tempNode = new MCNode<CheckersData>(temp);
            tempNode.lastMove= move;
            tempNode.parent = curr;
            curr.children.add(tempNode);
        }
    }
    
    /**
     * expands node curr to find all legal moves
     * 
     * NOTE: overloaded, this one is used for makeMove where the inital list of legal moves is given, whereas the other
     * takes the node curr and generates legal moves using CheckersData getLegalMoves()
     * 
     * @param curr, node which move generation begins with
     * @param legalMoves, list of legal moves for this node (curr)
     */
    private void expand(MCNode<CheckersData> curr, CheckersMove[] legalMoves){
        for(CheckersMove move: legalMoves){
        	CheckersData temp = new CheckersData();
        	//copy current board to temporary one to save state of current
        	for(int i = 0; i < game.board.length; i++){
        		for(int j = 0; j < game.board[0].length; j++){
        			
        			temp.board[i][j] = game.board[i][j];
        		}
        	}
            temp.makeMove(move);
            MCNode<CheckersData> tempNode = new MCNode<CheckersData>(temp);
            tempNode.lastMove= move;
            tempNode.parent = curr;
            curr.children.add(tempNode);
            tempNode.player = (curr.player == CheckersData.BLACK ? CheckersData.RED : CheckersData.BLACK);
        }
    }
    
    /**
     * traverses down the tree using UCB1 formula from the textbook.
     * 
     * @param MCNode<CheckersData> n, the selected node to begin traversal down on
     * @return MCNode<CheckersData> node or node.child, depending on which had a higher UBC value
     */
    private MCNode<CheckersData> select(MCNode<CheckersData> n){
        MCNode<CheckersData> node = n;
        while(!node.children.isEmpty()){
            MCNode<CheckersData> temp = node.children.get(0);
            double tempBound = temp.UCB();
            //for each child of our temp node, check if it has a larger UCB value than its parent
            for(MCNode<CheckersData> child : node.children){
                if(child.UCB() > tempBound){
                	//if it does, choose to traverse down this child's path
                	tempBound = child.UCB();
                    temp = child;
                }
            }
            node = temp;
        }
        //if this node is not visited, return it as it has no valuable child paths
        if(node.visits == 0) {
            return node;
        }
        else{
            expand(node);
        }
        //null check for debugging
        if(node.children.isEmpty()){
            return null;
        }
        return node.children.get(0);
    }


    /**
     * simulation: Play a random game from the given node's state.
     */
    private int simulate(MCNode<CheckersData> node){
    	CheckersData iterativeBoard = new CheckersData(node.state.board);
        int turn = node.player == CheckersData.BLACK ? CheckersData.RED : CheckersData.BLACK;
        ArrayList<CheckersMove> legalMoves;
        CheckersMove[] moves2 = iterativeBoard.getLegalMoves(turn);
        if (moves2 != null){
            legalMoves = new ArrayList<CheckersMove>(Arrays.asList(moves2));
        } 
        else{
            legalMoves = new ArrayList<CheckersMove>();
        }
        if(legalMoves.isEmpty()){
            legalMoves = new ArrayList<>();
        }
        int index;
        Random rand = new Random();
        while(!legalMoves.isEmpty()){
            index = rand.nextInt(legalMoves.size());
            iterativeBoard.makeMove(legalMoves.get(index));
            CheckersMove[] moves = iterativeBoard.getLegalMoves(turn);
            if (moves != null) {
                legalMoves = new ArrayList<CheckersMove>(Arrays.asList(moves));
            } 
            else{
                legalMoves = new ArrayList<CheckersMove>();
            }
            turn = turn == CheckersData.BLACK ? CheckersData.RED : CheckersData.BLACK;
        }
        return turn == CheckersData.RED ? 1 : -1; // Adjust based on scoring rules.
    }

    
    
    /**
     * backpropagate: update the node statistics after a simulation.
     * 
     * @param node the node to backpropagate from.
     * @param score the result of the simulation (1 for win, -1 for loss).
     */
    private void backpropagate(MCNode<CheckersData> node, int score){
        while (node != null) {
            node.visits += 1;
            node.wins += score;
            node = node.parent;
        }
    }

}
