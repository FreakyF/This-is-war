package pl.kielce.tu.drylofudala.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kielce.tu.drylofudala.model.card.Card;
import pl.kielce.tu.drylofudala.model.card.CardProperties;
import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.model.player.PositionType;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.server.ServerMessages;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class HandPanel extends JPanel implements MouseListener {
	private static final int CARD_WIDTH = 100;
	private static final int CARD_HEIGHT = 140;
	private static final int CARD_PADDING = 5;
	private static final int NUMBER_OF_CARDS = CardProperties.LocalCards.size();
	private static final Logger logger = LogManager.getLogger(HandPanel.class);
	private final transient Card[] playerCards = new Card[NUMBER_OF_CARDS];
	private final JLabel[] cardLabels = new JLabel[NUMBER_OF_CARDS];
	private final transient Player player;
	private final transient Player player2;
	private final int oponentTurn;
	private final int playerTurn;
	private final transient IPersistanceRepository persistanceRepository;
	public BoardPanel meleePanel;
	public BoardPanel rangedPanel;
	public Game game;
	public Gameplay gameplay;
	int playerPoints;
	int player2Points;
	private ImageIcon backgroundImage;
	private transient PrintWriter out2;
	private JDialog enlargedCardDialog;

	public HandPanel(Game game, Player player, Player player2, BoardPanel meleePanel, BoardPanel rangedPanel, Gameplay gameplay, boolean isOponentHandPanel, IPersistanceRepository persistanceRepository) {
		setLayout(null);
		this.player = player;
		this.player2 = player2;
		this.meleePanel = meleePanel;
		this.rangedPanel = rangedPanel;
		this.gameplay = gameplay;
		this.game = game;
		this.oponentTurn = isOponentHandPanel ? 0 : 1;
		this.playerTurn = isOponentHandPanel ? 1 : 0;
		this.persistanceRepository = persistanceRepository;
		this.player2Points = player2.getHp();
		this.playerPoints = player.getHp();
		playerCardInit();
		initializeCardLabels();

		int x = CARD_PADDING;
		for (Component c : getComponents()) {
			c.setBounds(x, CARD_PADDING, CARD_WIDTH, CARD_HEIGHT);
			x += CARD_WIDTH + CARD_PADDING;
		}
	}

	private void initializeCardLabels() {
		for (int i = 0; i < this.playerCards.length; i++) {
			final Card card = this.playerCards[i];
			final ImageIcon cardIcon = persistanceRepository.getImageIconForPath(card.getPathToImage());
			final JLabel cardLabel = new JLabel(cardIcon);
			cardLabel.setName(card.getName());
			cardLabel.addMouseListener(this);
			this.cardLabels[i] = cardLabel;
			add(cardLabel);
		}
	}

	public void setBackgroundImage(ImageIcon imageIcon) {
		this.backgroundImage = imageIcon;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			Image image = backgroundImage.getImage();
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

	public void playerCardInit() {
		List<Card> allCards = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_CARDS; i++) {
			String cardPath = CardProperties.LocalCards.get(i).name();
			OptionalInt cardPoints = CardProperties.getPointsForCard(cardPath);
			Optional<PositionType> cardPositionType = CardProperties.getPositionType(cardPath);
			Card newCard = new Card(cardPath, cardPoints.orElse(0), cardPositionType.orElse(null));
			allCards.add(newCard);
		}
		Collections.shuffle(allCards, new Random());
		for (int i = 0; i < NUMBER_OF_CARDS; i++) {
			playerCards[i] = allCards.get(i);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switchPlayerTurn(true);
		if (player.getHp() <= 0 || player2.getHp() <= 0 || game.getCurrentTurn() == oponentTurn) {
			return;
		}

		JLabel card = (JLabel) e.getSource();
		Card clickedCard = getCardByLabel(card);

		if (game.getCurrentTurn() == playerTurn) {
			if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON2) {
				switch (clickedCard.getPosition()) {
					case RANGED -> {
						meleePanel.addCard(card);
						meleePanel.revalidate();
						meleePanel.repaint();
					}
					case MELEE -> {
						rangedPanel.addCard(card);
						rangedPanel.revalidate();
						rangedPanel.repaint();
					}
					default -> throw new UnsupportedOperationException("Unhandled card position type");
				}
			}

			remove(card);
			revalidate();
			repaint();
			game.repainter();
			out2 = game.getOut();

			if (game.getCurrentTurn() == playerTurn) {
				logger.debug("HandPanel inputline matched this.game.GetCurrentTurn()");
				switchPlayerTurn(false);
				game.setTurn(oponentTurn);
				int turn = game.getCurrentTurn();
				logger.debug("TURN INFORMATION: {}", turn);
				out2.println(ServerMessages.OPPONENT_TURN);
				out2.println(turn);
			}
		}

		if (e.getClickCount() == 1) {
			final MouseEvent event = e;
			Timer timer = new Timer(0, e1 -> mouseClicked(event));
			timer.setRepeats(false);
			timer.start();
		}
		sendInformation(true, card);
	}

	public void switchPlayerTurn(boolean state) {
		this.game.handPanel.setEnabled(state);
		this.game.rangedPanel.setEnabled(state);
		for (JLabel cardLabel : this.cardLabels) {
			cardLabel.setEnabled(state);
		}

		this.game.oponentHandPanel.setEnabled(state);
		this.game.oponentRangedPanel.setEnabled(state);
		for (JLabel cardLabel : this.game.oponentHandPanel.cardLabels) {
			cardLabel.setEnabled(!state);
		}
	}

	public void sendInformation(boolean mouseClicked, JLabel card) {
		if (!mouseClicked) {
			return;
		}
		processCardClick(card);
	}

	private void processCardClick(JLabel cardLabel) {
		logger.debug("HandPanel input line processCardClick");
		Card card = getCardByLabel(cardLabel);
		int cardPoints = card.getPoints();

		String cardClickedMessage = String.format("%s: %s", ServerMessages.PLAYER_CLICKED_CARD, card.getName());
		logger.debug("cardClickedMessage: {}", cardClickedMessage);
		this.out2.println(cardClickedMessage);
		this.player2.setHp(this.player2.getHp() - cardPoints);


		String healthPoints = Integer.toString(this.player2.getHp());
		String damageHandpanelMessage = String.format("%s: %s", ServerMessages.PLAYER_DAMAGE_HANDPANEL, card.getName());
		this.out2.println(damageHandpanelMessage);
		this.out2.println(healthPoints);
	}

	private Card getCardByLabel(JLabel cardLabel) {
		return getCardByName(cardLabel.getName());
	}

	public Card getCardByName(String cardName) {
		Optional<Card> card = Arrays.stream(this.playerCards).filter(c -> c.getName().contains(cardName)).findFirst();

		if (card.isEmpty()) {
			throw new UnsupportedOperationException("Card not found");
		}
		return card.get();
	}

	public JLabel getCardLabelByName(String cardName) {
		Optional<JLabel> cardLabel = Arrays.stream(this.cardLabels).filter(c -> c.getName().contains(cardName)).findFirst();

		if (cardLabel.isEmpty()) {
			throw new UnsupportedOperationException("Card label not found");
		}
		return cardLabel.get();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JLabel card = (JLabel) e.getSource();
		ImageIcon cardIcon = (ImageIcon) card.getIcon();
		Image cardImage = cardIcon.getImage();
		Image enlargedImage = cardImage.getScaledInstance(CARD_WIDTH * 2, -1, Image.SCALE_SMOOTH);
		ImageIcon enlargedCardImage = new ImageIcon(enlargedImage);
		enlargedCardDialog = new JDialog();
		enlargedCardDialog.setUndecorated(true);
		Window gameWindow = SwingUtilities.getWindowAncestor(card);
		Point dialogLocation = new Point(75, 700);
		SwingUtilities.convertPointToScreen(dialogLocation, gameWindow);
		enlargedCardDialog.setLocation(dialogLocation);
		JLabel enlargedCardLabel = new JLabel(enlargedCardImage);
		enlargedCardLabel.setBorder((BorderFactory.createLineBorder(Color.BLACK, 2)));

		enlargedCardDialog.getContentPane().add(enlargedCardLabel);
		enlargedCardDialog.pack();
		enlargedCardDialog.setVisible(true);
		setEnlargedCardDialog(enlargedCardDialog);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JDialog dialog = getEnlargedCardDialog();
		if (dialog != null) {
			dialog.dispose();
			setEnlargedCardDialog(null);
		}
	}

	public JDialog getEnlargedCardDialog() {
		return enlargedCardDialog;
	}

	public void setEnlargedCardDialog(JDialog enlargedCardDialog) {
		this.enlargedCardDialog = enlargedCardDialog;
	}
}
