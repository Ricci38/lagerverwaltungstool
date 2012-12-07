package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Buchung;
import model.Lager;
import view.Oberflaeche;

public class Einbuchungsassistent_handler implements ActionListener, TreeSelectionListener {

	Oberflaeche GUI_einbuchung;
	private int menge;
	HashMap<Lager, JTextField> lagerListe = new HashMap<Lager, JTextField>();
	

	public void announceGUI_Einbuchung(Oberflaeche myGUI) {
		this.GUI_einbuchung = myGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().toLowerCase().equals(("Bestätigen").toLowerCase())) {
			
			lagerListe = Oberflaeche.getInstance().getHinzugefuegteLager();
			for(Map.Entry<Lager, JTextField> element:lagerListe.entrySet())
			{
				try 
				{
					menge = Integer.parseInt(element.getValue().getText());	
					element.getKey().addBuchung(new Buchung(menge));
				} 
				catch (NumberFormatException ex) 
				{
					JOptionPane.showMessageDialog(null, "Es sind nur Zahlenwerte erlaubt! ");	
				}	
			}
			
			JOptionPane.showMessageDialog(null, "Buchung ausgeführt!");
			
		} else if (e.getActionCommand().toLowerCase().equals(("Abbruch").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Abbruch!");
		} else if (e.getActionCommand().toLowerCase().equals(("prozentAnteil").toLowerCase()))		//Vergleich funktioniert nicht, 
																								//da als String der 
																								//Textfeldwert zurückgegeben wird
		{
			JOptionPane.showMessageDialog(null, "Textfeld wurde verändert");

		} 	
		else 
		{
			JOptionPane.showMessageDialog(null, "Hier kommt bald was...");
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {

		if (((Lager) e.getPath().getLastPathComponent()).isBestandHaltend())			
		{
			this.GUI_einbuchung.addLager( (Lager) e.getPath().getLastPathComponent(), this);
		} else
			JOptionPane.showMessageDialog(null, "Dieses Lager kann keinen Bestand halten!");
	}

}
