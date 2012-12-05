package gui;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class Lieferungsuebersicht extends JFrame {

	private static final long serialVersionUID = 3073425246104436942L;

	JPanel p_top, p_left, p_rigth;
	JTable tbl_buchungsdetails;

	Lieferungsuebersicht() {
		super("Lieferungsübersicht");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // FIXME: Muss definitiv
														// geändert werden -
														// Anwendung schließt
														// sich beim Schließen
														// dieses Fensters sonst
														// vollständig
		this.setSize(800, 500);
		this.setLocation(300, 100);
		// this.setResizable(false); //XXX: Ändern?
		Container c = this.getContentPane();
		c.setLayout(null);

		p_top = new JPanel();
		p_top.setLocation(0, 0);
		p_top.setSize(this.getWidth(), 50);
		p_top.setBackground(Color.ORANGE);

		p_left = new JPanel();
		p_left.setLayout(null);
		p_left.setLocation(0, 50);
		p_left.setSize(200, this.getHeight() - p_top.getHeight() - 40);

		JTree lagerTree = new JTree(new DefaultMutableTreeNode("root"));
		JScrollPane scrollBar = new JScrollPane(lagerTree);
		scrollBar.setSize(p_left.getWidth(), p_left.getHeight());
		p_left.add(scrollBar);

		p_rigth = new JPanel();
		// p_rigth.setLayout(null);
		p_rigth.setBackground(Color.magenta);
		p_rigth.setLocation(p_left.getWidth(), 50);
		p_rigth.setSize(this.getWidth() - p_left.getWidth() - 25, this.getHeight() - p_top.getHeight() - 40);

		tbl_buchungsdetails = new JTable(2, 6);
		tbl_buchungsdetails.setLocation(p_left.getWidth() + 50, p_top.getHeight() + 80);

		p_rigth.add(tbl_buchungsdetails);

		c.add(p_top);
		c.add(p_left);
		c.add(p_rigth);
		this.setVisible(true);

	}

}
