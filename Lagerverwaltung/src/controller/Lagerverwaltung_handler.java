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

			neuesLager(e);
			
		} else if (e.getActionCommand().toLowerCase().equals(("Neue Lieferung").toLowerCase())) {
			
			neueLieferung(e);
			
		} else if (e.getActionCommand().toLowerCase().equals(("undo").toLowerCase())) {
			GUI_lager.enableJetztBuchen();
			befehlBuchung.undo();

		} else if (e.getActionCommand().toLowerCase().equals(("redo").toLowerCase())) {
			befehlBuchung.redo();
			if (GUI_lager.getVerbleibendeMenge() == 0) {
				GUI_lager.enableAlleBuchungenBestaetigen();
			}

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


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (GUI_lager.isCardUebersichtAktiv())
			GUI_lager.zeigeLagerbuchungen(((Lager) e.getPath().getLastPathComponent()).getBuchungen());
		else if (GUI_lager.isCardNeueLieferungAktiv() && GUI_lager.getAusgewaehlterKnoten().isLeaf())
			GUI_lager.showLagerFuerBuchung(GUI_lager.getAusgewaehlterKnoten().getName());
	}

	// XXX Bedarf einer gr�ndlichen �berpr�fung :)
	@Override
	public void mousePressed(MouseEvent e) {
		try {
			int selectedRow;
			String value;
			JTable tbl_lieferungsUebersicht = (JTable) e.getSource();
			selectedRow = tbl_lieferungsUebersicht.getSelectedRow();
			if (selectedRow == -1)		//Keine Zeile ausgew�hlt
				return;

			value = tbl_lieferungsUebersicht.getValueAt(selectedRow, 0).toString();				//Wert (Datum) der ausgew�hlten Zeile und ersten Spalte

			Lieferung lieferung = Lieferung.getLieferung(value);

			List<Buchung> buchungen = lieferung.getBuchungen();
			GUI_lager.showTabLieferungsBuchungen(buchungen);
		} catch (ClassCastException cce) {
			//Unterscheidung zwischen Zahl und Text in dem Textfeld 
			//Bei Zahl: Textfeld wird nicht geleert
			//Bei Text: Textfeld wird geleert
			if (!isItANumber(((JTextField) e.getSource()).getText()))
				((JTextField) e.getSource()).setText("");
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


	private void neuesLager(ActionEvent e)
	{
		// Neuen Knoten hinzuf�gen
		Lager pre_knoten;
		pre_knoten = GUI_lager.getAusgewaehlterKnoten();

		// Falls ein Knoten ausgew�hlt wurde
		if (null != pre_knoten) {
			// Lagererstellung ist nur bei einem Bestand von 0 zul�ssig!
			if (pre_knoten.getEinzelBestand() == 0) {
				String name = null, menge_str = null;
				int menge = 0, pane_value;
				JTextField lagername = new JTextField();
				JTextField bestand = new JTextField();
				Object message[] = { "Lagername: ", lagername,
						"Anfangs Bestand: ", bestand };
				JOptionPane pane = new JOptionPane(message,
						JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

				// Dialog erstellen und Eingabeparameter einlesen
				do {
					pane.createDialog("Neues Lager erstellen").setVisible(true); // Eingabemaske
																					// f�r
																					// den
																					// Knotennamen
					pane_value = ((Integer) pane.getValue()).intValue(); // Button
																			// Benutzung
																			// des
																			// Benutzers
																			// einlesen
																			// (Ok
																			// oder
																			// Abbruch)
					name = lagername.getText().trim();
					menge_str = bestand.getText().trim();

					if (pane_value == JOptionPane.OK_OPTION) {

						if (name.isEmpty()) {
							JOptionPane
									.showMessageDialog(
											null,
											"Es ist ein ung�ltiger Lagername eingegeben worden!",
											"Ung�ltige Bezeichnung",
											JOptionPane.ERROR_MESSAGE);
						} else if (!(null == menge_str || menge_str.isEmpty())) {
							if (isItANumber(menge_str)) {
								menge = Integer.parseInt(menge_str);
								if (menge < 0) {
									Tools.showMsg("Es sind keine negativen Best�nde m�glich!");
									menge_str = ""; // Zuweisung eines leeren
													// Strings, damit die do
													// while Schleife erneut
													// durchl�uft
								}
							} else {
								Tools.showMsg("Als Menge sind nur Zahlen erlaubt!");
								menge_str = ""; // Zuweisung eines leeren
												// Strings, damit die do while
												// Schleife erneut durchl�uft
							}
						}
						// Falls kein Bestand eingegeben wurde wird ein Fehler
						// ausgegeben
						else
							Tools.showMsg("Bitte geben Sie eine Bestandsmenge an!");
					}
				} while ((pane_value == JOptionPane.OK_OPTION)
						&& ((name.isEmpty() || name == null) || (menge_str
								.isEmpty() || menge_str == null))); // falls auf
																	// OK
																	// geklickt
																	// wurde
																	// und...

				if (pane_value == JOptionPane.OK_OPTION) {
					try
					{
					pre_knoten.addTreeElement(name).veraenderBestand(menge);
					GUI_lager.refreshTree(); // Anzeige des Trees aktualisieren
					}
					catch(Exception ex)		//Falls der Lagername bereits vergeben wurde wird eine Ecxeption geworfen
					{
						Tools.showMsg(ex.getMessage());
					}
					
				}
			} else {
				Tools.showMsg("Das ausgew�hlte Lager besitzt einen Bestand. Lagererstellung nicht m�glich!");
			}

		}
		// Falls kein Lager ausgew�hlt wurde wird ein Fehler ausgegeben
		else
			Tools.showMsg("Es ist kein Lager ausgew�hlt, unter das das neue erstellt werden soll!");
	}

	private void neueLieferung(ActionEvent e)
	{
		//TODO Ausbuchungen = negative Lieferungen...   evtl. schon behoben. Aber testen!
		//TODO Klappt nicht ganz, da die Berechnung f�r den Prozentsatz (oder so!) nur auf positive Zahlen ausgelegt ist
		GUI_lager.disableNeuesLager();
		GUI_lager.disableNeueLieferung();
		GUI_lager.disableAlleBuchungenBestaetigen();
		GUI_lager.showCardNeueLieferung();
		GUI_lager.showUndoRedo();
		GUI_lager.enableJetztBuchen();

		GUI_lager.disableNeueLieferung();
	}
	
	private void jetztBuchen(ActionEvent e)
	{
		int restMenge;
		int menge = getBuchungsMenge();
		if (menge != -1) {
			// FIXME du hast diese funktion darauf ausgelegt, dass sie ausschlie�lich mit positiven Zahlen arbeiten kann :D
			if (GUI_lager.getVerbleibendeMenge() == -1) 
				GUI_lager.setVerbleibendeMenge(Integer.parseInt(GUI_lager.getGesamtmenge()));

			if (menge < 0)
				menge = menge *-1;
			restMenge = GUI_lager.getVerbleibendeMenge() - menge;
			//FIXME Falls die verbleibende Menge < als 1% der Gesamtmenge ist auf die letzte Buchung mit aufschlagen
			if (restMenge >= 0) {
				befehlBuchung.execute(GUI_lager.getAusgewaehlterKnoten(), menge, new Date());
				GUI_lager.setVerbleibendeMenge(restMenge);
				GUI_lager.showVerbleibendeMenge();
				if (restMenge == 0) {
					GUI_lager.disableJetztBuchen();
					GUI_lager.enableAlleBuchungenBestaetigen();
				}

				//Lagerbuchungen aktualisieren, sodass die Tabelle die soeben get�tigte Buchung auff�hrt
				GUI_lager.zeigeLagerbuchungen(GUI_lager.getAusgewaehlterKnoten().getBuchungen());
			} else {
				int prozentsatz = Math.round((GUI_lager.getVerbleibendeMenge() * 100 / Integer.parseInt(GUI_lager.getGesamtmenge())));
				Tools.showMsg("Der prozentuale Anteil ist zu hoch!\n\nDer gr��te m�gliche Wert w�re: " + prozentsatz + "%");
			}
		}
		GUI_lager.showLagerFuerBuchung(GUI_lager.getAusgewaehlterKnoten().getName());

	}
	
	private int getBuchungsMenge() {
		String gesamtmenge_str, prozentualerAnteil_str;
		int gesamtmenge, prozentualerAnteil, anteil;
		gesamtmenge_str = GUI_lager.getGesamtmenge();
		prozentualerAnteil_str = GUI_lager.getProzentualerAnteil();

		if (isItANumber(gesamtmenge_str) && isItANumber(prozentualerAnteil_str)) {
			gesamtmenge = Integer.parseInt(gesamtmenge_str);
			prozentualerAnteil = Integer.parseInt(prozentualerAnteil_str);

			if ((prozentualerAnteil < 1) || (prozentualerAnteil > 100))
				Tools.showMsg("Ung�ltiger prozentueler Anteil! Nur ganzzahlige Werte von 1 bis 100.");
			else {
				//Kaufm�nnisch runden
				anteil = Math.round((float) (gesamtmenge * prozentualerAnteil) / 100);
				return anteil;
			}
		} else
			Tools.showMsg("Es sind nur ganzzahlige Werte erlaubt!");
		//FIXME: Keine sichere Fehlererkennung, denn -1 kann bei Ausbuchungen ein richtiger Wert sein!
		return -1;		//Ung�ltige Eingabe
	}

	private void lieferungBestaetigen(ActionEvent e)
	{
		if (!Buchung.getNeueBuchungen().isEmpty()) {
			GUI_lager.enableNeuesLager();
			GUI_lager.enableLagerUebersicht();
			GUI_lager.hideUndoRedo();
			GUI_lager.showCardUebersicht();
			befehlLieferung.execute(new Date(), Buchung.getGesamtMenge(), Buchung.getNeueBuchungen());
			befehlBuchung.clearAll();
			GUI_lager.setVerbleibendeMenge(-1); // XXX w�re das dann nicht eine verbleibende negative menge, die mit eingebucht werden m�sste?
			GUI_lager.refreshTree();		//Anzeige des Trees aktualisieren
		} else
			Tools.showMsg("Bitte zuerst auf \"Jetzt buchen\" klicken");
	}
	
	private void lieferungAbbrechen(ActionEvent e)
	{
		befehlBuchung.undoAll();
		GUI_lager.enableNeuesLager();
		GUI_lager.enableLagerUebersicht();
		GUI_lager.hideUndoRedo();
		GUI_lager.showCardUebersicht();
		GUI_lager.setVerbleibendeMenge(-1);
	}
	
	private void zeigeLieferungsLagerUebersicht(ActionEvent e)
	{
		if (GUI_lager.isCardNeueLieferungAktiv())				//Falls eine neue Lieferung durchgef�hrt wird
		{
			int value = JOptionPane.showOptionDialog(null, "M�chten Sie die aktuelle Lieferung abbrechen um zu der Lieferungs- / Lager�bersicht zu gelangen?\nAlle �nderungen werden verworfen!", "Sicher?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			if (value == JOptionPane.YES_OPTION)
			{
				lieferungAbbrechen(e);				//Aktion entspricht dem Lieferungs Abbruch
			}
			else if (value == JOptionPane.NO_OPTION)
			{
				// mach nichts
			}
				
		}
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
