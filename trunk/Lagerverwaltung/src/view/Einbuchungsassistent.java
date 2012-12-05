package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;

import model.Lager;

public class Einbuchungsassistent extends JFrame {

	private static final long serialVersionUID = 9039036090326121805L;
	private static int anz_hinzugefuegterLager = 0;

	JPanel p_top, p_left, p_rigth, p_button;
	JLabel l_treeUeberschrift;
	JTextField gesamtmenge, prozentAnteil;
	JLabel lagerBezeichnung, anteilsMenge;
	JButton btn_bestaetigen, btn_abbruch;
	int kopfzeilenHoehe = 40;
	GridBagLayout gbl;

	public Einbuchungsassistent(EventListener listener) {
		super("Einbuchungsasstistent");
		this.setSize(550, 400);
		this.setLocation(300, 250);
		this.setResizable(true);
		this.setMinimumSize(new Dimension(500, 350)); 
		this.setDefaultCloseOperation(Einbuchungsassistent.DISPOSE_ON_CLOSE);		//Schließt das einzelne Frame, 
																						//aber nicht das gesamte Programm

		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());

		p_top = new JPanel();
		p_top.setSize(this.getWidth(), kopfzeilenHoehe);			//Breite wird wegen des BorderLayouts ignoriert!
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

		this.setVisible(true);

	}
	
	public void addLager(String name, ActionListener textField_listener)
	{
		/*
		 * Doppelte Einträge müssen noch verhindert werden
		 * 
		 * Erzeugte Textfelder in einer statischen ArrayList sammeln und diese per statischer Methode zugänglich machen.
		 * Dann kann aus der Handlerklasse auf die Methode und somit auf die Textfeldreferenzen zugegriffen werden
		 */
		
		Tools.addComponent(p_rigth, gbl, new JLabel(name), 0, 7 + anz_hinzugefuegterLager, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		Tools.addComponent(p_rigth, gbl, prozentAnteil = new JTextField("Prozentualer Anteil"), 1, 7 + anz_hinzugefuegterLager, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL);
		prozentAnteil.addActionListener(textField_listener);
		p_rigth.updateUI();
		anz_hinzugefuegterLager++;
	}
}
