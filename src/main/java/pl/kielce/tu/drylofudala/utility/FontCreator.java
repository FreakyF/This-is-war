package pl.kielce.tu.drylofudala.utility;

import pl.kielce.tu.drylofudala.persistence.IPersistanceRepository;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public class FontCreator {
	IPersistanceRepository persistanceRepository;

	public FontCreator(IPersistanceRepository persistanceRepository) {
		this.persistanceRepository = persistanceRepository;

	}

	public Font getMenuFont() {
		Font font = new Font("Arial", Font.PLAIN, 36);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, persistanceRepository.getResourceFromPath("font\\Stencilia-A.ttf").openStream());
			font = font.deriveFont(Font.PLAIN, 36);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return font;
	}

	public Font getButtonFont() {
		Font font = new Font("Arial", Font.PLAIN, 48);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, persistanceRepository.getResourceFromPath("font\\Stencilia-A.ttf").openStream());
			font = font.deriveFont(Font.PLAIN, 48);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return font;
	}

	public Font getTitleFont() {
		Font font = new Font("Arial", Font.PLAIN, 72);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, persistanceRepository.getResourceFromPath("font\\Stencilia-A.ttf").openStream());
			font = font.deriveFont(Font.PLAIN, 72);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		return font;
	}
}
