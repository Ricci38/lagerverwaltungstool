package model;

import javax.swing.tree.DefaultMutableTreeNode;

public class Lager extends DefaultMutableTreeNode {

	static Lager wurzel;
	Lager knoten, blatt;
	boolean isBestandHaltend;
	int bestand;
	String name;

	private static final long serialVersionUID = -6664495404957376450L;

	public Lager(String bez) {
		super(bez);
		bestand = 0;
	}

	public Lager(String bez, int bestand) {
		super(bez);
		this.bestand = bestand;
	}

	public int getBestand() {
		int bestand_summe = 0;

		if (this.isLeaf()) // falls dieser Knoten keine Kinder hat
		{
			return this.bestand;
		} else {
			// Bestände der einzelnen Kinder/Blätter zusammenaddieren
			for (int i = 0; i < this.getChildCount(); i++) {
				bestand_summe = bestand_summe + ((Lager) this.getChildAt(i)).getBestand();
			}
			return bestand_summe;
		}
	}

	/**
	 * Setzt den Bestand eines Lagers, insofern das Lager einen Bestand haben
	 * darf und die übergebene Menge größer oder gleich 0 ist.
	 * 
	 * @param menge
	 * @return 0 = keine Fehler; 1 = gegebene Menge ist kleiner 0; 2 = kann
	 *         keinen Bestand haben
	 */
	public int setBestand(int menge) {
		if (this.isBestandHaltend) {
			if (menge >= 0) {
				this.bestand = menge;
				return 0;
			} else
				return 1;
		}
		return 2;
	}

	public int veraenderBestand(int menge) {
		if (menge >= 0) {
			this.bestand = this.bestand + menge;
			return 0; // Fehlerfrei
		} else if ((this.bestand - menge >= 0)) {
			this.bestand = this.bestand - menge;
			return 0;
		} else
			return 1; // Fehler!
	}

	// Wurzel erstellen
	public static Lager addWurzel(String bez) {
		wurzel = new Lager(bez);
		wurzel.name = bez;
		return wurzel;
	}

	// Element (Blatt oder Knoten) hinzufügen
	public Lager addTreeElement(String bez, int menge) {
		blatt = new Lager(bez + " " + ((Integer) menge).toString());
		this.add(blatt);
		this.isBestandHaltend = false; // übergeordneter Knoten darf keinen
										// Bestand zeigen - falls diese Methode
										// an einem Blatt aufgerufen wurde ist
										// dieses ebenfalls nicht mehr fähig
										// einen Bestand anzuzeigen
		blatt.isBestandHaltend = true;
		blatt.name = bez;
		blatt.bestand = menge;

		this.setUserObject(this.name); // übergeordneten Knoten umbenennen,
										// sodass der Bestand nicht mehr
										// angezeigt wird

		return blatt;
	}

	// erstellten Baum zurückgeben
	public static Lager getTree() {
		return wurzel;
	}

	public boolean isBestandHaltend() {
		return this.isBestandHaltend;
	}
}
