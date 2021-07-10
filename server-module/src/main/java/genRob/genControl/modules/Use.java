// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * <SPAN style="color:darkgrey">
 * Instanzen dieser Klasse repräsentieren die momentane Nutzung eines
 * Moduls durch ein bestimmtes Roblet und werden <B>Nutzungszähler</B>
 * genannt.&nbsp;
 * </SPAN>
 * Derartiges wird benötigt, damit der Roblet-Server sicherstellen kann, daß
 * kein Thread eines bestimmten Roblets sich mehr in einem Modul befindet.
 * <P>
 * Roblets treten in Module nur über Methoden von Einheiten-Instanzen oder
 * anderer Instanzen des Moduls ein.&nbsp;
 * Diese Tatsache muß mitgezählt werden.&nbsp;
 * Bei jedem Eintritt muß daher der entsprechende Nutzungszähler erhöht und vor
 * jedem Austritt wieder erniedrigt werden.&nbsp;
 * Dabei muß sichergestellt werden, daß die Erniedrigung auch wirklich
 * stattfindet und nicht etwa durch eine geworfene Ausnahme entfällt.
 * <P>
 * Folgende Sequenz sichert derartige Anforderungen und sollte in jeder
 * Methode jeder Instanz des Moduls, in die ein Roblet eintreten kann,
 * verwendete werden:
 * <BLOCKQUOTE><PRE>
 * class  ClassInTheModuleWithInstancesUsedInTheRoblet
 * {
 *     ...
 *     private Use  m_rUse;  // Instanz von 'getUnit4Slot'
 *     ...
 *     public void  someUnitMethodImplementation (WhateverParameter p)
 *         throws SomeException
 *     {
 *         m_rUse. raise ();  // Erhöhe den Nutzungszähler
 * 
 *         try
 *         {
 *             // die eigentlichen Ausführungen hier
 *         }
 *         finally           // Nachfolgendes wird in jedem Falle ausgeführt 
 *         {
 *             m_rUse. lower (); // Erniedrige den Nutzungszähler
 *         }
 *     }
 * }
 *     ...
 * </PRE></BLOCKQUOTE>
 * 
 * @deprecated  Fades away along with {@link Module}
 * @see Module
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public class  Use
{

    /**
     * Enthält den augenblicklichen 'Halt'-Zustand.
     */
    private boolean  m_bHalted = false;

    /**
     * Gibt an, ob ein Roblet demnächst angehalten wird und daher alle
     * Threads des Roblets das Modul verlassen sollen.
     * <P>
     * Gedacht ist diese Methode zur Verwendung in Methoden, deren Verhalten
     * darin besteht, einen eintretenden (Roblet-)Thread solange anzuhalten,
     * bis irgendein gewisses Ereignis eintritt.&nbsp;
     * Mit Hilfe der nachfolgenden Sequenz lassen sich Warteschleifen derart
     * implementieren, daß Roblets sicher vom Roblet-Server beendet werden
     * können.
     * <BLOCKQUOTE><PRE>
     * ...
     * m_rUse. raise ();
     * try {
     *     // Wiederhole, solange ein gewisses Kriterium erfüllt ist
     *     while (...)
     *     {
     *         // Warte für eine halbe Sekunde
     *         wait (500);
     *         // Soll der Thread das Modul verlassen?
     *         if (m_rUse. isHalted ())
     *             // JA.
     *             throw new RuntimeException ("Roblet halted.");
     *     }
     *     ...
     * } finally {
     *     m_rUse. lower ();
     * }
     * </PRE></BLOCKQUOTE>
     * 
     * @return {@code true}, wenn alle Roblet-Threads das Modul verlassen
     *          sollen
     */
    public boolean  isHalted ()
    {
        return m_bHalted;
    }

    /**
     * Anzahl Threads, die gerade im Modul sind.
     */
    private int  m_iInside = 0;

    /**
     * <B>Unterdrücke</B> weitere Eintritte von Threads in das Modul bzw.
     * genauer in die Methoden der Instanzen und <B>warte</B>, bis keine
     * Threads mehr im Modul sind.&nbsp;
     * Jeder weitere Aufruf von {@link #raise()} wird von nun an eine
     * {@link java.lang.RuntimeException} auslösen.
     * <P>
     * <I>
     *  Diese Methode ist ausschließlich für den Roblet-Server reserviert.
     * </I>
     */
    public synchronized void  halt ()
    {
        // Hisse Halt-Fahne
        m_bHalted = true;

        // Prüfe, ob noch ein Thread im Modul ist
        while (m_iInside > 0)    // Schleife gegen Spurious-Wakeups
            // Ja, ...
            try
            {
                // Warte, daß dieser Monitor gesetzt wird
                // ('synchronized' verhindert, daß das nach der Prüfung
                //  geschehen konnte)
                wait ();
            }
            catch (InterruptedException ie)
            {
                // Sollte nie passieren, da es sich um einen Thread des
                // Roblet-Servers handelt.
                // Aber falls es doch passiert ...
                ie. printStackTrace ();
            }
    }

    /**
     * <B>Prüfe</B>, ob ein Eintritt in das Modul bzw. genauer die Methode der
     * Instanz erlaubt ist und werfe eine Ausnahme, wenn das nicht der Fall ist.
     * Ansonsten <B>erhöhe</B> den Nutzungszähler.&nbsp;
     * Diese Methode muß als erstes in einer Methoden-Implementierung einer
     * jeden von Roblets benutzen Instanz aufgerufen werden.
     * <P>
     * Es muß dann sichergestellt werden, daß {@link #lower()}
     * vor Austritt aus derselben Methode wieder aufgerufen wird.
     * 
     * @throws  RuntimeException    falls der Zugang zum Modul momentan nicht
     *                              erlaubt ist
     */
    public synchronized void  raise ()
    {
        // Prüfe, ob die Halt-Fahne gehisst wurde
        if (m_bHalted)
            // Zugang ist nicht erlaubt - werfe Ausnahme
            throw new RuntimeException ("Unit halted");

        // ansonsten erhöhe den Nutzungszähler
        m_iInside++;
    }

    /**
     * <B>Erniedrigt</B> den Nutzungszähler und <B>informiert</B> ein
     * möglicherweise wartendenden Roblet-Server, für den Fall, daß der Zähler
     * auf Null ging.&nbsp;
     * Diese Methode muß vor Austritt einer jeden Methode, in der beim
     * Eintritt {@link #raise()} aufgerufen wurde, aufgerufen werden.&nbsp;
     * Das darf nie ausgelassen werden.
     * <P>
     * Die Anzahl der Aufrufe dieser Methode muß genau mit der Anzahl der
     * Aufrufe von {@link #raise()} übereinstimmen.&nbsp;
     * Ist der Zähler auf Null gegangen, so bedeutet das, daß kein Thread mehr
     * im Modul ist.
     */
    public synchronized void  lower ()
    {
        // Erniedrige den Nutzungszähler
        m_iInside--;

        // Wenn der Zähler auf Null geht ...
        if (m_iInside == 0)
            // ... dann kann ein ausstehendes 'Halt' aufgelöst werden
            notify ();
    }

}
