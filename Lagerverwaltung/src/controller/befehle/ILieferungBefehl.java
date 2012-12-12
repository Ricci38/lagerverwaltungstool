package controller.befehle;

import java.util.Date;
import java.util.List;

import model.Buchung;
import model.Lieferung;

public interface ILieferungBefehl {

	public abstract Lieferung execute(Date d, int menge, List<Buchung> buchungen);

}