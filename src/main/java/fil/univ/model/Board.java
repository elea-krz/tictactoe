package fil.univ.model;


import static fil.univ.model.Player.X;
import static fil.univ.model.Player.O;

public class Board {

    private Cell[][] cells;
    private int rows;
    private int cols;
    private int winningLength;

    private static final int DEFAULT_SIZE = 3;

    private Player winner;
    private GameState state;
	private Player currentTurn;
	private enum GameState { IN_PROGRESS, FINISHED }

    public Board(int nbRows, int nbCols, int winningLength) {
        this.rows = nbRows;
        this.cols = nbCols;
        this.winningLength = winningLength;
        cells = new Cell[nbRows][nbCols];
    }

    public Board(){
        this(DEFAULT_SIZE,DEFAULT_SIZE,DEFAULT_SIZE);
    }

    public void start(){
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
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                if(cells[i][j].getValue() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private Player getPlayerAt(int row, int col) {
        if(row < 0 || col < 0 || row >= this.rows || col >= this.cols) {
            return null;
        }
        return cells[row][col].getValue();
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
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void isValid(int row, int col ) {
        if( state == GameState.FINISHED ) {
            throw new IllegalArgumentException("Game is already finished.");
        }
        if( isRowOutOfBounds(row) || isColOutOfBounds(col) ) {
            throw new IllegalArgumentException("Coordinates are out of bounds.");
        }  
        if( isCellValueAlreadySet(row, col) ) {
            throw new IllegalArgumentException("Cell is already set.");
        } 
    }

    private  boolean isRowOutOfBounds(int row) {
        return row < 0 || row >= this.rows;
    }

    private  boolean isColOutOfBounds(int col) {
        return col < 0 || col >= this.cols;
    }

    private boolean isCellValueAlreadySet(int row, int col) {
        return cells[row][col].getValue() != null;
    }

    private boolean isWinningInARow(Player player, int row){
        int consecutive = 0;
        for(int col = 0; col < this.cols; col++) {
            if(getPlayerAt(row,col) == player){
                consecutive++;
                if(consecutive >= winningLength) {
                    return true;
                }
            }else{
                consecutive = 0;
            }
        }
        return false;
    }

    private boolean isWinningInAColumn(Player player, int col){
        int consecutive = 0;
        for(int row = 0; row < this.rows; row++) {
            if(getPlayerAt(row,col) == player) {
                consecutive++;
                if (consecutive >= winningLength) {
                    return true;
                }
            }else {
                consecutive = 0;
            }
        }
        return false;
    }

    private boolean isWinningInADiagonal(Player player, int row, int col){
        int r = row;
        int c = col;
        while (r > 0 && c > 0){
            r--;
            c--;
        }

        int consecutive = 0;
        while (r < this.rows && c < this.cols){
            if (getPlayerAt(r,c) == player) {
                consecutive++;
                if (consecutive >= winningLength) {
                    return true;
                }
            }else{
                consecutive = 0;
            }
            r++;
            c++;
        }
        return false;
    }

    private boolean isWinningInOppositeDiagonal(Player player, int row, int col){
        int r = row;
        int c = col;
        while (r > 0 && c < this.cols - 1){
            r--;
            c++;
        }

        int consecutive = 0;
        while (r < this.rows && c >= 0){
            if (getPlayerAt(r,c) == player) {
                consecutive++;
                if (consecutive >= winningLength) {
                    return true;
                }
            } else  {
                consecutive = 0;
            }
            r++;
            c--;
        }
        return false;
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

        return isWinningInARow(player,currentRow)
                || isWinningInAColumn(player,currentCol)
                || isWinningInADiagonal(player,currentRow,currentCol)
                || isWinningInOppositeDiagonal(player,currentRow,currentCol);
    }

    private void flipCurrentTurn() {
        currentTurn = currentTurn == X ? O : X;
    }

}
