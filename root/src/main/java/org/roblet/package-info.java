// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

/**
 * This package contains definitions essential to the roblet world.

<P>
    Roblets are used in distributed applications.
    Such applications have components running on different network components.
    To make it work a couple of definitions are necessary
    that need to be the same in all such distributed components.
    And the most visible to the developer can be found here.
</P>

<P>
    This package defines {@code interface} classes only.
    They are implemented as part of the roblet libraries
    or need to be implemented by using applications
    or functionality providers.
    The central definition and a good point to start reading
    is the {@link Roblet}.
</P>


<HR>

<H2 id='unit'>{@link org.roblet.Unit} - Einheiten</H2>

<P>
    Ein Roblet-Server bietet einem <A href="#roblet">Roblet</A> die von ihm
    verwalteten Ressourcen mit Hilfe von Instanzen von Klassen an, die die
    Java&trade;-Schnittstelle {@link org.roblet.Unit} implementieren.&nbsp;
    Roblets können sich diese Instanzen über die (einzige) Methode des
    <A href="#robot">Kontexts</A> holen:&nbsp;
    {@link org.roblet.Robot#getUnit(Class)}.
</P>

<H3>Definition von Einheiten</H3>

<P>
    Der Themenkomplex <I>Einheit</I> (engl. <I>unit</I>) beginnt damit, daß
    ein <B>Hersteller</B> oder einfach ein Entwickler eine Ressource zur
    Verfügung stellen will.&nbsp;
    Eine Ressource kann ein Stück Hardware sein, aber auch eine Software
    oder oft auch eine Kombination aus beidem.
</P>

<P>
    Die Bereitstellung erfolgt, indem zunächst eine
    Java&trade;-Schnittstelle ({@code interface}) erstellt wird.&nbsp;
    Diese Schnittstelle muß {@link org.roblet.Unit}
    erweitern ({@code extends}).&nbsp;
    Damit wird eine Einheit <I>definiert</I>.&nbsp;
    Diese Definition sollte dokumentiert werden und kann dann an
    <B>Anwendungsentwickler</B> herausgegeben werden.
</P>

<H3>Implementierung von Einheiten</H3>

<P>
    Damit die Ressource genutzt werden kann, erstellt der Hersteller
    der anzubietenden Ressource passend noch eine Klasse,
    die die o.g. eigene Schnittstelle <I>implementiert</I>
    ({@code implements}).&nbsp;
    Diese Klasse platziert<SUP>4</SUP> er als Teil eines Moduls
    in einem Roblet-Server, so daß dieser bei Bedarf von ihr Instanzen
    erzeugen kann.
</P>

<P>
    Die Implementierung der Klasse kann reines Java&nbsp; sein, aber auch eine
    Anbindung anderer Software in C/C++ per JNI und damit praktisch auch
    der Zugriff auf beliebige Hardware und Bibliotheken ist möglich.&nbsp;
    Eine solche Implementierung kann mit Treibern in Betriebssystemen verglichen
    werden.
</P>

<H3>Instanzen von Einheiten</H3>

<P>
    Fragt ein <A href="#roblet">Roblet</A> nach der durch die
    Einheiten<I>definition</I> dargestellten Ressource, so gibt der Roblet-Server
    daraufhin eine <I>Instanz</I> der Einheiten<I>implementierung</I>
    zurück.&nbsp;
    Der Methode {@link org.roblet.Robot#getUnit(Class)} des
    <A href="#robot">Kontexts</A> wird dazu die Klasse der
    Einheiten-Schnittstelle als Parameter übergeben.
</P>

<H3>Zusammenfassung zur Thematik Einheiten</H3>

<P>
    Eine Einheiten<I>definition</I> ist ein Java&trade;-{@code interface}
    zuzüglich einer textuellen Beschreibung, d.h. der Dokumentation der Art und
    Weise, wie sie benutzt werden kann und soll, inkl. Grundzustand,
    Fehlerfälle, Sonderfälle etc.&nbsp;
    Diese Definition erweitert {@link org.roblet.Unit}.&nbsp;
    Die Einheiten<I>implementierung</I> (dann eine Java&trade;-{@code class})
    wird als Teil eines Roblet-Servers bereitgestellt.&nbsp;
    Ein Roblet erhält auf Anfrage zur Laufzeit jeweils eine <I>Instanz</I>
    einer solchen Klasse und kann damit die dargestellte Ressource benutzen.
</P>

<P>
    Wesentlich ist, daß nach Veröffentlichung der Definition einer Einheit keine
    Änderung mehr vorgenommen werden darf.&nbsp;
    Eine Änderung würde dazu führen, daß die bereits existierenden Anwendungen
    nicht mehr das vorfinden, wogeben sie entwickelt wurden.
</P>

<H3>Weiterentwicklung von Einheiten</H3>

<P>
    Eine Weiterentwicklung kann jedoch erfolgen, indem einfach eine weitere
    Definition gemacht wird.&nbsp;
    Diese Definition kann der vorigen sehr ähnlich sein.&nbsp;
    Auch für die neue Definition muß eine Implementierung bereitgestellt
    werden, die aber für den Fall, daß sie der vorigen sehr ähnlich ist,
    auch sehr ähnlich und daher einfach sein wird.&nbsp;
    Der Roblet-Server kann dann möglicherweise sogar beide
    Implementierungen zur Verfügung stellen.
</P>

<P>
    Auf diese Weise ist erfahrungsgemäß ein einfaches aber sehr tragfähiges
    Konzept der Versionierung gegeben.&nbsp;
    Alte Anwendungen verwenden noch die alte Einheit (sofern verfügbar),
    neue Anwendungen können wählen.
</P>

<H3>Weiterführende Informationen</H3>

<P>
    Auch zum Thema Einheiten sind Informationen und Beispiele
    auf <A href="http://roblet.org" target="_parent">roblet.org</A> verfügbar.
</P>


<HR>

<SUP>1</SUP>&nbsp;
<SMALL>
    Diese Klassen werden <I>Roblet-Klassen</I> genannt.
</SMALL>
<BR>
<SUP>2</SUP>&nbsp;
<SMALL>
    Diese Instanzen werden <I>Roblet-Instanzen</I> genannt.
</SMALL>
<BR>
<SUP>3</SUP>&nbsp;
<SMALL>
    Eine in einem Roblet-Server laufende Instanz wird <I>Roblet</I> genannt.
</SMALL>
<BR>
<SUP>4</SUP>&nbsp;
<SMALL>
    Die Beschreibung zum Roblet-Server enthält genauere Informationen darüber,
    wie die Implementierung im Server platziert wird.
</SMALL>

 *
 * @author Hagen Stanek
 */
package org.roblet;
