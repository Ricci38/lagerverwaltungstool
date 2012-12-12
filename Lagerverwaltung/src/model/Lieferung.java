package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lieferung {
	private final Date lieferungsDatum;
	private final List<Buchung> buchungen;
	private final int gesamtMenge;
	private static List<Lieferung> lieferungen = new ArrayList<Lieferung>();
	

	public Lieferung(Date d, int gesamtMenge, List<Buchung> b) {
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
		lieferungen.add(this);
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
	
	//Lieferungen werden durch das Datum eindeutig identifiziert -> Selektion anhand des Datums ist möglich
	public static Lieferung getLieferung(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
		
		for (Lieferung l : lieferungen)
		{
			if ((sdf.format(l.getLieferungsDatum())).equals(date))
				return l;
		}
		return null;
	}

	public int getGesamtMenge() {
		return gesamtMenge;
	}
}
