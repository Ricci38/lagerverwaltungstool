package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;

import model.Buchung;
import model.Lager;

public class Oberflaeche {

	// ### Singeltonvariable ###
	private static Oberflaeche theInstance;
	
	// ### ActionListener f�r die Oberfl�chen ###
	static ActionListener listener_Lagerverwaltung, listener_EinbuchungsAssi, listener_LieferungsUebersicht;

	// ### Variablen f�r die einzelnen Oberfl�chen ###
	private JFrame lagerverwaltung;
	private JFrame einbuchungsAssi;

	// ### Variablen f�r das Hauptfenster ###
	private JPanel p_top, p_top_sub_top, p_top_sub_bottom, p_tree, p_center, p_platzhalter1, p_platzhalter2;
	private JButton redo, undo, buchen, buchungsuebersicht, lieferungFuerLager, neuesLager;
	private JLabel l_titel;
	private JTree lagerTree;
	private JTable tbl_buchungsUebersicht;

	// ### Variablen f�r den EinbuchungsAssistent ###
	private JPanel p_EiAs_top, p_EiAs_left, p_EiAs_rigth, p_EiAs_button, p_EiAs_rigth_center;
	private JLabel l_EiAs_treeUeberschrift;
	private JTextField gesamtmenge, prozentAnteil;
	private JLabel lagerBezeichnung, anteilsMenge;
	private JButton btn_bestaetigen, btn_abbruch;
	private final int kopfzeilenHoehe = 40;
	private GridBagLayout gbl;
	private static int anz_hinzugefuegterLager = 0;
	private final HashMap<Lager, JTextField> hinzugefuegteLager = new HashMap<Lager, JTextField>();


	// ### privater Konstruktor (Singelton) ###
	private Oberflaeche() {
		
		buildLagerverwaltung();
		buildEinbuchungsAssi();
	}

	/**
	 * Erstellt die Oberfl�che f�r das Hauptfenster der Lagerverwaltung
	 */
	private void buildLagerverwaltung() {
		if (lagerverwaltung != null) return;
		// TODO �berpr�fen der gesamten Methode! (Gefahr von Copypasta!)
		lagerverwaltung = new JFrame("Lagerverwaltung");
		lagerverwaltung.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lagerverwaltung.setSize(1200, 800);
		lagerverwaltung.setMinimumSize(new Dimension(900, 500));
		lagerverwaltung.setLocation(100, 50);

		Container c = lagerverwaltung.getContentPane();
		c.setLayout(new BorderLayout(10, 10)); // in Klammer kann der Freiraum zwischen den einzelnen Elementen angegeben werden

		// ### Panel f�r den 'undo' und den 'redo' Button ###
		// Panel dient als Container f�r zwei Subpanels
		p_top = new JPanel();
		p_top.setLayout(new GridLayout(2, 1)); // Tabellenlayout um die beiden Panels untereinander zu stapeln
		p_top.setSize(lagerverwaltung.getWidth(), 80);
		p_top_sub_top = new JPanel();
//		p_top_sub_top.setBackground(Color.RED);
		l_titel = new JLabel("Lagerverwaltungstool v1.0");
		l_titel.setFont(new Font("Helvetica", Font.BOLD, 18));
		p_top_sub_top.add(l_titel);

		p_top_sub_bottom = new JPanel();
//		p_top_sub_bottom.setBackground(Color.CYAN);
		GridBagLayout gbl2 = new GridBagLayout();
		p_top_sub_bottom.setLayout(gbl2);

		p_platzhalter1 = new JPanel();
		p_platzhalter2 = new JPanel();

		undo = new JButton("undo");
		redo = new JButton("redo");
		buchen = new JButton("Neue Lieferung");
		neuesLager = new JButton("Neues Lager");
		buchungsuebersicht = new JButton("Lieferungs�bersicht");
		lieferungFuerLager = new JButton("Lager�bersicht");

		// ### Actionlistener bekannt machen ###
		undo.addActionListener(listener_Lagerverwaltung);
		redo.addActionListener(listener_Lagerverwaltung);
		buchen.addActionListener(listener_Lagerverwaltung);
		neuesLager.addActionListener(listener_Lagerverwaltung);
		buchungsuebersicht.addActionListener(listener_Lagerverwaltung);
		lieferungFuerLager.addActionListener(listener_Lagerverwaltung);

		// ### Platzhalter ###
		p_platzhalter1.setPreferredSize(new Dimension(5, 30));
		p_platzhalter1.setMinimumSize(new Dimension(5, 30));
		p_platzhalter2.setPreferredSize(new Dimension(150, 30));
		p_platzhalter2.setMinimumSize(new Dimension(150, 30));

		// ### Komponenten dem unteren Unterpanel hinzuf�gen ###
		Tools.addComponent(p_top_sub_bottom, gbl2, neuesLager, 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, buchen, 1, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, p_platzhalter1, 2, 0, 1, 0, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, undo, 3, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, redo, 4, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, p_platzhalter2, 5, 0, 5, 1, 1, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, buchungsuebersicht, 10, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_top_sub_bottom, gbl2, lieferungFuerLager, 11, 0, 1, 1, 0, 0, GridBagConstraints.NONE);

		// ### Dem oberen Panel die beiden Unterpanels zuweisen ###
		p_top.add(p_top_sub_top);
		p_top.add(p_top_sub_bottom);

		// ### Baum mit Scrollbar im WEST Element ###
		lagerTree = new JTree(Lager.getTree());
		
		p_tree = new JPanel(new GridLayout());
		p_tree.setPreferredSize(new Dimension(250, 50)); // Breite des Trees festlegen. H�he wird aufgrund des BorderLayouts ignoriert!
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		lagerTree.addTreeSelectionListener((TreeSelectionListener) listener_Lagerverwaltung);
		p_tree.add(scrollBar);

		// ### Men�auswahl im CENTER ###
		p_center = new JPanel();
		p_center.setBackground(Color.GREEN);
		
		//TODO: Layout aussuchen und schauen in welchem Layout die Tabelle am besten passt
//		GridBagLayout gbl = new GridBagLayout();
//		p_center.setLayout(gbl);
		
		c.add(p_tree, BorderLayout.WEST);
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_center, BorderLayout.CENTER);
	}

	/**
	 * Erstellt die Oberfl�che f�r den Einbuchungsassistenten
	 */
	private void buildEinbuchungsAssi() {
		if (einbuchungsAssi != null) return;
		// TODO Gesamte Methode auf Fehler �berpr�fen! (Copypasta)
		einbuchungsAssi = new JFrame("Einbuchungsassistent");
		einbuchungsAssi.setSize(550, 400);
		einbuchungsAssi.setLocation(300, 250);
		einbuchungsAssi.setMinimumSize(new Dimension(500, 350));
		einbuchungsAssi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Schlie�t das einzelne Frame, aber nicht das gesamte Programm

		Container c = einbuchungsAssi.getContentPane();
		c.setLayout(new BorderLayout());

		p_EiAs_top = new JPanel();
		p_EiAs_top.setSize(einbuchungsAssi.getWidth(), kopfzeilenHoehe); //Breite wird wegen des BorderLayouts ignoriert!
		l_EiAs_treeUeberschrift = new JLabel();
		l_EiAs_treeUeberschrift.setText("Bitte w�hlen Sie die Lager aus, dessen Bestand ver�ndert werden soll.");
		p_EiAs_top.add(l_EiAs_treeUeberschrift);

		p_EiAs_left = new JPanel();
		p_EiAs_left.setLayout(new GridLayout());
		p_EiAs_left.setPreferredSize(new Dimension(150, 100));
		p_EiAs_left.setLocation(0, kopfzeilenHoehe);

		JTree lagerTree = new JTree(Lager.getTree());
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_EiAs_left.add(scrollBar);

		
		p_EiAs_rigth = new JPanel();
		p_EiAs_rigth.setLayout(new BorderLayout());
		
		p_EiAs_rigth_center = new JPanel();
		gbl = new GridBagLayout();
		p_EiAs_rigth_center.setLayout(gbl);

		// Panel f�r die Buttons, um eine bessere Formatierung zu erreichen
		p_EiAs_button = new JPanel();
		p_EiAs_button.add(btn_bestaetigen = new JButton("Best�tigen"));
		p_EiAs_button.add(btn_abbruch = new JButton("Abbruch"));

		// Listener den Buttons und dem Tree bekannt machen
		btn_bestaetigen.addActionListener(listener_EinbuchungsAssi);
		btn_abbruch.addActionListener(listener_EinbuchungsAssi);
		lagerTree.addTreeSelectionListener((TreeSelectionListener) listener_EinbuchungsAssi);

		Tools.addComponent(p_EiAs_rigth_center, gbl, gesamtmenge = new JTextField("Gesamtmenge"), 1, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		gesamtmenge.addMouseListener((MouseListener) listener_EinbuchungsAssi);

		//Scrollbar zum rechten Panel hinzuf�gen
		JScrollPane scrollBar_center = new JScrollPane(p_EiAs_rigth_center);
		p_EiAs_rigth.add(scrollBar_center, BorderLayout.CENTER);
		p_EiAs_rigth.add(p_EiAs_button, BorderLayout.SOUTH);

		c.add(p_EiAs_top, BorderLayout.NORTH);
		c.add(p_EiAs_left, BorderLayout.WEST);
		c.add(p_EiAs_rigth, BorderLayout.CENTER);
	}

	/**
	 * Get the instance of this singelton
	 * 
	 * @return the singelton object of the class 'Oberflaeche'
	 */
	public static Oberflaeche getInstance() {
		if (theInstance == null) {
			theInstance = new Oberflaeche();
		}
		return theInstance;
	}

	
	public void zeigeBuchungsdetails(ArrayList<Buchung> buchungsListe)
	{
		Lager lager = getAusgewaehlterKnoten();
		if (lager == null) return;
		String[] spalten = new String[]{"ID","Lagername","Menge"};
		String[][] daten = new String[20][3];
		DefaultTableModel test1 = new DefaultTableModel(spalten, 1);
		int i = 0;
		
		//TODO: Daten in Tabelle anzeigen
		p_center.removeAll();
		p_center.add(new JLabel("Saldo von " + lager.getName() + ": " + lager.getBestand()));
		
		if (lager.isLeaf()) {
			for (Buchung b : buchungsListe) {
				daten[i][0] = ((Integer)b.getLieferungID()).toString();
				daten[i][1] = ((Integer)b.getMenge()).toString();
				daten[i++][2] = b.getDatum().toString();
			}
		}
		tbl_buchungsUebersicht = new JTable(3,2); // FIXME Daten richtig anzeigen & Spalten nicht bearbeitbar machen
//		tbl_buchungsUebersicht.getColumnModel().setColumnSelectionAllowed(false);
		p_center.add(tbl_buchungsUebersicht);
		p_center.updateUI();
	}
	
	public void resetEinbuchungsAssi()
	{
		einbuchungsAssi = null;
		hinzugefuegteLager.clear();
		buildEinbuchungsAssi();
	}
	public void addLager(Lager lager, EventListener textField_listener)
	{
		
		if(!hinzugefuegteLager.containsKey(lager))
		{
			Tools.addComponent(p_EiAs_rigth_center, gbl, lagerBezeichnung = new JLabel(lager.toString()), 0, 7 + anz_hinzugefuegterLager, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
			Tools.addComponent(p_EiAs_rigth_center, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 7 + anz_hinzugefuegterLager, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
			prozentAnteil.addActionListener((ActionListener) textField_listener);
			prozentAnteil.addMouseListener((MouseListener) textField_listener);
			p_EiAs_rigth_center.updateUI();
			anz_hinzugefuegterLager++;
			
			hinzugefuegteLager.put(lager, prozentAnteil);
		}
	}

	public HashMap<Lager, JTextField> getHinzugefuegteLager()
	{
		return hinzugefuegteLager;
	}
	
	public static void setLagerListener(ActionListener l)
	{
		listener_Lagerverwaltung = l;
	}
	
	public static void setEinbuchungListener(ActionListener l)
	{
		listener_EinbuchungsAssi = l;
	}
	
	public static void setLieferungListener(ActionListener l)
	{
		listener_LieferungsUebersicht = l;
	}
	
	
	// ### Show & Hide Frames ###
	public void showLagerverwaltung() {
		lagerverwaltung.setVisible(true);
	}

	public void hideLagerverwaltung() {
		lagerverwaltung.setVisible(false);
	}

	public void showEinbuchungsAssi() {
		einbuchungsAssi.setVisible(true);
	}

	public void hideEinbuchungsAssi() {
		einbuchungsAssi.setVisible(false);
	}

	// ### JTree neu aufbauen ###
	public void refreshTree() {
		((DefaultTreeModel) lagerTree.getModel()).reload();
	}
	
	public Lager getAusgewaehlterKnoten() {
		Lager pfad = (Lager) lagerTree.getLastSelectedPathComponent();

		return pfad;
	}

	// ### Disabling clone() by throwing CloneNotSupportedException ###
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singelton, dude! No cloning at all!");
	}

}
