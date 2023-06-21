package pl.kielce.tu.drylofudala.model.player;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Player {
	private String nick;
	private int playerInit;
	private int hp = 80;

	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public int getPlayerInit() {
		return this.playerInit;
	}

	public void setPlayerInit(int playerInit) {
		this.playerInit = playerInit;
	}

	public int getHp() {
		return this.hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}
