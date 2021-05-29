// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client;

import org.roblet.client.SlotException;


/**
 * Jede Instanz repräsentiert genau einen Roblet-Server
 * und ein Roblet-Server wird pro Klient durch genaue eine solche Instanz
 * repräsentiert (<b>Server-Repräsentanz</b>).
 * <p>
 * <i>M.a.W.:</i>&nbsp;
 * Ein Roblet-Server ist ein fernes System, zu welchem eine Instanz
 * von diesem Typ eine Referenz darstellt.&nbsp;
 * Diese Instanz wirkt demnach als lokale Repräsentanz des
 * fernen Roblet-Server.
 * <p>
 * Ein Roblet-Server stellt Fächer ({@link Slot}) für Roblets bereit.&nbsp;
 * Über {@link #getSlot()} kann man solche bekommen.
 * <p>
 * In jedem Fach kann zu jedem Zeitpunkt nur ein Roblet laufen,
 * aber eine Anwendung kann sich (fast) beliebig viele Fächer besorgen.
 * 
 * @see Client
 * @see Slot
 * @author Hagen Stanek
 */
@Deprecated
public interface  Server
{

    /**
     * Gibt ein <b>neues Fach</b> zurück.&nbsp;
     * Diese Aktion ist mit Netzwerkaktivität verbunden und kann daher
     * unbestimmte Zeit in Anspruch nehmen.&nbsp;
     * Es wird also <I>nicht</I> mit einem <I>timeout</I> gearbeitet, jedoch der
     * Thread, der diese Methode aufruft, kann jederzeit unterbrochen werden.
     * <p>
     * Eine Anwendung kann (fast) beliebig viele Fächer gleichzeitig
     * auf einem Server nutzen, in denen auch parallel
     * Roblets laufen können.&nbsp;
     * Jedes Fach reserviert allerdings einige Ressourcen, weshalb
     * Server-seitig Einschränkungen vorliegen können.
     * 
     * @return neues Fach des Servers
     * @throws  InterruptedException  falls der aufrufende Thread
     *              beim Warten auf das Fach durch die Anwendung
     *              zum Unterbrechen aufgefordert wird oder bereits
     *              vorher sein Unterbrechungssignal (interrupt flag)
     *              gesetzt war
     * @throws  SlotException  falls der Server wechselte oder eine alte
     *              Verbindung nicht mehr wiederaufgenommen werden konnte
     */
    public Slot  getSlot ()
        throws InterruptedException, SlotException;

}
