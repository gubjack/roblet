// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.server;


/**
 * <b>Ausführungsausnahme</b> - Ausnahmetyp für den Fall,
 * daß eine nicht abgefangene Ausnahme
 * in einem der zu einem Roblet® gehörigen Threads auftritt.
 * <blockquote><i>
 * Diese Klasse unterliegt momentan noch Änderungen und soll zukünftig
 * <SPAN style="color:red">entfallen</SPAN> oder
 * in die Roblet®-Bibliothek wandern, um dann eine Standard-Schnittstelle
 * für Roblet®-Server beliebigen Typs darzustellen.&nbsp;
 * Die Roblet®-Bibliothek wird von <a href="http://roblet.org">Roblet®.org</a>
 * verwaltet und weiterentwickelt.
 * </i></blockquote>
 * <p>
 * Die Ausnahme wird dabei als Anlaß mitgegeben (verkettet).
 * 
 * @see OwnerException
 * @author Hagen Stanek
 */
public class  ExecutionException
    extends Exception
{

    /**
     * Initialisiert eine neue Instanz mit Anlaß.
     * @param rThrowable    Anlaßausnahme
     */
    public ExecutionException (Throwable rThrowable)
    {
        super (rThrowable);
    }

    /**
     * Initialisiert eine neue Instanz mit Botschaft und mit Anlaß.
     * @param strMessage    Botschaft
     * @param rThrowable    Anlaßausnahme
     */
    public ExecutionException (String strMessage, Throwable rThrowable)
    {
        super (strMessage, rThrowable);
    }

}
