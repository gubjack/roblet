// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.BufferedInputStream;
import  java.io.BufferedOutputStream;
import  java.io.ByteArrayInputStream;
import  java.io.ByteArrayOutputStream;
import  java.io.IOException;
import  java.io.ObjectInputStream;
import  java.io.ObjectOutputStream;


/**
 * Diese Klasse konvertiert Objekte zum Verkehr zwischen Klient und Server.
 * <P>
 * Auf Server-Seite gibt es eine zentrale Klasse, die auf diese Klasse zugreift.
 * 
  * @author Hagen Stanek
*/
class  Convert
{

    /**
     * Serialisiert einen String in ein Byte-Feld.&nbsp;
     * Diese Methode wird im Klienten und
     * von der Konvertierungsklasse des Servers gerufen.
     * 
     * @param str  String
     * @return  serialisierter String
     * @throws IOException  falls die Kodierung des String nicht unterstützt ist
     */
    static byte[]  str2bytes (String str)
        throws IOException
    {
        return str. getBytes ("utf-8");
    }
    /**
     * Konvertiert einen serialisierten String zurück.&nbsp;
     * Diese Methode wird im Klienten und
     * von der Konvertierungsklasse des Servers gerufen.
     * <P>
     * Änderungen in der Ausnahmeliste sind in letzterer einzupflegen.
     * 
     * @param ba  serialisierter String
     * @return  String
     * @throws IOException  falls der Byte-Strom fehlerhaft ist
     */
    static String  bytes2str (byte[] ba)
        throws IOException
    {
        return new String (ba, "utf-8");
    }

    /**
     * Creates a serialisation of an object as needed by the Roblet protocol.
     * 
     * @param o  object
     * @return  serialisation of the object
     * @throws IOException  in case serialisation fails
     */
    static <T> byte[]  object2bytes (T o)
        throws IOException
    {
        ByteArrayOutputStream  baos = new ByteArrayOutputStream ();
        try (ObjectOutputStream  oos
                = new ObjectOutputStream (
                        new BufferedOutputStream (baos)))
        {
            oos. writeObject (o);
        }
        return baos. toByteArray ();
    }
    /**
     * Converts a serialized object back into an object.
     * 
     * @param ba  serialized object
     * @return  object
     * @throws IOException  in case the byte array is erroneous
     * @throws ClassNotFoundException  in case a needed class cannot be resolved
     */
    @SuppressWarnings("unchecked")
    static <T> T  bytes2object (byte[] ba)
        throws IOException, ClassNotFoundException
    {
        try (ObjectInputStream  ois
                = new ConvertInputStream (
                        new BufferedInputStream (
                                new ByteArrayInputStream (ba))))
        {
            return (T) ois. readObject ();
        }
    }

}
