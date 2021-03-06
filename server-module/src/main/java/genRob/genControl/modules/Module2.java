// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;

import  org.roblet.Unit;


/**
 * <SPAN style="color:blue">
 * Um ein <B>Modul</B> für den Roblet-Server
 * bereitzustellen, muß man diese Schnittstelle implementieren.&nbsp;
 * </SPAN>
 * Eine Implementierung wird <B>Modulklasse</B> genannt.&nbsp;
 * Eine Modulklasse stellt Einheiten für Roblets bereit, wobei der Server
 * dabei als Vermittler auftritt.&nbsp;
 * Diese Schnittstelle ersetzt in absehbarer Zeit {@link Module}.
 * <P>
 * Beim Start des Servers ist ihm als Kommandozeilenargument u.a. der Name
 * der gewünschten Modulklassen (einschließlich möglicher
 * {@code package}-Namen) anzugeben.&nbsp;
 * Der Server lädt dann diese Klassen und erzeugt über ihren parameterlosen
 * Konstruktor (der auch dann für eine Klasse vom Compiler erzeugt wurde,
 * wenn er nicht explizit definiert wurde) eine Instanz.&nbsp;
 * Diese Instanz stellt daraufhin das Modul innerhalb des Servers dar.&nbsp;
 * Es wird zur Laufzeit des Servers nur eine Instanz einer Modulklasse
 * pro angegebenen Modul bei den Startparametern erzeugt.
 * <P>
 * Der parameterlose Konstruktor der Modulklasse und statische
 * Initialisierer sollten nicht zu komplex sein.&nbsp;
 * Am besten man verwendet diese Möglichkeiten gar nicht.&nbsp;
 * Die eigentliche Initialisierung eines Moduls soll in
 * {@link #moduleInit(ModuleContext)} erfolgen.&nbsp;
 * Ausgaben kann ein Modul mit dem {@link Logger} machen.
 * <P>
 * Einheiten werden den Roblets unter Vermittlung des Roblet-Server vom Modul
 * mit Hilfe von {@link #getUnit(Class, RobletHandle)} bereitgestellt.&nbsp;
 * Weiter unten sind mehr Informationen zur Definition,
 * Implementierung und Instanziierung von Einheiten zu finden.
 * 
 * <H2>Lebenszyklus</H2>
 * Nach dem Laden der Modulklasse und erzeugen einer Instanz wird (einmal)
 * {@link #moduleInit(ModuleContext)} aufgerufen.&nbsp;
 * Wird hier eine Ausnahme geworfen, so wird der Server beendet.
 * <P>
 * Bevor der Server ein Roblet startet, wird
 * {@link #robletInit(RobletHandle)} aufgerufen;&nbsp;
 * nachdem es endete {@link #robletDone(RobletHandle)}.&nbsp;
 * Ausnahmen, die hierbei geworfen werden, werden nach Möglichkeit vom Server
 * ignoriert.
 * <P>
 * Fordert ein Roblet eine Einheiteninstanz an, so wird vom Server dazu die
 * Methode {@link #getUnit(Class, RobletHandle)} bemüht.
 * <P>
 * Sind keine Roblets mehr im Server vorhanden und will der Server enden,
 * so ruft er (einmal) {@link #moduleDone()} auf.&nbsp;
 * Ausnahmen, die hier geworfen werden, werden ebenso nach Möglichkeit vom
 * Server ignoriert.
 * 
 * <H2>Einheiten</H2>
 * Ein Modul soll zu von ihm verwalteten Einheiten<I>definitionen</I> auf
 * Anfrage durch den Roblet-Server aus der zugehörigen
 * Einheiten<I>implementierung</I> eine Einheiten<I>instanz</I> erzeugen.&nbsp;
 * Die Methode {@link #getUnit(Class, RobletHandle)} legt dabei jeweils den
 * Zusammenhang fest und gibt auf Anfrage Einheiteninstanzen zurück.
 * <P>
 * Wenn ein Roblet mit Hilfe einer Einheitendefinition eine Einheiteninstanz
 * anfordert, so ruft der Roblet-Server die Methode
 * {@link #getUnit(Class, RobletHandle)} auf und gibt das Resultat (nach
 * Umwandlung in eine Ersatzinstanz) an das Roblet zurück.
 * 
 * <H3>Einheitendefinition und -implementierungen</H3>
 * Einheiten<I>definitionen</I> müssen Schnittstellen ({@code interface}) sein,
 * die {@link org.roblet.Unit} erweitern ({@code extends}).&nbsp;
 * Eine Einheiten<I>implementierung</I> ist eine öffentliche Klasse
 * ({@code public class}), die eine Einheitendefinition implementiert
 * ({@code implements}).&nbsp;
 * Damit ist dann auch automatisch die Einheitenimplementierung vom Typ
 * {@link org.roblet.Unit}.
 * <P>
 * Einheiten sind dazu gedacht, vom Modul verwaltete Ressourcen zu
 * manipulieren.&nbsp;
 * Der Roblet-Server stellt durch dynamische, für Modul- und
 * Roblet-Entwickler transparente Erzeugung von Ersatzinstanzen sicher,
 * daß diese Ressource-Manipulation nicht zu Komplikationen führt, wenn der
 * Server den Ablauf des Roblets beeinflußt.&nbsp;
 * Die Ersatzinstanzen werden dem Roblet statt der Einheiteninstanzen des
 * Moduls zur Verfügung gestellt.
 * 
 * <H3>Manipulierende Rückgabewerte (und Parameter)</H3>
 * Wenn Methoden von Einheiten Werte zurückgeben, gilt es eine analoge
 * Problemstellung zu berücksichtigen.&nbsp;
 * Gibt eine solche Methode eine Instanz zurück, die nichts an den Ressourcen
 * manipuliert, so ist nichts weiter zu beachten.&nbsp;
 * <SPAN style="color:red">
 * Gibt aber eine Einheit eine Instanz zurück, die ihrerseits eine Ressource
 * manipulieren kann, so muß die Einheiten<I>definition</I> als Rückgabetyp
 * der jeweiligen Methode eine Schnittstelle vorsehen, die
 * {@link org.roblet.Unit} erweitert - also letztlich auch eine
 * Einheitendefinition.&nbsp;
 * </SPAN>
 * Auf diese Weise ist der Server in der Lage, auch für die genannten Instanzen
 * Ersatzinstanzen für das Roblet zu erzeugen und somit Gefahren zu
 * bannen.
 * <P>
 * Werden derartige Instanzen vom Roblet wieder an das Modul zurückübergeben,
 * so werden dem Modul jedoch die korrekten Originale überreicht.&nbsp;
 * Im Modul muß also auch für diesen Fall nichts weiter beachtet werden.
 * <P>
 * Die eben gemachten Betrachtungen für Rückgabewerte und Parameter, die
 * Ressourcen manipulieren können, gelten dann rekursiv die gleichen
 * Betrachtungen, wie für Einheiten.&nbsp;
 * Auch aus diesem Grund kann man auch hier der Einfachheit von
 * Einheiten reden.
 * 
 * <H2>Einschränkungen</H2>
 * 
 * <H3>Hash-Wert von Einheiten</H3>
 * Einheitenimplementierungen müssen darauf achten, daß sich der Hash-Wert
 * einer Einheiten<I>instanz</I> nicht ändert.&nbsp;
 * Hintergrund ist das o.g. Wandeln von Instanzen in Ersatzinstanzen und
 * umgekehrt.
 * <P>
 * Ausschließen kann man das, indem eine Einheitenimplementierung keine
 * Klasse erweitert und die eigenen Hash-Methoden nicht überschreibt
 * (vgl. {@link Object#hashCode()}).&nbsp;
 * Denn beispielsweise ändert sich der Hash-Wert einer
 * {@link java.util.ArrayList}, wenn Einträge hinzugefügt oder weggenommen
 * werden, da die Liste an sich ihren Hash-Wert über die Einträge berechnet.
 * 
 * <H3>Daten von Roblets</H3>
 * Ein Modul muß sicherstellen, daß es keine Instanzen, die in einem Roblet
 * erzeugt wurden, nach dessen Ende weiter aufbewahrt.&nbsp;
 * Anderenfalls kann ein Roblet nicht vollständig aufgeräumt werden, da der
 * zugehörige Klassenlader des Roblets möglicherweise noch in Verwendung
 * ist.&nbsp;
 * Gegebenenfalls müssen also nach Ende eines Roblets Kopien von benötigten
 * Daten im Modul erzeugt werden.
 * 
 * @see Logger
 * @see ModuleContext
 * @see RobletHandle
 * @author Hagen Stanek
 */
public interface  Module2
{

    /**
     * <B>Initialisiert ein Modul</B>.&nbsp;
     * Diese Methode wird vom Roblet-Server einmal aufgerufen,
     * nachdem die Modulklasse erfolgreich geladen und instanziiert worden
     * ist.&nbsp;
     * Bei Aufruf dieser Methode existieren noch keine Roblets.
     * <P>
     * Initialisierungen sollten hier erfolgen, anstatt in einem
     * parameterlosen Konstruktor oder in statischen Initialisierern.&nbsp;
     * In der Praxis hat sich bewährt, daß der Entwickler für die Modulklasse
     * gar keinen Konstruktor definiert und alle zu initialisierenden
     * Instanz-Variablen in der hier beschriebenen Methode erzeugt werden.
     * <P>
     * Wenn die Initialisierung aus irgendeinem Grunde fehlschlägt,
     * sollte eine Ausnahme mit einer informativen Nachricht geworfen
     * werden.&nbsp;
     * In einem solchen Fall wird der Server die Ausnahme ins Logbuch eintragen
     * und sich beenden.&nbsp;
     * Will ein Modul nicht, daß der Server beendet wird, so darf es keine
     * Ausnahme werfen, sondern muß später sicherstellen, daß keine Einheiten
     * angeboten werden.
     * <P>
     * Die Initialisierung kann prinzipiell beliebig lange erfolgen.&nbsp;
     * Über den {@link ModuleContext} kann auf den {@link Logger} zugegriffen
     * werden, um gegebenenfalls Informationen über den Stand der
     * Initialisierung auszugeben.&nbsp;
     * Der Server wird solange ein Modul für Roblets nicht verwenden, solange
     * es nicht initialisiert ist.&nbsp;
     * Er läßt sich solange auch nicht herunterfahren.
     * 
     * @param   context   Kontext des Moduls, der u.a. Zugang auf das
     *                          {@link Logger} ermöglicht
     * @throws  Exception   falls das Modul sich nicht korrekt initialisieren
     *                      konnte
     * @see     #moduleDone()
     * @see     #robletInit(RobletHandle)
     */
    public void  moduleInit (ModuleContext context)
        throws Exception;

    /**
     * Ermöglicht ein abschließendes <B>Aufräumen des Moduls</B>.&nbsp;
     * Diese Methode erlaubt dem Modul benutzte Resourcen freizugeben
     * bzw. in einen guten Zustand zurückzuversetzen.&nbsp;
     * Bevor ein Server endet, wird diese Methode einmal aufgerufen.&nbsp;
     * Es existieren zu diesem Zeitpunkt keine Roblets mehr.
     * <P>
     * Wenn hier ein Problem auftritt, so sollte dennoch ohne Ausnahme
     * zurückgekehrt werden.&nbsp;
     * Gegebenenfalls können Informationen über den Fehler über den
     * {@link Logger} ausgegeben werden.&nbsp;
     * Der Server ignoriert nach Möglichkeit geworfene Ausnahmen.
     * <P>
     * Das Aufräumen kann prinzipiell beliebig lange erfolgen.&nbsp;
     * Über den {@link ModuleContext} kann auf den {@link Logger} zugegriffen
     * werden, um gegebenenfalls Informationen über den Stand des Aufräumens
     * auszugeben.&nbsp;
     * Der Server wird nicht enden, solange ein Modul nicht fertig aufgeräumt ist.
     * 
     * @see     #moduleInit(ModuleContext)
     */
    public void  moduleDone ();

    /**
     * Ermöglicht <B>Vorbereitungen pro Roblet</B>.&nbsp;
     * Diese Methode wird vom Roblet-Server einmal aufgerufen,
     * <I>bevor</I> ein Roblet zum Laufen kommt.&nbsp;
     * Tritt hier eine Ausnahme auf, so wird der Server sie nach Möglichkeit
     * ignorieren.
     * <P>
     * <SPAN style="color:brown">
     * ACHTUNG:&nbsp;
     * Die Aktivitäten in dieser Methode sollten <I>von kurzer Dauer</I> sein,
     * da sie die Startzeit für jedes Roblet betreffen.&nbsp;
     * Der Server wird solange ein Roblet nicht laufenlassen, wie diese
     * Routine nicht beendet ist.
     * </SPAN>
     * <P>
     * Gewöhnlich wird das übergebene {@link RobletHandle} benutzt, um darin
     * per {@link RobletHandle#put(Object, Object)} einen Eintrag zu machen,
     * den das Modul dann bei späteren Aurufen für das Roblet per
     * {@link RobletHandle#get(Object)} wiedererkennen kann.
     * 
     * @param  handle  Repräsentant des neuen Roblets
     * @see     #robletDone(RobletHandle)
     * @see     #moduleInit(ModuleContext)
     */
    public void  robletInit (RobletHandle handle);

    /**
     * Ermöglicht <B>Aufräumarbeiten pro Roblet</B>.&nbsp;
     * Diese Methode wird vom Roblet-Server einmal aufgerufen,
     * <I>nachdem</I> ein Roblet endete.&nbsp;
     * Tritt ein Ausnahme auf, so wird der Server sie nach Möglichkeit
     * ignorieren.
     * <P>
     * <SPAN style="color:brown">
     * ACHTUNG:&nbsp;
     * Die Aktivitäten in dieser Methode sollten <I>von kurzer Dauer</I> sein,
     * da sie die Startzeit für jedes nachfolgende Roblet betreffen.&nbsp;
     * Der Server wird mindestens solange kein neues Roblet laufenlassen, wie
     * diese Routine nicht beendet ist.
     * </SPAN>
     * 
     * @param  handle  Repräsentant des beendeten Roblets
     * @see     #robletDone(RobletHandle)
     * @see     #moduleInit(ModuleContext)
     */
    public void  robletDone (RobletHandle handle);

    /**
     * Muß für ein Roblet eine Einheiteninstanz passend zur angegebenen
     * Einheitendefinition zurückgeben.&nbsp;
     * Wird {@code null} zurückgegeben, so befragt der Server das nächste
     * Modul.
     * <P>
     * Diese Methode wird pro Roblet unter Umständen mehrfach pro
     * Einheitendefinition aufgerufen, jedoch betreibt ein Roblet-Server pro
     * Roblet auch Caching.&nbsp;
     * Fordert ein Roblet eine Einheit mehrfach an, obwohl noch eine Referenz
     * auf diese existiert, so gibt der Server automatisch die an das Roblet
     * zurück, die beim letzten Mal zurückgegeben wurde, ohne seinerseits noch
     * einmal das Modul zu konsultieren.
     * <P>
     * <SPAN style="color:brown">
     * ACHTUNG:&nbsp;
     * Der übergebene Repräsentant des Roblets sollte nicht innerhalb des
     * Moduls vermerkt werden.
     * </SPAN>
     * 
     * @param clazz  Einheitendefinition (Schnittstelle der Einheit)
     * @param handle  Repräsentant eines Roblets
     * @return  Einheiteninstanz oder {@code null}, wenn keine Implementierung
     *          zur Definition vom Modul zur Verfügung gestellt wird
     */
    @SuppressWarnings("rawtypes")
    public Unit  getUnit (Class clazz, RobletHandle handle);

}
