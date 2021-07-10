// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;

import  org.roblet.Unit;


/**
 * Eine Instanz diesen Typs verwaltet alle Besitzrechte eines Moduls
 * und den Einheitenbesitz eines jeden Roblets in Hinsicht auf
 * dieses Modul und fungiert daher
 * als eine Art <b>Grundbuch</b>.&nbsp;
 * Jedes Modul hat nur eine Ableitung diesen Typs mit einer Instanz.
 * <p>
 * Roblets kommen mit Besitzrechten niemals direkt in Verbindung,
 * sondern nur mit Rechten an Einheiten.&nbsp;
 * Eine Einheit kann für ihre Arbeit verschiedene Ressourcen
 * benötigen.&nbsp;
 * Diese Ressourcen werden dann gegebenenfalls der Einheit dadurch verbrieft,
 * dass sie dem Roblet (vertreten durch das Fach, in dem es läuft)
 * verbrieft wird.
 * <p>
 * Einheiten vollziehen jegliche Aktivitäten nur nach Aufruf durch
 * ein Roblet.&nbsp;
 * Insbesondere führen Einheiten mit Besitzrechten verbundene
 * Aktivitäten nur dann aus, wenn das aufrufende Roblet
 * die aufgerufene Einheit besitzt (Einheitenbesitz).
 * <p>
 * Wird somit der Besitz einer Einheit von einem Roblet angefordert,
 * so müssen alle damit verknüpften Besitzrechte (an Ressourcen)
 * verfügbar sein oder verfügbar gemacht werden.&nbsp;
 * Den Zusammenhang zwischen Roblet und Besitzrechten stellt das
 * Grundbuch dabei über das Fach her, in dem das Roblet läuft.
 * 
 * @see  org.roblet.Roblet
 * @see  Slot
 * @see  org.roblet.Unit
 * @see  Module
 * @deprecated  <SPAN style="color:darkgrey">Entfällt in Zukunft, da sich das
 *              Modul-Konzept geändert hat.</SPAN>
 * @author Hagen Stanek
 */
@Deprecated(forRemoval=true)
public interface  Registry
{

    /**
     * Bittet das Grundbuch, dem Roblet (vertreten durch das angegebene
     * Fach) die angegebene Einheit als Besitz einzutragen.&nbsp;
     * Das schliesst entsprechend die Übertragung von Besitzrechten
     * an Ressourcen ein.
     * <p>
     * Besitzt kein anderes Roblet diese Einheit (und die damit
     * verknüpften Besitzrechte an Ressourcen),
     * so erfolgt die Eintragung und
     * diese Methode gibt {@code true} zurück.&nbsp;
     * Ebenso wird {@code true} zurückgegeben, wenn das Roblet
     * die Einheit besitzt.
     * <p>
     * Benötigt die betreffende Einheit keine Besitzrechte an Ressourcen
     * oder wird von dem Module gar nicht verwaltet, so gibt diese Methode
     * {@code false} zurück und nimmt entsprechend keinerlei Eintragung
     * vor.
     * <BLOCKQUOTE>
     * <B>Achtung:</B>&nbsp;
     * Wird das Grundbuchkonzept nicht benutzt, so muß diese Methode
     * {@code false} zurückliefern oder einfacher noch
     * {@link Module#getRegistry()} sollte {@code null} zurückliefern.
     * </BLOCKQUOTE>
     *
     * @param   rSlot   Fach mit dem anfragenden Roblet
     * @param   rUnit   Instanz der Einheit, die in Besitz kommen soll
     * @return  {@code true}, wenn das Roblet nun Besitzer der
     *          Einheit ist oder bereits war
     * @see  #force(Slot,Unit)
     * @see  #free(Slot,Unit)
     */
    public boolean  claim (Slot rSlot, Unit rUnit);

    /**
     * Zwingt das Grundbuch, dem Roblet (vertreten durch das angegebene
     * Fach) die angegebene Einheit als Besitz einzutragen,
     * sofern grundsätzlich möglich.&nbsp;
     * Das schliesst entsprechend die Übertragung von Besitzrechten
     * an Ressourcen ein.
     * <p>
     * War bisher ein anderes Roblet der Besitzer, so wird stellvertretend
     * dessen Fach zurückgegeben.&nbsp;
     * Ansonsten ist der Rückgabewert {@code null}.
     * <p>
     * Benötigt die betreffende Einheit keine Besitzrechte an Ressourcen
     * oder wird von dem Module gar nicht verwaltet, so gibt diese Methode
     * {@code null} zurück und nimmt entsprechend keinerlei Eintragung
     * vor.
     * <BLOCKQUOTE>
     * <B>Achtung:</B>&nbsp;
     * Wird das Grundbuchkonzept nicht benutzt, so muß diese Methode
     * {@code null} zurückliefern oder einfacher noch
     * {@link Module#getRegistry()} sollte {@code null} zurückliefern.
     * </BLOCKQUOTE>
     *
     * @param   rSlot   Fach mit dem fordernden Roblet
     * @param   rUnit   Instanz der Einheit, die in Besitz kommen wird
     * @return  Fach des Roblets, welches vorher Besitzer war
     *          oder {@code null}
     * @see  #claim(Slot,Unit)
     * @see  #free(Slot,Unit)
     */
    public Slot  force (Slot rSlot, Unit rUnit);

    /**
     * Trägt für ein Roblet (vertreten durch das angegebene
     * Fach) den möglichen Besitz der angegebenen Einheit
     * aus dem Grundbuch aus.&nbsp;
     * Das schliesst entsprechend möglicherweise die Freigabe
     * von Besitzrechten an Ressourcen ein.
     * <p>
     * Ist das Roblet vor dem Aufruf dieser Methode der Besitzer der
     * Einheit, so ist nach dem Aufruf kein Roblet mehr Besitzer dieser
     * Einheit.
     * <p>
     * Ist das Roblet vor dem Aufruf dieser Methode nicht der Besitzer
     * der Einheit, so bleiben die Besitzverhältnisse bestehen, d.h.
     * ein anderes Roblet bleibt Besitzer oder kein Roblet ist
     * Besitzer.
     * <BLOCKQUOTE>
     * <B>Achtung:</B>&nbsp;
     * Wird das Grundbuchkonzept nicht benutzt, so darf diese Methode
     * nichts tun oder einfacher noch
     * {@link Module#getRegistry()} sollte {@code null} zurückliefern.
     * </BLOCKQUOTE>
     *
     * @param   rSlot   Fach mit dem freigebenden Roblet
     * @param   rUnit   Instanz der Einheit, die freigegeben werden soll
     * @see  #claim(Slot,Unit)
     * @see  #force(Slot,Unit)
     * @see  #free(Slot)
     */
    public void  free (Slot rSlot, Unit rUnit);

    /**
     * Trägt für ein Roblet (vertreten durch das angegebene
     * Fach) sämtlichen Besitz an Einheit
     * aus dem Grundbuch aus.&nbsp;
     * Das schliesst entsprechend die Freigabe
     * von Besitzrechten an Ressoucen ein.
     * <BLOCKQUOTE>
     * <B>Achtung:</B>&nbsp;
     * Wird das Grundbuchkonzept nicht benutzt, so darf diese Methode
     * nichts tun oder einfacher noch
     * {@link Module#getRegistry()} sollte {@code null} zurückliefern.
     * </BLOCKQUOTE>
     * 
     * @param   rSlot   Fach mit dem freigebenden Roblet
     * @see  #free(Slot,Unit)
     */
    public void  free (Slot rSlot);

}
