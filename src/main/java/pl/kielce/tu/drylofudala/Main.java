package pl.kielce.tu.drylofudala;

import pl.kielce.tu.drylofudala.game.Game;
import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;
import pl.kielce.tu.drylofudala.persistence.PersistanceRepository;
import pl.kielce.tu.drylofudala.server.Server;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Main {
	private static final IPersistanceRepository PERSISTANCE_REPOSITORY = new PersistanceRepository();

	public static void main(String[] args) {
		new Game(PERSISTANCE_REPOSITORY);
		new Game(PERSISTANCE_REPOSITORY);
		Server server = new Server();
		server.start();
	}
}
