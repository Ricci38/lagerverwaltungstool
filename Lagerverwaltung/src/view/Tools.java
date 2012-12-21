package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JOptionPane;

/**
 * Stellt verschiedene Methoden zur Verfügung, die in verschiedenen Klassen zum
 * Einsatz kommen können.
 * 
 * @version 1.1.0
 * @author Philo Könneker
 * 
 */
public class Tools {

	final static Insets insets = new Insets(5, 5, 5, 5);

	/**
	 * Fügt einem Container mit GridBagLayout eine neue Komponente hinzu.
	 * 
	 * Diese Methode wurde in Anlehnung an Hißmann, M. (2012),
	 * Vorlesungsunterlagen erstellt.
	 * 
	 * @author Hißmann
	 * 
	 * @param cont
	 *            Der Container, dem die Komponente hinzugefügt werden soll
	 * @param gbl
	 *            Das GridBagLayout in dem die Komponente positioniert werden
	 *            soll
	 * @param c
	 *            Die Komponente, die hinzugefügt werden soll
	 * @param x
	 *            Die X-Koordinate die die Komponente im Layout erhalten soll
	 * @param y
	 *            Die Y-Koordinate die die Komponente im Layout erhalten soll
	 * @param width
	 *            Die Breite die die Komponente im Layout erhalten soll
	 * @param height
	 *            Die Höhe die die Komponente im Layout erhalten soll
	 * @param weightx
	 *            Wie weit sich die Komponente in X-Richtung gestreckt werden
	 *            darf
	 * @param weighty
	 *            Wie wiet sich die Komponente in Y-Richtung gestreckt werden
	 *            darf
	 * @param gbcFill
	 *            Definiert, ob die Komponente in ihrer Größe angepasst werden
	 *            soll
	 */
	public static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height, double weightx, double weighty,
			int gbcFill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = insets; // Platzhalter zu den Seiten
		gbc.fill = gbcFill;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

	/**
	 * Überprüft, ob eine Zeichenkette in eine Ganzzahl überführt werden kann.
	 * 
	 * @param s
	 *            Die zu prüfende Zeichenkette
	 * @return true, wenn die Zeichenkette eine gültige Ganzzahl ist
	 */
	public static boolean isStringANumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Zeigt ein Pop-Up mit der Meldung, die von einer Exception stammt.
	 * 
	 * @param e
	 *            Die Exception
	 */
	public static void showErr(Exception e) {
		JOptionPane.showMessageDialog(null, e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Zeigt ein Pop-Up mit einer der übergebenen Zeichenkette
	 * 
	 * @param s
	 *            Der anzuzeigende Fehlertext
	 */
	public static void showErr(String s) {
		JOptionPane.showMessageDialog(null, s, "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Zeigt eine Pop-Up-Meldung, mit der übergebenen Zeichenkette
	 * 
	 * @param s
	 *            Die anzuzeigende Zeichenkette
	 */
	public static void showMsg(String s) {
		JOptionPane.showMessageDialog(null, s);
	}
}
