package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class Lagerverwaltung_handler implements ActionListener, TreeSelectionListener, MouseListener {

	Oberflaeche GUI_lager;
	static int lieferungID = 0;

	private static IBuchungBefehl befehlBuchung = new BuchungBefehlImpl();
	private static ILieferungBefehl befehlLieferung = new LieferungBefehlImpl();

	public void announceGUI_Lager(Oberflaeche myGUI) {
		this.GUI_lager = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand().toLowerCase().equals(("Neues Lager").toLowerCase())) {

			// Neuen Knoten hinzufügen
			Lager pre_knoten;
			pre_knoten = GUI_lager.getAusgewaehlterKnoten();

			// Falls ein Knoten ausgewählt wurde
			if (pre_knoten != null) {
				//Lagererstellung ist nur bei einem Bestand von 0 zulässig!
				if (pre_knoten.getEinzelBestand() == 0) {
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
							} else if (!(null == menge_str || menge_str.isEmpty())) {
								try {
									menge = Integer.parseInt(menge_str);
								} catch (NumberFormatException ex) {
									Tools.showMsg("Es sind nur Werte gleich oder größer 0 erlaubt!: ");
									menge_str = "";			//Zuweisung eines leeren Strings, damit die do while Schleife erneut durchläuft
								}

							}
							// Falls kein Bestand eingegeben wurde wird ein Fehler ausgegeben
							else
								Tools.showMsg("Die eingegebene Bestandsmenge ist ungültig!");
						}
					} while ((pane_value == JOptionPane.OK_OPTION) && ((name.isEmpty() || name == null) || (menge_str.isEmpty() || menge_str == null))); // falls auf OK geklickt wurde und...

					if (pane_value == JOptionPane.OK_OPTION) {
						pre_knoten.addTreeElement(name).veraenderBestand(menge);
						GUI_lager.refreshTree(); // Anzeige des Trees aktualisieren
					}
				} else {
					Tools.showMsg("Das ausgewählte Lager besitzt einen Bestand. Lagererstellung nicht möglich!");
				}

			}
			// Falls kein Lager ausgewählt wurde wird ein Fehler ausgegeben
			else
				Tools.showMsg("Es ist kein Lager ausgewählt, unter das das neue erstellt werden soll!");
		} else if (e.getActionCommand().toLowerCase().equals(("Neue Lieferung").toLowerCase())) {
			GUI_lager.disableLagerUebersicht();
			GUI_lager.showCardNeueLieferung();
			GUI_lager.showUndoRedo();
//			GUI_lager.x(GUI_lager.getAusgewaehlterKnoten().getName(), this);

			//TODO: Nach Abschluss der Lieferung:
			//			GUI_lager.enableLagerUebersicht();
		} else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			befehlBuchung.undo();
		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			//			Tools.showMsg("redo");

			Tools.showMsg(Lieferung.getAllLieferungen().size());

		} else if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			befehlBuchung.execute(GUI_lager.getAusgewaehlterKnoten(), getBuchungsMenge(), new Date());
			GUI_lager.enableLagerUebersicht();
			GUI_lager.hideUndoRedo();
			GUI_lager.showCardUebersicht();
			befehlLieferung.execute(new Date(), Buchung.getGesamtMenge(), Buchung.getNeueBuchungen());
			befehlBuchung.clearAll();
		} else if (e.getActionCommand().toLowerCase().equals(("Abbrechen").toLowerCase())) {
			befehlBuchung.undoAll();
			GUI_lager.enableLagerUebersicht();
			GUI_lager.hideUndoRedo();
			GUI_lager.showCardUebersicht();
		}
	}

	private int getBuchungsMenge() {
		// TODO Auto-generated method stub
		// TODO Anteilsmenge ausrechnen
		// TODO Überprüfe ob int mit isItANumber(s)
		GUI_lager.getGesamtmenge(); 
		GUI_lager.getProzentualerAnteil();
		return 0;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (GUI_lager.isCardUebersichtAktiv())
			GUI_lager.zeigeLagerbuchungen(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
		else if (GUI_lager.isCardNeueLieferungAktiv() && GUI_lager.getAusgewaehlterKnoten().isLeaf())
			GUI_lager.x(GUI_lager.getAusgewaehlterKnoten().getName());
	}

	// XXX Bedarf einer gründlichen Überprüfung :)
	@Override
	public void mousePressed(MouseEvent e) {
		try {
			int selectedRow;
			String value;
			JTable tbl_lieferungsUebersicht = (JTable) e.getSource();
			selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
			if (selectedRow == -1)		//Keine Zeile ausgewählt
				return;

			value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();				//Wert (Datum) der ausgewählten Zeile und ersten Spalte

			Lieferung lieferung = Lieferung.getLieferung(value);

			List<Buchung> buchungen = lieferung.getBuchungen();
			GUI_lager.showTabLieferungsBuchungen(buchungen);
		} catch (ClassCastException cce) {
			//Unterscheidung zwischen Zahl und Text in dem Textfeld 
			//Bei Zahl: Textfeld wird nicht geleert
			//Bei Text: Textfeld wird geleert
			try {
				Integer.parseInt(((JTextField) e.getSource()).getText());
				//Falls hier keine Exception geworfen wird ist es eine Zahl
			} catch (NumberFormatException ex) {
				((JTextField) e.getSource()).setText("");
			}
//		} finally {
//			Tools.showMsg("finally block");
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

	public static IBuchungBefehl getBefehlBuchung() {
		return befehlBuchung;
	}

	public static ILieferungBefehl getBefehlLieferung() {
		return befehlLieferung;
	}
	
	private boolean isItANumber(String s) {
	try {
		Integer.parseInt(s);
		return true;
	} catch (Exception e) {
		return false;
	}
}
}
