package view.impl;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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

	// ### ActionListener f�r die Oberfl�chen ###
	static ActionListener listener_Lagerverwaltung, listener_EinbuchungsAssi;
	static MouseListener listener_LieferungsUebersicht;

	// ### Variablen f�r die einzelnen Oberfl�chen ###
	private JFrame lagerverwaltung;
	//	private JFrame einbuchungsAssi;

	// ### Variablen f�r das Hauptfenster ###
	private JPanel p_top, p_top_sub_top, p_top_sub_bottom, p_tree, p_center, p_platzhalter1, p_platzhalter2;
	private JPanel p_center_lieferungen, p_center_lieferungdetails, p_center_neue_lieferung, p_center_neue_lieferung_north, p_center_neue_lieferung_south,
			p_center_neue_lieferung_center, p_center_lagerbuchungen;
	private JButton redo, undo, buchen, lageruebersicht, neuesLager;
	private JLabel l_titel;
	private JTree lagerTree;
	private JTable tbl_buchungsUebersicht, tbl_lieferungsUebersicht, tbl_lagerbuchungen;
	private JTabbedPane p_center_tabbs = new JTabbedPane();

	// ### Variablen f�r den EinbuchungsAssistent ###
	private JTextField gesamtmenge, prozentAnteil, saldo;
	private JLabel lagerBezeichnung, anteilsMenge;
	private JButton btn_best, btn_abbr;
	private GridBagLayout gbl;
	private static int anz_hinzugefuegterLager = 0;
	private final HashMap<Lager, JTextField> hinzugefuegteLager = new HashMap<Lager, JTextField>();

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
	
	private boolean isCardUebersichtAktiv = false;
	private boolean isCardNeueLieferungAktiv = false;

	// ### privater Konstruktor (Singelton) ###
	private OberflaecheImpl() {

		buildLagerverwaltung();
		hideUndoRedo();
		showCardUebersicht();
	}

	/**
	 * Erstellt die Oberfl�che f�r das Hauptfenster der Lagerverwaltung
	 */
	private void buildLagerverwaltung() {
		if (lagerverwaltung != null) return;
		lagerverwaltung = new JFrame("Lagerverwaltung");
		lagerverwaltung.setSize(1000, 700);
		lagerverwaltung.setMinimumSize(new Dimension(900, 500));
		lagerverwaltung.setLocation(30, 10);
		lagerverwaltung.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		lagerverwaltung.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(lagerverwaltung, "M�chten Sie die Lagerverwaltung wirklich beenden?\n"
						+ "Einstellungen werden nicht gespeichert.", "Wirklich beenden?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
						null);
				if (confirm == JOptionPane.YES_OPTION) {
					hideLagerverwaltung();
					lagerverwaltung.dispose();
					System.exit(0);
				}
			}
		});

		Container c = lagerverwaltung.getContentPane();
		c.setLayout(new BorderLayout(10, 10)); // in Klammer kann der Freiraum zwischen den einzelnen Elementen angegeben werden

		// ### Panel f�r den 'undo' und den 'redo' Button ###
		// Panel dient als Container f�r zwei Subpanels
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
		lageruebersicht = new JButton("Lieferungs-/ Lager�bersicht");

		// ### Actionlistener bekannt machen ###
		undo.addActionListener(listener_Lagerverwaltung);
		redo.addActionListener(listener_Lagerverwaltung);
		buchen.addActionListener(listener_Lagerverwaltung);
		neuesLager.addActionListener(listener_Lagerverwaltung);
		lageruebersicht.addActionListener(listener_Lagerverwaltung);

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
		Tools.addComponent(p_top_sub_bottom, gbl2, lageruebersicht, 10, 0, 1, 1, 0, 0, GridBagConstraints.NONE);

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
		p_center_tabbs = new JTabbedPane();
		p_center_tabbs.addTab("Lieferungen", p_center_lieferungen = new JPanel());
		p_center_tabbs.addTab("Lieferungdetails", p_center_lieferungdetails = new JPanel());
		p_center_tabbs.addTab("Lagerbuchungen", p_center_lagerbuchungen = new JPanel());

		gbl = new GridBagLayout();
		
		buildNeueLieferung();
		
		//TODO: Schei� Version des Einbuchungsassistenten
		p_center = new JPanel();
		p_center.setLayout(new CardLayout());
		p_center.add(p_center_tabbs, "�bersicht");
		p_center.add(p_center_neue_lieferung, "NeueLieferung");
		c.add(p_tree, BorderLayout.WEST);
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_center, BorderLayout.CENTER);
	}

	private void buildNeueLieferung() {
		p_center_neue_lieferung = new JPanel();
		p_center_neue_lieferung_north = new JPanel();
		p_center_neue_lieferung_center = new JPanel();
		p_center_neue_lieferung_south = new JPanel();
		p_center_neue_lieferung_south.setLayout(gbl);
		p_center_neue_lieferung_south.setPreferredSize(new Dimension(1, 100));
		Tools.addComponent(p_center_neue_lieferung_south, gbl, btn_best = new JButton("Best�tigen"), 1, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		Tools.addComponent(p_center_neue_lieferung_south, gbl, btn_abbr = new JButton("Abbrechen"), 3, 0, 1, 1, 0, 0, GridBagConstraints.NONE);
		p_center_neue_lieferung.setLayout(new BorderLayout());
		p_center_neue_lieferung.add(p_center_neue_lieferung_north, BorderLayout.NORTH);
		p_center_neue_lieferung.add(p_center_neue_lieferung_center, BorderLayout.CENTER);
		p_center_neue_lieferung.add(p_center_neue_lieferung_south, BorderLayout.SOUTH);

		btn_best.addActionListener(listener_Lagerverwaltung);
		btn_abbr.addActionListener(listener_Lagerverwaltung);

		p_center_neue_lieferung_north.add(new JLabel("Gesamtmenge: "));
		p_center_neue_lieferung_north.add(gesamtmenge = new JTextField("Gesamtmenge"));
		gesamtmenge.addMouseListener(listener_LieferungsUebersicht);
	}

	@Override
	public void x(String n, ActionListener l) {
		p_center_neue_lieferung_center.removeAll();
		buildNeueLieferung();
		Tools.addComponent(p_center_neue_lieferung_center, gbl, new JLabel(n), 0, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		Tools.addComponent(p_center_neue_lieferung_center, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 1, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		prozentAnteil.addActionListener(l);
		p_center_neue_lieferung_center.updateUI();
		p_center.updateUI();
	}

	//gibt das CardLayout zur�ck, um die Cards ansprechen zu k�nnen
	@Override
	public void showCardNeueLieferung() {
		((CardLayout) p_center.getLayout()).show(p_center, "NeueLieferung");
		isCardUebersichtAktiv = false;
		isCardNeueLieferungAktiv = true;
	}

	@Override
	public void showCardUebersicht() {
		((CardLayout) p_center.getLayout()).show(p_center, "�bersicht");
		isCardUebersichtAktiv = true;
		isCardNeueLieferungAktiv = false;
	}
	
	@Override
	public boolean isCardNeueLieferungAktiv() {
		return isCardNeueLieferungAktiv;
	}
	
	@Override
	public boolean isCardUebersichtAktiv() {
		return isCardUebersichtAktiv;
	}

	@Override
	public void disableLagerUebersicht() {
		lageruebersicht.setEnabled(false);
	}

	@Override
	public void enableLagerUebersicht() {
		lageruebersicht.setEnabled(true);
	}

	@Override
	public void hideUndoRedo() {
		undo.setVisible(false);
		redo.setVisible(false);
	}

	@Override
	public void showUndoRedo() {
		undo.setVisible(true);
		redo.setVisible(true);
	}

	@Override
	public void selectTreeRoot() {
		lagerTree.setSelectionRow(0);
	}

	/**
	 * Erstellt die Oberfl�che f�r den Einbuchungsassistenten
	 */

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

	@Override
	public void zeigeLieferungsdetails(List<Buchung> buchungsListe) {
		Lager lager = getAusgewaehlterKnoten();
		JLabel lagerSaldo;

		p_center_lieferungdetails.setLayout(new BorderLayout());

		// Aufbau einer 4 x X Matrix
		List<ArrayList<String>> daten = new ArrayList<ArrayList<String>>();
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());
		daten.add(new ArrayList<String>());

		if (lager == null || buchungsListe.isEmpty()) return;

		String[] spalten = new String[] { "Buchungs ID", "Lager", "Datum", "Menge" };

		p_center_lieferungdetails.removeAll();

		int i = buchungsListe.size();

		/*
		 * Hier vielleicht schon eine zweite for-Schleife drum herum legen, um
		 * auch von einem Lager, das kein 'leaf' ist, alle darunter liegenden
		 * Lager zu erfassen. Dann m�sste auch die Matrix oben um eine
		 * ArrayList<String> erweitert werden, um den Lagernamen aufnehmen zu
		 * k�nnen. Das ist denke ich mal einfacher, als wenn wir das dann noch
		 * einmal anders machen f�r die "Oberlager".
		 */
		if ((lager != null && lager.isLeaf()) || !(buchungsListe.isEmpty())) {
			for (Buchung b : buchungsListe) {

				daten.get(0).add(((Integer) b.getBuchungID()).toString());
				daten.get(1).add(b.getLagerName());
				daten.get(2).add(sdf.format(b.getDatum()));
				daten.get(3).add(((Integer) b.getMenge()).toString());
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
				p_center_lieferungdetails.add(new JScrollPane(tbl_buchungsUebersicht), BorderLayout.CENTER);
			}
		}
		p_center_lieferungdetails.updateUI();
	}

	@Override
	public void zeigeLieferungen(List<Lieferung> lieferungen) {
		if (lieferungen.isEmpty()) {
			p_center_lieferungen.add(new JLabel("Es wurden noch keine Lieferungen ausgef�hrt."));
			return;
		}
		p_center_lieferungen.removeAll();
		p_center_lieferungen.setLayout(new BorderLayout());

		int i = 0;

		String[] spalten = new String[] { "Datum", "Anzahl Buchungen", "BlaBla" };
		String[][] daten = new String[lieferungen.size()][3];

		for (Lieferung l : lieferungen) {
			daten[i][0] = sdf.format(l.getLieferungsDatum());
			daten[i][1] = ((Integer) l.getBuchungen().size()).toString();
			daten[i][2] = "asdf";
			i++;
		}

		tbl_lieferungsUebersicht = new JTable(daten, spalten) {
			private static final long serialVersionUID = -6588102605213723085L;

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return false;
			}
		};

		tbl_lieferungsUebersicht.setFillsViewportHeight(true);
		tbl_lieferungsUebersicht.addMouseListener(listener_LieferungsUebersicht);
		p_center_lieferungen.add(new JLabel("Lieferungs�bersicht: "), BorderLayout.NORTH);
		p_center_lieferungen.add(new JScrollPane(tbl_lieferungsUebersicht), BorderLayout.CENTER);
		p_center_lieferungen.updateUI();
	}

	@Override
	public void zeigeLagerbuchungen(List<Buchung> b) {
		p_center_lagerbuchungen.removeAll();
		if (null == b || b.isEmpty()) {
			if (!getAusgewaehlterKnoten().isLeaf() && getAusgewaehlterKnoten().getBestand() > 0) {
				p_center_lagerbuchungen.add(new JLabel("Gesamtsaldo des Oberlagers \"" + getAusgewaehlterKnoten() + "\": "
						+ getAusgewaehlterKnoten().getBestand()));
			} else
				p_center_lagerbuchungen.add(new JLabel("Es wurden noch keine Buchungen ausgef�hrt."));
		} else {
			p_center_lagerbuchungen.setLayout(new BorderLayout());

			int i = 0;

			String[] spalten = new String[] { "Buchungs ID", "Datum", "Menge" };
			String[][] daten = new String[b.size()][3];

			for (Buchung bu : b) {
				daten[i][0] = ((Integer) bu.getBuchungID()).toString();
				daten[i][1] = sdf.format(bu.getDatum());
				daten[i++][2] = ((Integer) bu.getMenge()).toString();
			}

			tbl_lagerbuchungen = new JTable(daten, spalten) {

				private static final long serialVersionUID = 6620092595652821138L;

				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					return false;
				}
			};

			tbl_lagerbuchungen.setFillsViewportHeight(true);
			p_center_lagerbuchungen.add(saldo = new JTextField("Buchungen von Lager \"" + getAusgewaehlterKnoten().getName() + "\" mit Saldo "
					+ getAusgewaehlterKnoten().getBestand() + ":"), BorderLayout.NORTH);
			p_center_lagerbuchungen.add(new JScrollPane(tbl_lagerbuchungen), BorderLayout.CENTER);
			saldo.setEditable(false);
			saldo.setFocusable(false);
		}
		p_center_lagerbuchungen.updateUI();
	}

	@Override
	public void showTabLieferungsBuchungen(List<Buchung> buchungen) {
		theInstance.zeigeLieferungsdetails(buchungen);
		// TODO zeige richtigen Tab an^^
	}

	@Override
	public void showTabLagerbuchung() {
		// TODO do sth
	}

	@Override
	public HashMap<Lager, JTextField> getHinzugefuegteLager() {
		return hinzugefuegteLager;
	}

	public static void setLagerListener(ActionListener l) {
		listener_Lagerverwaltung = l;
	}

	public static void setEinbuchungListener(ActionListener l) {
		listener_EinbuchungsAssi = l;
	}

	public static void setLieferungListener(MouseListener l) {
		listener_LieferungsUebersicht = l;
	}

	// ### Show & Hide Frames ###
	@Override
	public void showLagerverwaltung() {
		lagerverwaltung.setVisible(true);
	}

	@Override
	public void hideLagerverwaltung() {
		lagerverwaltung.setVisible(false);
	}

	// ### JTree neu aufbauen ###
	@Override
	public void refreshTree() {
		((DefaultTreeModel) lagerTree.getModel()).reload();
	}

	@Override
	public Lager getAusgewaehlterKnoten() {
		return (Lager) lagerTree.getLastSelectedPathComponent();
	}

	// ### Disabling clone() by throwing CloneNotSupportedException ###
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("This is a singelton, dude! No cloning at all!");
	}

}
