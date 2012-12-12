package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import view.impl.OberflaecheImpl;

public class Lagerverwaltung_handler implements ActionListener, TreeSelectionListener, MouseListener {

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
			if (pre_knoten != null) {
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
									Tools.showMsg("Es sind nur Werte gleich oder gr��er 0 erlaubt!: ");
									menge_str = "";			//Zuweisung eines leeren Strings, damit die do while Schleife erneut durchl�uft
								}

							}
							// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
							else
								Tools.showMsg("Die eingegebene Bestandsmenge ist ung�ltig!");
						}
					} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null))); // falls auf OK geklickt wurde und...

					if (pane_value == JOptionPane.OK_OPTION) {
						pre_knoten.addTreeElement(name, menge);
						OberflaecheImpl.getInstance().refreshTree(); // Anzeige des Trees aktualisieren
					}
				} else {
					Tools.showMsg("Das ausgew�hlte Lager besitzt einen Bestand. Lagererstellung nicht m�glich!");
				}

			}
			// Falls kein Lager ausgew�hlt wurde wird ein Fehler ausgegeben
			else
				Tools.showMsg("Es ist kein Lager ausgew�hlt, unter das das neue erstellt werden soll!");
		}

		else if (e.getActionCommand().toLowerCase().equals(("Neue Lieferung").toLowerCase())) {
			OberflaecheImpl.getInstance().resetEinbuchungsAssi();
			OberflaecheImpl.getInstance().showEinbuchungsAssi();
		}

		else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			Tools.showMsg("undo");
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			Tools.showMsg("redo");
		} else if (e.getActionCommand().toLowerCase().equals(("Lieferungs�bersicht").toLowerCase())) {
			// FIXME !!!
			OberflaecheImpl.getInstance().zeigeLieferungen(Lieferung.getAllLieferungen());
			
		} else if (e.getActionCommand().toLowerCase().equals(("Lager�bersicht").toLowerCase())) {
			//XXX: dieser Button ist doch eigentlich auch Sinnfrei... Man bekommt die Lager�bersicht
			//sobald man ein Lager anklickt...
			Tools.showMsg("Lager�bersicht");
		} 
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
			OberflaecheImpl.getInstance().zeigeBuchungsdetails(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int selectedRow;
		String value;
		JTable tbl_lieferungsUebersicht = (JTable) e.getSource();
		selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
		if (selectedRow == -1)		//Keine Zeile ausgew�hlt
			return;
		
		value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();				//Wert (Datum) der ausgew�hlten Zeile und ersten Spalte
		
		Lieferung lieferung = Lieferung.getLieferung(value);
		
		List<Buchung> buchungen = lieferung.getBuchungen();
		OberflaecheImpl.getInstance().zeigeLieferungsBuchungen(buchungen);
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

}
