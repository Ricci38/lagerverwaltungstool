package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import view.Tools;
import controller.befehle.IBuchungBefehl;
import controller.befehle.ILieferungBefehl;
import controller.befehle.impl.BuchungBefehlImpl;
import controller.befehle.impl.LieferungBefehlImpl;
import exception.LagerverwaltungsException;

/**
 * Diese Klasse kümmert sich um alle Aktionen, die zur Laufzeit auf der
 * Oberfläche getätigt werden.
 * 
 * @version 1.1.0
 * @author Dominik Klüter
 * @author Philo Könneker
 * 
 */
public class GUI_handler extends MouseAdapter implements ActionListener, TreeSelectionListener {

	static int lieferungID = 0; // Startwert = 0

	private static IBuchungBefehl befehlBuchung = new BuchungBefehlImpl();
	private static ILieferungBefehl befehlLieferung = new LieferungBefehlImpl();

	Oberflaeche gui;

	/**
	 * Deligiert alle möglichen Klicks auf Buttons in der Oberfläche an die
	 * entsprechenden Methoden
	 * 
	 * @author Dominik Klüter
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().toLowerCase().equals(("Neues Lager").toLowerCase())) {
			neuesLager(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Neue Lieferung").toLowerCase())) {
			neueLieferung(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Lager umbenennen").toLowerCase())) {
			lagerUmebennnen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			undo();
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			redo();
		} else if (e.getActionCommand().toLowerCase().equals(("Jetzt buchen").toLowerCase())) {
			jetztBuchen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			lieferungBestaetigen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Abbrechen").toLowerCase())) {
			lieferungAbbrechen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs-/ Lagerübersicht").toLowerCase())) {
			zeigeLieferungsLagerUebersicht(e);
		}
	}

	/**
	 * Reagiert auf Änderung der Auswahl eines Lagers in der Baumstruktur der
	 * Oberfläche
	 * 
	 * @author Dominik Klüter
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (gui.isCardUebersichtAktiv())
			gui.showLagerbuchungen((Lager) e.getPath().getLastPathComponent());
		else if (gui.isCardNeueLieferungAktiv() && null != gui.getAusgewaehlterKnoten() && gui.getAusgewaehlterKnoten().isLeaf())
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten().getName());
	}

	/**
	 * Reagiert auf das Anklicken eines Tabelleneintrags in der
	 * Lieferungsübersicht oder auf das Anklicken von Textfeldern.
	 * 
	 * @author Philo Könneker
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		try {
			JTable tbl_lieferungsUebersicht = (JTable) e.getSource();
			int selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
			if (selectedRow == -1) // Keine Zeile ausgewählt
				return;
			// Wert (Datum) der ausgewählten Zeile und ersten Spalte
			String value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();
			gui.showTabLieferungsBuchungen(Lieferung.getLieferung(value).getBuchungen());
		} catch (ClassCastException cce) {
			// Unterscheidung zwischen Zahl und Text in dem Textfeld
			// Bei Zahl: Textfeld wird nicht geleert
			// Bei Text: Textfeld wird geleert
			if (!Tools.isStringANumber(((JTextField) e.getSource()).getText()))
				((JTextField) e.getSource()).setText("");
		}
	}

	/**
	 * Führt die passende Aktion zu dem Button "Neues Lager" aus
	 * 
	 * @author Dominik Klüter
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void neuesLager(ActionEvent e) {
		// Neuen Knoten hinzufügen
		Lager pre_knoten = gui.getAusgewaehlterKnoten();

		// Falls ein Knoten ausgewählt wurde
		if (null != pre_knoten) {
			// Lagererstellung ist nur bei einem Bestand von 0 zulässig!
			if (pre_knoten.getEinzelBestand() == 0) {
				String name = null, menge_str = null;
				int menge = 0, pane_value;
				JTextField lagername = new JTextField();
				JTextField bestand = new JTextField();
				Object message[] = { "Lagername: ", lagername, "Anfangs Bestand: ", bestand };
				JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

				// Dialog erstellen und Eingabeparameter einlesen
				do {
					// Eingabemaske für den Knotennamen
					pane.createDialog("Neues Lager erstellen").setVisible(true);

					// Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
					pane_value = ((Integer) pane.getValue()).intValue();
					name = lagername.getText().trim();
					menge_str = bestand.getText().trim();

					if (pane_value == JOptionPane.OK_OPTION) {
						if (name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Es ist ein ungültiger Lagername eingegeben worden!", "Ungültige Bezeichnung",
									JOptionPane.ERROR_MESSAGE);
						} else if (!(null == menge_str || menge_str.isEmpty())) { // wenn keine Menge im Pop-Up angegeben wurde
							if (Tools.isStringANumber(menge_str)) {
								menge = Integer.parseInt(menge_str);
								if (menge < 0) {
									Tools.showErr("Es sind keine negativen Bestände möglich!");
									menge_str = ""; // Zuweisung eines leeren Strings, damit die do while Schleife erneut durchläuft
								}
							} else {
								Tools.showErr("Als Menge sind nur Zahlen erlaubt!");
								menge_str = ""; // Zuweisung eines leeren Strings, damit die do while Schleife erneut durchläuft
							}
						}
						// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
						else {
							Tools.showErr("Bitte geben Sie eine Bestandsmenge an!");
							continue;
						}
					}
					// falls auf OK geklickt wurde und...
				} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null)));

				if (pane_value == JOptionPane.OK_OPTION) {
					try {
						pre_knoten.addTreeElement(name).veraenderBestand(menge);
						gui.refreshTree(gui.getAusgewaehlterKnoten()); // Anzeige des Trees aktualisieren
					} catch (LagerverwaltungsException ex) {
						// Falls der Lagername bereits vergeben wurde, wird eine Exception geworfen
						Tools.showErr(ex);
					}
				}
			} else {
				Tools.showErr("Das ausgewählte Lager besitzt einen Bestand. Lagererstellung nicht möglich!");
				return;
			}
		}
		// Falls kein Lager ausgewählt wurde wird ein Fehler ausgegeben
		else {
			Tools.showErr("Es ist kein Lager ausgewählt, unter dem das neue Lager erstellt werden soll!");
			return;
		}
	}

	/**
	 * Führt die passende Aktion zu dem Button "Neue Lieferung" aus
	 * 
	 * @author Dominik Klüter
	 * @author Philo Könneker
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void neueLieferung(ActionEvent e) {
		if (gui.isCardNeueLieferungAktiv()) {
			int value = JOptionPane.showOptionDialog(null,
					"Möchten Sie die aktuelle Lieferung abbrechen um eine neue Lieferung zu machen?\nAlle Änderungen werden dabei verworfen!", "Sicher?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (value == JOptionPane.YES_OPTION) {
				lieferungAbbrechen(e); // Aktion entspricht dem Lieferungs
										// Abbruch
			} else if (value == JOptionPane.NO_OPTION) {
				return;
			}
		}
		gui.disableNeuesLager();
		gui.disableAlleBuchungenBestaetigen();
		gui.disableLagerUmbenennen();
		gui.enableBuchungsArt();
		gui.selectTreeRoot();
		gui.showCardNeueLieferung();
		gui.showUndoRedo();
		gui.enableGesamtmenge();
		gui.refreshTree(gui.getAusgewaehlterKnoten());
		gui.disableUndo();
		gui.disableRedo();
		gui.disableNeuesLager();
	}

	/**
	 * Führt die passende Aktion zu dem Button "Lager umbenennen" aus
	 * 
	 * @author Dominik Klüter
	 * @author Philo Könneker
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void lagerUmebennnen(ActionEvent e) {
		// Neuen Knoten hinzufügen
		Lager knoten = gui.getAusgewaehlterKnoten();

		// Falls ein Knoten ausgewählt wurde
		if (null != knoten && !knoten.isRoot()) {
			String neuerName = null;
			int pane_value;
			JTextField lagername = new JTextField();
			JTextField neuerLagername = new JTextField();
			lagername.setText(knoten.getName());
			lagername.setEditable(false);
			Object message[] = { "Alter Lagername: ", lagername, "Neuer Lagername: ", neuerLagername };
			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

			// Dialog erstellen und Eingabeparameter einlesen
			do {
				// Eingabemaske für den Knotennamen
				pane.createDialog("Lager umbenennen").setVisible(true);

				// Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
				pane_value = ((Integer) pane.getValue()).intValue();
				neuerName = neuerLagername.getText().trim();

				if (pane_value == JOptionPane.OK_OPTION) {
					if (null == neuerName || neuerName.isEmpty()) {
						Tools.showErr("Es ist ein ungültiger Lagername eingegeben worden!");
						continue;
					} else
						break;
				} else {
					pane.setVisible(false);
					return;
				}
				// falls auf OK geklickt wurde und...
			} while (true);

			pane.setVisible(false);
			if (pane_value == JOptionPane.OK_OPTION) {
				try {
					knoten.veraendereName(neuerName);
					gui.refreshTree(knoten);
				} catch (LagerverwaltungsException ex) {
					// Falls der Lagername bereits vergeben wurde, wird eine Exception geworfen
					Tools.showErr(ex);
					return;
				}
			}

		}
		// Falls kein Lager ausgewählt wurde, wird ein Fehler ausgegeben
		else {
			Tools.showMsg("Es ist kein Lager ausgewählt, das umgenannt werden kann!");
			return;
		}

		JTable tbl_lieferungsUebersicht = gui.getTbl_lieferungsUebersicht();
		int selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
		if (selectedRow == -1) // Keine Zeile ausgewählt
			return;

		// Wert (Datum) der ausgewählten Zeile und ersten Spalte
		String value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();
		gui.showLieferungsdetails(Lieferung.getLieferung(value).getBuchungen());
		gui.showLagerbuchungen(knoten);
	}

	/**
	 * Macht die zuletzt getätigte Buchung rückgängig und passt die Oberfläche
	 * anschließend an.
	 * 
	 * @author Philo Könneker
	 */
	private void undo() {
		befehlBuchung.undo();
		gui.enableJetztBuchen();
		gui.enableRedo();
		gui.disableAlleBuchungenBestaetigen();
		if (!befehlBuchung.hasRemainingUndos())
			gui.disableUndo();
		gui.refreshTree(gui.getAusgewaehlterKnoten());
	}

	/**
	 * Wiederholt die zuletzt rückgängig gemachte Buchung und passt die
	 * Oberfläche anschließend an.
	 * 
	 * @auther Philo Könneker
	 */
	private void redo() {
		befehlBuchung.redo();
		gui.disableGesamtmenge();
		gui.disableBuchungsArt();
		gui.enableUndo();
		if (!befehlBuchung.hasRemainingRedos())
			gui.disableRedo();
		if (gui.getVerbleibendeMenge() == 0)
			gui.enableAlleBuchungenBestaetigen();
		gui.refreshTree(gui.getAusgewaehlterKnoten());
	}

	/**
	 * Führt die passende Aktion zu dem Button "Jetzt Buchen" aus
	 * 
	 * @author Dominik Klüter
	 * @author Philo Könneker
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void jetztBuchen(ActionEvent e) {
		int restMenge, restProzent, gesamtMenge, diff = 0;
		Lager l = gui.getAusgewaehlterKnoten();
		int menge = -1;
		try {
			menge = getBuchungsMenge();
		} catch (LagerverwaltungsException ex) {
			Tools.showErr(ex);
			return;
		}
		if (menge != -1) {
			gesamtMenge = Integer.parseInt(gui.getGesamtmenge());
			if (gui.getVerbleibendeMenge() == -1)
				gui.setVerbleibendeMenge(gesamtMenge);

			if (gui.getVerbleibenderProzentanteil() == -1)
				gui.setVerbleibenderProzentanteil(100);

			restMenge = gui.getVerbleibendeMenge() - menge;
			restProzent = gui.getVerbleibenderProzentanteil() - Integer.parseInt(gui.getProzentualerAnteil());

			if (restMenge >= 0) {
				// Falls weniger als 1 % der Restmenge verbleiben, wird die
				// Restmenge der aktuellen Buchung hinzugefügt
				if (restProzent < 1 && restMenge > 0) {
					Tools.showMsg("Die Restmenge von " + (gui.getVerbleibendeMenge() - menge) + " wurde zur letzten Buchungsmenge hinzugefügt.");
					menge = gui.getVerbleibendeMenge();
					restMenge = 0;
				}
				if (gui.isAbBuchung())
					menge = menge * -1;
				try {
					diff = befehlBuchung.execute(l, menge, new Date(), Integer.parseInt(gui.getProzentualerAnteil()));
				} catch (LagerverwaltungsException ex) {
					Tools.showErr(ex);
					return;
				}
				restMenge += diff;
				restProzent += (int) ((diff / (float) gesamtMenge) * 100);

				gui.disableBuchungsArt();
				gui.disableGesamtmenge();
				gui.setVerbleibendeMenge(restMenge);
				gui.setVerbleibenderProzentanteil(restProzent);
				gui.showVerbleibendeMenge();
				gui.enableUndo();

				if (restMenge <= 0 || restProzent <= 0) {
					gui.disableJetztBuchen();
					gui.enableAlleBuchungenBestaetigen();
				}

				// Lagerbuchungen aktualisieren, sodass die Tabelle die soeben
				// getätigte Buchung aufführt
				gui.showLagerbuchungen(l);
			} else {
				Tools.showErr("Der prozentuale Anteil ist zu hoch!\n\nDer größte mögliche Wert wäre: " + gui.getVerbleibenderProzentanteil() + "%");
				return;
			}
		}
		gui.showLagerFuerBuchung(l.getName());
		gui.refreshTree(l);
		befehlBuchung.clearRedos();
		gui.disableRedo();
	}

	/**
	 * Führt die passende Aktion zu dem Button "Bestätigen" in der Ansicht
	 * "Neue Lieferung" aus
	 * 
	 * @author Philo Könneker
	 * @author Dominik Klüter
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void lieferungBestaetigen(ActionEvent e) {
		if (!Buchung.getNeueBuchungen().isEmpty()) {
			befehlLieferung.execute(new Date(), Buchung.getGesamtMenge(), gui.isAbBuchung() ? "Abbuchung" : "Zubuchung", Buchung.getNeueBuchungen());
			gui.enableNeuesLager();
			gui.enableLagerUebersicht();
			gui.enableLagerUmbenennen();
			gui.hideUndoRedo();
			gui.showCardUebersicht();
			gui.setVerbleibendeMenge(-1);
			gui.setVerbleibenderProzentanteil(-1);
			gui.refreshTree();
			befehlBuchung.clearAll();
		} else
			Tools.showErr("Bitte zuerst auf \"Jetzt buchen\" klicken");
	}

	/**
	 * Führt die passende Aktion zu dem Button "Abbrechen" in der Ansicht
	 * "Neue Lieferung" aus
	 * 
	 * @author Philo Könneker
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void lieferungAbbrechen(ActionEvent e) {
		befehlBuchung.undoAll();
		gui.enableNeuesLager();
		gui.enableLagerUebersicht();
		gui.enableLagerUmbenennen();
		gui.hideUndoRedo();
		gui.showCardUebersicht();
		gui.setVerbleibendeMenge(-1);
		gui.setVerbleibenderProzentanteil(-1);
		gui.refreshTree();
	}

	/**
	 * Führt die passende Aktion zu dem Button "Lieferungs-/ Lagerübersicht"
	 * aus. Alle aktuellen Buchungen, die nicht bestätigt wurden, werden
	 * abgebrochen und die Lieferungsübersicht wird wieder angezeigt.
	 * 
	 * @author Dominik Klüter
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) übergeben
	 */
	private void zeigeLieferungsLagerUebersicht(ActionEvent e) {
		// Falls eine neue Lieferung durchgeführt wird
		if (gui.isCardNeueLieferungAktiv()) {
			int value = JOptionPane
					.showOptionDialog(
							null,
							"Möchten Sie die aktuelle Lieferung abbrechen um zu der Lieferungs- / Lagerübersicht zu gelangen?\nAlle Änderungen werden dabei verworfen!",
							"Sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (value == JOptionPane.YES_OPTION) {
				lieferungAbbrechen(e); // Aktion entspricht dem Lieferungs Abbruch
			}
		}
		gui.refreshTree();
	}

	/**
	 * Holt sich die Werte für die Berechnung der zu buchenden Menge aus der GUI
	 * und berechnet die zu verbuchende Menge
	 * 
	 * @author Dominik Klüter
	 * @author Philo Könneker
	 * @return Die Menge, die bei einer Buchung verbucht werden muss
	 */
	private int getBuchungsMenge() {
		String gesamtmenge_str, prozentualerAnteil_str;
		int gesamtmenge, prozentualerAnteil;
		gesamtmenge_str = gui.getGesamtmenge();
		prozentualerAnteil_str = gui.getProzentualerAnteil();
		List<String> result = new ArrayList<String>();

		if ((gesamtmenge_str.matches("\\d+") && !Tools.isStringANumber(gesamtmenge_str))
				|| (prozentualerAnteil_str.matches("\\d+") && !Tools.isStringANumber(prozentualerAnteil_str))) {
			result.add("Erlaubter Zahlenraum: 1 bis " + Integer.MAX_VALUE);
			throw new LagerverwaltungsException("Zahl ist leider zu lang.", result, null);
		}

		if (Tools.isStringANumber(gesamtmenge_str) && Tools.isStringANumber(prozentualerAnteil_str)) {
			gesamtmenge = Integer.parseInt(gesamtmenge_str);
			prozentualerAnteil = Integer.parseInt(prozentualerAnteil_str);

			if (gesamtmenge < 1) {
				result.add("Es sind nur Gesamtmengen größer 0 zulässig!");
				throw new LagerverwaltungsException("Gesamtmenge ist zu klein.", result, null);
			} else {
				if ((prozentualerAnteil < 1) || (prozentualerAnteil > 100)) {
					result.add("Es sind nur ganzzahlige Werte von 1 bis 100 erlaubt.");
					throw new LagerverwaltungsException("Ungültiger prozentueler Anteil!", result, null);
				} else
					// Abrunden
					return (int) Math.floor(((double) (gesamtmenge * prozentualerAnteil) / 100));
			}
		} else {
			result.add("Es sind nur ganzzahlige Werte erlaubt!");
			throw new LagerverwaltungsException("Ungültige Eingabe", result, null);
		}
	}

	/**
	 * Macht dem Objekt dieser Klasse die GUI bekannt.
	 * 
	 * @param gui
	 */
	public void announceGui(Oberflaeche gui) {
		this.gui = gui;
	}

	/**
	 * Gibt die Referenz zum BuchungBefehlImpl-Objekt zurück
	 * 
	 * @return Referenz zum BuchungBefehlImpl-Objekt
	 */
	public static IBuchungBefehl getBefehlBuchung() {
		return befehlBuchung;
	}

	/**
	 * Gibt die Referenz zum LieferungBefehlImpl-Objekt zurück
	 * 
	 * @return Referenz zum LieferungBefehlImpl-Objekt
	 */
	public static ILieferungBefehl getBefehlLieferung() {
		return befehlLieferung;
	}

}
