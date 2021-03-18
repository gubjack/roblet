// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * <B>Functionality controlled by a roblet server</B> is of this type.

<P>
    This {@code interface} serves to identify the functionality in question.
    Any actual definition and implementation will provide the real function.
    See below for terminology, contract and versioning.
</P>

<P style="font-size:smaller">
    In a strict sense this type is not needed.
    But the experience is that by having this type
    any dicussion, code reading and quality improves.
</P>

<H2>Terminology</H2>

<UL>
    <LI>
        Unit <I>definitions</I> are {@code interface} classes with
        actual functionality specifyed in methods
        by <SPAN style="color:darkgreen">extending</SPAN> this type:
        <BLOCKQUOTE><PRE>
public interface  MyUnit
    <B><SPAN style="color:darkgreen">extends</SPAN> org.roblet.Unit</B>
{
    // Answer to the Ultimate Question of Life,
    // the Universe, and Everything
    public int  calculate ();
}
        </PRE></BLOCKQUOTE>
    </LI>

    <LI>
        Unit <I>implementations</I> really provide a functionality 
        as part of a roblet server and need to
        <SPAN style="color:darkred">implement</SPAN> the <I>definition</I>:
        <BLOCKQUOTE><PRE>
class  MyUnitImpl
     <B><SPAN style="color:darkred">implements</SPAN> MyUnit</B>
{
    public int  calculate ()
    {
        return 42;
    }
}
        </PRE></BLOCKQUOTE>
    </LI>

    <LI>
        Unit <I>instances</I> of an implementation
        are obtained by an interested {@link Roblet} 
        using {@link Robot#getUnit(Class)}:
        <BLOCKQUOTE><PRE>
public class  MyRoblet
     implements org.roblet.Roblet, java.io.Serializable
{
    public Object  execute (org.roblet.Robot robot)
        throws Exception
    {
        MyUnit  <B>myUnit</B> = robot. getUnit (MyUnit.class);
        int  answer = <B>myUnit. calculate ()</B>;
        return answer;
    }
}
        </PRE></BLOCKQUOTE>
    </LI>
</UL>

<H2>Contract</H2>
<P>
    In case a roblet ends any ressources hold by a unit instance will be
    freed by the roblet server automatically.
</P>

<H2>Versioning</H2>
<P>
    Once a unit definition has been published to external (unknown) users
    changes should be avoided as otherwise using roblets will not compile
    anymore or will behave differently in the roblet server.
    In addition if a third party is implementing a unit definition similar
    problems would come up.
</P>
<P>
    But further devopment of unit definitions can simply be done by
    providing new definitions with new names.
    Good experience has been made with just adding a number
    to the new unit definition name.
</P>
<P>
    It is also technically well possible to {@code extend} unit definitions
    to add new methods:
</P>
<BLOCKQUOTE><PRE>
public interface  MyUnit2
    <B>extends</B> MyUnit
{
    public String  newMethod ();
}
</PRE></BLOCKQUOTE>
<P>
    This way a roblet server could provide the same unit instance for
    roblets using either definition:
</P>
<BLOCKQUOTE><PRE>
MyUnit  myUnit = robot. getUnit (MyUnit.class);
MyUnit2  myUnit2 = robot. getUnit (MyUnit2.class);
assert (myUnit. calculate () == myUnit2. calculate ());
</PRE></BLOCKQUOTE>

 * @see Robot#getUnit(Class)
 * @author Hagen Stanek
 */
public interface  Unit
{
    // no attributes nor methods
    // just a marker interface
}
