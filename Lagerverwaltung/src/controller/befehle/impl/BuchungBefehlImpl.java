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
		Buchung buchung;
		l.veraenderBestand(menge);
		l.addBuchung(buchung = new Buchung(menge, d));
		buchungsStackUndo.push(buchung);
		lagerStackUndo.push(l);
		return buchung;
	}

	@Override
	public void undo() {
		try {
			Buchung buchung = buchungsStackRedo.push(buchungsStackUndo.pop());
			Lager lager = lagerStackRedo.push(lagerStackUndo.pop());
			lager.removeBuchung(buchung);
			lager.veraenderBestand(-buchung.getMenge());
		}catch (Exception e) {
			Tools.showMsg("Ich nix rückgängig machen können.");
			return;
		}
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoAll() {
		// TODO Auto-generated method stub
		
	}
	
}
