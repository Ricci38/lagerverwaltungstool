package controller.befehle;

import java.util.Date;

import model.Lager;

/**
 * @version 1.1.0
 * @author Dominik Klüter
 *
 */
public interface IBuchungBefehl {
	
	public int execute(Lager l, int menge, Date d, int prozent);

	public void undo();

	public void redo();

	public void undoAll();

	public void clearAll();

	public void clearRedos();

	public boolean hasRemainingUndos();

	public boolean hasRemainingRedos();

}