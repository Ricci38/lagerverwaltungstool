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

public class GUI_handler extends MouseAdapter implements ActionListener, TreeSelectionListener {

	static int lieferungID = 0;

	private static IBuchungBefehl befehlBuchung = new BuchungBefehlImpl();
	private static ILieferungBefehl befehlLieferung = new LieferungBefehlImpl();

	Oberflaeche gui;
	
	/**
	 * TODO finde Text plox
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
			gui.enableJetztBuchen();
			befehlBuchung.undo();
			gui.enableRedo();
			gui.disableAlleBuchungenBestaetigen();
			if (!befehlBuchung.hasRemainingUndos())
				gui.disableUndo();
			gui.refreshTree(gui.getAusgewaehlterKnoten());
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			befehlBuchung.redo();
			gui.disableGesamtmenge();
			gui.disableBuchungsArt();
			gui.enableUndo();
			if (!befehlBuchung.hasRemainingRedos())
				gui.disableRedo();
			if (gui.getVerbleibendeMenge() == 0)
				gui.enableAlleBuchungenBestaetigen();
			gui.refreshTree(gui.getAusgewaehlterKnoten());
		} else if (e.getActionCommand().toLowerCase().equals(("Jetzt buchen").toLowerCase())) {
			jetztBuchen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Best�tigen").toLowerCase())) {
			lieferungBestaetigen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Abbrechen").toLowerCase())) {
			lieferungAbbrechen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs-/ Lager�bersicht").toLowerCase())) {
			zeigeLieferungsLagerUebersicht(e);
		}
	}

	/**
	 * TODO finde Text plox
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (gui.isCardUebersichtAktiv())
			gui.showLagerbuchungen(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
		else if (gui.isCardNeueLieferungAktiv() && null != gui.getAusgewaehlterKnoten() && gui.getAusgewaehlterKnoten().isLeaf())
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten().getName());
	}

	/**
	 * TODO finde Text plox
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		try {
			JTable tbl_lieferungsUebersicht = (JTable) e.getSource();
			int selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
			if (selectedRow == -1) // Keine Zeile ausgew�hlt
				return;

			// Wert (Datum) der ausgew�hlten Zeile und ersten Spalte
			String value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();
			gui.showTabLieferungsBuchungen(Lieferung.getLieferung(value).getBuchungen());
		} catch (ClassCastException cce) {
			// Unterscheidung zwischen Zahl und Text in dem Textfeld
			// Bei Zahl: Textfeld wird nicht geleert
			// Bei Text: Textfeld wird geleert
			if (!Tools.isStringANumber(((JTextField) e.getSource()).getText()))
				((JTextField) e.getSource()).setText("");
		} catch (NullPointerException npe) {
			Tools.showMsg("Sie haben soeben einen NullPointer gewonnen!\nUnd niemand wei� warum.");
		}
	}

	/**
	 * F�hrt die passende Aktion zu dem Button "Neues Lager" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
	 */
	private void neuesLager(ActionEvent e) {
		// Neuen Knoten hinzuf�gen
		Lager pre_knoten = gui.getAusgewaehlterKnoten();

		// Falls ein Knoten ausgew�hlt wurde
		if (null != pre_knoten) {
			// Lagererstellung ist nur bei einem Bestand von 0 zul�ssig!
			if (pre_knoten.getEinzelBestand() == 0) {
				String name = null, menge_str = null;
				int menge = 0, pane_value;
				JTextField lagername = new JTextField();
				JTextField bestand = new JTextField();
				Object message[] = { "Lagername: ", lagername, "Anfangs Bestand: ", bestand };
				JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

				// Dialog erstellen und Eingabeparameter einlesen
				do {
					// Eingabemaske f�r den Knotennamen
					pane.createDialog("Neues Lager erstellen").setVisible(true);

					// Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
					pane_value = ((Integer) pane.getValue()).intValue();
					name = lagername.getText().trim();
					menge_str = bestand.getText().trim();

					if (pane_value == JOptionPane.OK_OPTION) {
						if (name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Es ist ein ung�ltiger Lagername eingegeben worden!", "Ung�ltige Bezeichnung",
									JOptionPane.ERROR_MESSAGE);
						} else if (!(null == menge_str || menge_str.isEmpty())) {
							if (Tools.isStringANumber(menge_str)) {
								menge = Integer.parseInt(menge_str);
								if (menge < 0) {
									Tools.showMsg("Es sind keine negativen Best�nde m�glich!");
									menge_str = ""; // Zuweisung eines leeren Strings, damit die do while Schleife erneut durchl�uft
								}
							} else {
								Tools.showMsg("Als Menge sind nur Zahlen erlaubt!");
								menge_str = ""; // Zuweisung eines leeren Strings, damit die do while Schleife erneut durchl�uft
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
				Tools.showErr("Das ausgew�hlte Lager besitzt einen Bestand. Lagererstellung nicht m�glich!");
				return;
			}
		}
		// Falls kein Lager ausgew�hlt wurde wird ein Fehler ausgegeben
		else {
			Tools.showErr("Es ist kein Lager ausgew�hlt, unter das das neue erstellt werden soll!");
			return;
		}
	}

	/**
	 * F�hrt die passende Aktion zu dem Button "Lager umbenennen" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
	 */
	private void lagerUmebennnen(ActionEvent e) {
		// Neuen Knoten hinzuf�gen
		Lager knoten = gui.getAusgewaehlterKnoten();

		// Falls ein Knoten ausgew�hlt wurde
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
				// Eingabemaske f�r den Knotennamen
				pane.createDialog("Lager umbenennen").setVisible(true);

				// Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
				pane_value = ((Integer) pane.getValue()).intValue();
				neuerName = neuerLagername.getText().trim();

				if (pane_value == JOptionPane.OK_OPTION) {
					if (null == neuerName || neuerName.isEmpty()) {
						Tools.showErr("Es ist ein ung�ltiger Lagername eingegeben worden!");
						continue;
					} else
						break;
				} else {
					pane.setVisible(false);
					return;
				}
				// falls auf OK geklickt wurde und...
			} while (true);

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
		// Falls kein Lager ausgew�hlt wurde, wird ein Fehler ausgegeben
		else {
			Tools.showMsg("Es ist kein Lager ausgew�hlt, das umgenannt werden kann!");
			return;
		}

		JTable tbl_lieferungsUebersicht = gui.getTbl_lieferungsUebersicht();
		int selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
		if (selectedRow == -1) // Keine Zeile ausgew�hlt
			return;

		// Wert (Datum) der ausgew�hlten Zeile und ersten Spalte
		String value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();
		gui.showLieferungsdetails(Lieferung.getLieferung(value).getBuchungen());
		gui.showLagerbuchungen(knoten.getBuchungen());
	}

	/**
	 * F�hrt die passende Aktion zu dem Button "Neue Lieferung" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
	 */
	private void neueLieferung(ActionEvent e) {
		if (gui.isCardNeueLieferungAktiv()) {
			int value = JOptionPane.showOptionDialog(null,
					"M�chten Sie die aktuelle Lieferung abbrechen um eine neue Lieferung zu machen?\nAlle �nderungen werden dabei verworfen!", "Sicher?",
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
	 * F�hrt die passende Aktion zu dem Button "Jetzt Buchen" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
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
				// Restmenge der aktuellen Buchung hinzugef�gt
				if (restProzent < 1 && restMenge > 0) {
					Tools.showMsg("Die Restmenge von " + (gui.getVerbleibendeMenge() - menge) + " wurde zur letzten Buchungsmenge hinzugef�gt.");
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
				// get�tigte Buchung auff�hrt
				gui.showLagerbuchungen(l.getBuchungen());
			} else {
				Tools.showErr("Der prozentuale Anteil ist zu hoch!\n\nDer gr��te m�gliche Wert w�re: " + gui.getVerbleibenderProzentanteil() + "%");
				return;
			}
		}
		gui.showLagerFuerBuchung(l.getName());
		gui.refreshTree(l);
		befehlBuchung.clearRedos();
		gui.disableRedo();
	}

	/**
	 * Holt sich die Werte f�r die Berechnung der zu buchenden Menge aus der GUI
	 * und berechnet die zu verbuchende Menge
	 * 
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
				result.add("Es sind nur Gesamtmengen gr��er 0 zul�ssig!");
				throw new LagerverwaltungsException("Gesamtmenge ist zu klein.", result, null);
			} else {
				if ((prozentualerAnteil < 1) || (prozentualerAnteil > 100)) {
					result.add("Es sind nur ganzzahlige Werte von 1 bis 100 erlaubt.");
					throw new LagerverwaltungsException("Ung�ltiger prozentueler Anteil!", result, null);
				} else
					// Abrunden
					return (int) Math.floor(((double) (gesamtmenge * prozentualerAnteil) / 100));
			}
		} else {
			result.add("Es sind nur ganzzahlige Werte erlaubt!");
			throw new LagerverwaltungsException("Ung�ltige Eingabe", result, null);
		}
	}

	/**
	 * F�hrt die passende Aktion zu dem Button "Best�tigen" in der Ansicht
	 * "Neue Lieferung" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
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
	 * F�hrt die passende Aktion zu dem Button "Abbrechen" in der Ansicht
	 * "Neue Lieferung" aus
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
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
	 * F�hrt die passende Aktion zu dem Button "Lieferungs-/ Lager�bersicht"
	 * aus. Alle aktuellen Buchungen, die nicht best�tigt wurden, werden
	 * abgebrochen und die Lieferungs�bersicht wird wieder angezeigt.
	 * 
	 * @param e
	 *            Wird aus actionPerformed(ActionEvent) �bergeben
	 */
	private void zeigeLieferungsLagerUebersicht(ActionEvent e) {
		// Falls eine neue Lieferung durchgef�hrt wird
		if (gui.isCardNeueLieferungAktiv()) {
			int value = JOptionPane
					.showOptionDialog(
							null,
							"M�chten Sie die aktuelle Lieferung abbrechen um zu der Lieferungs- / Lager�bersicht zu gelangen?\nAlle �nderungen werden dabei verworfen!",
							"Sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (value == JOptionPane.YES_OPTION) {
				lieferungAbbrechen(e); // Aktion entspricht dem Lieferungs
										// Abbruch
			} else if (value == JOptionPane.NO_OPTION) {
				// mach nichts
			}
		}
		gui.refreshTree();
	}

	/**
	 * Macht dem Objekt dieser Klasse die GUI bekannt.
	 * @param gui
	 */
	public void announceGui(Oberflaeche gui) {
		this.gui = gui;
	}

	/**
	 * Gibt die Referenz zum BuchungBefehlImpl-Objekt zur�ck
	 * 
	 * @return Referenz zum BuchungBefehlImpl-Objekt
	 */
	public static IBuchungBefehl getBefehlBuchung() {
		return befehlBuchung;
	}

	/**
	 * Gibt die Referenz zum LieferungBefehlImpl-Objekt zur�ck
	 * 
	 * @return Referenz zum LieferungBefehlImpl-Objekt
	 */
	public static ILieferungBefehl getBefehlLieferung() {
		return befehlLieferung;
	}

}