package controller.befehle.impl;

import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;
import view.impl.OberflaecheImpl;
import controller.Lagerverwaltung_handler;
import controller.befehle.ILieferungBefehl;

public class LieferungBefehlImpl implements ILieferungBefehl {

	@Override
	public Lieferung execute(Date d, int menge, List<Buchung> buchungen) {
		for (Buchung b : buchungen) {
			b.updateDate(d);
		}
		Lieferung l = new Lieferung(d, menge, buchungen);
		Lagerverwaltung_handler.getBefehlBuchung().clearAll();
		OberflaecheImpl.getInstance().zeigeLieferungen(Lieferung.getAllLieferungen());
		return l;
	}
}
