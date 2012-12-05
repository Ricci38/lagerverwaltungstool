package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class GUI_tools {
	
	final static Insets insets = new Insets(5,5,5,5);

	public static void addComponent(Container cont, GridBagLayout gbl, Component c, int x, int y, int width, int height, double weightx, double weighty,
			int gbcFill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = insets; // Platzhalter zu den Seiten
		gbc.fill = gbcFill;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		cont.add(c);
	}

}
