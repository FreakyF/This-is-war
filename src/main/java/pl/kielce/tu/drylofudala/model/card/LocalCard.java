package pl.kielce.tu.drylofudala.model.card;

import pl.kielce.tu.drylofudala.model.player.PositionType;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public record LocalCard(String name, int points, PositionType positionType) {
}
