/*This class was used as reference to solve many algorithms :)*/
public class SudokuSolver {
	
	public final static int[][] MATRIX_TO_SOLVE = {
			{ 3, 0, 6, 5, 0, 8, 4, 0, 0},
			{ 5, 2, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 8, 7, 0, 0, 0, 0, 3, 1},
			{ 0, 0, 3, 0, 1, 0, 0, 8, 0},
			{ 9, 0, 0, 8, 6, 3, 0, 0, 5},
			{ 0, 5, 0, 0, 9, 0, 6, 0, 0},
			{ 1, 3, 0, 0, 0, 0, 2, 5, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 7, 4},
			{ 0, 0, 5, 2, 0, 6, 3, 0, 0}
	};
	
	//doesn't work
	public final static int[][] MATRIX_TO_SOLVE_2 = {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	public static boolean solveSudoku(int [][]matrix, int n) {
		int rowIndex = -1;
		int columnIndex = -1;
		int i = 0, j = 0;
		
		for(i = 0; i < n; i++) {
			for(j = 0; j < n; j++) {
				if(matrix[i][j] == 0) {
					rowIndex = i;
					columnIndex = j;
					//System.out.printf("Number is 0\nRow: %d, Col: %d\n", rowIndex, columnIndex);
					break;
				}
			}
			if(rowIndex != -1) {
				//System.out.println("break out of loop");
				break;
			}
		}
		
		//check if last square
		if(i == n && j == n) {
			return true;
		}else {
			for(int value = 1; value <= n; value++) {
				//System.out.println("Check if matrix is safe. Value: "+value);
				if(isSafe(matrix, value, rowIndex, columnIndex, n)){
					matrix[rowIndex][columnIndex] = value;
					if(!solveSudoku(matrix, n)) {
						matrix[rowIndex][columnIndex] = 0;
					}
					else {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public static boolean isSafe(int[][] matrix, int value, int rowIndex, int colIndex, int n) {
		//row check	
		for(int j = 0; j < n; j++) {
			if(matrix[rowIndex][j] == value) {
				/*System.out.println("Row check - number exist");
				System.out.printf("Row: %d Col: %d Num: %d\n", rowIndex, j, value);*/
				//System.out.println("Row check return false\n");
				return false;
			}
		}
		
		//column check
		for(int i = 0; i < n; i++) {
			if(matrix[i][colIndex] == value) {
				/*System.out.println("Col check - number exist");
				System.out.printf("Row: %d Col: %d Num: %d\n", i, colIndex, value);*/
				//System.out.println("Col check return false\n");
				return false;
			}
		}
		
		//sub-matrix check
		int baseRowIndex = rowIndex - (rowIndex % 3);
		int baseColIndex = colIndex - (colIndex % 3);
		for(int i = baseRowIndex; i < baseRowIndex - 3; i++) {
			for(int j = baseColIndex; j < baseColIndex - 3; j++) {
				if(matrix[i][j] == value) {
					/*System.out.println("Sub-matrix check - number exist");
					System.out.printf("Row: %d Col: %d Num: %d\n", i, j, value);*/
					//System.out.println("Sub-matrix check return false\n");
					return false;
				}
			}
		}
		//System.out.println("isSafe return true\n");
		return true;
	}
	
	public static void main(String[]args) {
		System.out.println("Solve the sudoku!");
		if(solveSudoku(MATRIX_TO_SOLVE, 9)) {
			for(int i = 0; i < 9; i++) {
				for(int j =0; j < 9; j++) {
					System.out.print(MATRIX_TO_SOLVE[i][j]+" ");
				}
				System.out.println();
			}
		}else {
			System.out.println("Unsolvable.");
		}
	}
}
