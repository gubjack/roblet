// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client;

import org.roblet.client.Logger;
import org.roblet.server.unit.net.NetUnit;

import  genRob.genControl.client.base.ClientContext;
import genRob.genControl.client.base.ServerImpl;
import  genRob.genControl.client.base.ServerMap;
import  genRob.genControl.client.connect.Name;


/**
 * Eine Instanz dieser Klasse stellt einen
 * <SPAN style="color:brown"><B>Klienten</B></SPAN> für einen
 * Roblet-Server dar.
 * <p>
 * Die Benutzung dieser Klasse in einer Anwendung ist denkbar einfach
 * (Beispiele weiter unten):
 * <ul>
 *  <li>Pro Anwendungsinstanz ist nur eine Instanz zu erzeugen (obgleich
 *      beliebig viele möglich sind).
 *  <li>Nutzt man {@link #getServer(String)}, so kann man sich eine
 *      Server-Repräsentanz ({@link Server}) geben lassen, die
 *      einen weitergehenden Zugriff auf den Server zuläßt
 *      - insbesondere auf einzelne Fächer ({@link Slot}) eines jeden
 *      Servers.
 * </ul>
 * <p>
 * Alle von den Klassen der Bibliothek verwalteten Threads sind Dämonen und
 * haben eine Priorität gleich des Klient-initialisierenden Threads.
 * 
 * <h2>Beispiel für das Senden eines Roblets
 *      auf einen namentlich bekannten Server</h2>
 * Der Server soll {@code roblet.org} sein.&nbsp;
 * Dort läuft für diese Zwecke ein Test-Server an Standard-Port 2001.
 * <BLOCKQUOTE><PRE style="font-size:smaller">
 * import  genRob.genControl.client.Client;
 * import  java.io.Serializable;
 * import  java.util.Date;
 * import  org.roblet.Roblet;
 * import  org.roblet.Robot;
 * 
 * // Anwendungsimplementierung
 * public class  DateApp
 * {
 * 
 *     // Methode läuft natürlich lokal als Teil der Anwendung
 *     public static void  main (String[] args)
 *         throws Exception
 *     {
 *         <b>Client  client = new Client ();</b>
 * 
 *         String  servername = "roblet.org";
 *         try
 *         {
 *             Roblet  roblet = new DateRoblet ();
 *             Date  date = (Date) client. <SPAN style="color:brown"><b>getServer (servername).
 *                                   getSlot (). run (roblet);</b></SPAN>
 * 
 *             System.out.println ("Date"
 *                                     + " of " + servername
 *                                     + " is " + date);
 *         }
 *         catch (Exception e) {   e. printStackTrace ();  }
 * 
 *         <b>client. close ();</b>
 *     }
 * 
 *     // Roblet-Implementierung
 *     private static class  DateRoblet
 *         implements Roblet, Serializable
 *     {
 *         // Interface Roblet
 *         <I>// Methode läuft fern im Server (!)</I>
 *         public Object  execute (Robot rRobot)
 *         {
 *             return new Date ();
 *         }
 *     }
 * 
 * }
 * </PRE></BLOCKQUOTE>
 * Vorausgesetzt, daß alle Java™-Archive des RDK
 * insbesondere {@code org.roblet.jar}
 * im gleichen Verzeichnis wie {@code DateApp.java} liegen,
 * kann mit folgendem kompiliert werden
 * (Unix/MacOS&trade;/Linux, JDK 6):
 * <PRE style="font-size:smaller">
 * javac  -classpath org.roblet.jar:.  DateApp.java
 * </PRE>
 * Und unter Windows&trade;:
 * <PRE style="font-size:smaller">
 * javac  -classpath org.roblet.jar;.  DateApp.java
 * </PRE>
 * 
 * Will man es nun das Beispiel laufen lassen, gibt man folgendes ein
 * (Unix/MacOS&trade;/Linux, JDK 6):
 * <PRE style="font-size:smaller">
 * java  -classpath org.roblet.jar:.  DateApp
 * </PRE>
 * Und unter Windows&trade;:
 * <PRE style="font-size:smaller">
 * java  -classpath org.roblet.jar;.  DateApp
 * </PRE>
 * 
 * @see Server
 * @see Slot
 * @author Hagen Stanek
 */
@Deprecated
public class  Client
{

    /**
     * Erzeugt einen Klienten, welcher Log-Informationen generieren kann
     * und dazu Kriterien übergeben bekommt.&nbsp;
     * Ein Klient sollte mit {@link #close()} geschlossen werden, um alle
     * möglicherweise verwendeten Ressourcen wieder freizugeben.
     * <p>
     * Alle im folgenden von
     * der Bibliothek intern gestarteten Threads bekommen die gleiche Priorität,
     * wie die des aufrufenden {@link Thread}s.&nbsp;
     * Ebenso wird die zugehörige {@link ThreadGroup} benutzt, um darin
     * eine ebensolche mit Namen <I>client#x</I> (<I>x</I> laufende Nummer)
     * für die zu startenen Threads zu erzeugen.&nbsp;
     * Alle gestarteten {@link Thread}s des Klienten sind als Dämon
     * gekennzeichnet.
     * 
     * @param logger  Instanz zur Verarbeitung von Log-Informationen
     * @param rNetUnit  Instanz vom Roblet-Server, falls die Bibliothek in einem
     *              Roblet verwendet wird
     */
    public  Client (Logger logger, NetUnit rNetUnit)
    {
        // Erzeuge Logger mit passenden Ausgabezulassungen
        mf_rLogger = logger;

        mf_iNumber = getClientNumber ();

        if (mf_rLogger. base)
            mf_rLogger. base (this
                    , "Client () #" + mf_iNumber + " - begin");

        try
        {
            // Erzeuge die Thread-Gruppe und Priorität für den Klienten
            ThreadGroup  rThreadGroup;
            {
                Thread  rThread = Thread. currentThread ();
    
                ThreadGroup  parent = rThread. getThreadGroup ();
                String  name = "roblet-c" + mf_iNumber;
                rThreadGroup = new ThreadGroup (parent, name);
            }

            // Setze Verzögerung für Netzwerkzugriffstests
            int  iDelayMS = 0;

            ClientContext  rClientContext
                = new ClientContext (mf_rLogger, rThreadGroup
                                            , iDelayMS, rNetUnit);

            // Erzeuge Server-Liste
            mf_rServerMap = new ServerMap (rClientContext);
        }
        finally
        {
            if (mf_rLogger. base)
                mf_rLogger. base (this
                        , "Client () #" + mf_iNumber + " - end");
        }
    }
    final Logger  mf_rLogger;
    private final int  mf_iNumber;
    private final ServerMap  mf_rServerMap;

    private static int  ms_iClient;
    private synchronized static int  getClientNumber ()
    {
        return ms_iClient++;
    }


   /**
     * Gibt eine <B>Server-Repräsentanz zum</B> angegebenen <B>Server-Namen</B>
     * zur Benutzung zurück.&nbsp;
     * Diese Aktion ist mit Netzwerkaktivität verbunden und kann daher
     * unbestimmte Zeit in Anspruch nehmen.&nbsp;
     * Es wird also <I>nicht</I> mit einem <I>timeout</I> gearbeitet, jedoch der
     * Thread, der diese Methode aufruft, kann jederzeit unterbrochen werden.
     * <P>
     * Es wird aus dem angegebenen Server-Namen der Host-Name und die
     * Port-Nummer extrahiert.&nbsp;
     * Ist kein Doppelpunkt im Server-Namen, so wird der Standard-Port
     * 2001 angenommen.&nbsp;
     * Ist der Server-Name {@code null}, so wird die Loopback-Schnittstelle
     * des lokalen Rechners mit Standard-Port 2001 angenommen.
     * <P>
     * ACHTUNG:&nbsp;
     * Die Aufspaltung nach Host-Name und Port-Nummer erfolgt einfach anhand
     * des Doppelpunktes ohne weitere Handlungen.&nbsp;
     * Der Aufrufer muß selbst sicherstellen, daß Host-Name und Port-Nummer
     * den Konventionen (DNS, IPv4, IPv6 etc.) bzw. dem Bereich (normalerweise
     * 1 bis 65535) entsprechen, also der Server-Name tatsächlich einen
     * existierenden Server anspricht.&nbsp;
     * Einzig für den Fall, daß nach dem Doppelpunkt keine ganze Zahl steht,
     * wird der Host-Name gleich dem Server-Namen gesetzt und das Port
     * als 2001 angenommen.
     * <P>
     * Es wird eine Verbindung zum Roblet-Server aufgebaut und eine
     * Server-Repräsentanz zurückgegeben.
     * 
     * @param servername  Name des Roblet-Servers in der Form <i>host:port</i>
     *                  oder <I>host</I>,
     *                  wobei <i>host</i> ein DNS-Name oder direkt eine
     *                  IP-Adresse sein kann und
     *                  <i>port</i> im Bereich von {@code 1}
     *                  bis {@code 65535} liegen muß;
     *                  ohne Port-Angabe oder bei Unverständlichkeit
     *                  wird Standard-Port 2001 angenommen;&nbsp;
     *                  ein Wert von {@code null} spricht die
     *                  Loopback-Netzwerkschnittstelle auf 2001 an
     * @return zugehörige Server-Repräsentanz
     * @throws  InterruptedException  falls der aufrufende Thread
     *              beim Warten auf den Roblet-Server durch die Anwendung
     *              zum Unterbrechen aufgefordert wird oder bereits
     *              vorher sein Unterbrechungssignal (interrupt flag)
     *              gesetzt war
     */
    public ServerImpl  getServer (String servername)
        throws InterruptedException
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this
                    , "getServer (" + servername + ") "
                        + "#" + mf_iNumber + " - begin");
        try
        {
            return mf_rServerMap. getServer (new Name (servername));
        }
        finally
        {
            if (mf_rLogger. base)
                mf_rLogger. base (this
                        , "getServer (" + servername + ") "
                            + "#" + mf_iNumber + " - end");
        }
    }

}
