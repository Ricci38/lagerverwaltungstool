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
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import model.Lager;

@Deprecated
public class Lagerverwaltung extends JFrame {

	private static final long serialVersionUID = 306848107804623710L;

	JPanel p_top, p_top_sub_top, p_top_sub_bottom, p_tree, p_center, p_platzhalter1, p_platzhalter2;
	JButton redo, undo, buchen, buchungsuebersicht, lagersaldo, lieferungFuerLager, neuesLager;
	JLabel l_titel, menue_beschriftung;
	static JTree lagerTree; // XXX: Trennung MVC?

	Lagerverwaltung(ActionListener listener) {
		super("Lagerverwaltung");
		this.setDefaultCloseOperation(Lagerverwaltung.EXIT_ON_CLOSE); // XXX: Soll das so bleiben? Sonst: Zuerst speichern, dann exit(0) - o. Ä.
		this.setSize(1200, 800);
		this.setMinimumSize(new Dimension(900, 500));
		this.setLocation(100, 50);
		this.setResizable(true); // Fenstergröße ist variabel XXX: soll das so bleiben?

		Container c = this.getContentPane();
		c.setLayout(new BorderLayout(10, 10)); // in Klammer kann der Freiraum zwischen den einzelnen Elementen angegeben werden

		// ### Panel für die undo redo Buttons ###
		// Panel dient als Container für zwei Subpanels
		p_top = new JPanel();
		p_top.setLayout(new GridLayout(2, 1)); // Tabellenlayout um die beiden Panels untereinander zu stapeln
		p_top.setSize(this.getWidth(), 80);
		p_top_sub_top = new JPanel();
		p_top_sub_top.setBackground(Color.RED);
		l_titel = new JLabel("Lagerverwaltungstool v1.0");
		l_titel.setFont(new Font("Helvetica", Font.BOLD, 18));
		p_top_sub_top.add(l_titel);

		p_top_sub_bottom = new JPanel();
		p_top_sub_bottom.setBackground(Color.CYAN);
		GridBagLayout gbl2 = new GridBagLayout();
		p_top_sub_bottom.setLayout(gbl2);

		p_platzhalter1 = new JPanel();
		p_platzhalter1.setBackground(Color.BLACK);

		p_platzhalter2 = new JPanel();
		p_platzhalter2.setBackground(Color.BLACK);

		undo = new JButton("undo");
		redo = new JButton("redo");
		buchen = new JButton("Neue Buchung");
		neuesLager = new JButton("Neues Lager");
		buchungsuebersicht = new JButton("Lieferungsübersicht");
		lagersaldo = new JButton("Lagersaldo");
		lieferungFuerLager = new JButton("Lagerübersicht");

		// ### Actionlistener bekannt machen ### FIXME Listener :)
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

		// ### Komponenten dem unteren Unterpanel hinzufügen ###
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
		p_tree.setPreferredSize(new Dimension(250, 50)); // Breite des Trees festlegen. Höhe wird wegen des BorderLayouts ignoriert!

		JScrollPane scrollBar = new JScrollPane(lagerTree);
		p_tree.add(scrollBar);

		// ### Menüauswahl im CENTER ###
		p_center = new JPanel();
		p_center.setBackground(Color.GREEN);
		GridBagLayout gbl = new GridBagLayout();
		p_center.setLayout(gbl);

		c.add(p_tree, BorderLayout.WEST);
		c.add(p_top, BorderLayout.NORTH);
		c.add(p_center, BorderLayout.CENTER);

		this.setVisible(true); // TODO: nicht sicher, ob wir das lieber an anderer Stelle machen sollten - gleiches gilt für die anderen Oberflächen

	}

	// ### Anzeige des Trees aktualisieren ###
	public static void TreeRefresh() {
		/*
		 * Refresh kann auch auf einzelne Knoten gelegt werden -> bessere
		 * Performance und es würden sich nicht alle Knoten immer zuklappen
		 */
		((DefaultTreeModel) lagerTree.getModel()).reload();
	}

	public static Lager getAusgewaehlterKnoten() {
		Lager pfad = (Lager) lagerTree.getLastSelectedPathComponent();

		return pfad;
	}

}
