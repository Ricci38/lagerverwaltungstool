package controller.befehle.impl;

import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;
import controller.befehle.ILieferungBefehl;

public class LieferungBefehlImpl implements ILieferungBefehl {
	
	/* (non-Javadoc)
	 * @see controller.ILieferungBefehl#execute(model.Lieferung)
	 */
	@Override
	public Lieferung execute(Date d, int menge, List<Buchung> buchungen) {
		// TODO Neue Lieferung anlegen
		Lieferung l = new Lieferung(d, menge, buchungen);
		
		return l;
	}
}
