// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.unit;

import  org.roblet.Unit;


/**
 * Dieser Einheit können Instanzen übergeben werden, deren Methoden dann auf
 * Seiten der Anwendung aufgerufen werden können
 * (<SPAN style="color:darkblue">Bereitstellen einer <I>fernen Instanz</I> für
 * die Anwendungsseite</SPAN>).
 * <P>
 * Dazu muß eine Schnittstelle definiert werden.&nbsp;
 * Diese Schnittstelle muß auf Seiten des Roblets implementiert werden.&nbsp;
 * Eine Instanz dieser Implementation kann dann per {@link #offer(Object)}
 * für die Anwendung bereitgestellt werden.
 * <P>
 * Auf Seiten der Anwendung wird dann per
 * <CODE>genRob.genControl.client.Slot#obtainProxy(java.lang.Class)</CODE>
 * eine Instanz geliefert, die die gleiche o.g. Schnittstelle bedient, jedoch
 * bei Aufruf einer Methode eine Weiterleitung zum Roblet vornimmt.&nbsp;
 * Dabei werden die Parameter der Methode serialisiert und übertragen,
 * die Methode auf Seiten des Roblets ausgeführt,
 * danach der Rückgabewert serialisiert und übertragen,
 * und auf Seiten der Anwendung dem Aufrufer übergeben.
 * 
 * @see Proxies
 * @author Hagen Stanek
 */
public interface  Remotes
    extends Unit
{

    /**
     * Nach Aufruf dieser Methode sind die Methoden der übergebenen Instanz
     * von der Anwendung aus erreichbar.
     * <P>
     * Zu beachten ist, daß sämtliche Parameter(typen) und Rückgabewerte
     * serialisiserbar sein müssen.
     * <P>
     * Der Vorgang kann über {@link #revoke(Object)} wieder rückgängig gemacht
     * werden.
     * 
     * @param object  Instanz, deren Methoden erreichbar werden
     */
    public void  offer (Object object);

    /**
     * Die Methoden der übergebenen Instanz werden wieder unerreichbar.
     * <P>
     * Ist die Instanz vorher nicht per {@link #offer(Object)} hinzugefügt
     * worden, so passiert nichts.
     * 
     * @param object  Instanz, deren Methoden erreichbar werden
     */
    public void  revoke (Object object);

}
