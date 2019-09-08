/** 
 * Implements a Cell panel representing a block in
 * ConnectFour frame
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class Cell extends JPanel {
	private int player;

	/**
	 * Constructs a Cell with player initialized to 0
	 */
	public Cell() {
		this.player = 0;
		this.setBackground(new Color(226, 226, 0));
		Border blackline = BorderFactory.createLineBorder(Color.BLACK, 1);
		setBorder(blackline);
	}

	/**
	 * Sets the player of this Cell and repaint it
	 * @param player new player this Cell is set to
	 */
	public void setPlayer(int player) {
		if (player != 0 && player != 1 && player !=2) {
			throw new IllegalArgumentException(" player must be either 0, 1 or 2");
		}
		this.player = player;
		repaint();
	}

	/**
	 * Returns the current player of this Cell
	 * @return
	 */
	public int getPlayer() {
		return player;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (player == 0) {
			g.setColor(Color.GRAY);
		} else if (player == 1) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}

		g.fillOval(4, 3, this.getWidth() - 10, this.getHeight() - 8);

	}
}
