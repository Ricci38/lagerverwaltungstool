package view;

import java.util.EventListener;
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

	public void resetEinbuchungsAssi();

	public void addLager(Lager lager, EventListener textField_listener);

	public HashMap<Lager, JTextField> getHinzugefuegteLager();

	// ### Show & Hide Frames ###
	public void showLagerverwaltung();

	public void hideLagerverwaltung();

	public void showEinbuchungsAssi();

	public void hideEinbuchungsAssi();

	// ### JTree neu aufbauen ###
	public void refreshTree();

	public Lager getAusgewaehlterKnoten();

}