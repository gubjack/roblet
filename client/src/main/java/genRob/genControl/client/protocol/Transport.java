// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  genRob.genControl.client.connect.ReconnectThread;
import  genRob.genControl.client.server.Invoke;
import  java.io.IOException;
import  org.roblet.Roblet;
import org.roblet.client.Logger;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;


/**
 * Dies stellt letztendlich die TCP-Verbindung des Klienten zu einem Server
 * dar.&nbsp;
 * Dabei wird hierüber stets ein Verbindungswiederaufbau vorgenommen, bis die
 * jeweilige Instanz aufgeräumt wird.
 * <P>
 * {@link genRob.genControl.client.Server}
 * und {@link genRob.genControl.client.Slot}
 * halten direkte Referenzen hierauf, so daß
 * eine Instanz solange nicht aufgeräumt wird, solange noch
 * Server- oder Fach-Repräsentanten existieren.&nbsp;
 * Für angebotene ferne Instanzen wird im {@link TransportContext} eine Referenz
 * bei Bedarf gehalten.
 * 
 * @author Hagen Stanek
 */
public class  Transport
    implements ErrorHandler
{

    /**
     * Initialise a transport.
     * @param rTransportContext  context to be used
     */
    public  Transport (TransportContext rTransportContext)
    {
        mf_rTransportContext = rTransportContext;
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = new Queues (mf_rLogger);
        mf_rSlotCraft = new SlotCraft (mf_rLogger, mf_rQueues);
        mf_rRunCraft = new RunCraft (mf_rLogger, mf_rQueues);
        mf_rResourceCraft = new ResourceCraft (mf_rLogger, mf_rTransportContext
                                                , mf_rQueues);
        mf_rInvokeInCraft = new InvokeInCraft (mf_rLogger, mf_rTransportContext
                                                , mf_rQueues);
        mf_rInvokeOutCraft = new InvokeOutCraft (mf_rLogger, mf_rQueues);

        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "Transport ID is "
                        + rTransportContext. mf_iTransport);
    }
    private final TransportContext  mf_rTransportContext;
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final SlotCraft  mf_rSlotCraft;
    private final RunCraft  mf_rRunCraft;
    private final ResourceCraft  mf_rResourceCraft;
    private final InvokeInCraft  mf_rInvokeInCraft;
    private final InvokeOutCraft  mf_rInvokeOutCraft;

    // Object
    @SuppressWarnings("deprecation")
    protected void  finalize ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "finalize () - begin");

        try
        {
            finish ();
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "finalize () - end");
        }
    }


    /**
     * thrown when the server already denied the transport
     * @author Hagen Stanek
     */
    public static class  ClosedException
        extends Exception
    {
    }

    private byte  m_byteProtocol;
    /**
     * protocol version
     * @return protocol version
     */
    public byte  getProtocol ()
    {
        return m_byteProtocol;
    }

    /**
     * Connect the tranport using a given tube.
     * @param rTube  tube
     * @throws InterruptedException  in case the calling thread is interrupted
     */
    public void  connect (Tube rTube)
        throws InterruptedException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "connect (" + rTube + ") begin");

        try
        {
            if (m_rSlotException != null)
                throw new RuntimeException ("Already closed");

            m_rBond = new Bond (m_rBond, mf_rTransportContext, mf_rQueues
                                , this, rTube
                                , mf_rSlotCraft, mf_rRunCraft
                                , mf_rResourceCraft
                                , mf_rInvokeInCraft, mf_rInvokeOutCraft);
            m_byteProtocol = m_rBond. mf_rTube. mf_byteProtocol;
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "connect (" + rTube + ") end");
        }
    }
    private Bond  m_rBond;
    private void  disconnect ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "disconnect () begin");

        try
        {
            m_rBond. _Bond ();
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "disconnect () end");
        }
    }
    /**
     * Reconnect to a transport with a given tube
     * @param rTube  tube to be used
     * @throws IOException in case of input or output stream problems
     * @throws InterruptedException  in case the calling thread is interrupted
     * @throws ClosedException  in case the server already denied the transport
     */
    public synchronized void  reconnect (Tube rTube)
        throws IOException, InterruptedException, ClosedException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "reconnect (" + rTube + ") begin");

        try
        {
            if (m_rSlotException != null)
                throw new ClosedException ();

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "Disconnect present tube");
            disconnect ();

            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "Negotiate confirmations");
            rTube. writeReceived (mf_rQueues. getReceived ());
            int  iReceived = rTube. readReceived ();

            mf_rQueues. connect (iReceived);

            connect (rTube);
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "reconnect (" + rTube + ") end");
        }
    }
    /**
     * Close the transport
     * @param rSlotException  exception indicating the reason
     */
    public synchronized void  close (SlotException rSlotException)
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "close () begin");

        try
        {
            if (m_rSlotException != null)
            {
                if (mf_rLogger. transport)
                    mf_rLogger. transport (this, "already closed");
                return;
            }
            m_rSlotException = rSlotException;

            disconnect ();

            // Alle wartenden Threads müssen noch beendet und
            // dem Rufer Fehlermeldungen gegeben werden
            mf_rQueues. cancel (rSlotException);
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this, "close () end");
        }
    }

    private SlotException  m_rSlotException = null;
    /**
     * Test if a transport is closed due to server denial.
     * @throws SlotException  in case the server denies the transport
     */
    public void  checkForClosed ()
        throws SlotException
    {
        if (m_rSlotException == null)
            return;
        throw m_rSlotException;
    }


    // ErrorHandler
    public void  handleIOException (Bond rBond)
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "handleIOException (" + rBond + ") begin");

        try
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "start reconnect thread for " + rBond);
            new ReconnectThread (mf_rTransportContext, rBond. mf_iCount
                                    , this)
                . start ();
        }
        finally
        {
            if (mf_rLogger. transport)
                mf_rLogger. transport (this
                        , "handleIOException (" + rBond + ") end");
        }
    }


    private void  finish ()
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "finish () begin");

        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "start close thread");
        close (new SlotException ("Communication abandoned"));

        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "finish () end");
    }


    /**
     * demand a slot from the server
     * @return  slot
     * @throws InterruptedException  in case the thread waiting is interrupted
     * @throws SlotException  in case the server denies the transport
     */
    public SlotData  getSlot ()
        throws InterruptedException, SlotException
    {
        checkForClosed ();

        return mf_rSlotCraft. getSlot ();
    }

    /**
     * run a roblet in a slot.
     * @param iSlot  slot
     * @param rRoblet  roblet instance to be run
     * @return  result of the roblet
     * @throws InterruptedException  in case the thread waiting is interrupted
     * @throws MarshalException  in case any transport conversion fails
     * @throws SlotException  in case the server denies the transport
     * @throws Exception  all other including those thrown by the roblet
     */
    public <RESULT> RESULT  run (int iSlot, Roblet<RESULT> rRoblet)
        throws InterruptedException, MarshalException
                , SlotException, Exception
    {
        checkForClosed ();

        return mf_rRunCraft. run (iSlot, rRoblet);
    }

    /**
     * Call a method of a far instance for a slot.
     * @param iSlot  slot
     * @param rInvoke  details to the call
     * @return  result of the called method
     * @throws InterruptedException  in case the thread waiting is interrupted
     * @throws MarshalException  in case any transport conversion fails
     * @throws SlotException  in case the server denies the transport
     * @throws Exception  all other including those thrown by the method called
     */
    public Object  invoke (int iSlot, Invoke rInvoke)
        throws InterruptedException, MarshalException
                , SlotException, Exception
    {
        checkForClosed ();

        return mf_rInvokeOutCraft. invoke (iSlot, rInvoke);
    }

    /**
     * Assign a local instance to a slot.
     * @param iSlot  slot
     * @param object  local instance
     * @throws SlotException  in case the server denies this transport
     */
    public void  offerRemote (int iSlot, Object object)
        throws SlotException
    {
        checkForClosed ();

        mf_rTransportContext. offerRemote (iSlot, object, this);
            // this mit übergeben, damit diese Instanz nicht aufgeräumt wird
    }
    /**
     * Unassign a local instance from a slot.
     * @param iSlot  slot
     * @param object  local instance
     * @throws SlotException  in case the server denies this transport
     */
    public void  revokeRemote (int iSlot, Object object)
        throws SlotException
    {
        checkForClosed ();

        mf_rTransportContext. revokeRemote (iSlot, object);
            // this wurde gegebenenfalls freigegeben.
    }

}
