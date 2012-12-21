package exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Eigene Exception, die bei Fehlern, die während der Laufzeit entstehen,
 * geworfen und vom Aufrufer verarbeitet wird.
 * 
 * @version 1.1.0
 * @author Philo Könneker
 * 
 */
public class LagerverwaltungsException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 141161380211463941L;

	private List<String> result;

	public LagerverwaltungsException(String message, List<String> result, Exception ex) {
		super(message, ex);
		this.result = result;
	}

	/**
	 * Gibt eine List<String> zurück und filtert vorher alle doppelten Einträge
	 * heraus.
	 * 
	 * @return Liste aller aufgetretenen Fehler, bereinigt um Duplikate
	 */
	public List<String> getResult() {
		Map<String, String> singleResult = new HashMap<String, String>(this.result.size());
		for (String s : this.result) {
			if (!singleResult.containsKey(s)) {
				singleResult.put(s, s);
			} else
				this.result.remove(s);
		}

		return this.result;
	}

	public void setResult(List<String> result) {
		this.result = result;
	}

	/**
	 * Gibt eine Zeichenkette zurück, bei der jede Nachricht in einer neuen
	 * Zeile ist.
	 * 
	 * @return msg Alle Nachrichten der geworfenen Exception
	 */
	@Override
	public String getMessage() {
		String msg = super.getMessage();
		for (String s : result) {
			msg += "\n" + s;
		}
		return msg;
	}

}
