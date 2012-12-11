package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import view.impl.OberflaecheImpl;

public class Lagerverwaltung_handler implements ActionListener, TreeSelectionListener {

	Oberflaeche GUI_lager;
	static int lieferungID = 0;

	public void announceGUI_Lager(Oberflaeche myGUI) {
		this.GUI_lager = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().toLowerCase().equals(("Neues Lager").toLowerCase())) {

			// Neuen Knoten hinzuf�gen
			Lager pre_knoten;
			pre_knoten = OberflaecheImpl.getInstance().getAusgewaehlterKnoten();

			// Falls ein Knoten ausgew�hlt wurde
			if (!(pre_knoten == null)) {
				//Lagererstellung ist nur bei einem Bestand von 0 zul�ssig!
				if (pre_knoten.getEinzelBestand() == 0) {
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
							} else if (!(menge_str.isEmpty() || menge_str == null)) {
								try {
									menge = Integer.parseInt(menge_str);
								} catch (NumberFormatException ex) {
									JOptionPane.showMessageDialog(null, "Es sind nur Werte gleich oder gr��er 0 erlaubt!: ");
									menge_str = "";			//Zuweisung eines leeren Strings, damit die do while Schleife erneut durchl�uft
								}

							}
							// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
							else
								JOptionPane.showMessageDialog(null, "Die eingegebene Bestandsmenge ist ung�ltig!");
						}
					} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null))); // falls auf OK geklickt wurde und...

					if (pane_value == JOptionPane.OK_OPTION) {
						pre_knoten.addTreeElement(name, menge);
						OberflaecheImpl.getInstance().refreshTree(); // Anzeige des Trees aktualisieren
					}
				} else {
					JOptionPane.showMessageDialog(null, "Das ausgew�hlte Lager besitzt einen Bestand. Lagererstellung nicht m�glich!");
				}

			}
			// Falls kein Lager ausgew�hlt wurde wird ein Fehler ausgegeben
			else
				JOptionPane.showMessageDialog(null, "Es ist kein Lager ausgew�hlt, unter das das neue erstellt werden soll!");
		}

		else if (e.getActionCommand().toLowerCase().equals(("Neue Lieferung").toLowerCase())) {
			// Anmeldung des Handlers und Erzeugung des Frames
			OberflaecheImpl.getInstance().resetEinbuchungsAssi();
			OberflaecheImpl.getInstance().showEinbuchungsAssi();
		}

		else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "undo");
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "redo");
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs�bersicht").toLowerCase())) {
			// FIXME !!!
			OberflaecheImpl.getInstance().zeigeLieferungen(Lieferung.getAllLieferungen());
			
		} else if (e.getActionCommand().toLowerCase().equals(("Lager�bersicht").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Lager�bersicht");
		} else if (e.getActionCommand().toLowerCase().equals(("Lagersaldo").toLowerCase())) {
			//FIXME: �berpr�fung ob ein Lager ausgew�hlt wurde muss noch hinzugef�gt werden!
			Lager pre_knoten;
			pre_knoten = OberflaecheImpl.getInstance().getAusgewaehlterKnoten();

			JOptionPane.showMessageDialog(null, pre_knoten.getBestand());
		}
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
			OberflaecheImpl.getInstance().zeigeBuchungsdetails(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
	}

}
