// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.modules;

import  java.io.Serializable;


/**
 * Eine Instanz diesen Typs dient einem Modul zum <B>Loggen</B>
 * in das Logbuch des Roblet-Servers.
 * <p>
 * Über {@link ModuleContext#getLogger()} kann sich ein Modul eine Instanz
 * zur Laufzeit besorgen.
 * 
 * @see Module2
 * @author Hagen Stanek
 */
public interface  Logger
{

    /**
     * Erzeugt einen Log-Eintrag mit optionalem Teil, Instanz und Nachricht.
     * <P>
     * Als <B>Teil</B> ({@code part}) wird üblicherweise eine Zeichenkette,
     * wie z.B. {@code "main"}, angegeben.
     * <P>
     * <B>Instanz</B> ({@code instance}) kann eine wirkliche Instanz einer
     * Klassen sein oder auch eine Klasse.&nbsp;
     * Letzteres ist interessant, wenn man eine Klassenmethode hat
     * und dann in deren Kontext natürlicherweise keine Klasseninstanz existiert.
     * <P>
     * Eine <B>Nachricht</B> ({@code message}) ist in den meisten Fällen
     * einfach eine
     * Zeichenkette ({@link String}) oder eine Zusammensetzung, die
     * letztlich eine Zeichenkette ergibt.&nbsp;
     * Aber auch die meisten Ausnahmen sind serialisierbar und damit
     * direkt als Nachricht verwendbar.
     * <P>
     * Teil und Nachricht müssen serialisierbar sein.&nbsp;
     * Dazu gehört auch, daß alle referenzierten Objekte serialisierbar
     * sind.
     * <P>
     * Teil und Nachricht müssen alle Referenzen (und deren Referenzen
     * und so fort) als {@code final} kennzeichnen.&nbsp;
     * Damit soll sichergestellt werden, daß ein Log-Eintrag
     * nicht nachträglich (fälschlicherweise) verändert werden kann.
     * <P>
     * Wird beim Loggen festgestellt, daß o.g. Bedingungen nicht eingehalten
     * werden, so wird die {@link java.lang.Object#toString()}-Methode
     * benutzt, um so einen {@link java.lang.String} zu erzeugen,
     * der dann stattdessen verwendet wird.&nbsp;
     * Ein die Nachricht interpretierendes Programm kann dann
     * später die Nachricht u.U. nicht mehr speziell verarbeiten.&nbsp;
     * Die genannten Tests und gegebenenfalls Wandlungen geschehen im
     * Kontext des loggenden Threads.
     * <P>
     * Jeder der Parameter darf {@code null} sein.
     * 
     * @param part  Teil des Moduls
     * @param instance  Instanz oder Klasse, wo der Fehler auftrat
     * @param message  Nachricht
     */
    void  log (Serializable part, Object instance, Serializable message);

}
