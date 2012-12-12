package view;

import java.util.HashMap;
import java.util.List;

import javax.swing.JTextField;

import model.Buchung;
import model.Lager;
import model.Lieferung;

public interface Oberflaeche {

	public void zeigeBuchungsdetails(List<Buchung> list);

	public void zeigeLieferungen(List<Lieferung> list);
	
	public void zeigeLieferungsBuchungen(List<Buchung> buchungen);

	public HashMap<Lager, JTextField> getHinzugefuegteLager();

	// ### Show & Hide Frames ###
	public void showLagerverwaltung();

	public void hideLagerverwaltung();

	public void showCardNeueLieferung();
	
	public void showCardUebersicht();
	
	public void enableLagerUebersicht();
	
	public void disableLagerUebersicht();
	
	public void showUndoRedo();
	
	public void hideUndoRedo();
	
	public void selectTreeRoot();
	
	// ### JTree neu aufbauen ###
	public void refreshTree();

	public Lager getAusgewaehlterKnoten();

}