package pl.kielce.tu.drylofudala.model.card;

import pl.kielce.tu.drylofudala.model.player.PositionType;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class Card {
	private final String name;
	private final String pathToImage;
	private final int points;
	private final PositionType position;

	public Card(String pathToImage, int points, PositionType position) {
		this.points = points;
		this.position = position;
		this.pathToImage = pathToImage;
		this.name = getCardName(pathToImage);
	}

	private static String getCardName(String pathToImage) {
		return pathToImage.substring(pathToImage.lastIndexOf("\\") + 1);
	}

	public String getPathToImage() {
		return this.pathToImage;
	}

	public int getPoints() {
		return this.points;
	}

	public PositionType getPosition() {
		return this.position;
	}

	public String getName() {
		return this.name;
	}
}
