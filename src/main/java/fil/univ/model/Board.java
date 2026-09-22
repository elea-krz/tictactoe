package fil.univ.model;

import java.util.Arrays;

import static fil.univ.model.Player.X;
import static fil.univ.model.Player.O;

public class Board {

    private Cell[][] cells;
    private int width;
    private int height;

    private static final int DEFAULT_SIZE = 3;

    private Player winner;
    private GameState state;
	private Player currentTurn;
	private enum GameState { IN_PROGRESS, FINISHED }

    public Board(int nbRows, int nbCols) {
        this.width = nbRows;
        this.height = nbCols;
        cells = new Cell[nbRows][nbCols];
        restart();
    }

    public Board(){
        this.width = DEFAULT_SIZE;
        this.height = DEFAULT_SIZE;
        cells = new Cell[DEFAULT_SIZE][DEFAULT_SIZE];
        restart();
    }

    /**
     *  Restart or start a new game, will clear the board and win status
     */
    public void restart() {
        clearCells();
        winner = null;
        currentTurn = Player.X;
        setInProgressMode();
    }

    /**
     * Mark the current row for the player who's current turn it is.
     * Will perform no-op if the arguments are out of range or if that position is already played.
     * Will also perform a no-op if the game is already over.
     *
     * @param row 0..2
     * @param col 0..2
     *
     */
    public void mark( int row, int col ) {
        isValid(row, col);

        cells[row][col].setValue(currentTurn);

        if (isWinningMoveByPlayer(currentTurn, row, col)) {
            setInFinishedMode();
            winner = currentTurn;

        } else if (isBoardFull()) {
            setInFinishedMode();
            winner = null; // Match nul
        }else {
            // flip the current turn and continue
            flipCurrentTurn();
        }

    }

    public boolean isDraw(){
        return state == GameState.FINISHED && winner == null;
    }

    public boolean isBoardFull(){
        for(int i = 0; i < this.width; i++) {
            for(int j = 0; j < this.height; j++) {
                if(cells[i][j].getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        return winner;
    }
    
    public Player getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(Player currentTurn) {
		this.currentTurn = currentTurn;
	}
	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	private void setInProgressMode() {
		setState(GameState.IN_PROGRESS);
	}
	private void setInFinishedMode() {
		setState(GameState.FINISHED);
	}
	
	public Boolean isInProgressMode() {
		return getState().equals(GameState.IN_PROGRESS);
	}
	public Boolean isFinishedMode() {
		return getState().equals(GameState.FINISHED);
	}
	
    private void clearCells() {
        for(int i = 0; i < this.width; i++) {
            for(int j = 0; j < this.height; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void isValid(int row, int col ) {
        if( state == GameState.FINISHED ) {
            throw new IllegalArgumentException("Game is already finished.");
        }
        if( isOutOfBounds(row) || isOutOfBounds(col) ) {
            throw new IllegalArgumentException("Coordinates are out of bounds.");
        }  
        if( isCellValueAlreadySet(row, col) ) {
            throw new IllegalArgumentException("Cell is already set.");
        } 
    }

    private boolean isOutOfBounds(int idx) {
        return idx < 0 || idx >= this.cells.length;
    }

    private boolean isCellValueAlreadySet(int row, int col) {
        return cells[row][col].getValue() != null;
    }

    private boolean isWinningInARow(Player player, int row) {
        return Arrays.stream(cells[row]).allMatch(cell -> cell.getValue() == player);
    }

    private boolean isWinningInAColumn(Player player, int column) {
        for(int i = 0; i < this.width; i++) {
            if(cells[i][column].getValue() != player) {
                return false;
            }
        }
        return true;
    }

    private boolean isWinningInADiagonal(Player player) {
        boolean result = true;
        int idx = 0;
        while(result && idx < this.width) {
            result = this.cells[idx][idx].getValue() == player;
            idx++;
        }
        return result;
    }

    private boolean isWinningInOppositeDiagonal(Player player) {
        boolean result = true;
        int idx = 0;
        while(result && idx < this.width) {
            result = this.cells[idx][this.height - idx - 1 ].getValue() == player;
            idx++;
        }
        return result;
    }


    /**
     * Algorithm adapted from http://www.ntu.edu.sg/home/ehchua/programming/java/JavaGame_TicTacToe.html
     * @param player
     * @param currentRow
     * @param currentCol
     * @return true if <code>player</code> who just played the move at the <code>currentRow</code>, <code>currentCol</code>
     *              has a tic tac toe.
     */
    private boolean isWinningMoveByPlayer(Player player, int currentRow, int currentCol) {

        return (isWinningInARow(player, currentRow)
                || isWinningInAColumn(player, currentCol)
                || isWinningInADiagonal(player)
                || isWinningInOppositeDiagonal(player));
    }

    private void flipCurrentTurn() {
        currentTurn = currentTurn == X ? O : X;
    }

}
