package controller.befehle.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.Lagerverwaltung_handler;
import controller.befehle.ILieferungBefehl;

public class LieferungBefehlImpl implements ILieferungBefehl {

	@Override
	public Lieferung execute(Date d, int menge, String typ, List<Buchung> buchungen) {
		for (Buchung b : buchungen) {
			b.updateDate(d);
		}
		for (Lieferung l : Lieferung.getAllLieferungen()) {
			if (l.getLieferungsDatum().equals(d)) {
				d.setTime(d.getTime() + 1000l);
				Tools.showMsg("Das Datum der Lieferung wurde angepasst, damit jede Lieferung einzigartig ist.\n" +
						"Neues Datum: " + new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss").format(d));
			}
		}
		
		Lieferung l = new Lieferung(d, menge, typ, buchungen);
		Lagerverwaltung_handler.getBefehlBuchung().clearAll();
		OberflaecheImpl.getInstance().showLieferungen(Lieferung.getAllLieferungen());
		return l;
	}
}
