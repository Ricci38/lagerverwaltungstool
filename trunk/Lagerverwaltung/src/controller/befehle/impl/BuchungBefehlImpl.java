package controller.befehle.impl;

import java.util.Date;
import java.util.Stack;

import model.Buchung;
import model.Lager;
import view.Tools;
import controller.befehle.IBuchungBefehl;

public class BuchungBefehlImpl implements IBuchungBefehl {
	
	private final Stack<Buchung> buchungsStackUndo = new Stack<Buchung>();
	private final Stack<Buchung> buchungsStackRedo = new Stack<Buchung>();
	private final Stack<Lager> lagerStackUndo = new Stack<Lager>();
	private final Stack<Lager> lagerStackRedo = new Stack<Lager>();
	

	@Override
	public Buchung execute(Lager l, int menge, Date d) {
		Buchung b;
		l.veraenderBestand(menge);
		l.addBuchung(b = new Buchung(menge, d));
		buchungsStackUndo.push(b);
		lagerStackUndo.push(l);
		return b;
	}

	@Override
	public void undo() {
		try {
			Buchung b = buchungsStackRedo.push(buchungsStackUndo.pop());
			Lager l = lagerStackRedo.push(lagerStackUndo.pop());
			l.removeBuchung(b);
			l.veraenderBestand(-b.getMenge());
		}catch (Exception e) {
			Tools.showMsg("Ich nix rückgängig machen können.");
			return;
		}
	}

	@Override
	public void redo() {
		
		Buchung b = buchungsStackUndo.push(buchungsStackRedo.pop());
		Lager l = lagerStackUndo.push(lagerStackRedo.pop());
		l.veraenderBestand(b.getMenge());
		l.addBuchung(b);
	}

	@Override
	public void undoAll() {
	
		while (!buchungsStackUndo.isEmpty() || !lagerStackUndo.isEmpty())
		{
			undo();
		}
		buchungsStackUndo.clear();
		buchungsStackRedo.clear();
		lagerStackUndo.clear();
		lagerStackRedo.clear();
	}
	
}
