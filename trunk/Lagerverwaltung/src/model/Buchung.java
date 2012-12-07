package model;

public class Buchung {
	
	private int menge;
	private int lieferung_ID;
	//...

	
	
	public Buchung(int m, int id)
	{
		//TODO: Attribute setzen
		this.menge = m;
		this.lieferung_ID = id;
	}
	
	
	public int getMenge()
	{
		return this.menge;
	}


	public int getLieferungID() {
		return lieferung_ID;
	}
}
