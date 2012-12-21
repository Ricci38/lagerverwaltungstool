package controller.befehle.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.GUI_handler;
import controller.befehle.ILieferungBefehl;

/**
 * @version 1.1.0
 * @author Dominik Klüter
 * 
 */
public class LieferungBefehlImpl implements ILieferungBefehl {

	/**
	 * Führt die Schritte, die für eine neue Lieferung nötig sind, aus.
	 */
	@Override
	public Lieferung execute(Date d, int menge, String typ, List<Buchung> buchungen) {
		for (Buchung b : buchungen) {
			b.updateDate(d); // Aktualisiert den Zeitpunkt der Buchungen, damit alle Buchungen einer Lieferungen den selben Zeitpunkt haben
		}
		for (Lieferung l : Lieferung.getAllLieferungen()) {
			if (l.getLieferungsDatum().equals(d)) {
				d.setTime(d.getTime() + 1000l); // Falls doch mal 2 Lieferungen den gleichen Lieferzeitpunkt haben sollten
				Tools.showMsg("Das Datum der Lieferung wurde angepasst, damit jede Lieferung einzigartig ist.\n" + "Neues Datum: "
						+ new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss").format(d));
			}
		}
		// Erstellt neue Lieferung mit den dazugehörigen Buchungen
		Lieferung l = new Lieferung(d, menge, typ, buchungen);
		GUI_handler.getBefehlBuchung().clearAll();
		OberflaecheImpl.getInstance().showLieferungen(Lieferung.getAllLieferungen());
		return l;
	}
}
