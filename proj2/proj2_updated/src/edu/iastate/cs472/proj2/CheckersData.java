package edu.iastate.cs472.proj2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
/**
 * 
 * @author Andrew McMahon
 *
 */
public class CheckersData{

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.
    int currentPlayer;

    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData(){
        board = new int[8][8];
        currentPlayer = RED;
        setUpGame();
    }
    
    CheckersData(int[][] b){
    	board = new int[b.length][b[0].length];
        for (int i = 0; i < b.length; i++){
        	for(int j = 0; j < b[0].length; j++){
                board[i][j] = b[i][j];
        	}
        }
    }


    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame(){
    	// Set up the board with pieces BLACK, RED, and EMPTY
    	for(int i = 0; i < board.length; i++){
    		for(int j = 0; j < board[0].length; j++){
    			//check if placement is valid from given rule
    			if(i % 2 == j % 2){
    				//check for black initial squares (top 3)
    				if(i < 3){
    					//since the square is valid for placement, and it is in black-initial territory, initialize this square to have a black checker
    					board[i][j] = BLACK;
    				}
    				//check for red initial square (bottom 3)
    				else if(i >= board.length - 3 && i < board.length){
    					//since the square is valid for placement, and it is in red-initial territory, initialize this square to have a red checker
    					board[i][j] = RED;
    				}
    				else{
    					//even row/col, but is not to-be-initialized to red or black. so, it is empty
    					board[i][j] = EMPTY;
    				}
    			}
    			else{
    				//if not even row/col, it is empty
    				board[i][j] = EMPTY;
    			}
    		}
    	}
    	
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col){
        if(!isValidBoardSpace(row, col)){
            return -1;
        }
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move){
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++){
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
        }
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol){
    	// Update the board for the given move. You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        CheckersMove move = new CheckersMove(fromRow,fromCol,toRow,toCol);
        // 2. if this move is a jump, remove the captured piece
        if(move.isJump()){
    		int rowMod = 0;
    		int colMod = 0;
    		if (fromRow > toRow){
    			rowMod = -1;
    		} 
    		else {
    			rowMod = 1;
    		}
    		if (fromCol > toCol){
    			colMod = -1;
    		} 
    		else {
    			colMod = 1;
    		}
    		board[fromRow + rowMod][fromCol + colMod] = EMPTY;
    	}
    	board[toRow][toCol] = board[fromRow][fromCol];
    	board[fromRow][fromCol] = EMPTY;
    	
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
    	if(board[toRow][toCol] == BLACK){
    		if(toRow == board.length - 1){
    			board[toRow][toCol] = BLACK_KING;
    		}
    	}
    	else if(board[toRow][toCol] == RED){
    		if(toRow == 0){
    			board[toRow][toCol] = RED_KING;
    		}
    	}
    }

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player){
    	//"The value of player should be one of the constants RED or BLACK; if not, null is returned."
    	if (player != RED && player != BLACK){
    		System.out.println("Player is not of value RED or BLACK.");
            return null;
        }
    	ArrayList<CheckersMove> moves = new ArrayList<>();
        CheckersMove[] m;
        //collect all legal jumps from all positions
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){	
            	m = getLegalJumpsFrom(player, row, col);
                if (m != null) {
                    moves.addAll(Arrays.asList(m));
                }
            }
        }
        if (moves.isEmpty()){
            return null;
        }

        //manually filter for jumps
        ArrayList<CheckersMove> manual = new ArrayList<>();
        for (CheckersMove move : moves){
            if (move.isJump()) {
            	manual.add(move);
            }
        }
        
        CheckersMove[] returner;
        //return the result, if the collection failed, return manual filter, else, return legal moves computed in loop
        if (!manual.isEmpty()){
        	returner = manual.toArray(new CheckersMove[manual.size()]);
        } 
        else{
        	returner = moves.toArray(new CheckersMove[moves.size()]);
        }
        return returner;
    }


    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps. 
     * Each move returned in the array represents a sequence of jumps 
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col){
        int my;
        int myKing;
        int opp; 
        int oppKing;
        int direction;       
        //initialize piece variables for ease of access
        if(player == 1){
            my = RED;
            myKing = RED_KING;
            opp = BLACK;
            oppKing = BLACK_KING;
        }
        else{
            my = BLACK;
            myKing = BLACK_KING;
            opp = RED;
            oppKing = RED_KING;
        }
        boolean isKing;
        if(pieceAt(row, col) == my){
            isKing = false;
        }
        else if(pieceAt(row, col) == myKing){
            isKing = true;
        }
        else{
            return null;
        }
        CheckersMove pos = new CheckersMove();
        pos.addMove(row, col);
        CheckersMove[] arr = RecursiveMoves(player, false, isKing, pos, new HashSet<CheckersCell>()).toArray(new CheckersMove[0]);
        return arr;
    }
    

    private boolean isValidBoardSpace(int x, int y){
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }
    
    ArrayList<CheckersMove> RecursiveMoves(int player, boolean jumpMode, boolean king, CheckersMove current, HashSet<CheckersCell> inUse) {
        int my;
        int myKing;
        int opp; 
        int oppKing;
        int direction;
        //initialize piece variables for ease of access
        if(player == 1){
            my = RED;
            myKing = RED_KING;
            opp = BLACK;
            oppKing = BLACK_KING;
            direction = -1;
        }
        else{
            my = BLACK;
            myKing = BLACK_KING;
            opp = RED;
            oppKing = RED_KING;
            direction = 1;
        }
        CheckersCell mostRecent = current.recentMove();
        int row = mostRecent.row;
        int col = mostRecent.col;
        ArrayList<CheckersMove> moves = new ArrayList<>();
        if(!jumpMode) {
            if (isValidBoardSpace(row + direction, col + 1) && pieceAt(row + direction, col + 1) == EMPTY) {
                CheckersMove temp = current.clone();
                temp.addMove(row+direction, col+1);
                moves.add(temp);
            }
            if (isValidBoardSpace(row + direction, col - 1) && pieceAt(row + direction, col - 1) == EMPTY) {
                CheckersMove temp = current.clone();
                temp.addMove(row+direction, col-1);
                moves.add(temp);

            }

            if(king){
                
                if (isValidBoardSpace(row - direction, col + 1) && pieceAt(row - direction, col + 1) == EMPTY) {
                    CheckersMove temp = current.clone();
                    temp.addMove(row-direction, col+1);
                    moves.add(temp);
                }
                if (isValidBoardSpace(row - direction, col - 1) && pieceAt(row - direction, col - 1) == EMPTY) {
                    CheckersMove temp = current.clone();
                    temp.addMove(row-direction, col-1);
                    moves.add(temp);
                }
            }
        }
        if ((pieceAt(row+direction, col-1) == opp || pieceAt(row+direction, col-1) == oppKing) && !inUse.contains(new CheckersCell(row+direction,col-1))) {
            if(pieceAt(row+(2*direction), col-2) == EMPTY){
                CheckersMove temp = current.clone();
                temp.addMove(row+(2*direction), col-2);
                //recurse(temp, moves, inUse, player, king, true, row, direction, col);
                
                
                HashSet<CheckersCell> taken = new HashSet<CheckersCell>(inUse);
                taken.add(new CheckersCell(row+direction, col-1));
                moves.add(temp);
                moves.addAll(RecursiveMoves(player, true, king, temp, taken));
            }
        }

        if ((pieceAt(row+direction, col+1) == opp || pieceAt(row+direction, col+1) == oppKing) && (!inUse.contains(new CheckersCell(row+(direction), col+1)))) {
            if(((pieceAt(row+(2*direction), col+2) == EMPTY))){
                CheckersMove temp = current.clone();
                temp.addMove(row+(2*direction), col+2);
                //recurse(temp, moves, inUse, player, king, true, row, direction, col);

                
                
                
                HashSet<CheckersCell> taken = new HashSet<CheckersCell>(inUse);
                taken.add(new CheckersCell(row+direction, col+1));
                moves.add(temp);
                moves.addAll(RecursiveMoves(player, king, true, temp, taken));
            }
        }

        // backwards
        if(king){
            direction *= -1;
            if ((pieceAt(row+direction, col-1) == opp || pieceAt(row+direction, col-1) == oppKing) && (!inUse.contains(new CheckersCell(row+direction, col-1)))) {
            if(pieceAt(row+(2*direction), col-2) == EMPTY){
            	
                CheckersMove temp = current.clone();
                temp.addMove(row+(2*direction), col-2);
                //recurse(temp, moves, inUse, player, king, true, row, direction, col);
                
                HashSet<CheckersCell> taken = new HashSet<CheckersCell>(inUse);
                taken.add(new CheckersCell(row+direction, col-1));
                moves.add(temp);
                moves.addAll(RecursiveMoves(player, true, king, temp, taken));
            }
        }

            if ((pieceAt(row+direction, col+1) == opp || pieceAt(row+direction, col+1) == oppKing) && (!inUse.contains(new CheckersCell(row+direction, col+1)))) {
                if((pieceAt(row+(2*direction), col+2) == EMPTY)){
                    CheckersMove temp = current.clone();
                    temp.addMove(row+(2*direction), col+2);
                   // recurse(temp, moves, inUse, player, king, true, row, direction, col);

                    HashSet<CheckersCell> taken = new HashSet<CheckersCell>(inUse);
                    taken.add(new CheckersCell(row+direction, col+1));
                    moves.add(temp);
                    moves.addAll(RecursiveMoves(player, true, king, temp, taken));
                }
            }
        }
        return moves;
    }
    
//    private void recurse(CheckersMove temp, ArrayList<CheckersMove> moves, HashSet<CheckersCell> inUse, int player, boolean king, boolean jumpMode, int row, int dir, int col) {
//    	HashSet<CheckersCell> taken = new HashSet<CheckersCell>(inUse);
//        taken.add(new CheckersCell(row+dir, col-1));
//        moves.add(temp);
//        moves.addAll(RecursiveMoves(player, true, king, temp, taken));
//    }


}
