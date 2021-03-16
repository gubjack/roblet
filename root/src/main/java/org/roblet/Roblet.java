// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Instances of this kind <B>can be run by a roblet server</B>.

<P>
    To be send to the roblet server such an instance is serialized.
    This way the instance variables will be available on server side.
    To allow serialization the {@link java.io.Serializable} interface
    needs to be implemented in addition.
    Instance variables also need to do this.
</P>

<P>
    To serialize and send roblet instances to a roblet server
    the roblet client library needs to be used.
</P>

<P>
    An instance can be send multiple times to the same roblet server
    or to different.
    Instance variables can be changed before sending to a
    roblet server when necessary.
    Always the current instance state gets serialized by the
    client library and executed in the server server.
</P>

<P>
    The roblet server de-serializes to an instance and invokes
    {@link #execute(Robot)}.
    As part of the mentioned method all instance variables can be used.
</P>

<H2>Example</H2>
<P>
    Here is an example of a <EM>roblet class</EM> (see terminology below):
</P>
<BLOCKQUOTE><PRE>
public class  MyRoblet
     <B>implements org.roblet.Roblet</B>
                 , java.io.Serializable
{
    public boolean  variable;
    public Object  execute (org.roblet.Robot robot)
        throws Exception
    {
        if (variable)
            // ...

        MyUnit  myUnit = (MyUnit) robot. getUnit (MyUnit.class);
        if (myUnit == null)
            throw new Exception ("Didn't find MyUnit!");

        Object  result = null;
        // ...
        return result;
    }
    // ...
}
</PRE></BLOCKQUOTE>

<H2>Terminology</H2>
<P>
    Roblet is <I>the</I> central notion of the roblet libraries.
    In practice this term will be used for several different concepts.
    The context will make clear what is talked about.
</P>
<UL>
    <LI>Roblet <I>classes</I> implement the interface described here - as
        shown in the example above.</LI>
    <LI>Roblet <I>instances</I> will be created by an application at runtime
        out of a roblet class.
        These are just Java objects possibly with certain variables
        differing from instance to instance.
        <BLOCKQUOTE><PRE>
MyRoblet  myRoblet = new MyRoblet ();
myRoblet. variable = true;
// ...
        </PRE></BLOCKQUOTE>
    </LI>
    <LI>The roblet <I>application</I> is the Java application in the JVM
        that creates roblet instances.</LI>
    <LI>The roblet <I>client</I> is a library that takes care of serializing
        and sending roblet instances. It handles the communication including
        the results of the execution of a roblet instance.</LI>
    <LI>A roblet <I>server</I> gets contacted by roblet clients, takes,
        de-serializes and executes the roblet instances
        and handles the communication with the client.</LI>
    <LI>A <I>roblet</I> finally is a de-serialized roblet instance that is
        executed by a roblet server.</LI>
</UL>

 * @see #execute(Robot)
 * @see Robot
 * @see Unit
 * @author Hagen Stanek
 */
public interface  Roblet
{

    /**
     * An implementation of this method is called by a roblet server.

<P>
    This method works like {@link java.lang.Runnable#run()} for a normal
    Java {@link Thread} - but with some important differences.
</P>

<P>
    The parameter {@link Robot} allows accessing roblet server controlled
    funtionality and is guaranteed to never be {@code null}.
</P>

<P>
    The roblet server may very tightly restrict the access to the
    functionality given by the Java libraries.
    If access is possible then normally via {@link Unit} implementations
    that can be optained via {@link Robot#getUnit(Class)}.
</P>

<P>
    The amount of time this method takes is not limited.
    Secondary {@link Thread}'s started in the course of this method
    will keep the roblet running in the server even if this method ends.
    So a roblet ends as soon as it's last own thread ends.
</P>

<P>
    The result of this method may be any Java object but needs to be
    {@link java.io.Serializable}.
    This e.g. comprises arrays like {@code String[]}.
    Also {@code null} is valid result.
</P>

<P>
    For an application sending a roblet instance using the client library
    it means that the result handed back only needs to be casted suitably
    by using the <I>cast</I> operator "{@code (class name)}".
    In our example this would be {@code (String[])}.
</P>

<P>
    A roblet execution may also result in an exception of type
    {@link Exception}.
    Such an will be transferred back to the application.
    As the client will re-throw them the application can handle them the
    normaly way by catching.
</P>

<P>
    In case of an exception in a secondary thread that is not caught
    the roblet server will end this and all other threads of the roblet.
</P>

<P>
    Usually exceptions are {@link java.io.Serializable}
    but in case not the roblet server will instead transfer an exception to
    the application telling that problem.
</P>

     * @param   robot  to access server controlled functionality
     *                  - never {@code null}
     * @return  any instance but {@link java.io.Serializable}
     *                  including {@code null}
     * @throws  Exception  any but {@link java.io.Serializable}
     */
    public Object  execute (Robot robot)
        throws Exception;

}
