package pl.kielce.tu.drylofudala.model.card;

import pl.kielce.tu.drylofudala.model.player.PositionType;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public final class CardProperties {
	public static final List<LocalCard> LocalCards = List.of(
			new LocalCard("graphics\\cards\\card_Andrzej.png", 15, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Infantry_2.png", 2, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Infantry_3.png", 3, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Jacek_Chrzan.png", 15, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Jan_Knot.png", 15, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Jan_Rambo.png", 15, PositionType.MELEE),
			new LocalCard("graphics\\cards\\card_Javelin_6.png", 6, PositionType.RANGED),
			new LocalCard("graphics\\cards\\card_Javelin_7.png", 7, PositionType.RANGED),
			new LocalCard("graphics\\cards\\card_Tank_6.png", 6, PositionType.RANGED));

	private CardProperties() {
	}

	public static OptionalInt getPointsForCard(String pathToCardImage) {
		return LocalCards.stream()
				.filter(card -> card.name().equals(pathToCardImage))
				.mapToInt(LocalCard::points)
				.findAny();
	}

	public static Optional<PositionType> getPositionType(String pathToCardImage) {
		return LocalCards.stream()
				.filter(card -> card.name().equals(pathToCardImage))
				.map(LocalCard::positionType)
				.findAny();
	}
}
