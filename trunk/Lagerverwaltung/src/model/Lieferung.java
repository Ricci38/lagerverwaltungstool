package model;

import java.util.ArrayList;
import java.util.Date;

public class Lieferung {
	private Date lieferungsDatum;
	private ArrayList<Buchung> buchungen = new ArrayList<Buchung>();
	private static ArrayList<Lieferung> lieferungen = new ArrayList<Lieferung>();
	
	
	public Lieferung(Date d)
	{
		this.lieferungsDatum = d;
		
	}
	
	public Date getLieferungsDatum()
	{
		return this.lieferungsDatum;
	}

	public ArrayList<Buchung> getBuchungen() {
		return buchungen;
	}

	public void addBuchung(Buchung b) {
		buchungen.add(b);
	}
	
	public static ArrayList<Lieferung> addLieferung(Lieferung l)
	{
		lieferungen.add(l);
		return lieferungen;
	}
	
	public static ArrayList<Lieferung> getAllLieferungen()
	{
		return lieferungen;
	}
}
