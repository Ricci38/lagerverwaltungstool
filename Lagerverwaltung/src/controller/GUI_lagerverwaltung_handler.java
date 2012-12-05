package controller;

import gui.GUI_lagerverwaltung;
import gui.GUI_main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.Lager;

public class GUI_lagerverwaltung_handler implements ActionListener {

	GUI_lagerverwaltung GUI_lager;

	public void announceGUI_Lager(gui.GUI_lagerverwaltung myGUI) {
		this.GUI_lager = myGUI;
	}

	// FIXME: Exception muss noch f�r String to Int hinzugef�gt werden (NumberFormatException)
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().toLowerCase().equals(("Neues Lager").toLowerCase())) {

			// Neuen Knoten hinzuf�gen
			Lager pre_knoten;
			pre_knoten = GUI_lagerverwaltung.getAusgewaehlterKnoten();

			// Falls ein Knoten ausgew�hlt wurde
			if (!(pre_knoten == null)) {
				String name = null, menge_str = null;
				int menge = 0, pane_value;
				JTextField lagername = new JTextField();
				JTextField bestand = new JTextField();
				Object message[] = { "Lagername: ", lagername, "Anfangs Bestand: ", bestand };
				JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

				// Dialog erstellen und Eingabeparameter einlesen
				do {
					pane.createDialog("Neues Lager erstellen").setVisible(true); // Eingabemaske f�r den Knotennamen
					pane_value = ((Integer) pane.getValue()).intValue(); // Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
					name = lagername.getText().trim();
					menge_str = bestand.getText().trim();

					if (pane_value == JOptionPane.OK_OPTION) {
						if (name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Es ist ein ung�ltiger Lagername eingegeben worden!", "Ung�ltige Bezeichnung",
									JOptionPane.ERROR_MESSAGE);
						} else if (!(menge_str.isEmpty() || menge_str == null))
							menge = Integer.parseInt(menge_str); // FIXME: Exception einf�gen! Evtl. Umwandlung in Methode verschieben um hier den Exception abfang zu implementieren

						// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
						else
							JOptionPane.showMessageDialog(null, "Die eingegebene Bestandsmenge ist ung�ltig!");
					}
				} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null))); // falls auf OK geklickt wurde und...

				if (pane_value == JOptionPane.OK_OPTION) {
					pre_knoten.addTreeElement(name, menge);
					GUI_lagerverwaltung.TreeRefresh(); // Anzeige des Trees aktualisieren
				}

			}
			// Falls kein Lager ausgew�hlt wurde wird ein Fehler ausgegeben
			else
				JOptionPane.showMessageDialog(null, "Es ist kein Lager ausgew�hlt, unter das das neue erstellt werden soll!");
		}

		else if (e.getActionCommand().toLowerCase().equals(("Neue Buchung").toLowerCase())) {
			// Anmeldung des Handlers und Erzeugung des Frames
			GUI_main.verbindungEinbuchungsassistent();
		}

		else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "undo");
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "redo");
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs�bersicht").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Lieferungs�bersicht");
		} else if (e.getActionCommand().toLowerCase().equals(("Lager�bersicht").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Lager�bersicht");
		} else if (e.getActionCommand().toLowerCase().equals(("Lagersaldo").toLowerCase())) {
			// JOptionPane.showMessageDialog(null, "Lagersaldo");

			Lager pre_knoten;
			pre_knoten = GUI_lagerverwaltung.getAusgewaehlterKnoten();

			JOptionPane.showMessageDialog(null, pre_knoten.getBestand());
		}
	}

}
