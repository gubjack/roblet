// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * Modul-Typ der darüber informiert wird, wie der Server-Name lautet, sobald
 * dieser zur Verfügung steht.
 * <P>
 * Module dieses Typs werden vor den Modulen vom Typ 2 initialisiert.
 * 
 * @deprecated  The notion of a server name will loose its internal meaning.
 * @see Module2
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Module3
    extends Module2
{

    /**
     * Wird aufgerufen, wenn der Name des Servers feststeht.&nbsp;
     * Dies ist dann der Fall, wenn die zugehörige Server-Socket geöffnet wurde
     * und daher sicher ihr lokales Port bekannt wird.
     * <P>
     * Diese Methode wird im Gegensatz zu {@link #moduleInit(ModuleContext)}
     * nicht in einem Thread der Thread-Gruppe des Moduls aufgerufen.
     * <P>
     * Treten in dieser Methode Fehler auf, so wird der Server sie
     * ignorieren.&nbsp;
     * Das Modul sollte die zugehörige Funktionalität dann nicht via Einheit
     * freigeben oder den Server dazu aufrufen, sich zu beenden.
     * 
     * @param name  Name des Servers als {@code host:port}
     */
    public void  serverOpen (String name);

}
