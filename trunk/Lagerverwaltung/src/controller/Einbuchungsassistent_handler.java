package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import exception.LagerverwaltungsException;

public class Einbuchungsassistent_handler implements ActionListener, TreeSelectionListener, MouseListener {

	Oberflaeche GUI_einbuchung;

	HashMap<Lager, JTextField> lagerListe = new HashMap<Lager, JTextField>();

	public void announceGUI_Einbuchung(Oberflaeche myGUI) {
		this.GUI_einbuchung = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().toLowerCase().equals(("Best�tigen").toLowerCase())) {

			Lieferung lieferung = new Lieferung(new Date());
			Buchung buchung;
			Lager lager;
			int menge;

			lagerListe = Oberflaeche.getInstance().getHinzugefuegteLager();
			for (Map.Entry<Lager, JTextField> element : lagerListe.entrySet()) {
				try {
					menge = Integer.parseInt(element.getValue().getText());
					lager = element.getKey();
					lager.veraenderBestand(menge);
					lager.addBuchung(buchung = new Buchung(menge, Lagerverwaltung_handler.lieferungID++, lieferung.getLieferungsDatum()));

					lieferung.addBuchung(buchung);
				} catch (NumberFormatException ex1) {
					JOptionPane.showMessageDialog(null, "Es sind nur Zahlenwerte erlaubt! ");
					return;
				} catch (LagerverwaltungsException ex2) {
					JOptionPane.showMessageDialog(null, ex2.getMessage());
					return;
				}

			}

			JOptionPane.showMessageDialog(null, "Buchung ausgef�hrt!");
			Oberflaeche.getInstance().hideEinbuchungsAssi();
			Oberflaeche.getInstance().refreshTree();

		} else if (e.getActionCommand().toLowerCase().equals(("Abbruch").toLowerCase())) {
			Oberflaeche.getInstance().hideEinbuchungsAssi();
		} else {
			JOptionPane.showMessageDialog(null, "Hier kommt bald was...");
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		if (((Lager) e.getPath().getLastPathComponent()).isBestandHaltend()) {
			this.GUI_einbuchung.addLager((Lager) e.getPath().getLastPathComponent(), this);
		} else
			JOptionPane.showMessageDialog(null, "Dieses Lager kann keinen Bestand halten!");
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

}