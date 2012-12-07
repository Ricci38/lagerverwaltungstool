package model;

import java.util.Date;

public class Lieferung {
	private Date lieferungsDatum;

	//XXX:Soll hier auch eine Liste aller Buchungen pro Lieferung gespeichert werden?
	//Oder soll man sich das aus den einzelnen Lagerobjekten zusammenflücken?
	
	public Lieferung(Date d)
	{
		this.lieferungsDatum = d;
	}
	
	public Date getLieferungsDatum()
	{
		return this.lieferungsDatum;
	}
}
