import java.util.Random;
import java.util.ArrayList;

public class SudokuPuzzleGenerator {

	// 9x9 puzzle
	private int[][] puzzle = new int[9][9];

	// generate a completely solved sudoku board
	public int[][] generate() {

		// set all values of puzzle to 0, which denotes an empty square
		resetPuzzle();

		// start by solving the board at the first square
		solve(0, 0);

		return puzzle;
	}

	private void solve(int row, int col) {
		Random rand = new Random();
		boolean numFound = false;

		// list of numbers to try to place in squares
		ArrayList<Integer> nums = new ArrayList<Integer>();

		// fill list with numbers 1-9
		for (int i = 1; i < 10; i++) {
			nums.add(i);
		}

		// while we still have numbers to try and have not found a valid number
		while ((nums.isEmpty() == false || numFound == false) && !emptyCheck()) {
			// pick random number from a list of available numbers
			int num = nums.get(rand.nextInt(nums.size()));

			// check if generated number is valid
			if (checkRow(row, num) && checkCol(col, num) && checkSection(row, col, num)) {
				// add number to square
				puzzle[row][col] = num;
				numFound = true;
				next(row, col);
				break;
			} else {
				// remove number from list of available numbers
				nums.remove(Integer.valueOf(num));
				// if we are out of numbers, stop trying to find a number
				if (nums.isEmpty()) {
					// System.out.println("No more available numbers");

					resetRow(row);
					solve(row, 0);

					break;
				}
			}
		}

	}

	private void resetPuzzle() {
		// set all values of puzzle to 0, which denotes an empty square
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				puzzle[row][col] = 0;
			}
		}
	}

	private void resetRow(int row) {
		for (int i = 0; i < 9; i++) {
			puzzle[row][i] = 0;
		}
	}

	// move to the next square
	private void next(int row, int col) {
		if (col == 8)
			solve(row + 1, 0);
		else
			solve(row, col + 1);
	}

	// move to the previous square
	private void back(int row, int col) {
		if (row == 0)
			solve(row - 1, 8);
		else
			solve(row, col - 1);
	}

	// check each element of the row for num, if num found return false
	private boolean checkRow(int row, int num) {
		for (int i = 0; i < 9; i++) {
			if (puzzle[row][i] == num)
				// row contains num, return false
				return false;
		}
		// row doesn't contain num, return true
		return true;
	}

	// check each element of the section for num, if num is found return false
	private boolean checkCol(int col, int num) {
		for (int i = 0; i < 9; i++) {
			if (puzzle[i][col] == num) {
				return false;
			}
		}

		return true;
	}

	// check each element of the section for num, if num is found return false
	private boolean checkSection(int xPos, int yPos, int num) {
		int[][] section = new int[3][3];
		section = getSection(xPos, yPos);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (section[i][j] == num) {
					return false;
				}
			}
		}

		return true;
	}

	private int[][] getSection(int xPos, int yPos) {
		int[][] section = new int[3][3];
		/*
		 * int xIndex = 3 * (xPos / 3); int yIndex = 3 * (yPos / 3);
		 */
		int xIndex = xPos - (xPos % 3);
		int yIndex = yPos - (yPos % 3);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				section[i][j] = puzzle[xIndex + i][yIndex + j];
			}
		}
		return section;

	}

	// search puzzle for empty squares, 0 denotes empty
	private boolean emptyCheck() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (puzzle[i][j] == 0) {
					return false;
				}
			}
		}

		return true;
	}

	
	public static void main(String[] args) {
		SudokuPuzzleGenerator puz = new SudokuPuzzleGenerator();
		int[][] board = puz.generate();

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
}
