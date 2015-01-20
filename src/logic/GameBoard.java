package logic;

public class GameBoard {

	private int[][] boardMatrix;
	private boolean[][] needsUpdating;
	
	public GameBoard() {
		boardMatrix = new int[4][4];
		needsUpdating = new boolean[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				boardMatrix[i][j] = 0;
				needsUpdating[i][j] = true;
			}
		}
	}
	
	public boolean needUpdate(int row, int column) {
		return needsUpdating[row][column];
	}
	
	public void clearUpdate(int row, int column) {
		needsUpdating[row][column] = false;
	}
	
	public void setValueAtPosition(int row, int column, int value) {
		if(row < 4 && row >= 0) {
			if(column < 4 && column >= 0) {
				if(boardMatrix[row][column] != value) {
					boardMatrix[row][column] = value;
					needsUpdating[row][column] = true;
				}
			} else {
				System.out.println("Column value " + column + " is out of range.");
			}
		} else {
			System.out.println("Row value " + row + " is out of range.");
		}
	}
	
	public int getValueAtPosition(int row, int column) {
		if(row < 4 && row >= 0) {
			if(column < 4 && column >= 0) {
				return boardMatrix[row][column];
			} else {
				System.out.println("Column value " + column + " is out of range.");
			}
		} else {
			System.out.println("Row value " + row + " is out of range.");
		}
		return -1;
	}
	
	public String toString() {
		String toReturn  = "";
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				toReturn += Integer.toString(boardMatrix[i][j]) + "\t"; 
			}
			toReturn += "\n";
		}
		
		return toReturn;
	}
	
	public static void main(String... args) {
		GameBoard gb = new GameBoard();
		
		System.out.println(gb);
		
		gb.setValueAtPosition(1, 1, 1024);
		gb.setValueAtPosition(3, 3, 4);
		
		System.out.println(gb);
		
	}
}
