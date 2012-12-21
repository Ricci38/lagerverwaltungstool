package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Lieferung {
	private final Date lieferungsDatum;
	private final List<Buchung> buchungen;
	private final int gesamtMenge;
	private final String lieferungsTyp;
	private static List<Lieferung> lieferungen = new ArrayList<Lieferung>();

	/**
	 * Erstellt eine neue Lieferung und fügt diese automatisch der Liste aller
	 * Lieferungen hinzu.
	 * 
	 * @param d
	 *            Das exakte Datum dieser Lieferung
	 * @param gesamtMenge
	 *            Die kummulierte Menge aller Buchungen für diese Lieferung
	 * @param typ
	 *            Buchungsart, z.B. "Zubuchung", "Abbuchung"
	 * @param b
	 *            Liste mit allen Buchungen, die zu dieser Lieferung gehören
	 */
	public Lieferung(Date d, int gesamtMenge, String typ, List<Buchung> b) {
		this.lieferungsDatum = d;
		this.gesamtMenge = gesamtMenge;
		this.lieferungsTyp = typ;
		this.buchungen = new ArrayList<Buchung>(b);
		lieferungen.add(this);
	}

	/**
	 * Gibt eine List<Buchung> mit allen Buchungen dieser Lieferung zurück.
	 * 
	 * @return Die Liste aller Buchungen dieser Lieferung
	 */
	public List<Buchung> getBuchungen() {
		return buchungen;
	}

	/**
	 * Gibt die Kummulierte Menge aller Buchungen aus dieser Lieferung zurück.
	 * 
	 * @return die gesamte Menge dieser Lieferung
	 */
	public int getGesamtMenge() {
		return gesamtMenge;
	}

	/**
	 * Gibt das genaue Datum der Lieferung zurück
	 * 
	 * @return Das Lieferdatum
	 */
	public Date getLieferungsDatum() {
		return this.lieferungsDatum;
	}

	/**
	 * FIXME Rückgabetyp auf boolean ändern? (--> return lieferungen.add(l) !)
	 * 
	 * Fügt eine neue Lieferung der Liste aller Lieferungen hinzu und gibt die
	 * neue Liste anschließend zurück.
	 * 
	 * @param l
	 *            Die hinzuzufügende Lieferung
	 * @return Die neue Liste aller Lieferungen
	 */
	public static List<Lieferung> addLieferungen(Lieferung l) {
		lieferungen.add(l);
		return lieferungen;
	}

	/**
	 * Liefert eine List<Lieferung> mit allen bereits getätigten Lieferungen und
	 * den darin enthalteten Buchungen zurück. Dabei wird nicht zwischen Zu- und
	 * Abbuchung unterschieden.
	 * 
	 * @return Liste aller Lieferungen
	 */
	public static List<Lieferung> getAllLieferungen() {
		return lieferungen;
	}

	/**
	 * Da die Lieferungen eindeutig an ihrem Datum identifiziert werden können,
	 * kann damit die entsprechende Lieferung gefunden und zurückgegeben werden.
	 * 
	 * @param date
	 *            Datum im Format "dd.MM.yyyy - hh:mm:ss"
	 * @return Die passende Lieferung zu dem Datum date
	 */
	public static Lieferung getLieferung(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
		for (Lieferung l : lieferungen) {
			if (date.equals(sdf.format(l.getLieferungsDatum())))
				return l;
		}
		return null;
	}

	/**
	 * Liefert den Typ der Lieferung zurück. Bsp.: Zubuchung oder Abbuchung
	 * 
	 * @return Typ der Lieferung
	 */
	public String getLieferungsTyp() {
		return lieferungsTyp;
	}
}
