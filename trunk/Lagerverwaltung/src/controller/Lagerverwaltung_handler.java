package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Lager;
import view.Main;
import view.Oberflaeche;

public class Lagerverwaltung_handler implements ActionListener, TreeSelectionListener {

	Oberflaeche GUI_lager;

	public void announceGUI_Lager(Oberflaeche myGUI) {
		this.GUI_lager = myGUI;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().toLowerCase().equals(("Neues Lager").toLowerCase())) {

			// Neuen Knoten hinzufügen
			Lager pre_knoten;
			pre_knoten = Oberflaeche.getInstance().getAusgewaehlterKnoten();

			// Falls ein Knoten ausgewählt wurde
			if (!(pre_knoten == null)) {
				String name = null, menge_str = null;
				int menge = 0, pane_value;
				JTextField lagername = new JTextField();
				JTextField bestand = new JTextField();
				Object message[] = { "Lagername: ", lagername, "Anfangs Bestand: ", bestand };
				JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

				// Dialog erstellen und Eingabeparameter einlesen
				do {
					pane.createDialog("Neues Lager erstellen").setVisible(true); // Eingabemaske für den Knotennamen
					pane_value = ((Integer) pane.getValue()).intValue(); // Button Benutzung des Benutzers einlesen (Ok oder Abbruch)
					name = lagername.getText().trim();
					menge_str = bestand.getText().trim();

					if (pane_value == JOptionPane.OK_OPTION) {

						if (name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Es ist ein ungültiger Lagername eingegeben worden!", "Ungültige Bezeichnung",
									JOptionPane.ERROR_MESSAGE);
						} else if (!(menge_str.isEmpty() || menge_str == null)) {
							try {
								menge = Integer.parseInt(menge_str);
							} catch (NumberFormatException ex) {
								JOptionPane.showMessageDialog(null, "Es sind nur Werte gleich oder größer 0 erlaubt!: ");
								menge_str = "";			//Zuweisung eines leeren Strings, damit die do while Schleife erneut durchläuft
							}
						}
						// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
						else
							JOptionPane.showMessageDialog(null, "Die eingegebene Bestandsmenge ist ungültig!");
					}
				} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null))); // falls auf OK geklickt wurde und...

				if (pane_value == JOptionPane.OK_OPTION) {
					pre_knoten.addTreeElement(name, menge);
					Oberflaeche.getInstance().refreshTree(); // Anzeige des Trees aktualisieren
				}

			}
			// Falls kein Lager ausgewählt wurde wird ein Fehler ausgegeben
			else
				JOptionPane.showMessageDialog(null, "Es ist kein Lager ausgewählt, unter das das neue erstellt werden soll!");
		}

		else if (e.getActionCommand().toLowerCase().equals(("Neue Buchung").toLowerCase())) {
			// Anmeldung des Handlers und Erzeugung des Frames
			Main.verbindungEinbuchungsassistent();
		}

		else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "undo");
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "redo");
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungsübersicht").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Lieferungsübersicht");
		} else if (e.getActionCommand().toLowerCase().equals(("Lagerübersicht").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Lagerübersicht");
		} else if (e.getActionCommand().toLowerCase().equals(("Lagersaldo").toLowerCase())) {
			// JOptionPane.showMessageDialog(null, "Lagersaldo");
			//FIXME: Überprüfung ob ein Lager ausgewählt wurde muss noch hinzugefügt werden!
			Lager pre_knoten;
			pre_knoten = Oberflaeche.getInstance().getAusgewaehlterKnoten();

			JOptionPane.showMessageDialog(null, pre_knoten.getBestand());
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Anzeige der Buchungen für das Lager in der Hauptoberfläche
		if (((Lager) e.getPath().getLastPathComponent()).isLeaf())
		{
			Oberflaeche.getInstance().zeigeBuchungsdetails(( (Lager) e.getPath().getLastPathComponent()).getBuchungen());
		}
		
	}

}
