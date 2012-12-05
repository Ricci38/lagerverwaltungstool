package controller;

import gui.GUI_einbuchungsassistent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import model.Lager;

public class GUI_einbuchungsassistent_handler implements ActionListener, TreeSelectionListener{

	GUI_einbuchungsassistent GUI_einbuchung;
	
	public void announceGUI_Einbuchung(GUI_einbuchungsassistent myGUI)
	{
		this.GUI_einbuchung = myGUI;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().toLowerCase().equals(("Best�tigen").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Best�tigung!");
		} else if (e.getActionCommand().toLowerCase().equals(("Abbruch").toLowerCase())) {
			JOptionPane.showMessageDialog(null, "Abbruch!");
		} else if (e.getActionCommand().toLowerCase().equals(("prozentAnteil").toLowerCase()))		//Vergleich funktioniert nicht, 
																									//da als String der 
																									//Textfeldwert zur�ckgegeben wird
		{
			JOptionPane.showMessageDialog(null, "Textfeld wurde ver�ndert");

		} else {
			JOptionPane.showMessageDialog(null, "Unbekannte Aktion: " + e.getActionCommand());
		}

	}


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		
		
		if (((Lager)e.getPath().getLastPathComponent()).isBestandHaltend())			//auch m�glich mit isLeaf()
		{
		this.GUI_einbuchung.addLager(e.getPath().getLastPathComponent().toString(), this);
		}
		else
			JOptionPane.showMessageDialog(null, "Dieses Lager kann keinen Bestand halten!");
	}

}
