package pl.kielce.tu.drylofudala.game;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class BoardPanel extends JPanel {
	private ImageIcon backgroundIcon;

	public void setBackgroundImage(ImageIcon imageIcon) {
		this.backgroundIcon = imageIcon;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundIcon != null) {
			Image image = backgroundIcon.getImage();
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

	public void addCard(JLabel cardLabel) {
		add(cardLabel);
	}
}
