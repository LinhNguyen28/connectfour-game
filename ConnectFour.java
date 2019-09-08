/**
 * Implements a ConnectFour panel specified by rows and columns. 
 * The frame includes buttons to interact with the panel and a label
 * status indicating the turn and the status of current game
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class ConnectFour extends JFrame {
	private int rows;
	private int columns;
	private JPanel cellPanel;
	private JPanel buttonPanel;
	private JLabel status;
	private Cell[][] cells;
	private JButton[] columnButtons;
	
	private Integer[] nextRow;
	private int currentPlayer;
	

	public static void main(String[] args) {
		new ConnectFour(6, 7);
	}
	
	/**
	 * Constructs a ConnectFour frame with specified rows and columns
	 * @param rows number of rows in this panel
	 * @param cols number of columns in this panel
	 */
	private ConnectFour(int rows, int cols) {
		super("Let's play Connect 4!");
		if (rows < 4 || cols < 4 || rows > 20 || cols > 20) {
			throw new IllegalArgumentException("Bad data");
		}
		this.rows = rows;
		this.columns = cols;
		currentPlayer = 1;
		nextRow = new Integer[columns];
		for (int i = 0; i < columns; i++) {
			nextRow[i] = rows - 1;
		}
		
		frameSetup();
		buttonPanelSetup();
		cellPanelSetup();
		statusSetup();
		pack();
		this.setVisible(true);
	}
	
	/**
	 * Updates and changes the color of the cell (with position specified by row and column
	 * by setting the player 
	 * @param rows the row of this cell
	 * @param cols the column of this cell
	 * @param player new player this cell is set to 
	 */
	private void updateCell(int rows, int cols) {
		cells[rows][cols].setPlayer(currentPlayer);
		if (currentPlayer == 1) {
			currentPlayer = 2;
		} else {
			currentPlayer = 1;
		}
		nextRow[cols]--;
		
		status.setText("Player " + currentPlayer + "'s turn...");
	}
	
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			JButton button = (JButton) e.getSource();
			int cols = Integer.parseInt(button.getText());
			if (nextRow[cols] >= 0) {
				updateCell(nextRow[cols], cols);
			} 
			if (nextRow[cols] < 0) {
				nextRow[cols] = 0;
				button.setEnabled(false);
			}
			checkWinningStatus();
		}
		
	}

	private void checkWinningStatus() {
		int row = Collections.min(Arrays.asList(nextRow));
		if (row >= rows - 3) {
			row = rows - 4;
		}
		boolean escape = false;

			for (int i = row; i < rows - 3; i++) {
				for (int j = 0; j < columns - 3; j++) {
					if (checkSquare(i, j)) {
						escape = true;
						break;
					}
				}
				if (escape) break;
			}
			
	}
	
	private boolean hasEnded(int[] arr) {
		if (arr[0] != -1) {
			int r = arr[1];
			int c = arr[2];
			for (int i = 0; i < 4; i++) {
				if (arr[0] == 0) cells[r][c + i].setBackground(Color.GREEN);
				else if (arr[0] == 1) cells[r + i][c].setBackground(Color.GREEN);
				else if (arr[0] == 2) cells[r+i][c+i].setBackground(Color.GREEN);
				else cells[r + i][c - i].setBackground(Color.GREEN);
			}
			
			for (JButton btn : columnButtons)
				btn.setEnabled(false);
			int winPlayer = cells[r][c].getPlayer();
			status.setText("Player " + winPlayer + " wins!!!");
			return true;
		} else {
			if (!hasEmptyCell()) {
				status.setText("Tie Game");
				for (JButton btn : columnButtons) {
					btn.setEnabled(false);
				}
				return true;
			}
			return false;
		}
	}

	// Returns an array containing 3 elements: 
	// the first: 0 if row, 1 if column, 2 if diagonal LR, 3 if diagonal RL
	// the next two is the position of the first cell of those consecutive
	// elements
	// -1 if there is no row/col/diagonal satisfying the conditions
	private boolean checkSquare(int row, int col) {
		// A square is 4*4 cells with the starting point at row and col
		int countDiagonalLR = 0; // diagonal left to right (up)
		int countDiagonalRL = 0; // diagonal right to left (down)
		int[] arr = new int[] {-1};
		for (int i = 0; i < 4; i++) {
			int countRow = 0;
			int countCol = 0;	
			for (int j = 0; j < 3; j++) {
				if (checkTwoConsecutiveInRow(row, col, i, j)) countRow++;
				if (checkTwoConsecutiveInCol(row, col, i, j)) countCol++;
				if (i==j) {
					if (checkTwoConsecutiveDiagonalLR(row, col, i)) countDiagonalLR++;	
					if (checkTwoConsecutiveDiagonalRL(row, col, i)) countDiagonalRL++;
				}
			}
			if (countRow == 0) arr = new int[] {0, row + i, col};
			else if (countCol == 0)  arr = new int[] {1, row, col + i};
			else if (countDiagonalLR == 3) arr = new int[] {2, row, col};
			else if (countDiagonalRL == 3) arr = new int[] {3, row, col + 3};
			
			if (hasEnded(arr)) return true;
		}
		return false;
	}

	private boolean checkTwoConsecutiveDiagonalLR(int row, int col, int i) {
		return (cells[row + i][col + i].getPlayer() != 0
				&& cells[row + i + 1][col + i + 1].getPlayer() != 0 
				&& cells[row + i][col + i].getPlayer() == 
				cells[row + i + 1][col + i + 1].getPlayer());
	}

	private boolean checkTwoConsecutiveDiagonalRL(int row, int col, int i) {
		return (cells[row + 3 - i][col + i].getPlayer() != 0
				&& cells[row + 2 - i][col + i + 1].getPlayer() != 0
				&& cells[row + 3 - i][col + i].getPlayer() == 
				cells[row + 2 - i][col + i + 1].getPlayer());
	}
	
	private boolean checkTwoConsecutiveInCol(int row, int col, int i, int j) {
		return cells[row + j][col + i].getPlayer() == 0 
				|| cells[row + j +1][col + i].getPlayer() == 0
				|| cells[row + j][col + i].getPlayer() != cells[row + j +1][col + i].getPlayer();
	}
	
	private boolean checkTwoConsecutiveInRow(int row, int col, int i, int j) {
		return cells[row + i][col + j].getPlayer() == 0
				|| cells[row + i][col + j+1].getPlayer() == 0 
				|| cells[row + i][col + j].getPlayer() != cells[row + i][col + j+1].getPlayer();
	}
	
//	private void changeWinRow(int row, int col, int i) {
//		
//	}

	private boolean hasEmptyCell() {
			for (int j = 0; j < columns; j++) {
				if (cells[0][j].getPlayer() == 0) {
					return true;
				}
			}
			return false;
	}
	
	private void frameSetup() {
		this.setLocation(50, 50);
		this.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
	}
	
	private void buttonPanelSetup() {
		// create new JButton array for buttons at the top of columns
		columnButtons = new JButton[columns];
		
		// create a panel for buttons and set the layout of this panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 0));
		
		// create new button for each column and add them to the panel
		for (int i = 0; i < columns; i++) {
			columnButtons[i] = new JButton(i+"");
			ButtonListener btnListener = new ButtonListener();
			columnButtons[i].addActionListener(btnListener);
			buttonPanel.add(columnButtons[i]);
		}
		
		// add panel to the frame
		this.add(buttonPanel, BorderLayout.NORTH);
	}
	
	private void cellPanelSetup() {
		// create and set the layout for cellPanel
		cellPanel = new JPanel();
		cellPanel.setPreferredSize(new Dimension(70 * columns, 70 * rows));

		cellPanel.setLayout(new GridLayout(rows, columns));
		
		// create an array to store the cell panels
		cells = new Cell[rows][columns];
		
		// create the cells and add them to the panel
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				cells[i][j] = new Cell();
				cellPanel.add(cells[i][j]);
			}
		}
		
		// add this panel to the frame
		this.add(cellPanel, BorderLayout.CENTER);
	}
	
	private void statusSetup() {
		// create the label and set the alignment for text
		status = new JLabel("Player " + currentPlayer + "'s turn...", JLabel.CENTER);
		//status.setHorizontalAlignment(JLabel.CENTER);
		
		// add the label to frame
		this.add(status, BorderLayout.SOUTH);
	}
}
