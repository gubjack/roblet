// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.server;


/**
 * <b>Besitzausnahme</b> - Ausnahmetyp für den Fall,
 * daß ein Roblet® zur Aufgabe des
 * Besitzes einer Einheit gezwungen wurde und daher nicht mehr weiterarbeiten
 * darf.
 * @see ExecutionException
 * @deprecated  Wird in Zukunft verschwinden, da sich die Konzepte ändern.
 * @author Hagen Stanek
 */
// * @see Owner
// * @see Owner#force(org.roblet.Unit)
@Deprecated
public class  OwnerException
    extends Exception
{

    /**
     * Initialisiert eine neue Instanz ohne eine Botschaft.
     */
    public  OwnerException ()
    {
    }

    /**
     * Initialisiert eine neue Instanz mit einer Botschaft.
     * @param strMessage    die Botschaft
     */
    public  OwnerException (String strMessage)
    {
        super (strMessage);
    }

    /**
     * Initialisiert eine neue Instanz mit Anlaß.
     * @param rThrowable    Anlaßausnahme
     */
    public  OwnerException (Throwable rThrowable)
    {
        super (rThrowable);
    }

    /**
     * Initialisiert eine neue Instanz mit Botschaft und mit Anlaß.
     * @param strMessage    Botschaft
     * @param rThrowable    Anlaßausnahme
     */
    public  OwnerException (String strMessage, Throwable rThrowable)
    {
        super (strMessage, rThrowable);
    }

}
