package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lieferung {
	private final Date lieferungsDatum;
	private final List<Buchung> buchungen;
	private final int gesamtMenge;
	private static List<Lieferung> lieferungen = new ArrayList<Lieferung>();

	public Lieferung(Date d, int gesamtMenge, ArrayList<Buchung> b) {
		this.lieferungsDatum = d;
		this.gesamtMenge = gesamtMenge;
		this.buchungen = new ArrayList<Buchung>(b);
	}
	
	public Lieferung(Date d, Buchung b) {
		this.lieferungsDatum = d;
		List<Buchung> bl = new ArrayList<Buchung>();
		bl.add(b);
		this.buchungen = bl;
		this.gesamtMenge = b.getMenge();
	}

	public Date getLieferungsDatum() {
		return this.lieferungsDatum;
	}

	public List<Buchung> getBuchungen() {
		return buchungen;
	}

	public static List<Lieferung> addLieferungen(Lieferung l) {
		lieferungen.add(l);
		return lieferungen;
	}

	public static List<Lieferung> getAllLieferungen() {
		return lieferungen;
	}

	public int getGesamtMenge() {
		return gesamtMenge;
	}
}