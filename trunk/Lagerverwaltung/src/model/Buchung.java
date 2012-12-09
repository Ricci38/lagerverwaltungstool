package model;

import java.util.Date;

public class Buchung {

	private final int menge;
	private final int lieferung_ID;
	private final Date datum;

	public Buchung(int m, int id, Date datum) {
		//FIXME: Hier m�sste eine Buchung ausgef�hrt werden, anstatt die Menge einfach zu setzen
		//Dann w�rde auch jedes Lager das mit einem Anfangsbestand (z.B. in der Beispielhierarchie) erstellt wurde eine Zugangsbuchung haben
		this.menge = m;
		this.lieferung_ID = id;
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
}
