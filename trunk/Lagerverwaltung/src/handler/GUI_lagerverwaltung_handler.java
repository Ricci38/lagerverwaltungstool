package handler;

import gui.GUI_lagerverwaltung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;


/*
 * 
 * XXX: Wird nicht verwendet!
 * Die Klasse wird nicht verwendet. Es wird der Handler im Package Controller verwendet
 * Darf der Handler mit dem Controller vermischt sein (so wie in controller.GUI_lagerverwaltung_handler)
 * oder muss der Handler vom Controllen abgelöst sein?
 * 
 * unnütz!!!!!!!!!!!
 * 
 * 
 */
public class GUI_lagerverwaltung_handler implements ActionListener {

	GUI_lagerverwaltung GUI_lager;
	
	
	
	public void announceGUI_Lager(gui.GUI_lagerverwaltung myGUI)
	{
		this.GUI_lager = myGUI;
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(null, e.getSource());

		
	}

}
