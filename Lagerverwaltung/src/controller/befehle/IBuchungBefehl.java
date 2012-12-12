package controller.befehle;

import java.util.Date;

import model.Buchung;
import model.Lager;

public interface IBuchungBefehl {
	
	public Buchung execute(Lager l, int menge, Date d);
	
	public void undo();
	
	public void redo();
	
	public void undoAll();

}