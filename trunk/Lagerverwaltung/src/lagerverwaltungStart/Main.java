package lagerverwaltungStart;

import javax.swing.JOptionPane;

import model.Lager;
import view.impl.OberflaecheImpl;
import controller.Einbuchungsassistent_handler;
import controller.Lagerverwaltung_handler;

public class Main {
	
	public static final String VERSION = "0.6a";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int pane_value;
		
		Lager wurzel = Lager.addWurzel("Lagerverwaltung");
		pane_value = JOptionPane.showConfirmDialog(null,
				"Willkommen im Lagerverwaltungstool v"+VERSION+"!\nSoll eine Beispielhierarchie für die Lagerverwaltung geladen werden?", "Lagerhierarchie laden",
				JOptionPane.YES_NO_OPTION);

		if (pane_value == JOptionPane.YES_OPTION) {
			beispielHierarchieLaden(wurzel);
		}

				Lagerverwaltung_handler myLagerverwaltungHandler = new Lagerverwaltung_handler();
				Einbuchungsassistent_handler myEinbuchungsHandler = new Einbuchungsassistent_handler();
		//		Lagerverwaltung myLagerverwaltung = new Lagerverwaltung(myLagerverwaltungHandler);
		//		myLagerverwaltungHandler.announceGUI_Lager(myLagerverwaltung);

		OberflaecheImpl.setLagerListener(myLagerverwaltungHandler);
		OberflaecheImpl.setEinbuchungListener(myEinbuchungsHandler);
		OberflaecheImpl.setLieferungListener(myLagerverwaltungHandler);
		OberflaecheImpl.getInstance().showLagerverwaltung();
		
		myLagerverwaltungHandler.announceGUI_Lager(OberflaecheImpl.getInstance());
		myEinbuchungsHandler.announceGUI_Einbuchung(OberflaecheImpl.getInstance());
		
	}

	private static void beispielHierarchieLaden(Lager root) {
		Lager knoten, blatt1, blatt2;
		knoten = root.addTreeElement("Deutschland", 0);
		blatt1 = knoten.addTreeElement("Niedersachsen", 0);
		blatt2 = blatt1.addTreeElement("Hannover-Misburg", 300);
		blatt2 = blatt1.addTreeElement("Nienburg", 1200);
		blatt1 = knoten.addTreeElement("NRW", 500);
		blatt1 = knoten.addTreeElement("Bremen", 500);
		blatt1 = knoten.addTreeElement("Hessen", 500);
		blatt1 = knoten.addTreeElement("Sachsen", 500);
		blatt1 = knoten.addTreeElement("Brandenburg", 500);
		blatt1 = knoten.addTreeElement("MV", 500);
		knoten = root.addTreeElement("Europa", 0);
		blatt1 = knoten.addTreeElement("Frankreich", 0);
		blatt2 = blatt1.addTreeElement("Paris-Nord", 500);
		blatt2 = blatt1.addTreeElement("Orléans", 500);
		blatt2 = blatt1.addTreeElement("Marseille", 500);
		blatt2 = blatt1.addTreeElement("Nîmes", 500);
		blatt1 = knoten.addTreeElement("Italien", 0);
		blatt2 = blatt1.addTreeElement("Mailand", 500);
		blatt2 = blatt1.addTreeElement("L'aquila", 500);
		blatt1 = knoten.addTreeElement("Spanien", 500);
		knoten = root.addTreeElement("Großbritannien", 500);
		knoten.veraenderBestand(100);
//		knoten.addBuchung(new Buchung(100, 22, new Date()));
	}
}
