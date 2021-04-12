// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet;


/**
 * Instances of this kind <B>can be executed by a roblet server</B>.

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
    {
        if (variable)
            // ...

        MyUnit  myUnit = robot. getUnit (MyUnit.class);
        if (myUnit == null)
            throw new RuntimeException ("Didn't find MyUnit!");

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

 * @param <RESULT>  result type of {@link #execute(Robot)}
 * @see #execute(Robot)
 * @see Robot
 * @see Unit
 * @author Hagen Stanek
 */
public interface  Roblet <RESULT>
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
    Secondary {@link Thread}'s may be started in the course of this method.
    The thread of this method and the secondary threads
    are said to belong to the roblet.
</P>

<P>
    A roblet ends as soon as it's last belonging thread ends.
    So secondary {@link Thread}'s will keep the roblet running in the server
    even if the method described here ends.
</P>

<P>
    The result of this method may be any Java object but should be
    {@link java.io.Serializable}.
    It is worth mentioning that this comprises arrays like {@code String[]}.
    And also {@code null} is a valid result.
</P>

<P>
    In case of an uncaught exception in any thread belonging to the roblet
    the roblet server will end the roblet.
    All threads of the roblet will end at once.
</P>

<P>
    The server will transfer the exception back to the application
    allowing the client to re-throw.
    This way the application can handle such exception as usual by catching.
</P>

     * @param   robot  to access server controlled functionality
     *                  - never {@code null}
     * @return  any instance but {@link java.io.Serializable}
     *                  including {@code null}
     */
    public RESULT  execute (Robot robot);

}
