// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.connect;

import  genRob.genControl.client.protocol.Transport;
import  genRob.genControl.client.protocol.TransportContext;
import  genRob.genControl.client.protocol.Tube;
import  genRob.genControl.client.server.ID;
import  java.io.IOException;
import  java.lang.ref.WeakReference;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;
import  org.roblet.protocol.Protocol;
import org.roblet.server.unit.net.Socket;


/**
 * Dient dem TCP-Verbindungswiederaufbau solange, bis eine Verbindung besteht
 * und ein {@link Tube} erzeugt werden konnte oder der {@link Transport}
 * verschwindet.&nbsp;
 * Eine Instanz diesen Typs wird vom {@link Transport} erzeugt, wenn die
 * TCP-Verbindung verlorengegangen ist.&nbsp;
 * Der {@link Transport} wird dabei <I>schwach</I> abgespeichert und sein
 * verschwinden als Indikator verwendet, daß kein Verbindungsaufbau wieder
 * versucht werden sollte.
 * 
 * @author Hagen Stanek
 */
public class  ReconnectThread
    extends Thread
{

    /**
     * initialise the thread with data including name, group, priority, daemon
     * @param rTransportContext  context to be used for reconnection
     * @param iCount  reconnection count
     * @param rTransport  existing transport instance
     */
    public  ReconnectThread (TransportContext rTransportContext, int iCount
                        , Transport rTransport)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + "^" + iCount);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rConnectContext = rTransportContext. mf_rConnectContext;
        mf_rID = rTransportContext. mf_rID;
        mf_iTransport = rTransportContext. mf_iTransport;
        mf_rWeakReference_Transport = new WeakReference<Transport> (rTransport);
    }
    private final Logger  mf_rLogger;
    private final ConnectContext  mf_rConnectContext;
    private final ID  mf_rID;
    private final int  mf_iTransport;
    private final WeakReference<Transport>  mf_rWeakReference_Transport;
    public void  run ()
    {
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "START");
        try
        {
            work ();
        }
        finally
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, "END");
        }
    }
    private void  work ()
    {
        try
        {
            loop ();
        }
        catch (InterruptedException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, "INTERRUPTED");
        }
        catch (Transport.ClosedException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, "Transport already closed");
        }
        catch (SlotException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, e. getMessage ());

            Transport  rTransport = mf_rWeakReference_Transport. get ();
            if (rTransport != null)
                rTransport. close (e);
        }
    }
    private void  loop ()
        throws InterruptedException, Transport.ClosedException, SlotException
    {
        while (mf_rWeakReference_Transport. get () != null)
        {
            try
            {
                connect ();
            }
            catch (IOException e)
            {
                if (mf_rLogger. connect)
                    mf_rLogger. connect (this, e);

                if (mf_rLogger. connect)
                    mf_rLogger. connect (this, "SLEEP for 5 seconds");
                Thread. sleep (5000);

                continue;
            }
            break;
        }
    }
    private void  connect ()
        throws InterruptedException, Transport.ClosedException, IOException
                , SlotException
    {
        // Besorge mir eine Socket
        if (mf_rLogger. connect)
            mf_rLogger. connect (this
                    , "Connect to " + mf_rConnectContext. mf_rName);
        Socket  rSocket = mf_rConnectContext. createSocket ();
            // Die Socket ist jetzt da.

        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Local address is "
                                    + rSocket. getLocalSocketAddress ());

        try
        {
            connectServer (rSocket);
        }
        catch (IOException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, e);

            // Ensure to free socket
            closeSocket (rSocket);

            // rethrow to allow loop to work
            throw e;
        }
    }
    private void  closeSocket (Socket rSocket)
    {
        if (mf_rLogger. connect)
            mf_rLogger. connect (this
                    , "CLOSE socket due to previous exception ...");
        try
        {
            rSocket. close ();

            if (mf_rLogger. connect)
                mf_rLogger. connect (this
                        , "... closed socket");
        }
        catch (IOException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this
                        , "... exception closing socket");
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, e);
        }
    }
    private void  connectServer (Socket rSocket)
        throws IOException, InterruptedException, Transport.ClosedException
                , SlotException
    {
        Tube  rTube = mf_rConnectContext. createTube (rSocket);

        // Lese Server-ID
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Read server ID");
        ID  rID = rTube. readID ();
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Server ID is " + rID);

        if (! rID. equals (mf_rID))
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this
                        , "Close tube since this is not our server");
            rTube. close ();

            throw new SlotException ("Target server has different ID");
        }

        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Demand transport");
        // Verbinde zu Anschluß
        rTube. writeTransport (mf_iTransport);
        // Hole Anschlußnummer
        int  iTransport = rTube. readTransport ();
        if (iTransport == Protocol. TUBE_TransportUnknown)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this
                    , "Close tube since server does not know our transport");
            rTube. close ();

            throw new SlotException (
                    "Target server does not know our transport");
        }

        Transport  rTransport = mf_rWeakReference_Transport. get ();
        if (rTransport != null)
            rTransport. reconnect (rTube);
        else
            rTube. close ();
    }
}
