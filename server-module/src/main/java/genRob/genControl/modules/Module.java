// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;

import  org.roblet.Unit;


/**
 * <SPAN style="color:darkgrey">
 * Um ein <B>Modul</B> für den Roblet-Server bereitzustellen, muß
 * man diesen Typ implementieren.&nbsp;
 * </SPAN>
 * Eine solche Implementierung kann dann Einheiten für Roblets
 * über den Server bereitstellen.&nbsp;
 * Der Server erzeugt zur Laufzeit eine Instanz der Modulklasse.
 * <P>
 * Beim Start des Servers ist der Name Module-Klasse
 * (einschließlich möglicher {@code package}-Namen)
 * anzugeben.&nbsp;
 * Der Server lädt dann die Modul-Klasse und erzeugt über den parameterlosen
 * Konstruktor (der auch dann für eine Klasse vom Compiler erzeugt wurde,
 * wenn er nicht definiert wurde) eine Instanz.&nbsp;
 * Diese Instanz stellt daraufhin das Modul innerhalb des Servers dar.
 * <P>
 * Es wird zur Laufzeit des Servers nur eine Instanz
 * (pro angegebenen Modul bei den Startparametern) erzeugt.
 * <P>
 * Der Standard-Konstruktor der Modul-Klasse und
 * {@code static}-Initialisierer sollte nicht zu komplex sein.&nbsp;
 * Die eigentliche Initialisierung eines Moduls soll in
 * {@link #moduleInit(Supervisor,Use)} erfolgen.
 * <p>
 * Ausgaben kann ein Modul mit dem {@link Log} machen.
 * 
 * @deprecated  <SPAN style="color:darkgrey">Wird vollständig von
 *              {@link Module2} ersetzt.</SPAN>
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Module
{

    // +++   Modul-bezogen   +++

    /**
     * <B>Initialisiert</B> ein Modul.&nbsp;
     * Diese Methode wird vom Roblet-Server einmal aufgerufen,
     * nachdem die Modul-Klasse erfolgreich instanziiert worden ist.
     * <P>
     * Initialisierungen sollten hier erfolgen, anstatt in einem
     * argumentlosen Konstruktor oder statischen Initialisierern.&nbsp;
     * Bei Aufruf dieser Methode existieren noch keine Roblets.&nbsp;
     * In der Praxis hat sich bewährt, daß die Modul-Klasse
     * gar keinen Konstruktor hat und alle zu initialisierenden
     * Instanz-Variablen in dieser Methode erzeugt werden.
     * <P>
     * Wenn die Initialisierung aus irgendeinem Grunde fehlschlägt,
     * sollte eine Ausnahme mit einer informativen Nachricht geworfen
     * werden.&nbsp;
     * In einem solchen Fall wird der Server enden und
     * die Ausnahme dem Administrator präsentieren.
     * <P>
     * Die Initialisierung kann prinzipiell beliebig lange erfolgen.&nbsp;
     * Über den {@link Supervisor} kann auf das {@link Log} zugegriffen werden,
     * um gegebenenfalls Informationen über den Stand auszugeben.
     * 
     * @param   supervisor   Verwalter der Module
     *                          der u.a. Zugang auf das {@link Log}
     *                          ermöglicht
     * @param   use         &lt;nicht mehr benutzt&gt;
     * @throws  Exception   falls das Modul sich nicht korrekt initialisieren
     *                      kann
     * @see     #moduleDone()
     */
    public void  moduleInit (Supervisor supervisor, Use use)
        throws Exception;

    /**
     * Ermöglicht ein abschließendes
     * <B>Aufräumen</B> eines Modules.&nbsp;
     * Bevor ein Server endet, wird für alle Module diese
     * Methode einmal aufgerufen.&nbsp;
     * Bei Aufruf dieser Routine existieren keine Roblets mehr.
     * <P>
     * Diese Methode erlaubt dem Modul benutzte Resourcen freizugeben
     * bzw. in einen guten Zustand zurückzuversetzen.
     * <P>
     * Wenn hier ein Problem auftritt, so sollte dennoch ohne Ausnahme
     * zurückgekehrt werden,
     * um anderen Modulen das Aufräumen zu ermöglichen.
     * Gegebenenfalls sind Informationen über den Fehler über das {@link Log}
     * auszugeben.
     * <P>
     * Das Aufräumen kann prinzipiell beliebig lange erfolgen.&nbsp;
     * Gegebenenfalls sind Informationen über den Stand über das {@link Log}
     * auszugeben.
     * 
     * @see     #moduleInit(Supervisor,Use)
     */
    public void  moduleDone ();


    /**
     * @param   clazz   &lt;unbenutzt&gt;
     * @return  ...
     * @deprecated  Wird nicht mehr benutzt.
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public Unit  getUnit (Class clazz);


    /**
     * Gibt eine <B>Einheiten-Instanz</B> heraus.&nbsp;
     * Es handelt sich dabei um die Instanz der Implementierung
     * zur angegebenen Einheiten-Definition.&nbsp;
     * Wenn ein Roblet um eine Einheiten-Instanz bittet, so wird vom
     * Server bei allen Modulen nacheinander einmal diese Methode aufgerufen,
     * bis es eine Instanz, d.h. nicht {@code null}, erhält.
     * <P>
     * Jedes Roblet, d.h. Instanz einer Roblet-Klasse, benötigt eine eigene
     * Instanz der Einheiten-Implementierung.&nbsp;
     * Pro Roblet ist dabei der Nutzungszähler eindeutig.
     * <P>
     * Die zurückgegebene Einheit muß den als Parameter
     * übergebenen <I>Nutzungszähler</I> benutzen, damit
     * der Server gegebenenfalls feststellen kann, wann der
     * letzte Thread des Roblets das Modul verlassen hat.&nbsp;
     * ({@code try}-{@code finally}-Mechanismus)
     * <P>
     * Diese Methode sollte schnell zurückkehren, da u.a. zwischenzeitlich
     * das anfragende Roblet nicht beendet werden kann.
     * <P>
     * Ausnahmen dürfen hier nicht zurückgegeben werden - einzig {@code null}
     * als Behelf.&nbsp;
     * Probleme mit Ressourcen sind immer durch die Einheiten-Implementierung
     * zu melden - nie an dieser Stelle.
     * 
     * @param   clazz      Einheiten-Definition, zu dem die Einheiten-Instanz
     *                      benötigt wird
     * @param   use        Nutzungszähler von der zurückgegebenen
     *                      Einheiten-Instanz zu benutzen ist
     * @param   slot       Fach, in dem das Roblet läuft, welches
     *                      die Einheit angefordert hat
     * @return  Instanz der Implementierung zum angegebenen Einheiten-Definition
     *          oder {@code null}, falls das Modul zur Definition keine
     *          Implementierung hat
     * @see     #resetUnit4Slot(Unit)
     */
    @SuppressWarnings("rawtypes")
    public Unit  getUnit4Slot (Class clazz, Use use, Slot slot);

    /**
     * Soll die Ressourcen einer Einheiten-Instanz <B>zurücksetzen</B>.&nbsp;
     * Bei Aufruf dieser Methode durch den Roblet-Server existiert das
     * Roblet, welches die übergebene Einheiten-Instanz einmal bekommen hat,
     * nicht mehr.
     * <P>
     * Diese Methode muß {@code true} ergeben, wenn dem Modul die
     * übergebene Instanz bekannt war.&nbsp;
     * Dadurch weiß dann der Server, daß keine weiteren Module zu befragen sind.
     * <P>
     * Diese Methode kann verzögert zurückkehren, aber es ist zu beachten, daß
     * zwischenzeitlich kein neues Roblet im gleichen Fach zum Laufen kommen
     * kann.
     * <P>
     * Ausnahmen dürfen hier nicht zurückgegeben werden.
     * 
     * @param   unit     Einheiten-Instanz, die zurückzusetzen ist
     * @return  {@code true} wenn die Instanz von diesem Modul stammt
     * @see     #getUnit4Slot(Class,Use,Slot)
     */
    public boolean  resetUnit4Slot (Unit unit);

    /**
     * An dieser Stelle kann ein moduleigenes Grundbuch zurückgegeben
     * werden.
     * <BLOCKQUOTE>
     * <B>Achtung:</B>&nbsp;
     * Das Grundbuchkonzept wird in Zukunft entfallen und sollte deshalb
     * für neue Module nicht mehr verwendet werden.&nbsp;
     * Diese Funktion sollte {@code null} zurückliefern, wenn das
     * Grundbuchkonzept nicht benutzt wird.
     * </BLOCKQUOTE>
     * @return  Grundbuch
     * @deprecated  Wird zukünftig entfallen und sollte {@code null} zurückgeben.
     */
    @Deprecated
    public Registry  getRegistry ();

}
