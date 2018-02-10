// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright 2001-2018  Hagen Stanek

package org.roblet;


/**
 * Diese Schnittstelle charakterisiert <B>Roblet-Klassen</B>.&nbsp;
 * Instanzen einer solchen Klasse können einem Roblet-Server
 * übergeben und von ihm zum Laufen gebracht werden.
 * <P>
 * Hier ein Beispiel für eine Roblet-Klasse:
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
 * (Da Roblets meist über ein Netzwerk übertragen werden, muß zusätzlich
 * zu der hier beschriebenen Schnittstelle normalerweise noch
 * {@link java.io.Serializable} implementiert werden.)
 * 
 * <H3>Begriffsbestimmung</H3>
 * Roblet ist <I>der</I> zentrale Begriff der Roblet-Technik.&nbsp;
 * Der Begriff kann verschiedene Dinge bedeuten - in der Praxis ergibt
 * sich jedoch aus dem Zusammenhang die jeweilige Bedeutung.
 * <P>
 * Roblet<I>-Klassen</I> implementieren die hier beschriebene
 * Schnittstelle (s. obiges Beispiel).
 * <P>
 * Roblet<I>-Instanzen</I> werden in einer Anwendung zur Laufzeit
 * erzeugt und sind Java-Objekte in der JVM der Anwendung:
 * <BLOCKQUOTE><PRE>
 * MyRoblet  myRoblet = new MyRoblet ();
 * // ...
 * </PRE></BLOCKQUOTE>
 * <I>Roblet</I> bezeichnet schließlich die in der JVM eines Roblet-Servers
 * laufenden Java-Objekte.
 * <P>
 * Der Grund für eine Unterscheidung zwischen Roblet-Instanz und
 * (laufendem) Roblet ist die Tatsache, daß eine Roblet-Instanz
 * beliebig oft verschickt und damit beliebig oft zum Laufen gebracht
 * werden kann.&nbsp;
 * D.h. von einer Roblet-Klasse können mehrere Roblet-Instanzen und von
 * diesen jeweils mehrere Roblets erzeugt werden.
 * 
 * @see #execute(Robot)
 * @see Robot
 * @author Hagen Stanek
 */
public interface  Roblet
{

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
