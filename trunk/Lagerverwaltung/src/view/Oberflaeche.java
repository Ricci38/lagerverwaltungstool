package view;

import java.util.HashMap;
import java.util.List;

import javax.swing.JTextField;

import model.Buchung;
import model.Lager;
import model.Lieferung;

public interface Oberflaeche {

	public void zeigeLieferungsdetails(List<Buchung> list);

	public void zeigeLieferungen(List<Lieferung> list);
	
	public void showTabLieferungsBuchungen(List<Buchung> buchungen);

	public HashMap<Lager, JTextField> getHinzugefuegteLager();

	public void showLagerverwaltung();

	public void hideLagerverwaltung();

	public void showCardNeueLieferung();
	
	public void showCardUebersicht();
	
	public void enableLagerUebersicht();
	
	public void disableLagerUebersicht();
	
	public void showUndoRedo();
	
	public void hideUndoRedo();
	
	public void selectTreeRoot();
	
	public void refreshTree();

	public Lager getAusgewaehlterKnoten();

	public void showTabLagerbuchung();

	public void zeigeLagerbuchungen(List<Buchung> buchungen);

	boolean isCardNeueLieferungAktiv();

	boolean isCardUebersichtAktiv();

	void x(String n);

	String getGesamtmenge();

	String getProzentualerAnteil();

	void setVerbleibendeMenge(int menge);

	int getVerbleibendeMenge();

	void showVerbleibendeMenge();

}