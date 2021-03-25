// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.unit;

import  org.roblet.Unit;


/**
 * Diese Einheit ermöglicht es, eine Instanz zu einer Schnittstelle zu erhalten,
 * deren Implementierung und eigentliche Instanzierung auf Seiten der Anwendung
 * erfolgt (<SPAN style="color:darkblue">Benutzung einer von Anwendungsseite
 * bereitgestellten <I>fernen Instanz</I></SPAN>).
 * <P>
 * Die Anwendungsseite muß dazu die zugehörige Instanz per
 * <CODE>genRob.genControl.client.Slot#offerRemote(java.lang.Object)</CODE>
 * bereitgestellt haben.&nbsp;
 * Die Methode {@link #obtain(java.lang.Class)} dieser Einheit gibt nur eine
 * Instanz eines bestimmten Typs zurück und bei Aufruf einer Methode des Typs
 * erfolgt eine Weiterleitung zur Instanz auf Anwendungsseite.
 * 
 * @author Hagen Stanek
 */
public interface  Proxies
    extends Unit
{

    /**
     * Zur angebenen Schnittstelle (<CODE>interface</CODE>) wird eine Instanz
     * zurückgegeben.&nbsp;
     * Die Methoden der Instanz entsprechen den Methoden der
     * Schnittstelle.&nbsp;
     * Wird eine Methode der hiesigen Instanz aufgerufen, so wird dieser Aufruf
     * an die Anwendung vermittelt.&nbsp;
     * Dort wird dann die passende Methode der zugehörigen Instanz aufgerufen.
     * <P>
     * Die zugehörige Instanz auf Seiten der Anwendung mußte vorher dort per
     * <CODE>genRob.genControl.client.Slot#offerRemote(java.lang.Object)</CODE>
     * bereitgestellt worden sein.&nbsp;
     * Die Klasse der dortigen Instanz implementiert die hier übergebene
     * Schnittstelle.
     * <P>
     * Zu beachten ist, daß sämtliche Parameter(typen) und Rückgabewerte
     * der Schnittstelle serialisiserbar sein müssen.
     * 
     * @param clazz  Schnittstelle, zu der eine lokale Instanz für den Zugriff
     *              auf eine entsprechende ferne Instanz erzeugt wird
     * @return lokale Instanz, welche auf die entsprechende ferne Instanz
     *          zugreift
     */
    public Object  obtain (@SuppressWarnings("rawtypes") Class clazz);

}
