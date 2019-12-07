// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Alle <B>Einheiten</B> der Roblet-Technik müssen von diesem Typ
 * sein.&nbsp;
 * Der Begriff <I>Einheiten</I> kann verschiedene Dinge bedeuten - in der
 * Praxis ergibt sich jedoch aus dem Zusammenhang die jeweilige Bedeutung.
 * <P>
 * Einheiten<I>definitionen</I> sind Schnittstellen
 * (Java&trade;-<TT>interface</TT>), die die hier beschriebene
 * Schnittstelle <SPAN style="color:darkgreen">erweitern</SPAN>:
 * <BLOCKQUOTE><PRE>
 * public interface  MyUnit
 *      <B><SPAN style="color:darkgreen">extends</SPAN> org.roblet.Unit</B>
 * {
 *     public int  sampleMethod ();
 *     // ...
 * }
 * </PRE></BLOCKQUOTE>
 * Eine Einheiten<I>implementierung</I> ist demgegenüber eine Klasse,
 * die eine Einheitendefinition
 * <SPAN style="color:darkred">implementiert</SPAN>:
 * <BLOCKQUOTE><PRE>
 * class  MyUnitImpl
 *      <B><SPAN style="color:darkred">implements</SPAN> MyUnit</B>
 * {
 *     public int  sampleMethod ()
 *     {
 *         return 42;
 *     }
 *     // ...
 * }
 * </PRE></BLOCKQUOTE>
 * Implementierungen sind in Roblet-Servern zu finden.&nbsp;
 * Ein {@link Roblet} kann über seinen {@link Robot} eine Einheit unter
 * Angabe einer Einheitendefintion anfordern.&nbsp;
 * Der Roblet-Server gibt dann die <I>Instanz</I> einer
 * Einheitendefinition zurück.
 * <P style="font-size:smaller">
 * Eigentlich bräuchte man einen derartigen Typ nicht, um Einheiten
 * zu definieren, zu implementieren und anzuwenden.&nbsp;
 * Dennoch hat sich in der Praxis gezeigt, daß durch die mit diesem Typ
 * verbundene Vorschrift der Erweiterung/Ableitung,
 * viele Methoden anderer Klassen
 * klarer in ihren Parametern und Rückgabewerten sind.&nbsp;
 * Damit wird dann auch die Dokumentation und das Verständnis der
 * Roblet-Technik einfacher und klarer.
 * </P>
 * 
 * @author Hagen Stanek
 */
public interface  Unit
{
}
