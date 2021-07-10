// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * <SPAN style="color:darkgrey">
 * Eine Instanz diesen Typs dient einem Modul zum <B>Loggen</B>
 * per Roblet-Server.
 * </SPAN>
 * <P>
 * Über {@link Supervisor#getLog()} kann sich das Module eine Instanz
 * zur Laufzeit besorgen.
 * 
 * @deprecated  Fades away along with {@link Module}
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Log
{

    /**
     * Erzeuge Log-Eintrag mit optionalem Teil, Typ und Nachricht.&nbsp;
     * Teil und Nachricht werden für die weitere Verarbeitung
     * per {@link java.lang.Object#toString()} auf den Type
     * {@link java.lang.String} gewandelt.
     * <P>
     * Einfache Module können als Teil einfach {@code null} übergeben.
     * <P>
     * Bei Aufruf durch eine Instanz-Methode kann bei Instanz-Typ einfach
     * {@code getClass()} angegeben werden.&nbsp;
     * Ansonsten kann auch die Notation <I>Klasse{@code .class}</I> verwendet
     * werden.
     * <P>
     * Die Nachricht wird per {@code toString()} in eine Instanz vom Typ
     * {@link java.lang.String} umgewandelt.
     * 
     * @param oPart  Teil des Moduls oder {@code  null}
     * @param rClass  Typ der Instanz, wo der Fehler auftrat, oder {@code null}
     * @param oMessage  Nachricht oder {@code null}
     */
    @SuppressWarnings("rawtypes")
    void  out (Object oPart, Class rClass, Object oMessage);

}
