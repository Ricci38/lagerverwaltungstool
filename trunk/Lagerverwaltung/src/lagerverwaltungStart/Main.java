package lagerverwaltungStart;

import java.util.Date;

import javax.swing.JOptionPane;

import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import view.Tools;
import view.impl.OberflaecheImpl;
import controller.GUI_handler;
import controller.befehle.IBuchungBefehl;

/**
 * 
 * @author Dominik Klüter & Philo Könneker
 * @version 1.0.6b
 * 
 */
public class Main {

	public static final String VERSION = "1.0.5b";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int pane_value;

			Lager wurzel = Lager.addWurzel("Lagerverwaltung");
			
			pane_value = JOptionPane.showConfirmDialog(null, "Willkommen im Lagerverwaltungstool v" + VERSION
					+ "!\nSoll eine Beispielhierarchie mit Beispielbuchungen für die Lagerverwaltung geladen werden?", "Lagerhierarchie laden", JOptionPane.YES_NO_OPTION);

			GUI_handler myLagerverwaltungHandler = new GUI_handler();

			OberflaecheImpl.setLagerListener(myLagerverwaltungHandler);
			OberflaecheImpl.setLieferungListener(myLagerverwaltungHandler);
			
			Oberflaeche gui = OberflaecheImpl.getInstance();

			myLagerverwaltungHandler.announceGui(gui);
			gui.showLagerverwaltung();

			if (pane_value == JOptionPane.YES_OPTION) {
				beispielLaden(wurzel);
			}

			gui.selectTreeRoot();
			gui.showLieferungen(Lieferung.getAllLieferungen());
			
			Tools.showMsg("Bitte passen Sie bei vielen kleinen Buchungen innerhalb einer Lieferung auf.\n\nAufgrund des verwendeten Rundungssystems kann es, je nach Anzahl der Buchungen und der Gesamtmenge, zu Abweichungen kommen.");
		} catch (Exception e) {
			Tools.showErr("Because FUCK YOU! That's why!");	//FIXME Ohne Schimpfwörter
			System.exit(1);
		}

	}

	private static void beispielLaden(Lager root) {
		//Anlegen der Lagerhierarchie
		Lager[][] lager = new Lager[10][10];
		IBuchungBefehl befehlBuchung = GUI_handler.getBefehlBuchung();
		lager[0][0] = root.addTreeElement("Deutschland");
			lager[1][0]= lager[0][0].addTreeElement("Niedersachsen");
				lager[2][0] = lager[1][0].addTreeElement("Hannover-Misburg");
				lager[2][1] = lager[1][0].addTreeElement("Nienburg");
			lager[1][1] = lager[0][0].addTreeElement("NRW");
			lager[1][2] = lager[0][0].addTreeElement("Bremen");
			lager[1][3] = lager[0][0].addTreeElement("Hessen");
			lager[1][4] = lager[0][0].addTreeElement("Sachsen");
			lager[1][5] = lager[0][0].addTreeElement("Brandenburg");
			lager[1][6] = lager[0][0].addTreeElement("MV");
		lager[0][1] = root.addTreeElement("Europa");
			lager[1][7] = lager[0][1].addTreeElement("Frankreich");
				lager[2][2] = lager[1][7].addTreeElement("Paris-Nord");
				lager[2][3] = lager[1][7].addTreeElement("Orléans");
				lager[2][4] = lager[1][7].addTreeElement("Marseille");
				lager[2][5] = lager[1][7].addTreeElement("Nîmes");
			lager[1][8] = lager[0][1].addTreeElement("Italien");
				lager[2][6] = lager[1][8].addTreeElement("Mailand");
				lager[2][7] = lager[1][8].addTreeElement("L'aquila");
			lager[1][9] = lager[0][1].addTreeElement("Spanien");
		lager[0][2] = root.addTreeElement("Großbritannien");
		
		
		//Anlegen der Beispielbuchungen
		Date datum1, datum2, datum3, datum4, datum5;
	
		//1. Lieferung
		datum1 = new Date();
		befehlBuchung.execute(lager[1][2], 500, datum1, 50); //Bremen
		befehlBuchung.execute(lager[1][6], 200, datum1, 20); //MV
		befehlBuchung.execute(lager[2][6], 100, datum1, 10); //Mailand
		befehlBuchung.execute(lager[1][9], 100, datum1, 10); //Spanien
		befehlBuchung.execute(lager[0][2], 100, datum1, 10); //Großbritannien
		
		GUI_handler.getBefehlLieferung().execute(datum1, Buchung.getGesamtMenge(), "Initiale Zubuchung", Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
		
		//2. Lieferung
		datum2 = new Date(datum1.getTime() + 1000l);
		befehlBuchung.execute(lager[2][1], 1000, datum2, 50); //Nienburg
		befehlBuchung.execute(lager[1][1], 400, datum2, 20); //NRW
		befehlBuchung.execute(lager[1][3], 400, datum2, 20); //Hessen
		befehlBuchung.execute(lager[1][4], 200, datum2, 10); //Sachsen
		
		GUI_handler.getBefehlLieferung().execute(datum2, Buchung.getGesamtMenge(), "Initiale Zubuchung" , Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
		
		//3. Lieferung
		datum3 = new Date(datum2.getTime() + 1000l);
		befehlBuchung.execute(lager[1][5], 2000, datum3, 20); //Brandenburg
		befehlBuchung.execute(lager[2][3], 1000, datum3, 10); //Orléans
		befehlBuchung.execute(lager[2][7], 2500, datum3, 25); //L'Aquila
		befehlBuchung.execute(lager[1][9], 2500, datum3, 25); //Spanien
		befehlBuchung.execute(lager[0][2], 2000, datum3, 20); //Großbritannien
		
		GUI_handler.getBefehlLieferung().execute(datum3, Buchung.getGesamtMenge(), "Initiale Zubuchung", Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
		
		//4. Lieferung
		datum4 = new Date(datum3.getTime() + 1000l);
		befehlBuchung.execute(lager[2][5], 2500, datum4, 50); //Nîmes
		befehlBuchung.execute(lager[1][6], 2000, datum4, 40); //MV
		befehlBuchung.execute(lager[2][1], 500, datum4, 10); //Nienburg
		
		GUI_handler.getBefehlLieferung().execute(datum4, Buchung.getGesamtMenge(), "Initiale Zubuchung", Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
		
		//5. Lieferung
		datum5 = new Date(datum4.getTime() + 1000l);
		befehlBuchung.execute(lager[2][2], 3750, datum5, 30); //Paris-Nord
		befehlBuchung.execute(lager[1][5], 2500, datum5, 20); //Brandenburg
		befehlBuchung.execute(lager[2][0], 1875, datum5, 15); //Hannover-Misburg
		befehlBuchung.execute(lager[1][2], 1875, datum5, 15); //Bremen
		befehlBuchung.execute(lager[2][6], 2500, datum5, 20); //Mailand
		
		GUI_handler.getBefehlLieferung().execute(datum5, Buchung.getGesamtMenge(), "Initiale Zubuchung", Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
	}
}
