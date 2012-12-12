package view.impl;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;

import lagerverwaltungStart.Main;
import model.Buchung;
import model.Lager;
import model.Lieferung;
import view.Oberflaeche;
import view.Tools;

public class OberflaecheImpl implements Oberflaeche {

	// ### Singeltonvariable ###
	private static Oberflaeche theInstance;
	
	// ### ActionListener für die Oberflächen ###
	static ActionListener listener_Lagerverwaltung, listener_EinbuchungsAssi;
	static MouseListener listener_LieferungsUebersicht;

	// ### Variablen für die einzelnen Oberflächen ###
	private JFrame lagerverwaltung;
	private JFrame einbuchungsAssi;

	// ### Variablen für das Hauptfenster ###
	private JPanel p_top, p_top_sub_top, p_top_sub_bottom, p_tree, p_center, p_platzhalter1, p_platzhalter2;
	private JButton redo, undo, buchen, buchungsuebersicht, lieferungFuerLager, neuesLager;
	private JLabel l_titel;
	private JTree lagerTree;
	private JTable tbl_buchungsUebersicht, tbl_lieferungsUebersicht;
	
	// ### Variablen für den EinbuchungsAssistent ###
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
	private OberflaecheImpl() {
		
		buildLagerverwaltung();
		buildEinbuchungsAssi();
	}

	/**
	 * Erstellt die Oberfläche für das Hauptfenster der Lagerverwaltung
	 */
	private void buildLagerverwaltung() {
		if (lagerverwaltung != null) return;
		lagerverwaltung = new JFrame("Lagerverwaltung");
		lagerverwaltung.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lagerverwaltung.setSize(1000, 700);
		lagerverwaltung.setMinimumSize(new Dimension(900, 500));
		lagerverwaltung.setLocation(30, 10);

		Container c = lagerverwaltung.getContentPane();
		c.setLayout(new BorderLayout(10, 10)); // in Klammer kann der Freiraum zwischen den einzelnen Elementen angegeben werden

		// ### Panel für den 'undo' und den 'redo' Button ###
		// Panel dient als Container für zwei Subpanels
		p_top = new JPanel();
		p_top.setLayout(new GridLayout(2, 1)); // Tabellenlayout um die beiden Panels untereinander zu stapeln
		p_top.setSize(lagerverwaltung.getWidth(), 80);
		p_top_sub_top = new JPanel();
		l_titel = new JLabel("Lagerverwaltungstool v" + Main.VERSION);
		l_titel.setFont(new Font("Helvetica", Font.BOLD, 18));
		p_top_sub_top.add(l_titel);

		p_top_sub_bottom = new JPanel();
		GridBagLayout gbl2 = new GridBagLayout();
		p_top_sub_bottom.setLayout(gbl2);

		p_platzhalter1 = new JPanel();
		p_platzhalter2 = new JPanel();

		undo = new JButton("undo");
		redo = new JButton("redo");
		buchen = new JButton("Neue Lieferung");
		neuesLager = new JButton("Neues Lager");
		buchungsuebersicht = new JButton("Lieferungsübersicht");
		lieferungFuerLager = new JButton("Lagerübersicht");

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

		// ### Komponenten dem unteren Unterpanel hinzufügen ###
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
		p_tree.setPreferredSize(new Dimension(250, 50)); // Breite des Trees festlegen. Höhe wird aufgrund des BorderLayouts ignoriert!
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		lagerTree.addTreeSelectionListener((TreeSelectionListener) listener_Lagerverwaltung);
		p_tree.add(scrollBar);

		// ### Menüauswahl im CENTER ###
		p_center = new JPanel();
		p_center.setBackground(Color.GREEN);
		
		c.add(p_tree, BorderLayout.WEST);
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_center, BorderLayout.CENTER);
	}

	/**
	 * Erstellt die Oberfläche für den Einbuchungsassistenten
	 */
	private void buildEinbuchungsAssi() {
		if (einbuchungsAssi != null) return;
		// TODO Gesamte Methode auf Fehler überprüfen! (Copypasta)
		einbuchungsAssi = new JFrame("Einbuchungsassistent");
		einbuchungsAssi.setSize(550, 400);
		einbuchungsAssi.setLocation(300, 250);
		einbuchungsAssi.setMinimumSize(new Dimension(500, 350));
		einbuchungsAssi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //Schließt das einzelne Frame, aber nicht das gesamte Programm

		Container c = einbuchungsAssi.getContentPane();
		c.setLayout(new BorderLayout());

		p_EiAs_top = new JPanel();
		p_EiAs_top.setSize(einbuchungsAssi.getWidth(), kopfzeilenHoehe); //Breite wird wegen des BorderLayouts ignoriert!
		l_EiAs_treeUeberschrift = new JLabel();
		l_EiAs_treeUeberschrift.setText("Bitte wählen Sie die Lager aus, dessen Bestand verändert werden soll.");
		p_EiAs_top.add(l_EiAs_treeUeberschrift);

		p_EiAs_left = new JPanel();
		p_EiAs_left.setLayout(new GridLayout());
		p_EiAs_left.setPreferredSize(new Dimension(200, 100));
		p_EiAs_left.setLocation(0, kopfzeilenHoehe);

		JTree lagerTree = new JTree(Lager.getTree());
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_EiAs_left.add(scrollBar);

		
		p_EiAs_rigth = new JPanel();
		p_EiAs_rigth.setLayout(new BorderLayout());
		
		p_EiAs_rigth_center = new JPanel();
		gbl = new GridBagLayout();
		p_EiAs_rigth_center.setLayout(gbl);

		// Panel für die Buttons, um eine bessere Formatierung zu erreichen
		p_EiAs_button = new JPanel();
		p_EiAs_button.add(btn_bestaetigen = new JButton("Bestätigen"));
		p_EiAs_button.add(btn_abbruch = new JButton("Abbruch"));

		// Listener den Buttons und dem Tree bekannt machen
		btn_bestaetigen.addActionListener(listener_EinbuchungsAssi);
		btn_abbruch.addActionListener(listener_EinbuchungsAssi);
		lagerTree.addTreeSelectionListener((TreeSelectionListener) listener_EinbuchungsAssi);

		Tools.addComponent(p_EiAs_rigth_center, gbl, gesamtmenge = new JTextField("Gesamtmenge"), 1, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		gesamtmenge.addMouseListener((MouseListener) listener_EinbuchungsAssi);

		//Scrollbar zum rechten Panel hinzufügen
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
			theInstance = new OberflaecheImpl();
		}
		return theInstance;
	}

	
	/* (non-Javadoc)
	 * @see view.Oberflaeche#zeigeBuchungsdetails(java.util.ArrayList)
	 */
	@Override
	public void zeigeBuchungsdetails(List<Buchung> buchungsListe)
	{
		Lager lager = getAusgewaehlterKnoten();
		JLabel lagerSaldo;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
		
		p_center.setLayout(new BorderLayout());
		
		// Aufbau einer 4 x X Matrix
		List<ArrayList<String>> daten = new ArrayList<ArrayList<String>>();
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());
		
		if (lager != null) {
			lagerSaldo = new JLabel("Saldo von " + lager.getName() + ": " + lager.getBestand());
			lagerSaldo.setFont(new Font("Helvetica", Font.BOLD, 13));
			p_center.add(lagerSaldo, BorderLayout.NORTH);
		}
		else if (lager == null || buchungsListe.isEmpty())
			return;
		
		String[] spalten = new String[]{"ID","Lager","Datum","Menge"};
		int i = 0;
		
		p_center.removeAll();
		if(lager != null)
		{
			lagerSaldo = new JLabel("Saldo von " + lager.getName() + ": " + lager.getBestand());
			lagerSaldo.setFont(new Font("Helvetica", Font.BOLD, 13));
			p_center.add(lagerSaldo, BorderLayout.NORTH);
		}
	

		/*
		 *  Hier vielleicht schon eine zweite for-Schleife drum herum legen, um auch von einem Lager, das kein 'leaf' ist,
		 *  alle darunter liegenden Lager zu erfassen.
		 *  Dann müsste auch die Matrix oben um eine ArrayList<String> erweitert werden, um den Lagernamen aufnehmen zu können.
		 *  Das ist denke ich mal einfacher, als wenn wir das dann noch einmal anders machen für die "Oberlager".
		 */
		if ((lager != null && lager.isLeaf()) || !(buchungsListe.isEmpty())) {
			for (Buchung b : buchungsListe) {
				
				daten.get(0).add(((Integer)b.getLieferungID()).toString());
				daten.get(1).add(lager.getName());
				daten.get(2).add(sdf.format(b.getDatum()));
				daten.get(3).add(((Integer)b.getMenge()).toString());
				i++;
			}
			
			if (i > 0) { 
				String[][] tblDaten = new String[i][4]; 
				
				for (int j = 0; j < i; j++) { 
					
					tblDaten[j][0] = daten.get(0).get(j); 
					tblDaten[j][1] = daten.get(1).get(j);
					tblDaten[j][2] = daten.get(2).get(j); 
					tblDaten[j][3] = daten.get(3).get(j); 
				}
				
				
				tbl_buchungsUebersicht = new JTable(tblDaten, spalten) {
					private static final long serialVersionUID = 861599783877862457L;

					@Override
					public boolean isCellEditable(int arg0, int arg1) {
						return false;
					}
				};
				
				tbl_buchungsUebersicht.setFillsViewportHeight(true);	
				p_center.add(new JScrollPane(tbl_buchungsUebersicht), BorderLayout.CENTER); 
			}
		}
		p_center.updateUI();
	}
	
	
	/* (non-Javadoc)
	 * @see view.Oberflaeche#zeigeLieferungen(java.util.ArrayList)
	 */
	@Override
	public void zeigeLieferungen(List<Lieferung> lieferungen)
	{
		if (lieferungen.isEmpty()) 
			{
				Tools.showMsg("Es wurden noch keine Lieferungen ausgeführt!");
				return;
			}
		p_center.setLayout(new BorderLayout());
		p_center.removeAll();
		
		int i = 0;
		
		String[] spalten = new String[]{"Datum","Anzahl Buchungen","BlaBla"};
		String[][] daten = new String[lieferungen.size()][3];
		
		for (Lieferung l : lieferungen) {
			daten[i][0] = l.getLieferungsDatum().toString(); 
			daten[i][1] = ((Integer)l.getBuchungen().size()).toString();
			daten[i][2] = "asdf";
			i++;
		}
		
		tbl_lieferungsUebersicht = new JTable(daten, spalten)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};
		
		tbl_lieferungsUebersicht.setFillsViewportHeight(true);
		tbl_lieferungsUebersicht.addMouseListener(listener_LieferungsUebersicht);
		p_center.add(new JLabel("Lieferungsübersicht: "), BorderLayout.NORTH);
		p_center.add(new JScrollPane(tbl_lieferungsUebersicht), BorderLayout.CENTER);
		p_center.updateUI();
	}
	
	@Override
	public void zeigeLieferungsBuchungen(List<Buchung> buchungen)
	{
		OberflaecheImpl.getInstance().zeigeBuchungsdetails(buchungen);
	}
	
	/* (non-Javadoc)
	 * @see view.Oberflaeche#resetEinbuchungsAssi()
	 */
	@Override
	public void resetEinbuchungsAssi()
	{
		einbuchungsAssi = null;
		hinzugefuegteLager.clear();
		anz_hinzugefuegterLager = 0;
		buildEinbuchungsAssi();
	}
	/* (non-Javadoc)
	 * @see view.Oberflaeche#addLager(model.Lager, java.util.EventListener)
	 */
	@Override
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

	/* (non-Javadoc)
	 * @see view.Oberflaeche#getHinzugefuegteLager()
	 */
	@Override
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
	
	public static void setLieferungListener(MouseListener l)
	{
		listener_LieferungsUebersicht = l;
	}
	
	
	// ### Show & Hide Frames ###
	/* (non-Javadoc)
	 * @see view.Oberflaeche#showLagerverwaltung()
	 */
	@Override
	public void showLagerverwaltung() {
		lagerverwaltung.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see view.Oberflaeche#hideLagerverwaltung()
	 */
	@Override
	public void hideLagerverwaltung() {
		lagerverwaltung.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see view.Oberflaeche#showEinbuchungsAssi()
	 */
	@Override
	public void showEinbuchungsAssi() {
		einbuchungsAssi.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see view.Oberflaeche#hideEinbuchungsAssi()
	 */
	@Override
	public void hideEinbuchungsAssi() {
		einbuchungsAssi.setVisible(false);
	}

	// ### JTree neu aufbauen ###
	/* (non-Javadoc)
	 * @see view.Oberflaeche#refreshTree()
	 */
	@Override
	public void refreshTree() {
		((DefaultTreeModel) lagerTree.getModel()).reload();
	}
	
	/* (non-Javadoc)
	 * @see view.Oberflaeche#getAusgewaehlterKnoten()
	 */
	@Override
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
