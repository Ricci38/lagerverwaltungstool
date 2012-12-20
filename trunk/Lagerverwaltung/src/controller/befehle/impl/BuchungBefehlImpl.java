package controller.befehle.impl;

import java.util.Date;
import java.util.Stack;

import model.Buchung;
import model.Lager;
import view.Oberflaeche;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.befehle.IBuchungBefehl;

public class BuchungBefehlImpl implements IBuchungBefehl {

	private final Stack<Buchung> buchungsStackUndo = new Stack<Buchung>();
	private final Stack<Buchung> buchungsStackRedo = new Stack<Buchung>();
	private final Stack<Lager> lagerStackUndo = new Stack<Lager>();
	private final Stack<Lager> lagerStackRedo = new Stack<Lager>();

	@Override
	public Buchung execute(Lager l, int menge, Date d, int prozent) {
		Buchung b;
		l.veraenderBestand(menge);
		l.addBuchung(b = new Buchung(menge, d, l.getName(), prozent));
		buchungsStackUndo.push(b);
		lagerStackUndo.push(l);
		return b;
	}

	@Override
	public void undo() {
		try {
			Oberflaeche gui = OberflaecheImpl.getInstance();
			Buchung b = buchungsStackRedo.push(buchungsStackUndo.pop());
			Lager l = lagerStackRedo.push(lagerStackUndo.pop());
			l.removeBuchung(b);
			l.veraenderBestand(-b.getMenge());
			Buchung.getNeueBuchungen().remove(b);
			gui.showLagerFuerBuchung(l.getName());
			gui.setVerbleibendeMenge(gui.getVerbleibendeMenge() + (gui.isAbBuchung() ? -b.getMenge() : b.getMenge()));
			gui.setVerbleibenderProzentanteil(gui.getVerbleibenderProzentanteil() + b.getProzentAnteil());
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten().getName());
		} catch (Exception e) {
			// Sollte normalerweise nicht mehr benötigt werden :)
			// Nur für den Fall, dass irgendetwas schief läuft
			Tools.showMsg("Sie haben gewonnen!\n\nUnd zwar ein Stickstoffatom.\nWir gratulieren Ihnen herzlichst! :)");
			return;
		}
	}

	@Override
	public void redo() {
		try {
			Oberflaeche gui = OberflaecheImpl.getInstance();
			Buchung b = buchungsStackUndo.push(buchungsStackRedo.pop());
			Lager l = lagerStackUndo.push(lagerStackRedo.pop());
			l.veraenderBestand(b.getMenge());
			l.addBuchung(b);
			Buchung.getNeueBuchungen().add(b);
			gui.setVerbleibendeMenge(gui.getVerbleibendeMenge() - (gui.isAbBuchung() ? -b.getMenge() : b.getMenge()));
			gui.setVerbleibenderProzentanteil(gui.getVerbleibenderProzentanteil() - b.getProzentAnteil());
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten().getName());
		} catch (Exception e) {
			// Sollte normalerweise nicht mehr benötigt werden :)
			// Nur für den Fall, dass irgendetwas schief läuft
			Tools.showMsg("Sie haben gewonnen!\n\nUnd zwar ein Sauerstoffatom.\nWir gratulieren Ihnen herzlichst! :)");
		}
	}

	@Override
	public void undoAll() {
		while (!buchungsStackUndo.isEmpty() || !lagerStackUndo.isEmpty()) {
			undo();
		}
		clearAll();
	}

	@Override
	public void clearAll() {
		buchungsStackUndo.clear();
		buchungsStackRedo.clear();
		lagerStackUndo.clear();
		lagerStackRedo.clear();
		Buchung.clearNeueBuchungen();
	}
	
	@Override
	public boolean hasRemainingUndos() {
		return buchungsStackUndo.size() != 0 ? true : false;
	}
	
	@Override
	public boolean hasRemainingRedos() {
		return buchungsStackRedo.size() != 0 ? true : false;
	}

	@Override
	public void clearRedos() {
		buchungsStackRedo.clear();
		lagerStackRedo.clear();
	}

}
