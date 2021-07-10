// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * <SPAN style="color:darkgrey">
 * Instanzen dieser Art repräsentieren ein <B>Fach</B> der Roblet-Umgebung
 * des Roblet-Servers.
 * </SPAN>
 * 
 * @deprecated  Fades away along with {@link Module}
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Slot
{

    /**
     * Beendet das Roblet in diesem Fach und läßt eine Ausnahme an
     * die zugehörige Anwendung zurückgeben.
     * @since 5.3
     */
    public void  halt ();

}
