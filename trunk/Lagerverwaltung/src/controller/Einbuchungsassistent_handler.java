package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Lager;
import view.Oberflaeche;

public class Einbuchungsassistent_handler implements ActionListener, TreeSelectionListener {

	Oberflaeche GUI_einbuchung;

	public void announceGUI_Einbuchung(Oberflaeche myGUI) {
		this.GUI_einbuchung = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Bestätigung!");
		} else if (e.getActionCommand().toLowerCase().equals(("Abbruch").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Abbruch!");
		} else if (e.getActionCommand().toLowerCase().equals(("prozentAnteil").toLowerCase()))		//Vergleich funktioniert nicht, 
																								//da als String der 
																								//Textfeldwert zurückgegeben wird
		{
			JOptionPane.showMessageDialog(null, "Textfeld wurde verändert");

		} else {
			JOptionPane.showMessageDialog(null, "Unbekannte Aktion: " + e.getActionCommand());
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		if (((Lager) e.getPath().getLastPathComponent()).isBestandHaltend())			
		{
			this.GUI_einbuchung.addLager(e.getPath().getLastPathComponent().toString(), this);
		} else
			JOptionPane.showMessageDialog(null, "Dieses Lager kann keinen Bestand halten!");
	}

}
