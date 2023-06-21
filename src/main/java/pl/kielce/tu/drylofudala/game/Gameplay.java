package pl.kielce.tu.drylofudala.game;

import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.server.ServerMessages;
import pl.kielce.tu.drylofudala.utility.FontCreator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Gameplay extends JFrame implements ActionListener {
	private final transient IPersistanceRepository persistanceRepository;
	public Game game;
	public transient Player player;
	public transient Player player2;
	public HandPanel handPanel;
	public HandPanel oponentHandPanel;
	public BoardPanel meleePanel;
	public BoardPanel rangedPanel;
	public BoardPanel oponentMeleePanel;
	public BoardPanel oponentRangedPanel;
	private boolean isPlayer1Connected;
	private boolean isPlayer2Connected;
	private JButton buttonSurender;
	private JButton buttonExit;

	private JButton buttonLoser;
	private JButton buttonWinner;

	Gameplay(Game game, Player player, Player player2, HandPanel handPanel, HandPanel oponentHandPanel, BoardPanel meleePanel, BoardPanel rangedPanel, BoardPanel oponentMeleePanel, BoardPanel oponentRangedPanel, IPersistanceRepository persistanceRepository) {
		this.game = game;
		this.player = player;
		this.player2 = player2;
		this.handPanel = handPanel;
		this.oponentHandPanel = oponentHandPanel;
		this.meleePanel = meleePanel;
		this.rangedPanel = rangedPanel;
		this.oponentMeleePanel = oponentMeleePanel;
		this.oponentRangedPanel = oponentRangedPanel;
		this.persistanceRepository = persistanceRepository;
		this.initElement();
		this.buttonEnd();
	}

	public void initPanel() {
		final ImageIcon handPanelBackground = persistanceRepository.getImageIconForPath("graphics\\ui\\handPanelBackground.png");

		if (isPlayer1Connected) {
			this.meleePanel = new BoardPanel();
			this.meleePanel.setBounds(350, 640, 1000, 150);
			this.meleePanel.setBackgroundImage(handPanelBackground);
			this.meleePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.drawAddBoard1(this.meleePanel);
			this.game.meleePanel = this.meleePanel;

			this.rangedPanel = new BoardPanel();
			this.rangedPanel.setBounds(350, 485, 1000, 150);
			this.rangedPanel.setBackgroundImage(handPanelBackground);
			this.rangedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.drawAddBoard2(this.rangedPanel);
			this.game.rangedPanel = this.rangedPanel;

			this.oponentMeleePanel = new BoardPanel();
			this.oponentMeleePanel.setBounds(350, 45, 1000, 150);
			this.oponentMeleePanel.setBackgroundImage(handPanelBackground);
			this.oponentMeleePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.oponentdrawAddBoard1(this.oponentMeleePanel);
			this.game.oponentMeleePanel = this.oponentMeleePanel;

			this.oponentRangedPanel = new BoardPanel();
			this.oponentRangedPanel.setBounds(350, 200, 1000, 150);
			this.oponentRangedPanel.setBackgroundImage(handPanelBackground);
			this.oponentRangedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.oponentdrawAddBoard2(this.oponentRangedPanel);
			this.game.oponentRangedPanel = this.oponentRangedPanel;

			this.handPanel = new HandPanel(this.game, this.player, this.player2, this.meleePanel, this.rangedPanel, this, false, persistanceRepository);
			this.game.handPanel = this.handPanel;
			this.handPanel.setBounds(350, 840, 1000, 150);
			this.handPanel.setBackgroundImage(handPanelBackground);
			this.handPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.drawAddHandle(this.handPanel);
		}

		if (isPlayer2Connected) {
			this.meleePanel = new BoardPanel();
			this.meleePanel.setBounds(350, 45, 1000, 150);
			this.meleePanel.setBackgroundImage(handPanelBackground);
			this.meleePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.drawAddBoard1(this.meleePanel);
			this.game.meleePanel = this.meleePanel;

			this.rangedPanel = new BoardPanel();
			this.rangedPanel.setBounds(350, 200, 1000, 150);
			this.rangedPanel.setBackgroundImage(handPanelBackground);
			this.rangedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.drawAddBoard2(this.rangedPanel);
			this.game.rangedPanel = this.rangedPanel;

			this.oponentMeleePanel = new BoardPanel();
			this.oponentMeleePanel.setBounds(350, 640, 1000, 150);
			this.oponentMeleePanel.setBackgroundImage(handPanelBackground);
			this.oponentMeleePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.oponentdrawAddBoard1(this.oponentMeleePanel);
			this.game.oponentMeleePanel = this.oponentMeleePanel;

			this.oponentRangedPanel = new BoardPanel();
			this.oponentRangedPanel.setBounds(350, 485, 1000, 150);
			this.oponentRangedPanel.setBackgroundImage(handPanelBackground);
			this.oponentRangedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.game.oponentdrawAddBoard2(this.oponentRangedPanel);
			this.game.oponentRangedPanel = this.oponentRangedPanel;

			this.oponentHandPanel = new HandPanel(this.game, this.player, this.player2, this.oponentMeleePanel, this.oponentRangedPanel, this, true, persistanceRepository);
			this.oponentHandPanel.setBounds(350, 840, 1000, 150);
			this.oponentHandPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
			this.oponentHandPanel.setBackgroundImage(handPanelBackground);
			this.game.oponentdrawAddHandle(this.oponentHandPanel);

		}
		pack();
		setLocationRelativeTo(null);
	}

	public void initElement() {
		FontCreator fontCreator = new FontCreator(persistanceRepository);
		Font font = fontCreator.getButtonFont();

		this.buttonSurender = new JButton();
		this.buttonSurender.setBounds(25, 45, 300, 100);
		this.buttonSurender.setText("SURRENDER");
		this.buttonSurender.setFont(font);
		buttonSurender.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonSurender.setVerticalTextPosition(SwingConstants.CENTER);

		this.buttonSurender.setVisible(false);
		this.buttonSurender.addActionListener(this);
		ImageIcon backgroundImageSurrender = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonSurender.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonSurender.setIcon(backgroundImageSurrender);

		this.buttonExit = new JButton();
		this.buttonExit.setBounds(25, 151, 300, 100);
		this.buttonExit.setText("EXIT");
		this.buttonExit.setFont(font);
		buttonExit.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonExit.setVerticalTextPosition(SwingConstants.CENTER);
		ImageIcon backgroundImageExit = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonExit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonExit.setIcon(backgroundImageExit);

		this.buttonExit.setVisible(false);
		this.buttonExit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		PrintWriter out2 = this.game.getOut();
		if (e.getSource() == this.buttonSurender) {
			int playerInit = this.player.getPlayerInit();
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to surrender?", "Surrender", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				player.setHp(0);
				player2.setHp(80);
				setButtonLoser(playerInit == 1);
				this.game.repainter();
				out2.println(ServerMessages.PLAYER_SURRENDER);
				out2.println(playerInit);
			}
			this.game.repainter();
		}

		if (e.getSource() == this.buttonExit) {
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				if (this.player.getPlayerInit() == 1) {
					System.exit(0);
				}
				if (this.player.getPlayerInit() == 2) {
					System.exit(0);
				}
			}
		}
	}

	public void buttonEnd() {
		FontCreator fontCreator = new FontCreator(persistanceRepository);
		Font font = fontCreator.getButtonFont();

		ImageIcon backgroundImage = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonWinner = new JButton("YOU WON");
		this.buttonWinner.setBounds(650, 370, 400, 100);

		this.buttonWinner.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonWinner.setIcon(backgroundImage);
		this.buttonWinner.setFont(font);
		buttonWinner.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonWinner.setVerticalTextPosition(SwingConstants.CENTER);
		this.buttonWinner.setVisible(false);
		this.buttonWinner.addActionListener(this);

		this.buttonLoser = new JButton("YOU LOST");
		this.buttonLoser.setBounds(650, 370, 400, 100);
		this.buttonLoser.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonLoser.setIcon(backgroundImage);
		this.buttonLoser.setFont(font);
		buttonLoser.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonLoser.setVerticalTextPosition(SwingConstants.CENTER);
		this.buttonLoser.setVisible(false);
		this.buttonLoser.addActionListener(this);
	}

	public JButton getButtonWinner() {
		return this.buttonWinner;
	}

	public void setButtonWinner(boolean x) {
		this.buttonWinner.setVisible(x);
	}

	public JButton getButtonLoser() {
		return this.buttonLoser;
	}

	public void setButtonLoser(boolean x) {
		this.buttonLoser.setVisible(x);
	}


	public JButton getButtonSurender() {
		return this.buttonSurender;
	}


	public JButton getButtonExit() {
		return this.buttonExit;
	}

	public void setVisible() {
		if (this.game.getView() == 2) {
			this.buttonSurender.setVisible(true);

			this.buttonExit.setVisible(true);
		} else {
			this.buttonSurender.setVisible(false);
			this.buttonExit.setVisible(false);
		}
	}

	public void setPlayer1Connected(boolean connected) {
		this.isPlayer1Connected = connected;
	}

	public void setPlayer2Connected(boolean connected) {
		this.isPlayer2Connected = connected;
	}
}
