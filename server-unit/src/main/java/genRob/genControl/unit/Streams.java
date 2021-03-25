// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.unit;

import  java.io.InputStream;
import  java.io.OutputStream;
import  org.roblet.Unit;


/**
 * Einheit, die es einem Roblet ermöglicht,
 * mit seiner Anwendung <SPAN style="color:darkblue">vermittels <I>Ströme</I> zu
 * kommunizieren</SPAN>.
 * <P>
 * Kennt der Klient der Anwendung das Konzept der Ströme nicht, so wird der
 * Server eine Einheit diesen Typs nicht an ein Roblet dieses Klienten
 * übergeben.&nbsp;
 * Oder anders herum gesehen, erhält ein Roblet auf Nachfrage
 * dann keine solche Einheit.
 * @deprecated Wird bei der nächsten Änderung am Server gestrichen.  Ersatz
 *  sind dann wie schon jetzt die fernen Instanzen (vgl. {@link Proxies} und
 *  {@link Remotes}).
 * @author Hagen Stanek
 */
public interface  Streams
    extends Unit
{

    /**
     * Gibt eine Strom-Instanz zurück, mit der <B>Daten von der Anwendung</B>
     * abgeholt werden können.
     * <P>
     * <CODE>genRob.genControl.client.Slot#getOutputStream()</CODE>
     * holt den zugehörigen Ausgabestrom auf Seiten der Anwendung.
     * @return  Strom-Instanz
     */
    public InputStream  getInputStream ();

    /**
     * Gibt eine Strom-Instanz zurück, mit der <B>Daten zur Anwendung</B>
     * gesandt werden können.
     * <P>
     * <CODE>genRob.genControl.client.Slot#getInputStream()</CODE>
     * holt den zugehörigen Eingabestrom auf Seiten der Anwendung.
     * @return  Strom-Instanz
     */
    public OutputStream  getOutputStream ();

}
