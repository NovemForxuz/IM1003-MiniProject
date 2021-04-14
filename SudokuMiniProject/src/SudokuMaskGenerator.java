import java.util.ArrayList;
import java.util.Random;

public class SudokuMaskGenerator {
//TODO set up masks for different levels
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

	public static int REST = 0; // to show in the status bar
	public int THISROUNDMASK; // to give back the number for the status bar in the reset()

	private boolean[][] masks = new boolean[GRID_SIZE][GRID_SIZE]; // to generate a new random mask

	// need counter to count blanks available
	public boolean[][] generate(int gameMode) {
		Random rn = new Random();
		ArrayList<Integer> index = new ArrayList<Integer>(); // create list of available index
		
		//iterate through row
		for (int i = 0; i < 9; i++) {
			// fill array with available index (0-8)
			for (int k = 0; k < 9; k++) {
				index.add(k);
			}
			//iterate through column
			for (int j = 0; j < 9; j++) {
				if (!index.isEmpty()) {
					int randInd = index.get(rn.nextInt(index.size())); // retrieve based on key-pair value

					System.out.println("Random no.: " + randInd);

					printIndexs(index);
					if (checkMask(masks, i, randInd, gameMode)) { // levelCode = 1 (easy)
						masks[i][randInd] = true;

					} else {
						masks[i][randInd] = false;

					}
					index.remove(Integer.valueOf(randInd)); // update list of available index to check
				}
			}

		}
		return masks;
	}

	public boolean checkMask(boolean[][] mask, int row, int col, int levelCode) {
		int hcount = 0; // horizontal count
		int vcount = 0; // vertical count
		//System.out.printf("row: %d col: %d\n", row, col);
		// check row
		for (int i = 0; i < 9; i++) {
			if (masks[row][i]) { // if contains 'true'
				hcount++;
			}
		}

		// check column
		for (int j = 0; j < 9; j++) {
			if (masks[j][col]) {
				vcount++;
			}
		}
		//System.out.printf("h: %d v: %d\n", hcount, vcount);
		/**levelCode: ez-1, med-2, hard-3*/
		if (hcount >= levelCode || vcount >= levelCode) {
			//System.out.println("return false");
			return false;
		}
		//System.out.println("return true");
		return true; // return after checking the number of available blanks
	}

	public void printIndexs(ArrayList<Integer> list) {
		System.out.print("List: ");
		for (int index : list) {
			System.out.print(index + " ");
		}
	}

	/*For debug*/
	public void displayMask() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(masks[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/*Takes the game mode as parameter*/
	/*public SudokuGenerateMask(int gameMode) {
		//Mode: ez-1, med-2, hard-3
		generate(gameMode);
	}*/

}