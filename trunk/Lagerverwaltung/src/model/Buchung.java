package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Buchung {

	private static int id;
	private static List<Buchung> neueBuchungen = new ArrayList<Buchung>();
	
	private final int menge;
	private final int buchung_ID;
	private final String lagerName;
	private Date datum;

	public Buchung(int m, Date datum, String lagerName) {
		this.menge = m;
		this.lagerName = lagerName;
		this.buchung_ID = getNextId();
		this.datum = datum;
		neueBuchungen.add(this);
	}

	public int getMenge() {
		return this.menge;
	}

	public int getBuchungID() {
		return buchung_ID;
	}

	public Date getDatum() {
		return datum;
	}
	
	public void updateDate(Date d) {
		this.datum = d;
	}

	public static List<Buchung> getNeueBuchungen() {
		return neueBuchungen;
	}

	public static void clearNeueBuchungen() {
		neueBuchungen.clear();
	}
	
	public static int getGesamtMenge() {
		int m = 0;
		for (Buchung b : neueBuchungen) {
			m += b.getMenge();
		}
		return m;
	}

	public String getLagerName() {
		return lagerName;
	}

	private static synchronized int getNextId() {
		return ++id;
	}
}