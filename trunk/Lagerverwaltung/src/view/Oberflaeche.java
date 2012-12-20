package view;

import java.util.List;

import javax.swing.JTable;
import javax.swing.tree.TreeNode;

import model.Buchung;
import model.Lager;
import model.Lieferung;

public interface Oberflaeche {

	public void enableAlleBuchungenBestaetigen();

	public void enableBuchungsArt();

	public void enableGesamtmenge();

	public void enableJetztBuchen();

	public void enableLagerUebersicht();

	public void enableLagerUmbenennen();

	public void enableNeuesLager();

	public void enableRedo();

	public void enableUndo();

	public void disableBuchungsArt();

	public void disableNeuesLager();

	public void disableNeueLieferung();

	public void disableJetztBuchen();

	public void disableGesamtmenge();

	public void disableAlleBuchungenBestaetigen();

	public void disableRedo();

	public void disableUndo();

	public void disableLagerUmbenennen();

	public void hideLagerverwaltung();

	public void hideUndoRedo();

	public void showUndoRedo();

	public void showLagerverwaltung();

	public boolean isAbBuchung();

	public boolean isCardNeueLieferungAktiv();

	public boolean isCardUebersichtAktiv();

	public List<Buchung> getAllBuchungen(Lager l);

	public Lager getAusgewaehlterKnoten();

	public String getGesamtmenge();

	public String getProzentualerAnteil();

	public int getVerbleibendeMenge();

	public int getVerbleibenderProzentanteil();

	public void setVerbleibendeMenge(int menge);

	public void setVerbleibenderProzentanteil(int verbleibenderProzentanteil);

	public void showCardNeueLieferung();

	public void showCardUebersicht();

	public void showLagerbuchungen(List<Buchung> b);

	public void showLagerFuerBuchung(String n);

	public void showLieferungsdetails(List<Buchung> buchungsListe);

	public void showLieferungen(List<Lieferung> lieferungen);

	public void showTabLieferungsBuchungen(List<Buchung> buchungen);

	public void showVerbleibendeMenge();
	
	public void selectTreeRoot();

	// ### JTree neu aufbauen ###
	public void refreshTree();

	public void refreshTree(TreeNode node);

	JTable getTbl_lieferungsUebersicht();

}