package pl.kielce.tu.drylofudala.persistence;

import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import java.net.URL;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public interface IPersistanceRepository {
	@NotNull
	URL getResourceFromPath(@NotNull final String pathToResource);

	@NotNull
	ImageIcon getImageIconForPath(@NotNull String pathToResource);
}
