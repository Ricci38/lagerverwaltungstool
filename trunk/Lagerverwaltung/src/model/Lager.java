package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import exception.LagerverwaltungsException;

public class Lager extends DefaultMutableTreeNode {

	private static Lager wurzel;
	private static List<String> namensListe = new ArrayList<String>();
	private Lager blatt;
	private int bestand;
	private boolean isBestandHaltend;
	private String name;

	private final List<Buchung> buchungen = new ArrayList<Buchung>();

	private static final long serialVersionUID = -6664495404957376450L;

	/**
	 * Erstellt ein neues Lager
	 * 
	 * @param bez
	 *            Der Name des Lagers
	 */
	public Lager(String bez) {
		super(bez + " 0");
		if (checkNamen(bez)) {
			namensListe.add(bez);
			this.name = bez;
			this.bestand = 0;
			this.isBestandHaltend = true;
		} else {
			List<String> result = new ArrayList<String>();
			result.add("Der Lagername \"" + bez + "\" wurde bereits verwendet!");
			throw new LagerverwaltungsException("Lagername bereits verwendet!", result, null);
		}
	}

	// Element (Blatt oder Knoten) hinzuf�gen
	public Lager addTreeElement(String bez) {
		blatt = new Lager(bez);
		this.add(blatt);
		this.isBestandHaltend = false; // �bergeordneter Knoten darf keinen Bestand zeigen - falls diese Methode an einem Blatt aufgerufen wurde ist
										// dieses ebenfalls nicht mehr f�hig
										// einen Bestand anzuzeigen
	
		this.setUserObject(this.name); // �bergeordneten Knoten umbenennen, sodass der Bestand nicht mehr angezeigt wird
	
		return blatt;
	}

	public boolean addBuchung(Buchung b) {
		return buchungen.add(b);
	}

	//�berpr�fung ob der Lagername bereits vorgekommen ist
	private boolean checkNamen(String bez) {
		if (namensListe.isEmpty())
			return true;
		else {
			for (String name : namensListe) {
				if (bez.equals(name))
					return false;
			}
		}
		return true;
	}

//	public boolean checkBestandsaenderung(int menge) {
//		if ((this.bestand + menge) >= 0) {
//			return true;
//		}
//		return false;
//	}

	/**
	 * 
	 * @param menge
	 * @throws LagerverwaltungsException
	 */
	public int veraenderBestand(int menge) {
		if ((this.bestand + menge >= 0)) {
			this.bestand = this.bestand + menge;
			this.setUserObject(this.name + " " + this.bestand); // �ndert angezeigten Namen im Baum
			return 0;
		} else {
			int diff = Math.abs(this.bestand + menge);
			this.bestand = 0;
			this.setUserObject(this.name + " 0");
			return diff;
//			List<String> result = new ArrayList<String>();
//			result.add("Bestand kleiner 0 nicht m�glich.");
//			throw new LagerverwaltungsException("Bestand vom Lager \"" + this.name + "\" kann nicht ge�ndert werden.", result, null);
		}
	}

	/**
	 * Ver�ndert den Namen eines Lagers, insofern nicht ein anderes Lager diesen
	 * Namen inne hat.
	 * 
	 * @param name
	 *            Der neue Name
	 * @return
	 */
	public void veraendereName(String name) {
		if (checkNamen(name)) {
			this.name = name;
			this.setUserObject(this.isBestandHaltend ? name + " " + this.bestand : name);
			return;
		} else {
			List<String> result = new ArrayList<String>();
			result.add("Ein Lager mit diesem Name existiert bereits.");
			throw new LagerverwaltungsException("Der Name konnte nicht ge�ndert werden.", result, null);
		}
	}

	public boolean removeBuchung(Buchung b) {
		return buchungen.remove(b);
	}

	/**
	 * 
	 * @return Gibt den eigenen oder oder kumulierten Bestand aller Unterlager
	 *         zur�ck
	 */
	public int getBestand() {
		int bestand_summe = 0;

		if (this.isLeaf()) { // falls dieser Knoten keine Kinder hat
			return this.bestand;
		} else {
			// Best�nde der einzelnen Kinder/Bl�tter zusammenaddieren
			for (int i = 0; i < this.getChildCount(); i++) {
				bestand_summe = bestand_summe + ((Lager) this.getChildAt(i)).getBestand();
			}
			return bestand_summe;
		}
	}

	public List<Buchung> getBuchungen() {
		return buchungen;
	}

	//Gibt den eigenen Bestand des ausgew�hlten Lagers zur�ck
	public int getEinzelBestand() {
		return this.bestand;
	}

	/**
	 * Setzt den Bestand eines Lagers, insofern das Lager einen Bestand haben
	 * darf und die �bergebene Menge gr��er oder gleich 0 ist.
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

	public boolean isBestandHaltend() {
		return this.isBestandHaltend;
	}

	public String getName() {
		return name;
	}

	public static Lager getTree() {
		return wurzel;
	}

	// Wurzel erstellen
	public static Lager addWurzel(String bez) {
		wurzel = new Lager(bez);
		wurzel.name = bez;
		return wurzel;
	}
}
