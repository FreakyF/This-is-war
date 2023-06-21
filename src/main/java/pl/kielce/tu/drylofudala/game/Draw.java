package pl.kielce.tu.drylofudala.game;

import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.utility.FontCreator;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Draw extends JPanel {

	private final transient IPersistanceRepository persistanceRepository;
	public Game game;
	public StartMenu startMenu;
	public transient Player player;
	public transient Player player2;
	public Gameplay gameplay;
	private transient Image backstartImg;

	public Draw(Game game, StartMenu startMenu, Player player, Player player2, Gameplay gameplay, IPersistanceRepository persistanceRepository) {
		this.game = game;
		this.startMenu = startMenu;
		this.gameplay = gameplay;
		this.player = player;
		this.player2 = player2;
		this.setLayout(null);
		this.persistanceRepository = persistanceRepository;
		backgroundStart();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int panelWidth = getWidth();
		int panelHeight = getHeight();

		for (int x = 0; x < panelWidth; x += backstartImg.getWidth(this)) {
			for (int y = 0; y < panelHeight; y += backstartImg.getHeight(this)) {
				g.drawImage(backstartImg, x, y, this);
			}
		}

		g.setColor(Color.white);
		this.drawMenuStart();
		this.drawElementGame(g);
	}

	public void backgroundStart() {
		final URL backgroundResource = persistanceRepository.getResourceFromPath("graphics\\ui\\background.png");
		backstartImg = Toolkit.getDefaultToolkit().createImage(backgroundResource);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(backstartImg.getWidth(this), backstartImg.getHeight(this));
	}

	public void drawMenuStart() {
		this.startMenu.setVisible();
		if (this.game.getView() == 1) {
			this.add(this.startMenu.getButtonStart());
			this.add(this.startMenu.getButtonConnect());
			this.add(this.startMenu.getButtonExit());
			this.add(this.startMenu.getButtonPlayer1());
			this.add(this.startMenu.getButtonPlayer2());
			this.add(this.startMenu.getTextFieldNick());
			this.add(this.startMenu.getTitleScreen());
		}
	}

	public void drawElementGame(Graphics g) {
		FontCreator fontCreator = new FontCreator(persistanceRepository);
		Font font = fontCreator.getMenuFont();
		g.setFont(font);

		this.gameplay.setVisible();
		if (this.game.getView() == 2) {
			this.add(this.gameplay.getButtonSurender());
			this.add(this.gameplay.getButtonExit());
			g.setFont(font);
			g.drawString("Nick:" + this.startMenu.getFirstInfo1(), 1360, 940);
			g.drawString("Nick: " + this.startMenu.getSecondInfo2(), 1360, 80);
			g.drawString("Points: " + this.player.getHp(), 1360, 980);
			g.drawString("Points: " + this.player2.getHp(), 1360, 150);

			if (this.player.getPlayerInit() == 1) {
				if (this.game.getCurrentTurn() == 0) {
					g.drawString("Player turn: " + this.player.getNick(), 1400, 550);

				} else if (this.game.getCurrentTurn() == 1) {
					g.drawString("Player turn:" + this.player2.getNick(), 1400, 550);
				}
				if (player.getHp() > 0 && player2.getHp() <= 0) {
					g.drawString("Player: " + this.player.getNick() + " won", 1400, 650);
					this.gameplay.setButtonWinner(true);
					this.add(this.gameplay.getButtonWinner());

				}
				if (player2.getHp() <= 0) {
					g.drawString("Player: " + this.player2.getNick() + " lost", 1400, 750);
				}
				if (player.getHp() <= 0 && player2.getHp() > 0) {
					g.drawString("Player: " + this.player2.getNick() + " won", 1400, 650);
					this.gameplay.setButtonLoser(true);
					this.add(this.gameplay.getButtonLoser());
				}
				if (player.getHp() <= 0) {
					g.drawString("Player: " + this.player.getNick() + " lost", 1400, 750);
				}
			}
			if (this.player.getPlayerInit() == 2) {
				if (this.game.getCurrentTurn() == 0) {
					g.drawString("Player turn: " + this.player2.getNick(), 1400, 550);
				} else if (this.game.getCurrentTurn() == 1) {
					g.drawString("Player turn: " + this.player.getNick(), 1400, 550);
				}
				if (player.getHp() > 0 && player2.getHp() <= 0) {
					g.drawString("Player: " + this.player.getNick() + " won", 1400, 650);
					this.gameplay.setButtonWinner(true);
					this.add(this.gameplay.getButtonWinner());

				}
				if (player2.getHp() <= 0) {
					g.drawString("Player: " + this.player2.getNick() + "lost", 1400, 750);
				}
				if (player.getHp() <= 0 && player2.getHp() > 0) {
					g.drawString("Player: " + this.player2.getNick() + " won", 1400, 650);
					this.gameplay.setButtonLoser(true);
					this.add(this.gameplay.getButtonLoser());
				}
				if (player.getHp() <= 0) {
					g.drawString("Player: " + this.player.getNick() + " won", 1400, 750);
				}

			}
		}
	}
}
