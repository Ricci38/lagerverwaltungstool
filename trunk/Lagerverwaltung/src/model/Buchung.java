package model;

import java.util.Date;

public class Buchung {

	private final int menge;
	private final int lieferung_ID;
	private final Date datum;
	private static int id;

	public Buchung(int m, Date datum) {
		//FIXME: Hier müsste eine Buchung ausgeführt werden, anstatt die Menge einfach zu setzen
		//Dann würde auch jedes Lager das mit einem Anfangsbestand (z.B. in der Beispielhierarchie) erstellt wurde eine Zugangsbuchung haben
		this.menge = m;
		this.lieferung_ID = getNextId();
		this.datum = datum;
	}

	public int getMenge() {
		return this.menge;
	}

	public int getLieferungID() {
		return lieferung_ID;
	}

	public Date getDatum() {
		return datum;
	}
	
	private static synchronized int getNextId() {
		return ++id;
	}
}
