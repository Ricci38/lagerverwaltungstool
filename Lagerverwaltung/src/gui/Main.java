package gui;

import model.Lager;
import controller.GUI_einbuchungsassistent_handler;
import controller.GUI_lagerverwaltung_handler;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Lager wurzel, knoten, blatt;
		wurzel = Lager.addWurzel("Lagerverwaltung");
		knoten = wurzel.addTreeElement("Lager A", 100);
		blatt = knoten.addTreeElement("Lager A.1", 200);
		blatt = knoten.addTreeElement("Lager A.2", 300);
		blatt = blatt.addTreeElement("Lager A.2.1", 1200);
		knoten = wurzel.addTreeElement("Lager B", 500);
		blatt = knoten.addTreeElement("Lager B.1", 900);
	  blatt = knoten.addTreeElement("Lager sdf", 10);
		
		
//		new GUI_einbuchungsassistent();
		
//		new GUI_lieferungsuebersicht();
		
		GUI_lagerverwaltung_handler myLagerverwaltungHandler = new GUI_lagerverwaltung_handler();
		Lagerverwaltung myLagerverwaltung = new Lagerverwaltung(myLagerverwaltungHandler);	
		myLagerverwaltungHandler.announceGUI_Lager(myLagerverwaltung);
		

	}
	
	public static void verbindungEinbuchungsassistent()
	{
		GUI_einbuchungsassistent_handler myEinbuchungsHandler = new GUI_einbuchungsassistent_handler();
		Einbuchungsassistent myEinbuchung = new Einbuchungsassistent(myEinbuchungsHandler);
		myEinbuchungsHandler.announceGUI_Einbuchung(myEinbuchung);
	}

}
