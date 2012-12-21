package controller.befehle.impl;

import java.util.Date;
import java.util.Stack;

import model.Buchung;
import model.Lager;
import view.Oberflaeche;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.befehle.IBuchungBefehl;

/**
 * Implementiert das Interface für den Buchungsbefehl.
 * 
 * @version 1.1.0
 * @author Philo Könneker
 * 
 */
public class BuchungBefehlImpl implements IBuchungBefehl {

	private final Stack<Buchung> buchungsStackUndo = new Stack<Buchung>();
	private final Stack<Buchung> buchungsStackRedo = new Stack<Buchung>();
	private final Stack<Lager> lagerStackUndo = new Stack<Lager>();
	private final Stack<Lager> lagerStackRedo = new Stack<Lager>();

	/**
	 * Führt die Schritte, die für eine neue Buchung nötig sind, aus.
	 */
	@Override
	public int execute(Lager l, int menge, Date d, int prozent) {
		Buchung b;
		int diff = l.veraenderBestand(menge);
		if (diff == menge) // Falls die zurückgegebene Differnz mit der Buchungsmenge identisch ist, wird keine Buchung ausgeführt
			return diff;
		l.addBuchung(b = new Buchung(menge + diff, d, l, prozent + ((diff > 0) ? (int) (menge / (float) prozent) : 0)));
		buchungsStackUndo.push(b);
		lagerStackUndo.push(l);
		return diff;
	}

	/**
	 * Macht die zuletzt getätigte Buchung rückgängig, insofern eine solche
	 * Buchung existiert, die rückgängig gemacht werden kann.
	 */
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
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten() != null ? gui.getAusgewaehlterKnoten().getName() : "Lagerverwaltung");
		} catch (Exception e) {
			// Sollte normalerweise nicht mehr benötigt werden :)
			// Nur für den Fall, dass irgendetwas schief läuft
			// Zeile 55 produzierte früher eine NullPointerException, deshalb dieser catch-Block
			Tools.showMsg("Sie haben gewonnen!\n\nUnd zwar ein Stickstoffatom.\nWir gratulieren Ihnen herzlichst! :)\n\n" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Wiederholt die zuletzt rückgängig gemachte Buchung.
	 */
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
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten() != null ? gui.getAusgewaehlterKnoten().getName() : "Lagerverwaltung");
		} catch (Exception e) {
			// Sollte normalerweise nicht mehr benötigt werden :)
			// Nur für den Fall, dass irgendetwas schief läuft
			// Zeile 80 produzierte früher eine NullPointerException, deshalb dieser catch-Block
			Tools.showMsg("Sie haben gewonnen!\n\nUnd zwar ein Sauerstoffatom.\nWir gratulieren Ihnen herzlichst! :)");
		}
	}

	/**
	 * Rollt alle getätigten Buchungen der aktuellen neuen Lieferung zurück auf
	 * den Ausgangszustand.
	 */
	@Override
	public void undoAll() {
		while (!buchungsStackUndo.isEmpty() || !lagerStackUndo.isEmpty()) {
			undo();
		}
		clearAll();
	}

	/**
	 * Leer alle Stacks und die Liste von neuen Buchungen in Buchung, um eine
	 * neue Lieferung zu ermöglichen.
	 */
	@Override
	public void clearAll() {
		buchungsStackUndo.clear();
		buchungsStackRedo.clear();
		lagerStackUndo.clear();
		lagerStackRedo.clear();
		Buchung.clearNeueBuchungen();
	}

	/**
	 * Löscht den Stack, der alle möglichen wiederholbaren Buchungen enthält.
	 * Wird aufgerufen, sobald eine neue Buchung getätigt wurde.
	 */
	@Override
	public void clearRedos() {
		buchungsStackRedo.clear();
		lagerStackRedo.clear();
	}

	/**
	 * @return true, wenn Buchungen rückgängig gemacht werden können
	 */
	@Override
	public boolean hasRemainingUndos() {
		return buchungsStackUndo.size() != 0;
	}

	/**
	 * @return true, wenn rückgängig gemachte Buchungen wiederholt werden
	 *         können.
	 */
	@Override
	public boolean hasRemainingRedos() {
		return buchungsStackRedo.size() != 0;
	}

}
