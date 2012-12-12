package view;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTextField;

import model.Buchung;
import model.Lager;
import model.Lieferung;

public interface Oberflaeche {

	public void zeigeLieferungsdetails(List<Buchung> list);

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
	
	public void x(String n, ActionListener l);
	
	// ### JTree neu aufbauen ###
	public void refreshTree();

	public Lager getAusgewaehlterKnoten();

	public void showTabLagerbuchung();

	public void zeigeLagerbuchungen(List<Buchung> buchungen);

}