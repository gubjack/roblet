// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client;

import  org.roblet.Roblet;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;


/**
 * Eine Instanz dieser Klasse stellt ein <b>Fach</b>
 * auf einem Roblet-Server dar (Fach-Repräsentanz).
 * <p>
 * Ein Fach ermöglicht das Platzieren und Verwalten von Roblets.&nbsp;
 * Dabei kann zu jedem Zeitpunkt höchstens ein Roblet pro Fach platziert
 * sein.
 * <p>
 * Verwaltung des Roblets meint im wesentlichen das <i>vorzeitige
 * Beenden</i> und die <I>Kommunikation mit seiner Anwendung</I>.&nbsp;
 * Ein Roblet kann vermittels der Methode {@link #run(Roblet)} beendet
 * werden.&nbsp;
 * Die Kommunikation kann per <I>fernen Instanzen</I> mit den Methoden
 * {@link #offerRemote(Object)}, {@link #revokeRemote(Object)} und
 * {@link #obtainProxy(Class)} erfolgen.
 * <P>
 * Ein Roblet kann durch eine Anwendung direkt vermittels
 * {@link #run(Roblet)} beendet werden,
 * wenn sie es über eine Instanz des hier beschriebenen Typs
 * an das zugehörige Fach des Servers geschickt hat.
 * 
 * @see  Server
 * @author Hagen Stanek
 */
@Deprecated
public interface  Slot
{

    public void  close ();
    public void  waitClosed ()
        throws InterruptedException;

    /**
     * <B>Platziert</B> eine Roblet-Instanz in diesem Fach des Roblet-Servers,
     * der es dann laufen läßt.&nbsp;
     * Diese Aktion ist mit Netzwerkaktivität verbunden und kann daher
     * unbestimmte Zeit in Anspruch nehmen.&nbsp;
     * Es wird also <I>nicht</I> mit einem <I>timeout</I> gearbeitet, jedoch der
     * Thread, der diese Methode aufruft, kann jederzeit unterbrochen werden.
     * <p>
     * Es kann nur immer je ein Roblet zu einem jeden Zeitpunkt in diesem Fach
     * laufen.&nbsp;
     * Wird eine weiteres Roblet-Instanz oder {@code null} vermittels
     * dieser Methode an dieses Fach geschickt,
     * so wird das bereits laufende Roblet vorher durch den Server abgebrochen.
     * <p>
     * Die dieser Methode übergebene Roblet-Instanz wird für den Transport
     * serialisiert und zum Server geschickt.&nbsp;
     * Aus diesem Grund muß die Roblet-Klasse den Typ
     * {@link java.io.Serializable} implementieren.
     * <p>
     * Wird statt einer Roblet-Instanz {@code null} übergeben,
     * so wird auch {@code null} zurückgegeben.
     * <p>
     * Ist die Roblet-Instanz nicht {@code null}, so wird vom
     * Roblet-Server dessen Methode
     * {@link Roblet#execute(org.roblet.Robot)} ausgeführt.&nbsp;
     * Nach deren Ende wird ihr Rückgabewert hier zurückgegeben.
     * <p>
     * Wird im Hauptthread, welcher auf Seiten des Roblet-Servers
     * {@link Roblet#execute(org.roblet.Robot)} ausführt, eine Ausnahme
     * abgeleitet von {@link Exception} geworfen, so wird sie an dieser Stelle
     * auch geworfen.&nbsp;
     * Schließt man diese Methode demnach in einen
     * {@code try}-Block ein, so kann man im {@code catch}-Block
     * direkt die selbst erzeugten Ausnahmen abfangen.
     * <p>
     * Wurde eine Ausnahme, abgeleitet von {@link Exception},
     * in einem beliebigen anderen Thread des Roblets erzeugt
     * (und nicht abgefangen) während der Hauptthread
     * des Roblets noch lief, so wird diese entsprechend hier geworfen
     * (während das Roblet bereits vom Server beendet wurde).
     * <p>
     * Entsteht im Hauptthread oder einem beliebigen anderen eine
     * Ausnahme, welche nicht von {@link Exception} abgeleitet ist,
     * so wird der Server nach Möglichkeit trotzdem Informationen
     * schicken, die vom Roblet-Klienten dann jedoch
     * in eine {@link ClientException} gepackt werden.
     * <p>
     * Wird der diese Methode aufrufende Thread unterbrochen,
     * so wird entweder eine Ausnahme vom Typ {@link InterruptedException}
     * geworfen oder das Unterbrechungssignal (interrupt flag)
     * des Threads gesetzt.&nbsp;
     * Ersteres wird gemacht, wenn die Netzwerkaktivität noch nicht oder
     * gerade ausgelöst wurde.&nbsp;
     * Letzteres geschieht, wenn die Aktivität bereits abgeschlossen
     * ist.&nbsp;
     * Im letzteren Fall sind die Ergebnisse zwar normal verwendbar, aber
     * es ist auch zu beachten, daß der Thread natürlich beim nächsten
     * {@link Object#wait()}, {@link Thread#sleep(long)} o.ä. eine Ausnahme
     * obigen Typs erzeugen wird.
     * <P>
     * Wurde der Thread, der diese Methode aufrief, unterbrochen, so bleibt in
     * diesem Fall offen, ob das Roblet erfolgreich ankam und lief oder
     * nicht.&nbsp;
     * Die später eingehenden Rückgabedaten eines solchen Aufrufs werden
     * vom Klienten verworfen.
     * <P>
     * Anfragen nach Klassen und Ressourcen für bzw. durch das Roblet
     * werden über den bei Aufruf dieser Methode gesetzten
     * Kontext-Klassenladers ({@link Thread#getContextClassLoader()})
     * befriedigt.&nbsp;
     * Ist keiner gesetzt, so wird der Klassenlader der Klasse des Roblets
     * verwendet.&nbsp;
     * Ist dieser {@code null}, so wird der der Klasse des diese Methode
     * aufrufenden {@link Thread}s benutzt.&nbsp;
     * Ist auch dieser {@code null}, so wird der System-Klassenlader
     * ({@link ClassLoader#getSystemClassLoader()}) eingesetzt.
     * 
     * @param   roblet  Roblet-Instanz, welche auf dem Server als Roblet
     *                  zum Laufen gebracht wird
     * @return  Resultat, welches vom Hauptthread des Roblet zurückgegeben wird
     * @throws  InterruptedException  falls der aufrufende Thread
     *              beim Warten auf das Resultat durch die Anwendung
     *              zum Unterbrechen aufgefordert wird oder bereits
     *              vorher sein Unterbrechungssignal (interrupt flag)
     *              gesetzt war
     * @throws  MarshalException    in case converting the roblet or the result
     *              into of from network transferable bytes fails
     * @throws  SlotException  falls der Server wechselte oder eine alte
     *              Verbindung nicht mehr wiederaufgenommen werden konnte
     * @throws Exception  falls im Roblet ein Fehler auftrat
     */
    public <RESULT> RESULT  run (Roblet<RESULT> roblet)
        throws InterruptedException, MarshalException
                , SlotException, Exception;


    /**
     * Zur als Klasse angebenen Schnittstelle ({@code interface}) wird eine
     * (lokale) <B>Vertreter-Instanz</B> zurückgegeben, mit deren Hilfe auf die
     * Methoden einer im Roblet veröffentlichte sogenannte ferne Instanz
     * zugegriffen werden kann.&nbsp;
     * Es wird an dieser Stelle stets eine Vertreter-Instanz zurückgegeben,
     * auch wenn auf Seiten des Roblets noch keine passende ferne Instanz
     * bereitgestellt wurde (s.u.).
     * <P>
     * Die angegebene Klasse muß ein {@code interface} sein.&nbsp;
     * Die lokale Instanz hat alle Methoden der angegebenen Schnittstelle
     * ({@code interface}).&nbsp;
     * Eine passende ferne Instanz ist von einer Klasse, die die angegebene
     * Schnittstelle implementiert.
     * <P>
     * Wird eine Methode der lokalen Instanz aufgerufen, so wird dieser Aufruf
     * an das Roblet dieses Faches vermittelt.&nbsp;
     * Dort wird dann die passende Methode der zugehörigen fernen Instanz
     * aufgerufen.&nbsp;
     * Nicht in der jeweiligen fernen Methode entstandene Fehler werden
     * über eine {@link org.roblet.client.UnexpectedException} angezeigt.
     * <P>
     * Die zugehörige ferne Instanz auf Seiten des Roblets muß vorher dort per
     * Einheit {@code genRob.genControl.unit.Remotes} bereitgestellt
     * werden.&nbsp;
     * Ein Roblet sollte nicht mehrere Instanzen mit gleicher Schnittstelle
     * veröffentlichen, da die Auswahl der fernen Instanz
     * implementierungsabhängig ist.
     * <P>
     * Zu beachten ist, daß sämtliche Parameter(typen) und Rückgabewerte
     * der Schnittstelle serialisierbar sein müssen.
     * 
     * @param clazz  Schnittstelle, zu der eine lokale Instanz für den Zugriff
     *              auf eine entsprechende ferne Instanz erzeugt wird
     * @return lokale Instanz, welche auf die entsprechende ferne Instanz
     *          zugreift oder {@code null}, wenn der Server das Konzept nicht
     *          unterstützt
     * @throws  SlotException  falls der Server wechselte oder eine alte
     *              Verbindung nicht mehr wiederaufgenommen werden konnte
     * @see genRob.genControl.unit.Remotes
     * @see org.roblet.client.UnexpectedException
     */
    public Object  obtainProxy (Class<?> clazz)
        throws SlotException;

    /**
     * Nach Aufruf dieser Methode sind die Methoden der übergebenen Instanz
     * von den Roblets dieses Faches aus erreichbar.
     * <P>
     * Zu beachten ist, daß sämtliche Parameter(typen) und Rückgabewerte
     * serialisiserbar sein müssen.
     * <P>
     * Der Vorgang kann über {@link #revokeRemote(Object)} wieder rückgängig
     * gemacht werden.
     * <P>
     * Diese Funktion ist wirkungslos, wenn der Server das Konzept nicht
     * unterstützt.&nbsp;
     * Der Server unterstützt die Funktionalität nicht, wenn die zugehörige
     * Einheit {@code genRob.genControl.unit.Proxies} nicht verfügbar ist.
     * 
     * @param object  Instanz, deren Methoden erreichbar werden
     * @throws  SlotException  falls der Server wechselte oder eine alte
     *              Verbindung nicht mehr wiederaufgenommen werden konnte
     */
    public void  offerRemote (Object object)
        throws SlotException;

    /**
     * Die Methoden der übergebenen Instanz werden wieder unerreichbar.
     * <P>
     * Ist die Instanz vorher nicht per {@link #offerRemote(Object)} hinzugefügt
     * worden, so passiert nichts.
     * <P>
     * Diese Funktion ist wirkungslos, wenn der Server das Konzept nicht
     * unterstützt.&nbsp;
     * Der Server unterstützt die Funktionalität nicht, wenn die zugehörige
     * Einheit {@code genRob.genControl.unit.Proxies} nicht verfügbar ist.
     * 
     * @param object  Instanz, deren Methoden erreichbar werden
     * @throws  SlotException  falls der Server wechselte oder eine alte
     *              Verbindung nicht mehr wiederaufgenommen werden konnte
     */
    public void  revokeRemote (Object object)
        throws SlotException;

}
