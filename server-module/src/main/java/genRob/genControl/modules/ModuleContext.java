// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * Eine Instanz dieser Art bietet einem Modul den Zugriff auf den einbettenden
 * Roblet-Server und stellt damit einen <B>Kontext für ein Modul</B> dar.
 * <P>
 * Eine Instanz diesen Typs wird jedem Modul
 * in {@link Module2#moduleInit(ModuleContext)}
 * übergeben.
 * 
 * @see Module2
 * @author Hagen Stanek
 */
public interface  ModuleContext
{

    /**
     * Gibt die <B>Instanz zum Loggen</B> zurück.
     * @return Logger-Instanz
     */
    public Logger  getLogger ();

    /**
     * Stößt im Roblet-Server den Vorgang des <B>Beendens des
     * Roblet-Servers</B> an.
     * <P>
     * Diese Routine wird nur den Ablauf auslösen, danach jedoch sofort
     * zurückkehren.
     */
    public void  shutdown ();

}
