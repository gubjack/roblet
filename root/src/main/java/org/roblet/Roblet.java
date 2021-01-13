// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * This interface characterizes <B>roblet classes</B>.&nbsp;
 * Instances of such a class can be handed over to a roblet server
 * which in turn can run it.
 * <P>
 * Here is an example of a roblet class:
 * <BLOCKQUOTE><PRE>
 * public class  MyRoblet
 *      <B>implements org.roblet.Roblet</B>
 *                  , java.io.Serializable
 * {
 *      public Object  execute (org.roblet.Robot robot)
 *      {
 *          Object  result = null;
 *          // ...
 *          return result;
 *      }
 *      // ...
 * }
 * </PRE></BLOCKQUOTE>
 * In this example the roblet class also implements
 * {@link java.io.Serializable} since usually the roblet server
 * is contacted via network.
 * 
 * <H2>Terminology</H2>
 * Roblet is <I>the</I> central notion of the roblet technology.&nbsp;
 * In practice this term will be used for several different concepts.&nbsp;
 * The context will make clear what is talked about.
 * <UL>
 *   <LI>Roblet <I>classes</I> implement the interface described here - as
 *     shown in the example above.</LI>
 *   <LI>Roblet <I>objects</I> will be created by an application at runtime out
 *     of a roblet class.&nbsp;
 *     These are just Java objects possibly with certain attributes differing
 *     from object ot object.
 * <BLOCKQUOTE><PRE>
 * MyRoblet  myRoblet = new MyRoblet ();
 * // ...
 * </PRE></BLOCKQUOTE>
 *   </LI>
 *   <LI>The roblet <I>application</I> is the Java application in the JVM that
 *     creates an roblet object.</LI>
 *   <LI>The roblet <I>client</I> is a library that takes care of serializing
 *     and sending roblet objects, handle the communication including the
 *     results of a roblet.</LI>
 *   <LI>A roblet <I>server</I> gets contacted by roblet clients, takes and
 *     de-serializes the roblet objects and handles the communication with the
 *     client.</LI>
 *   <LI>A <I>roblet</I> finally is a roblet object that is run by a roblet
 *     server.</LI>
 * </UL>
 * The reason for the differentiation between roblet object and a (running)
 * roblet is the fact that any sinble object can be send arbitrarily often
 * but will result in different (running) roblets always.
 * 
 * @see #execute(Robot)
 * @see Robot
 * @author Hagen Stanek
 */
public interface  Roblet
{

    // TODO Kann beliebig lange laufen
    // TODO Roblet endet, wenn keine von ihm gestarteten Threads mehr laufen
    // (Threads von Einheiten werden nicht einberechnet.)
    // Endet ein vom Roblet gestarteter Thread mit einer Ausnahme, so
    // werden alle Threads des Roblets beendet - inklusive des Threads, der
    // execute ausführt.  In diesem Fall wird die Ausnahme des auslösenden
    // Threads zurückgegeben.
    // Endet ein Roblet, so werden die Resourcen benutzer Einheiten
    // automatisch wieder freigegeben.
    // Der roblet server schränkt den Zugriff auf die Resourcen der
    // JVM und des unterliegenden Systems nach Möglichkeit ein.
    // Stattdessen müssen Einheiten benutzt werden.
    // Nach Möglichkeit wird vom roblet server oder den Resourcen selbst
    // eine Sicherheitsausnahme ausgelöst.
    /**
     * Ein Roblet wird vom Roblet-Server durch Aufruf dieser
     * Methode laufengelassen - quasi zum Leben erweckt.&nbsp;
     * Sie entspricht dabei in der Bedeutung etwa einer
     * {@code main(...)}-Einsprungsroutine für eine Java&trade;-Anwendung
     * oder dem {@code run()} der Schnittstelle
     * {@link java.lang.Runnable} aus der Java&trade;-Standard-Bibliothek.
     * <P>
     * Der Parameter {@link Robot} ist der "Schlüssel" zum Roblet-Server
     * (ursprünglich ein Roboter - heute allgemeiner "Kontext").&nbsp;
     * Diese übergebene Instanz kann benutzt werden, um festzustellen,
     * ob der Server gewünschte Einheiten ({@link Unit}) bereitstellt.&nbsp;
     * Sie ist niemals {@code null}.
     * <P>
     * Es kann die Instanz einer beliebigen Klasse zurückgeben
     * werden.&nbsp;
     * Dazu gehören auch <I>Felder</I> eines beliebigen Klassentyps
     * (z.B. {@code String[]}).
     * <P>
     * Der Roblet-Server gibt die jeweilige Instanz an die aufrufende
     * Anwendung zurück.&nbsp;
     * Dabei ist zu beachten, daß es sich beim Arbeiten im Netz um
     * serialisierbare (vgl. {@link java.io.Serializable}) Klassen handeln
     * muß.&nbsp;
     * Es gilt, daß Felder serialisierbarer Klassen serialisierbar sind.
     * <P>
     * Da der Entwickler weiß, welchen Typ seine zurückgegebene Instanz
     * zur Laufzeit haben wird, kann man auch den entsprechenden
     * <I>cast</I>-Operator "{@code (Klassenname)}" auf
     * Anwendungsseiten auf den Rückgabewert anwenden.&nbsp;
     * Natürlich ist auch eine Rückgabe von {@code null} möglich.&nbsp;
     * {@code null} läßt sich notfalls auf jeden Typ wandeln
     * (<I>cast</I>).
     * <P>
     * Das ausgeführte Roblet braucht Ausnahmen, die vom Typ
     * {@link java.lang.Exception} abgeleitet sind, nicht
     * abzufangen.&nbsp;
     * Die jeweilige Ausnahme wird auf Seiten der Anwendung ebenso als
     * Ausnahme vom gleichen Typ geworfen und kann dort abgefangen werden.
     *
     * @param   robot      Erlaubt Zugriff auf die Einheiten des
     *                      unterliegenden Roboters;&nbsp;
     *                      stets ungleich {@code null}
     * @return  Beliebige  Instanz einer Klasse oder auch {@code null}
     * @throws  Exception  Beliebige Ausnahme
     */
    public Object  execute (Robot robot)
        throws Exception;

}
