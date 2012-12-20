package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

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

public class Lagerverwaltung_handler implements ActionListener, TreeSelectionListener, MouseListener {

	Oberflaeche gui;
	static int lieferungID = 0;

	private static IBuchungBefehl befehlBuchung = new BuchungBefehlImpl();
	private static ILieferungBefehl befehlLieferung = new LieferungBefehlImpl();

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
		} else if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			lieferungBestaetigen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Abbrechen").toLowerCase())) {
			lieferungAbbrechen(e);
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs-/ Lagerübersicht").toLowerCase())) {
			zeigeLieferungsLagerUebersicht(e);
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (gui.isCardUebersichtAktiv())
			gui.showLagerbuchungen(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
		else if (gui.isCardNeueLieferungAktiv() && null != gui.getAusgewaehlterKnoten() && gui.getAusgewaehlterKnoten().isLeaf())
			gui.showLagerFuerBuchung(gui.getAusgewaehlterKnoten().getName());
	}

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
		} catch (NullPointerException npe) {
			Tools.showMsg("Sie haben soeben einen NullPointer gewonnen!\nUnd niemand weiß warum.");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

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
						} else if (!(null == menge_str || menge_str.isEmpty())) {
							if (Tools.isStringANumber(menge_str)) {
								menge = Integer.parseInt(menge_str);
								if (menge < 0) {
									Tools.showMsg("Es sind keine negativen Bestände möglich!");
									menge_str = ""; // Zuweisung eines leeren Strings, damit die do while Schleife erneut durchläuft
								}
							} else {
								Tools.showMsg("Als Menge sind nur Zahlen erlaubt!");
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
			Tools.showErr("Es ist kein Lager ausgewählt, unter das das neue erstellt werden soll!");
			return;
		}
		
	}

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

			if (pane_value == JOptionPane.OK_OPTION) {
				try {
					knoten.veraendereName(neuerName);
					gui.refreshTree(knoten);
				} catch (LagerverwaltungsException ex) {
					// Falls der Lagername bereits vergeben wurde, wird eine Exception geworfen
					Tools.showErr(ex);
				}
			}

		}
		// Falls kein Lager ausgewählt wurde wird ein Fehler ausgegeben
		else
		{
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
		gui.showLagerbuchungen(knoten.getBuchungen());
	}

	private void neueLieferung(ActionEvent e) {
		gui.disableNeuesLager();
		gui.disableNeueLieferung();
		gui.disableAlleBuchungenBestaetigen();
		gui.enableBuchungsArt();
		gui.selectTreeRoot();
		gui.showCardNeueLieferung();
		gui.showUndoRedo();
		gui.enableGesamtmenge();
		gui.disableNeueLieferung();
		gui.refreshTree(gui.getAusgewaehlterKnoten());
		gui.disableUndo();
		gui.disableRedo();
		gui.disableNeuesLager();
	}

	private void jetztBuchen(ActionEvent e) {
		int restMenge, restProzent, gesamtMenge;
		int menge = getBuchungsMenge();
		Lager l = gui.getAusgewaehlterKnoten();

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
				befehlBuchung.execute(l, menge, new Date(), Integer.parseInt(gui.getProzentualerAnteil()));
				gui.setVerbleibendeMenge(restMenge);
				gui.setVerbleibenderProzentanteil(restProzent);
				gui.showVerbleibendeMenge();
				gui.disableBuchungsArt();
				gui.disableGesamtmenge();
				gui.enableUndo();

				if (restMenge <= 0 || restProzent <= 0) {
					gui.disableJetztBuchen();
					gui.enableAlleBuchungenBestaetigen();
				}

				// Lagerbuchungen aktualisieren, sodass die Tabelle die soeben
				// getätigte Buchung aufführt
				gui.showLagerbuchungen(l.getBuchungen());
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

	private int getBuchungsMenge() {
		String gesamtmenge_str, prozentualerAnteil_str;
		int gesamtmenge, prozentualerAnteil;
		gesamtmenge_str = gui.getGesamtmenge();
		prozentualerAnteil_str = gui.getProzentualerAnteil();

		if (Tools.isStringANumber(gesamtmenge_str) && Tools.isStringANumber(prozentualerAnteil_str)) {
			gesamtmenge = Integer.parseInt(gesamtmenge_str);
			prozentualerAnteil = Integer.parseInt(prozentualerAnteil_str);

			if (gesamtmenge < 1)
				Tools.showErr("Es sind nur Gesamtmengen größer 0 zulässig!");
			else {
				if ((prozentualerAnteil < 1) || (prozentualerAnteil > 100))
					Tools.showErr("Ungültiger prozentueler Anteil! Nur ganzzahlige Werte von 1 bis 100.");
				else
					// Abrunden
					return (int) Math.floor(((double) (gesamtmenge * prozentualerAnteil) / 100));
			}
		} else
			Tools.showErr("Es sind nur ganzzahlige Werte erlaubt!");
		return -1; // Ungültige Eingabe
	}

	private void lieferungBestaetigen(ActionEvent e) {
		if (!Buchung.getNeueBuchungen().isEmpty()) {
			befehlLieferung.execute(new Date(), Buchung.getGesamtMenge(), Buchung.getNeueBuchungen());
			gui.enableNeuesLager();
			gui.enableLagerUebersicht();
			gui.hideUndoRedo();
			gui.showCardUebersicht();
			gui.enableLagerUmbenennen();
			gui.setVerbleibendeMenge(-1);
			gui.refreshTree();
			befehlBuchung.clearAll();
		} else
			Tools.showErr("Bitte zuerst auf \"Jetzt buchen\" klicken");
	}

	private void lieferungAbbrechen(ActionEvent e) {
		befehlBuchung.undoAll();
		gui.enableNeuesLager();
		gui.enableLagerUebersicht();
		gui.hideUndoRedo();
		gui.showCardUebersicht();
		gui.setVerbleibendeMenge(-1);
		gui.refreshTree();
	}

	private void zeigeLieferungsLagerUebersicht(ActionEvent e) {
		// Falls eine neue Lieferung durchgeführt wird
		if (gui.isCardNeueLieferungAktiv()) {
			int value = JOptionPane.showOptionDialog(null,
					"Möchten Sie die aktuelle Lieferung abbrechen um zu der Lieferungs- / Lagerübersicht zu gelangen?\nAlle Änderungen werden verworfen!",
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

	public void announceGUI_Lager(Oberflaeche myGUI) {
		this.gui = myGUI;
	}

	public static IBuchungBefehl getBefehlBuchung() {
		return befehlBuchung;
	}

	public static ILieferungBefehl getBefehlLieferung() {
		return befehlLieferung;
	}

}
