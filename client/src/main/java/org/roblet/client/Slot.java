// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;

import org.roblet.Remote;
import org.roblet.Roblet;
import org.roblet.Robot;
import org.roblet.server.unit.net.NetUnit;

import genRob.genControl.client.Client;


/**
 * This can be used to send {@link Roblet} instances,
 * organize communication with them and if needed terminate them.

<P>
    Be aware that there is no timeout when working with slots.
    There is only the notion of validity.
    A slot is valid as long it has not been {@link #close()}d
    or got invalid on server side.
    The latter can happen if e.g. the server restarts and thus trivially
    does not have the slot anymore reserved or decided for other reasons
    to remove the slot.
</P>
<P>
    The following example sends a roblet to the roblet server running
    at the local host.
    The local host is represented by {@code null} and easily can be replaced
    by e.g. {@code args[0]} to allow providing the server name and if needed
    port as a command line argument.
    In addition to simplify the class is used as roblet application and as
    roblet class - so starting (A) first locally as a normal Java application
    ({@code main(...)}),
    then (B) instantiating the roblet ({@code new Test()}),
    (C) sending that to the server ({@code slot. run (...)}) including waiting
    for a result,
    (D) executing the roblet in the server ({@code execute(...)}),
    (E) handing back a result ({@code return ...})
    and finally (F) printing the result to standard out.
</P>
<BLOCKQUOTE><PRE>
package sample;
import java.io.Serializable;
import java.time.Instant;
import org.roblet.Roblet;
import org.roblet.client.Slot;

public class  Test  implements Roblet&lt;Instant&gt;, Serializable
{
    public static void  main (String[] args) throws InterruptedException
    {
        try (Slot slot = new Slot (null)) {
            System.out.println (slot. run (new Test ()));
        }
    }
    public Instant  execute (org.roblet.Robot robot)
    {
        return Instant. now ();
    }
}
</PRE></BLOCKQUOTE>

 * @author Hagen Stanek
 */
@SuppressWarnings("deprecation")
public class  Slot
    implements AutoCloseable
{

    /**
     * This is a shorthand for calling {@code Slot(server,null,null)}.
     * @param server  name of the host the server is listening on
     *          possibly including a port number like {@code host.domain:2001} 
     * @throws InterruptedException  in case the calling thread is interrupted
     * @see #Slot(String, Robot, Logger)
     */
    public  Slot (String server)
        throws InterruptedException
    {
        this (server, null, null);
    }
    /**
     * This is a shorthand for calling {@code Slot(server,robot,null)}.
     * 
     * @param server  name of the host the server is listening on
     *          possibly including a port number like {@code host.domain:2001} 
     * @param robot  when used inside a roblet otherwise {@code null}
     * @throws InterruptedException  in case the calling thread is interrupted
     * @see #Slot(String, Robot, Logger)
     */
    public  Slot (String server, Robot robot)
        throws InterruptedException
    {
        this (server, robot, null);
    }
    /**
     * Establish a connection to a server and establish basic communication.
     * <EM>Note that there is no timeout.</EM>
     * 
     * @param server  name of the host the server is listening on
     *          possibly including a port number like {@code host.domain:2001} 
     * @param robot  when used inside a roblet otherwise {@code null}
     * @param logger  if logging is desired otherwise {@code null}
     * @throws InterruptedException  in case the calling thread is interrupted
     */
    public  Slot (String server, Robot robot, Logger logger)
        throws InterruptedException
    {
        if (logger == null)
            logger = new NoLogger ();
        NetUnit  n =  robot == null  ?  null  :  robot. getUnit (NetUnit.class);
        Client  c = new Client (logger, n);
        genRob.genControl.client.Slot  slot;
        for (;;)
            try
            {
                slot = c. getServer (server). getSlot ();
                break;
            }
            catch (SlotException e)
            {
                // ignore and try again immediately
            }
        this.slot = slot;
    }
    private final genRob.genControl.client.Slot  slot;

    /**
     * Invalidate the slot, stop abruptly communication and free resources.
     * <EM>Note that there is no timeout.</EM>
     */
    public void  close ()
    {
        slot. close ();
    }

    /**
     * Send a roblet to the server that is then executing it.
     * This includes marshalling the roblet instance, sending
     * and waiting for the result.
     * Exceptions may happen while marshalling, inside the roblet and
     * when unmarshalling the result - or if the slot is invalid.
     * <EM>Note that there is no timeout.</EM>
     * Any roblet running in the slot will be stopped before.
     * If {@code null} is send as a roblet the slot on server side is
     * simply empty.
     * @param <RESULT>  the result type of the {@code Roblet#execute(Robot)}
     * @param roblet  an instance of a {@code Roblet}
     * @return  the result of the {@code Roblet#execute(Robot)}
     * @throws InterruptedException  in case the calling thread is interrupted
     * @throws MarshalException  in case (un)marshalling failed
     * @throws SlotException  in case the slot is invalid
     */
    public <RESULT> RESULT  run (Roblet<RESULT> roblet)
        throws InterruptedException, MarshalException, SlotException
    {
        try
        {
            return slot. run (roblet);
        }
        catch (InterruptedException e)
        {
            throw e;
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new RuntimeException (e);
        }
    }

    /**
     * Obtain a proxy for a remote object.
     * The remote object resides in the roblet.
     * It needs to exist latest when methods of the proxy are being called.
     * @param <REMOTE>  the type of the remote object
     * @param clazz  class of the type of the remote object
     * @return  proxy for the remote object
     * @throws SlotException  in case the slot is invalid
     */
    @SuppressWarnings("unchecked")
    public <REMOTE extends Remote> REMOTE  obtain (Class<REMOTE> clazz)
        throws SlotException
    {
        return (REMOTE) slot. obtainProxy (clazz);
    }

    /**
     * Offeres a remote object for the roblet running in the slot in the
     * server.
     * @param <REMOTE>  the type of the remote object
     * @param object  remote object
     * @throws SlotException  in case the slot is invalid
     * @see #revoke(Remote)
     */
    public <REMOTE extends Remote> void  offer (REMOTE object)
        throws SlotException
    {
        slot. offerRemote (object);
    }

    /**
     * Revokes a previously offered remote object.
     * The method will not complain if the object given
     * is not offered (anymore).
     * @param <REMOTE>  the type of the remote object
     * @param object  remote object
     * @throws SlotException  in case the slot is invalid
     * @see #offer(Remote)
     */
    public <REMOTE extends Remote> void  revoke (REMOTE object)
        throws SlotException
    {
        slot. revokeRemote (object);
    }

}
