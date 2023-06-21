package pl.kielce.tu.drylofudala.persistence;

import org.jetbrains.annotations.NotNull;

import javax.swing.ImageIcon;
import java.net.URL;

/*
 * @author Kamil Fudala
 * @author Karol Dryło
 */
public final class PersistanceRepository implements IPersistanceRepository {
	@NotNull
	public URL getResourceFromPath(@NotNull final String pathToResource) {
		final URL resource = PersistanceRepository.class.getClassLoader().getResource(pathToResource);
		if (resource == null) {
			throw new IllegalArgumentException(String.format("File %s is null!", pathToResource));
		}
		return resource;
	}

	@NotNull
	public ImageIcon getImageIconForPath(@NotNull final String pathToResource) {
		return new ImageIcon(getResourceFromPath(pathToResource));
	}
}
