package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import controller.Lagerverwaltung_handler;
import exception.LagerverwaltungsException;

public class Lager extends DefaultMutableTreeNode {

	static Lager wurzel;
	private Lager blatt;
	private boolean isBestandHaltend;
	private int bestand;
	private String name;

	private final List<Buchung> buchungen = new ArrayList<Buchung>();

	private static final long serialVersionUID = -6664495404957376450L;

	// ### Konstruktoren ###
	public Lager(String bez) {
		super(bez);
		this.name = bez;
	}

	//XXX Darf man hier direkt eine Buchung ausführen???
	public Lager(String bez, int bestand) {
		super(bez + " " + bestand);
		Date d = new Date();
		this.name = bez;
		Lagerverwaltung_handler.getBefehlBuchung().execute(this, bestand, d);
	}

	//Gibt den eigenen oder oder kumulierten Bestand aller Unterlager wieder
	public int getBestand() {
		int bestand_summe = 0;

		if (this.isLeaf()) { // falls dieser Knoten keine Kinder hat
			return this.bestand;
		} else {
			// Bestände der einzelnen Kinder/Blätter zusammenaddieren
			for (int i = 0; i < this.getChildCount(); i++) {
				bestand_summe = bestand_summe + ((Lager) this.getChildAt(i)).getBestand();
			}
			return bestand_summe;
		}
	}

	//Gibt den eigenen Bestand des ausgewählten Lagers zurück
	public int getEinzelBestand() {
		return this.bestand;
	}

	/**
	 * Setzt den Bestand eines Lagers, insofern das Lager einen Bestand haben
	 * darf und die übergebene Menge größer oder gleich 0 ist.
	 * 
	 * @param menge
	 * @throws LagerverwaltungsException
	 */
	public void setBestand(int menge) {
		List<String> result = new ArrayList<String>();
		if (this.isBestandHaltend) {
			if (menge >= 0) {
				this.bestand = menge;
				return;
			} else {
				result.add("Bestand kann nicht kleiner als 0 sein.");
				throw new LagerverwaltungsException("Bestand vom Lager \"" + this.name + "\" kann nicht gesetzt werden", result, null);
			}
		} else {
			result.add("Lager \"" + this.name + "\" kann keinen Bestand haben.");
			throw new LagerverwaltungsException("Lager kann keinen Bestand haben.", result, null);
		}
	}

	/**
	 * 
	 * @param menge
	 * @throws LagerverwaltungsException
	 */
	public void veraenderBestand(int menge) {
		List<String> result = new ArrayList<String>();
		if ((this.bestand + menge >= 0)) {
			this.bestand = this.bestand + menge;
			this.setUserObject(this.name + ((menge != 0) ? " " + this.bestand : "")); // Ändert angezeigten Namen im Baum
		} else {
			result.add("Bestand kleiner 0 nicht möglich.");
			throw new LagerverwaltungsException("Bestand vom Lager \"" + this.name + "\" kann nicht geändert werden.", result, null);
		}
	}
	
	public boolean checkBestandsaenderung(int menge) {
		if ((this.bestand + menge) >= 0) {
			return true;
		}
		return false;
	}

	// Wurzel erstellen
	public static Lager addWurzel(String bez) {
		wurzel = new Lager(bez);
		wurzel.name = bez;
		return wurzel;
	}

	// Element (Blatt oder Knoten) hinzufügen
	public Lager addTreeElement(String bez) {
		blatt = new Lager(bez);
		this.add(blatt);
		this.isBestandHaltend = false; // übergeordneter Knoten darf keinen
										// Bestand zeigen - falls diese Methode
										// an einem Blatt aufgerufen wurde ist
										// dieses ebenfalls nicht mehr fähig
										// einen Bestand anzuzeigen
		blatt.isBestandHaltend = true;
		blatt.name = bez;

		this.setUserObject(this.name); // übergeordneten Knoten umbenennen,
										// sodass der Bestand nicht mehr
										// angezeigt wird

		return blatt;
	}

	
	public static Lager getTree() {
		return wurzel;
	}

	public boolean isBestandHaltend() {
		return this.isBestandHaltend;
	}

	public boolean addBuchung(Buchung b) {
		return buchungen.add(b);
	}
	
	public boolean removeBuchung(Buchung b) {
		return buchungen.remove(b);
	}

	public List<Buchung> getBuchungen() {
		return buchungen;
	}
	
	public String getName() {
		return name;
	}
}
