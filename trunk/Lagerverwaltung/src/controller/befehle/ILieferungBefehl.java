package controller.befehle;

import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;

/**
 * Interface für den Lieferungsbefehl
 * 
 * @version 1.1.0
 * @author Dominik Klüter
 * 
 */
public interface ILieferungBefehl {

	public abstract Lieferung execute(Date d, int menge, String typ, List<Buchung> buchungen);

}