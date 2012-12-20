package view;

import java.util.List;

import javax.swing.tree.TreeNode;

import model.Buchung;
import model.Lager;
import model.Lieferung;

public interface Oberflaeche {

	
	//TODO ‹bersichtlich gestalten (Sortieren)
	public void zeigeLieferungsdetails(List<Buchung> list);

	public void zeigeLieferungen(List<Lieferung> list);
	
	public void showTabLieferungsBuchungen(List<Buchung> buchungen);

	public void showLagerverwaltung();

	public void hideLagerverwaltung();

	public void showCardNeueLieferung();
	
	public void showCardUebersicht();
	
	public void enableLagerUebersicht();
	
	public void disableNeueLieferung();
	
	public void showUndoRedo();
	
	public void hideUndoRedo();
	
	public void selectTreeRoot();
	
	public void refreshTree();
	
	void refreshTree(TreeNode node);

	public Lager getAusgewaehlterKnoten();

	public void zeigeLagerbuchungen(List<Buchung> buchungen);

	boolean isCardNeueLieferungAktiv();

	boolean isCardUebersichtAktiv();

	void showLagerFuerBuchung(String n);

	String getGesamtmenge();

	String getProzentualerAnteil();

	void setVerbleibendeMenge(int menge);

	int getVerbleibendeMenge();

	void showVerbleibendeMenge();

	void enableJetztBuchen();

	void disableJetztBuchen();

	void enableAlleBuchungenBestaetigen();

	void disableAlleBuchungenBestaetigen();

	void enableNeuesLager();

	void disableNeuesLager();

	List<Buchung> getAllBuchungen(Lager l);

	boolean isAbBuchung();

	void disableBuchungsArt();

	void enableBuchungsArt();

	public void enableGesamtmenge();
	
	public void disableGesamtmenge();

	void enableRedo();

	void disableRedo();

	void enableUndo();

	void disableUndo();

	int getVerbleibenderProzentanteil();

	void setVerbleibenderProzentanteil(int verbleibenderProzentanteil);

}