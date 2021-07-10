// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * <SPAN style="color:darkgrey">
 * Eine Instanz dieser Art bietet einem Modul den Zugriff auf den einbettenden
 * Roblet-Server und repräsentiert eine Art <B>Server-Kontext</B>.
 * </SPAN>
 * 
 * @deprecated  Fades away along with {@link Module}
 * @see Module
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Supervisor
{

    /**
     * Stößt im Roblet-Server den Vorgang des Beendens an.
     * <p>
     * Diese Routine wird nur den Ablauf auslösen, danach jedoch sofort
     * zurückkehren.
     */
    void  shutdown ();

    /**
     * Gibt die Instanz zum Loggen zurück.
     * @return Log-Instanz
     */
    Log  getLog ();

}
