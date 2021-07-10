// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;


/**
 * Eine Instanz dieses Typs <B>repräsentiert ein Roblet</B> für ein
 * Modul.&nbsp;
 * Dadurch kann ein Modul die verschiedenen Roblets unterscheiden.
 * <P>
 * <SPAN style="color:brown">
 * ACHTUNG:&nbsp;
 * Ein Modul darf diese Instanzen nicht aufbewahren, sondern nur nutzen,
 * wenn sie als Parameter übergeben werden.&nbsp;
 * Wurden Schlüssel-Wert-Paare hinzugefügt, so können jedoch sowohl Schlüssel
 * als auch Wert frei benutzt werden.
 * </SPAN>
 * 
 * @author Hagen Stanek
 */
public interface  RobletHandle
{

    /**
     * Fügt ein Schlüssel-Wert-Paar zum Roblet-Repräsentanten hinzu.&nbsp;
     * Werden dann später Methoden mit diesem Repräsentanten als Parameter
     * aufgerufen, so kann mit Hilfe von {@link #get(Object)} dann auf die
     * eigenen Werte zurückgegriffen werden.
     * <P>
     * Es können beliebig viele Paare eingehängt werden.&nbsp;
     * Typischerweise wird als Schlüssel das Modul oder eine Einheiteninstanz
     * verwendet.
     * 
     * @param key  Schlüssel
     * @param value  Wert
     * @return  Wurde mit dem Schlüssel vorher schon Wert hinzugefügt, so wird
     *          dieser Wert hier zurückgegeben, ansonsten {@code null}.
     * @see Module2
     */
    public Object  put (Object key, Object value);

    /**
     * Gibt den Wert zum angegebenen Schlüssel zurück.
     * <P>
     * War vorher kein Wert zum Schlüssel abgelegt, so wird {@code null}
     * zurückgegeben.
     * 
     * @param key  Schlüssel
     * @return  Wert zum Schlüssel oder {@code null}
     */
    public Object  get (Object key);

    /**
     * Löscht ein Schlüssel-Wert-Paar von diesem Roblet-Repräsentanten.
     * <P>
     * Ist kein derartiges Paar aufzufinden, so wird {@code null} zurückgeben.
     * 
     * @param key  Schlüssel
     * @return  (bisheriger) Wert zum Schlüssel oder {@code null}
     */
    public Object  remove (Object key);

    /**
     * Beendet das Roblet und läßt eine Ausnahme an
     * die zugehörige Roblet-Anwendung zurückgeben.
     */
    public void  halt ();

}
