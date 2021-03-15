// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Instances of this kind can be handed over to a roblet server
 * which in turn can run it.

<P>
    To be send to the roblet server such an instance is serialized.
    This way the instance variables will be available on server side.
    To allow serialization the {@link java.io.Serializable} interface
    needs to be implemented in addition.
    Instance variables also need to do this.
</P>

<P>
    An instance can be send multiple times to the same roblet server
    or to different.
    Instance variables can simply be changed before sending to a
    roblet server when necessary.
    Always the current state gets serialized and used on server side.
</P>

<P>
    The roblet server deserializes to an instance and simply invokes
    {@code #execute(Robot)}.
    As part of this method all instance variables can be used.
</P>

<P>
    To serialize and send roblet instances to a roblet server
    a roblet client library needs to be used.
</P>

<H2>Example</H2>
<P>
    Here is an example of a <EM>roblet class</EM> (see terminology below):
</P>
<BLOCKQUOTE><PRE>
public class  MyRoblet
     <B>implements org.roblet.Roblet</B>
                 , java.io.Serializable
{
     public boolean  variable;
     public Object  execute (org.roblet.Robot robot)
     {
         if (variable)
             // ...
         Object  result = null;
         // ...
         return result;
     }
     // ...
}
</PRE></BLOCKQUOTE>

<H2>Terminology</H2>
<P>
    Roblet is <I>the</I> central notion of the roblet technology.
    In practice this term will be used for several different concepts.
    The context will make clear what is talked about.
</P>
<UL>
    <LI>Roblet <I>classes</I> implement the interface described here - as
        shown in the example above.</LI>
    <LI>Roblet <I>instances</I> will be created by an application at runtime
        out of a roblet class.
        These are just Java objects possibly with certain variables
        differing from instance to instance.
        <BLOCKQUOTE><PRE>
MyRoblet  myRoblet = new MyRoblet ();
// ...
        </PRE></BLOCKQUOTE>
    </LI>
    <LI>The roblet <I>application</I> is the Java application in the JVM
        that creates roblet instances.</LI>
    <LI>The roblet <I>client</I> is a library that takes care of serializing
        and sending roblet instances. It handles the communication including
        the results of the execution of a roblet instance.</LI>
    <LI>A roblet <I>server</I> gets contacted by roblet clients, takes,
        de-serializes and executes the roblet instances
        and handles the communication with the client.</LI>
    <LI>A <I>roblet</I> finally is a de-serialized roblet instance that is
        executed by a roblet server.</LI>
</UL>

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
