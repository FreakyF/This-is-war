package pl.kielce.tu.drylofudala.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.utility.Validator;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Game extends JFrame {
	private static final Logger logger = LogManager.getLogger(Game.class);

	private final transient IPersistanceRepository persistanceRepository;
	public Gameplay gameplay;
	public HandPanel handPanel;
	public HandPanel oponentHandPanel;
	public transient Validator validator;
	public BoardPanel meleePanel;
	public BoardPanel rangedPanel;
	public BoardPanel oponentMeleePanel;
	public BoardPanel oponentRangedPanel;
	private transient Socket socket;
	private int view = 1;
	private StartMenu startMenu;
	private Draw draw;
	private int currentTurn;


	public Game(final IPersistanceRepository persistanceRepository) {
		logger.debug("New Game Object Instantieted");
		this.persistanceRepository = persistanceRepository;
		initializeGameWindow();
		initializeGameClasses();

		add(draw);
		setVisible(true);
	}

	public void initializeGameWindow() {
		this.setSize(1920, 1080);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void initializeGameClasses() {
		Player player = new Player();
		Player player2 = new Player();
		this.gameplay = new Gameplay(this, player, player2, handPanel, oponentHandPanel, meleePanel, rangedPanel, oponentMeleePanel, oponentRangedPanel, persistanceRepository);
		this.handPanel = new HandPanel(this, player, player2, meleePanel, rangedPanel, gameplay, false, persistanceRepository);
		this.oponentHandPanel = new HandPanel(this, player, player2, oponentMeleePanel, oponentRangedPanel, gameplay, true, persistanceRepository);
		this.startMenu = new StartMenu(persistanceRepository, this, player, player2, gameplay);
		this.validator = new Validator(this.startMenu, this, gameplay, handPanel, oponentHandPanel, player, player2);
		this.draw = new Draw(this, startMenu, player, player2, gameplay, persistanceRepository);
	}

	public PrintWriter getOut() {
		return this.startMenu.getOut();
	}

	public void startup() {
		this.validator.validate();
	}

	public Socket getSocket() {
		return this.socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getView() {
		return this.view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public void repainter() {
		this.draw.repaint();
	}

	public void drawAddHandle(HandPanel handle) {
		this.draw.add(handle);
	}

	public void drawAddBoard1(BoardPanel panel) {
		this.draw.add(panel);
	}

	public void drawAddBoard2(BoardPanel panel) {
		this.draw.add(panel);
	}

	public void oponentdrawAddHandle(HandPanel handle) {
		this.draw.add(handle);
	}

	public void oponentdrawAddBoard1(BoardPanel panel) {
		this.draw.add(panel);
	}

	public void oponentdrawAddBoard2(BoardPanel panel) {
		this.draw.add(panel);
	}

	public void setTurn(int turn) {
		this.currentTurn = turn;
	}

	public int getCurrentTurn() {
		return this.currentTurn;
	}

}
