package pl.kielce.tu.drylofudala.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Server {
	private static final Logger logger = LogManager.getLogger(Server.class);
	private final String[] player1Information = new String[5];
	private final String[] player2Information = new String[5];
	ClientThread client1Thread;
	ClientThread client2Thread;

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(1000)) {
			Socket clientSocket1 = null;
			Socket clientSocket2 = null;
			logger.debug("Waiting for connection.");

			while (true) {
				logger.debug(serverSocket);
				Socket clientSocket = serverSocket.accept();
				logger.debug("Client connected: {}", clientSocket.getInetAddress().getHostAddress());

				if (clientSocket1 == null) {
					clientSocket1 = clientSocket;
					logger.debug("First Client connected: {}", clientSocket1.getInetAddress().getHostAddress());
				} else if (clientSocket2 == null) {
					clientSocket2 = clientSocket;
					logger.debug("Second client connected: {}", clientSocket2.getInetAddress().getHostAddress());

					if (client1Thread == null) {
						logger.debug("First Thread Started.");
						client1Thread = new ClientThread(clientSocket1, clientSocket2, this, "player1", player1Information, player2Information);
						client1Thread.start();
					}

					if (client2Thread == null) {
						logger.debug("Second Thread started.");
						client2Thread = new ClientThread(clientSocket2, clientSocket1, this, "player2", player2Information, player1Information);
						client2Thread.start();
					}
				}
			}
		} catch (IOException e) {
			logger.debug(String.format("Server error: %s", e.getMessage()));
		}
	}
}
