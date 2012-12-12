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
		knoten = root.addTreeElement("Deutschland");
		blatt1 = knoten.addTreeElement("Niedersachsen");
		blatt2 = blatt1.addTreeElement("Hannover-Misburg");
		blatt2.veraenderBestand(300);
		blatt2 = blatt1.addTreeElement("Nienburg");
		blatt2.veraenderBestand(1200);
		blatt1 = knoten.addTreeElement("NRW");
		blatt1.veraenderBestand(500);
		blatt1 = knoten.addTreeElement("Bremen");
		blatt1 = knoten.addTreeElement("Hessen");
		blatt1 = knoten.addTreeElement("Sachsen");
		blatt1 = knoten.addTreeElement("Brandenburg");
		blatt1 = knoten.addTreeElement("MV");
		knoten = root.addTreeElement("Europa");
		blatt1 = knoten.addTreeElement("Frankreich");
		blatt2 = blatt1.addTreeElement("Paris-Nord");
		blatt2 = blatt1.addTreeElement("Orléans");
		blatt2 = blatt1.addTreeElement("Marseille");
		blatt2 = blatt1.addTreeElement("Nîmes");
		blatt1 = knoten.addTreeElement("Italien");
		blatt2 = blatt1.addTreeElement("Mailand");
		blatt2 = blatt1.addTreeElement("L'aquila");
		blatt1 = knoten.addTreeElement("Spanien");
		knoten = root.addTreeElement("Großbritannien");
		
		
		
	}
}
