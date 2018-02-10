// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright 2001-2018  Hagen Stanek

package org.roblet;


/**
 * Stellt den <B>Kontext</B> eines Roblets dar und
 * ermöglicht das Erfragen von Einheiten ({@link Unit}) eines
 * Roblet-Servers.&nbsp;
 * Der Name dieser Schnittstelle ist historisch bedingt - ein
 * Roblet-Server kann auch ganz allgemeine Dinge, wie Datei-Systeme,
 * Algorithmen u.v.a.m. mit Hilfe von Einheiten darstellen.
 * <p>
 * Jedem Roblet wird bei seiner Ausführung eine Instanz dieses Typs
 * übergeben (vgl. {@link Roblet#execute(Robot)}).&nbsp;
 * Es muß sich dabei nicht immer um die gleiche Instanz handeln.
 *
 * @see  #getUnit(Class)
 * @see  Roblet
 * @see  Unit
 * @author Hagen Stanek
 */
public interface  Robot
{
    
    /**
     * Zu einer Einheitendefinition wird eine zugehörige
     * Instanz zurückgegeben.
     * <P>
     * Die Einheiten<I>definition</I> (vgl. {@link Unit})
     * wird als Klasse übergeben.&nbsp;
     * Die Klasse bekommt man durch Anhängen von {@code .class}.&nbsp;
     * Z.B.:
     * <BLOCKQUOTE><PRE>
     * MyUnit<B>.class</B>
     * </PRE></BLOCKQUOTE>
     * <P>
     * Man erhält bei Aufruf diese Methode eine Instanz der zugehörigen
     * Einheiten<I>implementierung</I> zurück.&nbsp;
     * Da jede Einheiten<I>definition</I> die Schnittstelle {@link Unit}
     * erweitert, paßt der Typ der zurückgegebenen Instanz.
     * <P>
     * Ein Roblet muß stets prüfen, ob eine erfragte Einheit im
     * Roblet-Server auch wirklich vorhanden ist.&nbsp;
     * Ist dies nämlich nicht der Fall, so wird {@code null}
     * zurückgegeben.&nbsp;
     * Hier ein beispielhafte Verwendung
     * <BLOCKQUOTE><PRE>
     * public Object  execute (org.roblet.Robot robot)
     *     throws Exception
     * {
     *     MyUnit  myUnit = (MyUnit) robot. getUnit (MyUnit.class);
     *     <B>if (myUnit == null)</B>
     *         throw new Exception ("MyUnit not available");
     *     // ...
     * }
     * </PRE></BLOCKQUOTE>
     *
     * @param   rClass      Klasse der Einheitendefinition
     * @return  Instanz der Einheitenimplementierung oder {@code null}
     * @see  Roblet
     */
    public Unit  getUnit (Class rClass);

}
