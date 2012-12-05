package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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

import model.Lager;

public class Oberflaeche {

	private JFrame lagerverwaltung;
	private JFrame einbuchungsAssi;
	private JFrame lieferungsUebersicht;

	// Lieferungsübersicht
	private JPanel p_LiUe_top, p_LiUe_left, p_LiUe_rigth;
	private JTable tbl_LiUe_buchungsdetails;

	// EinbuchungsAssi TODO Variabelnnamen anpassen (s.o.)
	JPanel p_top, p_left, p_rigth, p_button;
	JLabel l_treeUeberschrift;
	JTextField gesamtmenge, prozentAnteil;
	JLabel lagerBezeichnung, anteilsMenge;
	JButton btn_bestaetigen, btn_abbruch;
	int kopfzeilenHoehe = 40;
	GridBagLayout gbl;
	private ActionListener listener; // FIXME: Variable füllen!
	
	private static Oberflaeche theInstance;

	private Oberflaeche() {
		buildLagerverwaltung();
		buildEinbuchungsAssi();
		buildLieferungsUebersicht();
	}

	private void buildLagerverwaltung() {
		// TODO Auto-generated method stub
		
	}

	private void buildLieferungsUebersicht() {
		// TODO Ganze Methode einmal gründlich überprüfen auf Fehler! (Copypasta)
		lieferungsUebersicht = new JFrame("Lieferungsübersicht");
		lieferungsUebersicht.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		lieferungsUebersicht.setSize(800, 500);
		lieferungsUebersicht.setLocation(300, 100);
		Container c = lieferungsUebersicht.getContentPane();
		c.setLayout(null);

		p_LiUe_top = new JPanel();
		p_LiUe_top.setLocation(0, 0);
		p_LiUe_top.setSize(lieferungsUebersicht.getWidth(), 50);
		p_LiUe_top.setBackground(Color.ORANGE);

		p_LiUe_left = new JPanel();
		p_LiUe_left.setLayout(null);
		p_LiUe_left.setLocation(0, 50);
		p_LiUe_left.setSize(200, lieferungsUebersicht.getHeight() - p_LiUe_top.getHeight() - 40);

		JTree lagerTree = new JTree(new DefaultMutableTreeNode("root"));
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		scrollBar.setSize(p_LiUe_left.getWidth(), p_LiUe_left.getHeight());
		p_LiUe_left.add(scrollBar);

		p_LiUe_rigth = new JPanel();
		// p_rigth.setLayout(null);
		p_LiUe_rigth.setBackground(Color.magenta);
		p_LiUe_rigth.setLocation(p_LiUe_left.getWidth(), 50);
		p_LiUe_rigth.setSize(lieferungsUebersicht.getWidth() - p_LiUe_left.getWidth() - 25,
				lieferungsUebersicht.getHeight() - p_LiUe_top.getHeight() - 40);

		tbl_LiUe_buchungsdetails = new JTable(2, 6);
		tbl_LiUe_buchungsdetails.setLocation(p_LiUe_left.getWidth() + 50,
				p_LiUe_top.getHeight() + 80);

		p_LiUe_rigth.add(tbl_LiUe_buchungsdetails);

		c.add(p_LiUe_top);
		c.add(p_LiUe_left);
		c.add(p_LiUe_rigth);
	}

	private void buildEinbuchungsAssi() {
		// TODO Gesamte Methode auf Fehler überprüfen! (Copypasta)
		lieferungsUebersicht.setSize(550, 400);
		lieferungsUebersicht.setLocation(300, 250);
		lieferungsUebersicht.setMinimumSize(new Dimension(500, 350)); 
		lieferungsUebersicht.setDefaultCloseOperation(Einbuchungsassistent.DISPOSE_ON_CLOSE);		//Schließt das einzelne Frame, 
																						//aber nicht das gesamte Programm

		Container c = lieferungsUebersicht.getContentPane();
		c.setLayout(new BorderLayout());

		p_top = new JPanel();
		p_top.setSize(lieferungsUebersicht.getWidth(), kopfzeilenHoehe);			//Breite wird wegen des BorderLayouts ignoriert!
		p_top.setBackground(Color.blue);
		l_treeUeberschrift = new JLabel();
		l_treeUeberschrift.setText("Bitte wählen Sie die Lager aus, deren Bestand verändert werden soll");
		p_top.add(l_treeUeberschrift);

		p_left = new JPanel();
		p_left.setLayout(new GridLayout());
		p_left.setPreferredSize(new Dimension(150, 100));
		p_left.setLocation(0, kopfzeilenHoehe);
		p_left.setBackground(Color.gray);

		JTree lagerTree = new JTree(Lager.getTree());
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_left.add(scrollBar);
		
		p_rigth = new JPanel();
		gbl = new GridBagLayout();
		p_rigth.setLayout(gbl);
		p_rigth.setBackground(Color.GREEN);
		
		//Panel für die Buttons um eine bessere Formatierung zu erreichen
		p_button = new JPanel();
		p_button.setBackground(Color.CYAN);
		p_button.add(btn_bestaetigen = new JButton("Bestätigen"));
		p_button.add(btn_abbruch = new JButton("Abbruch"));
		
		//Listener zu den Buttons und dem Tree hinzufügen
		btn_bestaetigen.addActionListener((ActionListener)listener);
		btn_abbruch.addActionListener((ActionListener)listener);
		lagerTree.addTreeSelectionListener((TreeSelectionListener)listener);

		

		// Aufbau muss dynamisch in einer Methode erfolgen! Nur zur Vorschau!
		Tools.addComponent(p_rigth, gbl, gesamtmenge = new JTextField("Gesamtmenge"), 1, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		/*
		GUI_tools.addComponent(p_rigth, gbl, lagerBezeichnung = new JLabel("Lager 1"), 0, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, anteilsMenge = new JLabel("XX"), 2, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		GUI_tools.addComponent(p_rigth, gbl, anteilsMenge = new JLabel("XX"), 2, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		*/
		//GUI_tools.addComponent(p_rigth, gbl, p_button, 0, 3, 4, 1, 0, 0, GridBagConstraints.NONE);
		
		//Scrollbar zum rechten Panel hinzufügen
		JScrollPane scrollBar_center = new JScrollPane(p_rigth);
		
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_left, BorderLayout.WEST);
		c.add(scrollBar_center, BorderLayout.CENTER);

	}
	
	
	// ### Get the Instance of this Class ###
	public static Oberflaeche getInstance() {
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

}
