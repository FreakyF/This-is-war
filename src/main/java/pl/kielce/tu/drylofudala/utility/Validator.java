package pl.kielce.tu.drylofudala.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kielce.tu.drylofudala.game.BoardPanel;
import pl.kielce.tu.drylofudala.game.Game;
import pl.kielce.tu.drylofudala.game.Gameplay;
import pl.kielce.tu.drylofudala.game.HandPanel;
import pl.kielce.tu.drylofudala.game.StartMenu;
import pl.kielce.tu.drylofudala.model.card.Card;
import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.server.ServerMessages;

import javax.swing.JLabel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Validator {

	private static final Logger logger = LogManager.getLogger(Validator.class);
	private final Player player1;
	private final Player player2;
	public StartMenu startMenu;
	public BoardPanel meleePanel;
	public BoardPanel rangedPanel;
	public BoardPanel oponentMeleePanel;
	public BoardPanel oponentRangedPanel;
	public Game game;
	public Gameplay gameplay;
	public HandPanel handPanel;
	public HandPanel oponentHandPanel;
	private PrintWriter out;
	private BufferedReader in;

	public Validator(StartMenu menuStart, Game game, Gameplay gameplay, HandPanel handPanel, HandPanel oponentHandPanel, Player player1, Player player2) {
		this.startMenu = menuStart;
		this.game = game;
		this.gameplay = gameplay;
		this.handPanel = handPanel;
		this.oponentHandPanel = oponentHandPanel;
		this.player1 = player1;
		this.player2 = player2;
	}

	public void validate() {
		new Thread(() -> {
			logger.debug("Sending information and validating it");
			this.in = startMenu.getIn();
			this.out = startMenu.getOut();
			this.meleePanel = this.gameplay.meleePanel;
			this.rangedPanel = this.gameplay.rangedPanel;
			this.oponentMeleePanel = this.gameplay.oponentMeleePanel;
			this.oponentRangedPanel = this.gameplay.oponentRangedPanel;

			while (true) {
				try {
					String input = this.in.readLine();
					if (input == null) {
						return;
					}
					handleInput(input);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void handleInput(String input) throws IOException {
		if (input.startsWith(ServerMessages.PLAYER_CLICKED_CARD)) {
			handlePlayerClickedCard(input);
		} else if (input.startsWith(ServerMessages.OPPONENT_CLICKED_CARD)) {
			handleOpponentClickedCard(input);
		} else if (input.startsWith(ServerMessages.PLAYER_HP_TAKEN)) {
			handlePlayerHpTaken();
		} else if (input.startsWith(ServerMessages.OPPONENT_HP_TAKEN)) {
			handleOpponentHpTaken();
		} else if (input.startsWith(ServerMessages.PLAYER_TURN)) {
			handlePlayerTurn();
		} else if (input.startsWith(ServerMessages.SURRENDER)) {
			handleSurrender();
		}
	}

	private void handlePlayerClickedCard(String input) {
		logger.debug("Validator inputline matched PLAYER_CLICKED_CARD");
		if (handPanel == null) {
			throw new NullPointerException("Handpanel is null");
		}
		String cardName = input.split(": ")[1];
		logger.debug("cardName {}", cardName);
		Card selectedCard = handPanel.getCardByName(cardName);
		JLabel selectedCardLabel = handPanel.getCardLabelByName(cardName);

		if (this.game.getCurrentTurn() == 1) {
			switch (selectedCard.getPosition()) {
				case RANGED -> {
					this.meleePanel.add(selectedCardLabel);
					this.meleePanel.revalidate();
					this.meleePanel.repaint();
					this.game.repainter();
				}
				case MELEE -> {
					this.rangedPanel.add(selectedCardLabel);
					this.rangedPanel.revalidate();
					this.rangedPanel.repaint();
					this.game.repainter();
				}
				default -> throw new IllegalArgumentException("Unhandled card position: " + selectedCard.getPosition());
			}
		} else {
			setCardToPosition(selectedCard, selectedCardLabel);
		}
	}

	private void handleOpponentClickedCard(String input) {
		logger.debug("Validator inputline matched OPPONENT_CLICKED_CARD");
		if (oponentHandPanel == null) {
			throw new NullPointerException("Oponent Handpanel is null");
		}
		String cardName = input.split(": ")[1];
		Card selectedCard = oponentHandPanel.getCardByName(cardName);
		logger.debug("cardName: {}", cardName);
		JLabel selectedCardLabel = oponentHandPanel.getCardLabelByName(cardName);
		setCardToPosition(selectedCard, selectedCardLabel);
	}

	private void handlePlayerHpTaken() throws IOException {
		logger.debug("Validator inputline matched PLAYER_HP_TAKEN");
		String data = this.in.readLine();
		logger.debug("data value: {}", data);
		int newHP = Integer.parseInt(data);
		this.player1.setHp(newHP);
	}

	private void handleOpponentHpTaken() throws IOException {
		logger.debug("Validator inputline matched OPPONENT_HP_TAKEN");
		String data = this.in.readLine();
		logger.debug("data value: {}", data);
		int newHP = Integer.parseInt(data);
		this.player2.setHp(newHP);
	}

	private void handlePlayerTurn() throws IOException {
		logger.debug("Validator inputline matched PLAYER_TURN");
		String data = this.in.readLine();
		logger.debug("data in player_turn {}", data);
		this.game.setTurn(Integer.parseInt(data));
	}

	private void handleSurrender() throws IOException {
		int surrendered = Integer.parseInt(this.in.readLine());
		this.player1.setHp(80);

		if (surrendered == 1) {
			this.gameplay.setButtonWinner(true);
			out.println(ServerMessages.PLAYER_HP_TAKEN);
		} else if (surrendered == 2) {
			this.gameplay.setButtonLoser(true);
			out.println(ServerMessages.OPPONENT_HP_TAKEN);
		}

		out.println("0");
		this.player2.setHp(0);
		this.game.repainter();
	}


	private void setCardToPosition(Card selectedCard, JLabel selectedCardLabel) {
		switch (selectedCard.getPosition()) {
			case MELEE -> {
				this.oponentMeleePanel.add(selectedCardLabel);
				this.oponentMeleePanel.revalidate();
				this.oponentMeleePanel.repaint();
				this.game.repainter();
			}
			case RANGED -> {
				this.oponentRangedPanel.add(selectedCardLabel);
				this.oponentRangedPanel.revalidate();
				this.oponentRangedPanel.repaint();
				this.game.repainter();
			}
			default -> throw new IllegalArgumentException("Unhandled card position: " + selectedCard.getPosition());
		}
	}
}
