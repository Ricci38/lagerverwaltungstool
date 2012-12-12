package lagerverwaltungStart;

import javax.swing.JOptionPane;

import model.Lager;
import model.Lieferung;
import view.impl.OberflaecheImpl;
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

		OberflaecheImpl.setLagerListener(myLagerverwaltungHandler);
		OberflaecheImpl.setLieferungListener(myLagerverwaltungHandler);
	
		
		myLagerverwaltungHandler.announceGUI_Lager(OberflaecheImpl.getInstance());
		OberflaecheImpl.getInstance().showLagerverwaltung();
		
		OberflaecheImpl.getInstance().selectTreeRoot();
		OberflaecheImpl.getInstance().zeigeLieferungen(Lieferung.getAllLieferungen());
		
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
