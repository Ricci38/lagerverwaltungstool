package handler;

import gui.Lagerverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


/*
 * 
 * XXX: Wird nicht verwendet!
 * Die Klasse wird nicht verwendet. Es wird der Handler im Package Controller verwendet
 * Darf der Handler mit dem Controller vermischt sein (so wie in controller.GUI_lagerverwaltung_handler)
 * oder muss der Handler vom Controllen abgel�st sein?
 * 
 * unn�tz!!!!!!!!!!!
 * 
 * 
 */
public class GUI_lagerverwaltung_handler implements ActionListener {

	Lagerverwaltung GUI_lager;
	
	
	
	public void announceGUI_Lager(gui.Lagerverwaltung myGUI)
	{
		this.GUI_lager = myGUI;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, e.getSource());

		
	}

}
