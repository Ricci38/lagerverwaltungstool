package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.befehle.IBuchungBefehl;
import controller.befehle.impl.BuchungBefehlImpl;
import exception.LagerverwaltungsException;

public class Einbuchungsassistent_handler implements ActionListener, TreeSelectionListener, MouseListener {

	Oberflaeche GUI_einbuchung;

	HashMap<Lager, JTextField> lagerListe = new HashMap<Lager, JTextField>();

	public void announceGUI_Einbuchung(Oberflaeche myGUI) {
		this.GUI_einbuchung = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			
			ArrayList<Buchung> buchungen = new ArrayList<Buchung>();
			Date d = new Date();
			IBuchungBefehl befehl = new BuchungBefehlImpl();
			Buchung buchung;
			Lager lager;
			int menge = 0;
			int gesamtMenge = 0;

			lagerListe = OberflaecheImpl.getInstance().getHinzugefuegteLager();
			for (Map.Entry<Lager, JTextField> element : lagerListe.entrySet()) {
				if (!isItANumber(element.getValue().getText())) {
					Tools.showMsg("Es sind nur Zahlenwerte erlaubt!");
					return;
				}
				menge = Integer.parseInt(element.getValue().getText());
				lager = element.getKey();
				if (!lager.checkBestandsaenderung(menge)) {
					Tools.showMsg("Bestand kann nicht geändert werden!");
				}
				else
					befehl.execute(lager, menge, d);
			}
			
			// FIXME TODO COMMAND PATTERN ANWENDEN!
			for (Map.Entry<Lager, JTextField> elem : lagerListe.entrySet()) {
				
				// bbi = new BuchungBefehlImpl();
				// for (bla) {
				// 		bbi.execute(Buchung);
				// }
				
				try {
					menge = Integer.parseInt(elem.getValue().getText());
					gesamtMenge += menge;
					lager = elem.getKey();
					lager.veraenderBestand(menge);
					lager.addBuchung(buchung = new Buchung(menge, d));
					buchungen.add(buchung);
				}catch (LagerverwaltungsException ex) {
					Tools.showMsg(ex.getMessage());
					// bbi.undoAll();
					return;
				}
			}
			Lieferung.addLieferungen(new Lieferung(d, gesamtMenge, buchungen));

			
			Tools.showMsg("Buchung ausgeführt!");
			OberflaecheImpl.getInstance().hideEinbuchungsAssi();
			OberflaecheImpl.getInstance().refreshTree();

		} else if (e.getActionCommand().toLowerCase().equals(("Abbruch").toLowerCase())) {
			OberflaecheImpl.getInstance().hideEinbuchungsAssi();
		} else {
			Tools.showMsg("Hier kommt bald was...");
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		if (((Lager) e.getPath().getLastPathComponent()).isBestandHaltend()) {
			this.GUI_einbuchung.addLager((Lager) e.getPath().getLastPathComponent(), this);
		} else
			Tools.showMsg("Dieses Lager kann keinen Bestand halten!");
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Unterscheidung zwischen Zahl und Text in dem Textfeld 
		//Bei Zahl: Textfeld wird nicht geleert
		//Bei Text: Textfeld wird geleert
		try			
		{
			Integer.parseInt(((JTextField) e.getSource()).getText());  
			//Falls hier keine Exception geworfen wird ist es eine Zahl
		}
		catch(NumberFormatException ex)
		{
			((JTextField) e.getSource()).setText("");
		}
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
	
	private boolean isItANumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
