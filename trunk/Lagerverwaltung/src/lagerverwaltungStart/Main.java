package lagerverwaltungStart;

import java.util.Date;

import javax.swing.JOptionPane;

import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.impl.OberflaecheImpl;
import controller.Lagerverwaltung_handler;
import controller.befehle.IBuchungBefehl;

public class Main {

	public static final String VERSION = "0.7a";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int pane_value;

		Lager wurzel = Lager.addWurzel("Lagerverwaltung");
		pane_value = JOptionPane.showConfirmDialog(null, "Willkommen im Lagerverwaltungstool v" + VERSION
				+ "!\nSoll eine Beispielhierarchie für die Lagerverwaltung geladen werden?", "Lagerhierarchie laden", JOptionPane.YES_NO_OPTION);

		Lagerverwaltung_handler myLagerverwaltungHandler = new Lagerverwaltung_handler();

		OberflaecheImpl.setLagerListener(myLagerverwaltungHandler);
		OberflaecheImpl.setLieferungListener(myLagerverwaltungHandler);

		myLagerverwaltungHandler.announceGUI_Lager(OberflaecheImpl.getInstance());
		OberflaecheImpl.getInstance().showLagerverwaltung();

		if (pane_value == JOptionPane.YES_OPTION) {
			beispielHierarchieLaden(wurzel);
		}

		OberflaecheImpl.getInstance().selectTreeRoot();
		OberflaecheImpl.getInstance().zeigeLieferungen(Lieferung.getAllLieferungen());

	}

	private static void beispielHierarchieLaden(Lager root) {
		Lager knoten, blatt1, blatt2;
		IBuchungBefehl befehlBuchung = Lagerverwaltung_handler.getBefehlBuchung();
		knoten = root.addTreeElement("Deutschland");
		blatt1 = knoten.addTreeElement("Niedersachsen");
		blatt2 = blatt1.addTreeElement("Hannover-Misburg");
		befehlBuchung.execute(blatt2, 200, new Date());
		blatt2 = blatt1.addTreeElement("Nienburg");
		befehlBuchung.execute(blatt2, 1200, new Date());
		blatt1 = knoten.addTreeElement("NRW");
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

		Lagerverwaltung_handler.getBefehlLieferung().execute(new Date(), Buchung.getGesamtMenge(), Buchung.getNeueBuchungen());
		befehlBuchung.clearAll();
	}
}
