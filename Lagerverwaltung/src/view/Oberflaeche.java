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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import model.Lager;

public class Oberflaeche {
	
	// ### Singeltonvariable ###
	private static Oberflaeche theInstance;

	// ### Variablen f�r die einzelnen Oberfl�chen ###
	private JFrame lagerverwaltung;
	private JFrame einbuchungsAssi;
	private JFrame lieferungsUebersicht;
	
	// ### Variablen f�r das Hauptfenster ###
	private JPanel p_top, p_top_sub_top, p_top_sub_bottom, p_tree, p_center, p_platzhalter1, p_platzhalter2;
	private JButton redo, undo, buchen, buchungsuebersicht, lagersaldo, lieferungFuerLager, neuesLager;
	private JLabel l_titel, menue_beschriftung;
	private JTree lagerTree;
	
	// ### Variablen f�r die Lieferungs�bersicht ###
	private JPanel p_LiUe_top, p_LiUe_left, p_LiUe_rigth;
	private JTable tbl_LiUe_buchungsdetails;

	// ### Variablen f�r den EinbuchungsAssistent ###
	private JPanel p_EiAs_top, p_EiAs_left, p_EiAs_rigth, p_EiAs_button;
	private JLabel l_EiAs_treeUeberschrift;
	private JTextField gesamtmenge, prozentAnteil;
	private JLabel lagerBezeichnung, anteilsMenge;
	private JButton btn_bestaetigen, btn_abbruch;
	private final int kopfzeilenHoehe = 40;
	private GridBagLayout gbl;
	
	// ### Listener ###
	private ActionListener listener; // FIXME passende Listener anpassen / erstellen!
	// ... TreeSelectionListener bla
	
	
	// ### privater Konstruktor (Singelton) ###
	private Oberflaeche() {
		buildLagerverwaltung();
		buildEinbuchungsAssi();
		buildLieferungsUebersicht();
	}

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
		buchen = new JButton("Neue Buchung");
		neuesLager = new JButton("Neues Lager");
		buchungsuebersicht = new JButton("Lieferungs�bersicht");
		lagersaldo = new JButton("Lagersaldo");
		lieferungFuerLager = new JButton("Lager�bersicht");
		
		// ### Actionlistener bekannt machen ###
		undo.addActionListener(listener);
		redo.addActionListener(listener);
		buchen.addActionListener(listener);
		neuesLager.addActionListener(listener);
		buchungsuebersicht.addActionListener(listener);
		lagersaldo.addActionListener(listener);
		lieferungFuerLager.addActionListener(listener);
		
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
		Tools.addComponent(p_top_sub_bottom, gbl2, lagersaldo, 12, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		
		// ### Dem oberen Panel die beiden Unterpanels zuweisen ###
		p_top.add(p_top_sub_top);
		p_top.add(p_top_sub_bottom);
		
		// ### Baum mit Scrollbar im WEST Element ###
		lagerTree = new JTree(Lager.getTree());
		p_tree = new JPanel(new GridLayout());
		p_tree.setPreferredSize(new Dimension(250, 50)); // Breite des Trees festlegen. H�he wird aufgrund des BorderLayouts ignoriert!

		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_tree.add(scrollBar);

		// ### Men�auswahl im CENTER ###
		p_center = new JPanel();
		p_center.setBackground(Color.GREEN);
		GridBagLayout gbl = new GridBagLayout();
		p_center.setLayout(gbl);

		c.add(p_tree, BorderLayout.WEST);
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_center, BorderLayout.CENTER);
	}

	private void buildLieferungsUebersicht() {
		if (lieferungsUebersicht != null) return;
		// TODO Fehlen hier irgendwelche Listener? Ich denke ja - nicht sicher :D
		// TODO Ganze Methode einmal gr�ndlich �berpr�fen auf Fehler! (Copypasta!)
		lieferungsUebersicht = new JFrame("Lieferungs�bersicht");
		lieferungsUebersicht.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lieferungsUebersicht.setSize(800, 500);
		lieferungsUebersicht.setLocation(300, 100);
		Container c = lieferungsUebersicht.getContentPane();
		c.setLayout(null);

		p_LiUe_top = new JPanel();
		p_LiUe_top.setLocation(0, 0);
		p_LiUe_top.setSize(lieferungsUebersicht.getWidth(), 50);
//		p_LiUe_top.setBackground(Color.ORANGE);

		p_LiUe_left = new JPanel();
		p_LiUe_left.setLayout(null);
		p_LiUe_left.setLocation(0, 50);
		p_LiUe_left.setSize(200, lieferungsUebersicht.getHeight() - p_LiUe_top.getHeight() - 40);

		JTree lagerTree = new JTree(new DefaultMutableTreeNode("root"));
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		scrollBar.setSize(p_LiUe_left.getWidth(), p_LiUe_left.getHeight());
		p_LiUe_left.add(scrollBar);

		p_LiUe_rigth = new JPanel();
		// p_rigth.setLayout(null); // XXX: was ist damit?
//		p_LiUe_rigth.setBackground(Color.magenta);
		p_LiUe_rigth.setLocation(p_LiUe_left.getWidth(), 50);
		p_LiUe_rigth.setSize(lieferungsUebersicht.getWidth() - p_LiUe_left.getWidth() - 25, lieferungsUebersicht.getHeight() - p_LiUe_top.getHeight() - 40);

		tbl_LiUe_buchungsdetails = new JTable(2, 6);
		tbl_LiUe_buchungsdetails.setLocation(p_LiUe_left.getWidth() + 50, p_LiUe_top.getHeight() + 80);

		p_LiUe_rigth.add(tbl_LiUe_buchungsdetails);

		c.add(p_LiUe_top);
		c.add(p_LiUe_left);
		c.add(p_LiUe_rigth);
	}

	private void buildEinbuchungsAssi() {
		if (lieferungsUebersicht != null) return;
		// TODO Gesamte Methode auf Fehler �berpr�fen! (Copypasta)
		einbuchungsAssi = new JFrame("Einbuchungsassistent");
		einbuchungsAssi.setSize(550, 400);
		einbuchungsAssi.setLocation(300, 250);
		einbuchungsAssi.setMinimumSize(new Dimension(500, 350)); 
		einbuchungsAssi.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		//Schlie�t das einzelne Frame, 
																						//aber nicht das gesamte Programm

		Container c = einbuchungsAssi.getContentPane();
		c.setLayout(new BorderLayout());

		p_EiAs_top = new JPanel();
		p_EiAs_top.setSize(einbuchungsAssi.getWidth(), kopfzeilenHoehe);			//Breite wird wegen des BorderLayouts ignoriert!
//		p_EiAs_top.setBackground(Color.blue);
		l_EiAs_treeUeberschrift = new JLabel();
		l_EiAs_treeUeberschrift.setText("Bitte w�hlen Sie die Lager aus, dessen Bestand ver�ndert werden soll.");
		p_EiAs_top.add(l_EiAs_treeUeberschrift);

		p_EiAs_left = new JPanel();
		p_EiAs_left.setLayout(new GridLayout());
		p_EiAs_left.setPreferredSize(new Dimension(150, 100));
		p_EiAs_left.setLocation(0, kopfzeilenHoehe);
//		p_EiAs_left.setBackground(Color.gray);

		JTree lagerTree = new JTree(Lager.getTree());
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_EiAs_left.add(scrollBar);
		
		p_EiAs_rigth = new JPanel();
		gbl = new GridBagLayout();
		p_EiAs_rigth.setLayout(gbl);
//		p_EiAs_rigth.setBackground(Color.GREEN);
		
		// Panel f�r die Buttons, um eine bessere Formatierung zu erreichen
		p_EiAs_button = new JPanel();
//		p_EiAs_button.setBackground(Color.CYAN);
		p_EiAs_button.add(btn_bestaetigen = new JButton("Best�tigen"));
		p_EiAs_button.add(btn_abbruch = new JButton("Abbruch"));
		
		// Listener den Buttons und dem Tree bekannt machen
		btn_bestaetigen.addActionListener(listener);
		btn_abbruch.addActionListener(listener);
		lagerTree.addTreeSelectionListener((TreeSelectionListener)listener);
		

		// TODO Aufbau muss dynamisch in einer Methode erfolgen! Nur zur Vorschau!
		Tools.addComponent(p_EiAs_rigth, gbl, gesamtmenge = new JTextField("Gesamtmenge"), 1, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		/*
		GUI_tools.addComponent(p_rigth, gbl, lagerBezeichnung = new JLabel("Lager 1"), 0, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, anteilsMenge = new JLabel("XX"), 2, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, anteilsMenge = new JLabel("XX"), 2, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		*/
		//GUI_tools.addComponent(p_rigth, gbl, p_button, 0, 3, 4, 1, 0, 0, GridBagConstraints.NONE);
		
		//Scrollbar zum rechten Panel hinzuf�gen
		JScrollPane scrollBar_center = new JScrollPane(p_EiAs_rigth);

		c.add(p_EiAs_top, BorderLayout.NORTH);
		c.add(p_EiAs_left, BorderLayout.WEST);
		c.add(scrollBar_center, BorderLayout.CENTER);
	}

	// ### Get the Instance of this Class ###
	public static Oberflaeche getInstance() {
		if (theInstance == null) {
			theInstance = new Oberflaeche();
		}
		return theInstance;
	}

	// ### Show & Hide Frames ###
	public void showLagerverwaltung() {
		lagerverwaltung.setVisible(true);
	}

	public void hideLagerverwaltung() {
		lagerverwaltung.setVisible(false);
	}

	public void showLieferungsUebersicht() {
		lieferungsUebersicht.setVisible(true);
	}
	
	public void hideLieferungsUebersicht() {
		lieferungsUebersicht.setVisible(false);
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

	// ## Getter & Setter ###
	public JFrame getLagerverwaltung() {
		return lagerverwaltung;
	}

	public void setLagerverwaltung(JFrame lagerverwaltung) {
		this.lagerverwaltung = lagerverwaltung;
	}

	public JFrame getEinbuchungsAssi() {
		return einbuchungsAssi;
	}

	public void setEinbuchungsAssi(JFrame einbuchungsAssi) {
		this.einbuchungsAssi = einbuchungsAssi;
	}

	public JFrame getLieferungsUebersicht() {
		return lieferungsUebersicht;
	}

	public void setLieferungsUebersicht(JFrame lieferungsUebersicht) {
		this.lieferungsUebersicht = lieferungsUebersicht;
	}

	// ### Disabling clone() by throwing CloneNotSupportedException ###
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singelton, dude! No cloning at all!");
	}

}
