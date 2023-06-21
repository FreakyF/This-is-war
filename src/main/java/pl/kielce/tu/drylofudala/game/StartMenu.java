package pl.kielce.tu.drylofudala.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.kielce.tu.drylofudala.model.player.Player;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.utility.FontCreator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class StartMenu extends JPanel implements ActionListener {
	private static final Logger logger = LogManager.getLogger(StartMenu.class);
	private final transient IPersistanceRepository persistanceRepository;
	public Game game;
	public transient Player player;
	public transient Player player2;
	public Gameplay gameplay;
	public int playerInit = 0;
	private transient Socket socket;
	private JButton buttonStart;
	private JButton buttonConnect;
	private JButton buttonExit;
	private JLabel titleScreen;
	private String ip;
	private boolean isLocalPlayer;
	private int port;
	private boolean connected;
	private transient PrintWriter out;
	private transient BufferedReader in;
	private JTextField textFieldNick;
	private String nick;
	private String firstInfo1;
	private String secondInfo2;
	private JButton buttonPlayer1;
	private JButton buttonPlayer2;

	public StartMenu(final IPersistanceRepository persistanceRepository, Game game, Player player, Player player2, Gameplay gameplay) {
		this.persistanceRepository = persistanceRepository;
		this.game = game;
		this.player = player;
		this.player2 = player2;
		this.gameplay = gameplay;
		this.socket = this.game.getSocket();
		this.isLocalPlayer = false;
		this.connected = false;
		initMenu();
	}

	public JButton getButtonPlayer1() {
		return this.buttonPlayer1;
	}

	public JButton getButtonPlayer2() {
		return this.buttonPlayer2;
	}

	public void initMenu() {
		FontCreator fontCreator = new FontCreator(persistanceRepository);
		Font font = fontCreator.getButtonFont();

		int containerWidth = game.getContentPane().getWidth();
		int x = (containerWidth - 300) / 2;
		int xTitle = (containerWidth - 436) / 2;

		Font titleFont = fontCreator.getTitleFont();

		this.titleScreen = new JLabel("THIS IS WAR");
		this.titleScreen.setHorizontalTextPosition(SwingConstants.CENTER);
		this.titleScreen.setVerticalTextPosition(SwingConstants.CENTER);
		this.titleScreen.setBounds(xTitle, -50, 436, 200);
		this.titleScreen.setFont(titleFont);
		this.titleScreen.setForeground(Color.WHITE);
		this.titleScreen.setVisible(false);

		this.buttonStart = new JButton();
		this.buttonStart.setBounds(x, 145, 300, 100);
		this.buttonStart.setText("START");
		this.buttonStart.setFont(font);
		buttonStart.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonStart.setVerticalTextPosition(SwingConstants.CENTER);
		ImageIcon backgroundImageStart = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonStart.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonStart.setIcon(backgroundImageStart);
		this.buttonStart.setFont(font);
		this.buttonStart.setVisible(false);
		this.buttonStart.addActionListener(this);

		this.buttonConnect = new JButton();
		this.buttonConnect.setBounds(x, 251, 300, 100);
		this.buttonConnect.setText("CONNECT");
		buttonConnect.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonConnect.setVerticalTextPosition(SwingConstants.CENTER);
		ImageIcon backgroundImageConnect = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonConnect.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonConnect.setIcon(backgroundImageConnect);
		this.buttonConnect.setFont(font);
		this.buttonConnect.setVisible(false);
		this.buttonConnect.addActionListener(this);

		this.buttonExit = new JButton();
		this.buttonExit.setBounds(x, 463, 300, 100);
		this.buttonExit.setText("EXIT");
		buttonExit.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonExit.setVerticalTextPosition(SwingConstants.CENTER);
		ImageIcon backgroundImageExit = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonExit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonExit.setIcon(backgroundImageExit);
		this.buttonExit.setFont(font);
		this.buttonExit.setVisible(false);
		this.buttonExit.addActionListener(this);

		this.textFieldNick = new JTextField("Nickname");
		this.textFieldNick.setBounds(x, 357, 300, 100);
		this.textFieldNick.setBackground(Color.black);
		this.textFieldNick.setForeground(Color.white);
		this.textFieldNick.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		this.textFieldNick.setFont(font);
		this.textFieldNick.setVisible(false);
		this.textFieldNick.addActionListener(this);

		this.buttonPlayer1 = new JButton();
		this.buttonPlayer1.setBounds(x + 325, 145, 300, 100);
		this.buttonPlayer1.setText("PLAYER 1");
		buttonPlayer1.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonPlayer1.setVerticalTextPosition(SwingConstants.CENTER);
		this.buttonPlayer1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		ImageIcon backgroundImagePlayer1 = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonPlayer1.setIcon(backgroundImagePlayer1);
		this.buttonPlayer1.setFont(font);
		this.buttonPlayer1.setVisible(false);
		this.buttonPlayer1.addActionListener(this);

		this.buttonPlayer2 = new JButton();
		this.buttonPlayer2.setBounds(x + 325, 251, 300, 100);
		this.buttonPlayer2.setText("PLAYER 2");
		buttonPlayer2.setHorizontalTextPosition(SwingConstants.CENTER);
		buttonPlayer2.setVerticalTextPosition(SwingConstants.CENTER);
		ImageIcon backgroundImagePlayer2 = persistanceRepository.getImageIconForPath("graphics\\ui\\buttonBackground.png");
		this.buttonPlayer2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		this.buttonPlayer2.setIcon(backgroundImagePlayer2);
		this.buttonPlayer2.setFont(font);
		this.buttonPlayer2.setVisible(false);
		this.buttonPlayer2.addActionListener(this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonStart) {
			if (this.port == 1000 && this.connected && this.isLocalPlayer) {
				this.game.setView(2);
				this.gameplay.initPanel();
				this.game.startup();
			}
			this.game.repainter();
		}
		if (e.getSource() == this.buttonConnect) {
			this.ip = "localhost";
			this.port = 1000;
			this.nick = this.textFieldNick.getText();
			if (this.playerInit != 0 && !this.connected) {
				this.connect();
			}
			this.reload();
			this.game.repainter();
		}
		if (e.getSource() == this.buttonExit) {
			this.game.dispatchEvent(new WindowEvent(this.game, WindowEvent.WINDOW_CLOSING));
		}

		if (e.getSource() == this.buttonPlayer1) {
			this.playerInit = 1;
			this.player.setPlayerInit(1);
			this.isLocalPlayer = true;
			this.gameplay.setPlayer1Connected(true);
			this.game.repainter();
		}

		if (e.getSource() == this.buttonPlayer2) {
			this.playerInit = 2;
			this.player.setPlayerInit(2);
			this.isLocalPlayer = true;
			this.gameplay.setPlayer2Connected(true);
			this.game.repainter();
		}
	}

	public JButton getButtonStart() {
		return this.buttonStart;
	}

	public JTextField getTextFieldNick() {
		return this.textFieldNick;
	}

	public JButton getButtonConnect() {
		return this.buttonConnect;
	}

	public JButton getButtonExit() {
		return this.buttonExit;
	}

	public JLabel getTitleScreen() {
		return this.titleScreen;
	}

	public void setVisible() {
		if (this.game.getView() == 1) {
			this.buttonStart.setVisible(true);
			this.buttonConnect.setVisible(true);
			this.buttonExit.setVisible(true);
			this.buttonPlayer1.setVisible(true);
			this.buttonPlayer2.setVisible(true);
			this.textFieldNick.setVisible(true);
			this.titleScreen.setVisible(true);
		} else {
			this.buttonStart.setVisible(false);
			this.buttonConnect.setVisible(false);
			this.buttonExit.setVisible(false);
			this.buttonPlayer1.setVisible(false);
			this.buttonPlayer2.setVisible(false);
			this.textFieldNick.setVisible(false);
			this.titleScreen.setVisible(false);
		}
	}

	public String getFirstInfo1() {
		return this.firstInfo1;
	}

	public String getSecondInfo2() {
		return this.secondInfo2;
	}

	public void connect() {
		try {
			this.socket = new Socket(this.ip, this.port);
			this.game.setSocket(this.socket);
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			logger.debug("Socket: {}", socket);
			String wiadomosc = this.nick + "," + this.playerInit;
			this.out.println(wiadomosc);
			this.connected = true;
		} catch (UnknownHostException e) {
			logger.debug("Uknown host: {}", this.ip);
		} catch (IOException e) {
			logger.debug("Error connecting to the server: {}", this.ip);
		}
	}

	public void reload() {
		new Thread(() -> {
			String userInput = "Dane";
			try {
				this.out.println(userInput);
				String dane = this.in.readLine();
				String[] names = dane.split(",");
				if (names.length >= 4) {
					this.firstInfo1 = names[0];
					this.playerInit = Integer.parseInt(names[1]);
					this.secondInfo2 = names[2];
					this.player.setNick(this.firstInfo1);
					this.player2.setNick(this.secondInfo2);

				} else {
					this.firstInfo1 = "";
					this.playerInit = 0;
					this.secondInfo2 = "";
					this.player.setNick("");
					this.player2.setNick("");
				}
				logger.debug("data: {}", dane);
				this.game.repainter();
			} catch (IOException e) {
				logger.debug("Connection I/O error: {}", this.ip);
			}
		}).start();
	}

	public PrintWriter getOut() {
		return out;
	}

	public BufferedReader getIn() {
		return in;
	}
}
