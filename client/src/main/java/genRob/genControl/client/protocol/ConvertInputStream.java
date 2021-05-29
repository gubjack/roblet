// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;


/**
 * Objekt-Eingabestrom, der Klassen auch den Klassenlader dieser Klasse
 * auflösen will.
 * 
 * @author Hagen Stanek
 */
class  ConvertInputStream
    extends ObjectInputStream
{

    /**
     * Erzeugt einen Objekt-Eingabestrom
     * @param in  Quell-Eingabestrom für den Objekt-Eingabestrom
     * @throws IOException  falls der Quell-Eingabestrom nicht korrekt ist
     */
    ConvertInputStream (InputStream in)
        throws IOException
    {
        super (in);
    }

    /**
     * Wird von der Basisklasse gerufen, wenn eine Klasse besorgt werden
     * muß.&nbsp;
     * Zunächst werden dabei die Mechanismen der Basisklasse genutzt.&nbsp;
     * Erst wenn das nicht reicht, wird der sekundäre Klassenlader
     * hinzugezogen.
     * @param desc  Informationen zur gewünschten Klasse
     */
    protected Class<?>  resolveClass (ObjectStreamClass desc)
        throws IOException, ClassNotFoundException
    {
        try
        {
            // Gehe über den zusätzlichen Lader
            // Do not initialise the class as it may only be needed far.
            return Class. forName (desc. getName (), true, CLASSLOADER);
                // An dieser Stelle wird nicht
                //   mf_rClassLoader. loadClass (desc. getName ())
                // verwendet, da auch Felder serialisiert worden sein
                // können und dann Klassennamen der Form "[..." auftreten.
                // Obige Lösung wurde über die Quellen von
                // ObjectInputStream.resolveClass(...) entdeckt.
        }
        catch (ClassNotFoundException e)
        {
            // Gehe nun über die Standard-Implementierung
            return super. resolveClass (desc);
        }
    }
    private static ClassLoader  CLASSLOADER
                                = ConvertInputStream.class. getClassLoader ();

}
