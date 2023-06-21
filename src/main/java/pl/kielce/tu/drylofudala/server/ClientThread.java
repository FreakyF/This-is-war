package pl.kielce.tu.drylofudala.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class ClientThread extends Thread {
	private static final Logger logger = LogManager.getLogger(ClientThread.class);

	private final Socket mySocket;
	private final Socket enemySocket;

	private final Server server;
	private final String playerName;
	private final String[] myInformation;
	private final String[] enemyInformation;

	public ClientThread(Socket socket1, Socket socket2, Server server, String playerName, String[] information1, String[] information2) {
		this.mySocket = socket1;
		this.enemySocket = socket2;
		this.server = server;
		this.playerName = playerName;
		this.myInformation = information1;
		this.enemyInformation = information2;
	}

	@Override
	public void run() {
		try (
				PrintWriter enemyOut = new PrintWriter(enemySocket.getOutputStream(), true);
				PrintWriter myOut = new PrintWriter(mySocket.getOutputStream(), true);
				BufferedReader myIn = new BufferedReader(new InputStreamReader(mySocket.getInputStream()))
		) {
			String message = myIn.readLine();
			String[] data = message.split(",");
			initializePlayerInformation(data);

			while (true) {
				String inputLine = myIn.readLine();
				if (inputLine == null) {
					break;
				}
				processInputLine(inputLine, enemyOut, myIn, myOut);
			}
		} catch (IOException e) {
			logger.debug(String.format("Disconnected %s", playerName));
		} finally {
			closeSockets();
			removeClientThreadFromServer();
		}
	}

	private void initializePlayerInformation(String[] data) {
		this.myInformation[0] = data[0];
		this.myInformation[1] = data[1];
		this.myInformation[2] = "0";
		this.myInformation[3] = "0";
		this.myInformation[4] = "0";

		logger.debug("Player name: {}\n myInformation[0]: {}\n myInformation[1]: {}", playerName, myInformation[0], myInformation[1]);
		logger.debug("myInformation[0]: {} \n myInformation[1]: {} \n myInformation[2]: {} \n myInformation[3]: {}", myInformation[0], myInformation[1], myInformation[2], myInformation[3]);
	}

	private void processInputLine(String inputLine, PrintWriter enemyOut, BufferedReader myIn, PrintWriter myOut) throws IOException {
		if (inputLine.startsWith(ServerMessages.PLAYER_CLICKED_CARD)) {
			logger.debug("inputline matched PLAYER_CLICKED_CARD");
			enemyOut.println(inputLine);
		} else if (inputLine.startsWith(ServerMessages.OPPONENT_CLICKED_CARD)) {
			logger.debug("inputline matched OPPONENT_CLICKED_CARD");
			enemyOut.println(inputLine);
		} else if (inputLine.startsWith(ServerMessages.PLAYER_DAMAGE_HANDPANEL)) {
			logger.debug("inputline matched PLAYER_DAMAGE_HANDPANEL");
			printLogServerInformation(enemyOut, myIn, inputLine);
		} else if (inputLine.startsWith(ServerMessages.OPPONENT_DAMAGE_HANDPANEL)) {
			logger.debug("inputline matched OPPONENT_DAMAGE_HANDPANEL");
			printLogServerInformation(enemyOut, myIn, inputLine);
		} else if (inputLine.equals("Dane")) {
			myOut.println(getPlayerData());
		} else if (inputLine.equals(ServerMessages.GET_PLAYER_ID)) {
			sendPlayerId(myOut);
		} else if (inputLine.startsWith(ServerMessages.OPPONENT_TURN)) {
			handleOpponentTurn(enemyOut, myIn);
		} else if (inputLine.startsWith(ServerMessages.PLAYER_SURRENDER)) {
			enemyOut.println("gracz1");
			handlePlayerSurrender(myIn, enemyOut);
		}
	}

	private void printLogServerInformation(PrintWriter enemyOut, BufferedReader myIn, String inputLine) throws IOException {
		logger.debug("myInformation[2]: {} enemyInformation[2]: {}", myInformation[2], enemyInformation[3]);
		String newData = myIn.readLine();
		this.enemyInformation[2] = newData;
		logger.debug("readLine matched newData here {}", newData);
		String cardName = inputLine.split(": ")[1];
		String hpTakenMessage = String.format("%s: %s", ServerMessages.PLAYER_HP_TAKEN, cardName);
		enemyOut.println(hpTakenMessage);
		enemyOut.println(enemyInformation[2]);
	}

	private String getPlayerData() {
		return this.myInformation[0] + "," + this.myInformation[1] + "," + this.enemyInformation[0] + "," + this.enemyInformation[1];
	}

	private void sendPlayerId(PrintWriter myOut) {
		if (this.playerName.equals("player1")) {
			myOut.println("1");
		} else if (this.playerName.equals("player2")) {
			myOut.println("2");
		}
	}

	private void handleOpponentTurn(PrintWriter enemyOut, BufferedReader myIn) throws IOException {
		logger.debug("inputline matched OPPONENT_TURN");
		logger.debug(ServerMessages.OPPONENT_TURN);
		logger.debug("{} : {}", ServerMessages.PLAYER_TURN, myInformation[3]);
		String newData = myIn.readLine();
		enemyInformation[3] = newData;
		enemyOut.println(ServerMessages.PLAYER_TURN);
		enemyOut.println(enemyInformation[3]);
	}

	private void handlePlayerSurrender(BufferedReader myIn, PrintWriter enemyOut) throws IOException {
		String newData = myIn.readLine();
		enemyInformation[4] = newData;
		enemyOut.println(ServerMessages.SURRENDER);
		enemyOut.println(enemyInformation[4]);
	}

	private void closeSockets() {
		try {
			mySocket.close();
			enemySocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void removeClientThreadFromServer() {
		if (this == server.client1Thread) {
			server.client1Thread = null;
		} else if (this == server.client2Thread) {
			server.client2Thread = null;
		}
	}
}
