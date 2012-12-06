package view;

import javax.swing.JOptionPane;

import model.Lager;
import controller.Einbuchungsassistent_handler;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int pane_value;

		Lager wurzel = Lager.addWurzel("Lagerverwaltung");
		pane_value = JOptionPane.showConfirmDialog(null,
				"Willkommen im Lagerverwaltungstool!\nSoll eine Beispielhierarchie für die Lagerverwaltung geladen werden?", "Lagerhierarchie laden",
				JOptionPane.YES_NO_OPTION);

		if (pane_value == JOptionPane.YES_OPTION) {
			beispielHierarchieLaden(wurzel);
		}
		//		new GUI_einbuchungsassistent();
		//		new GUI_lieferungsuebersicht();

		//		Lagerverwaltung_handler myLagerverwaltungHandler = new Lagerverwaltung_handler();
		//		Lagerverwaltung myLagerverwaltung = new Lagerverwaltung(myLagerverwaltungHandler);
		//		myLagerverwaltungHandler.announceGUI_Lager(myLagerverwaltung);

		Oberflaeche.getInstance().showLagerverwaltung();

	}

	public static void verbindungEinbuchungsassistent() {
		Einbuchungsassistent_handler myEinbuchungsHandler = new Einbuchungsassistent_handler();
		Einbuchungsassistent myEinbuchung = new Einbuchungsassistent(myEinbuchungsHandler);
		myEinbuchungsHandler.announceGUI_Einbuchung(myEinbuchung);
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
	}
}
