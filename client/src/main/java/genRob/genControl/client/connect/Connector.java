// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.connect;

import  genRob.genControl.client.base.ServerImpl;
import  genRob.genControl.client.protocol.Transport;
import  genRob.genControl.client.protocol.TransportContext;
import  genRob.genControl.client.protocol.Tube;
import  genRob.genControl.client.server.ID;
import  java.io.IOException;

import org.roblet.client.Logger;
import  org.roblet.protocol.Protocol;
import org.roblet.server.unit.net.Socket;


/**
 * Encapsulate creating a server representation
 * @see #getServer()
 * @author Hagen Stanek
 */
public class  Connector
{

    /**
     * initialise with a context
     * @param rConnectContext  context for connection
     */
    public  Connector (ConnectContext rConnectContext)
    {
        mf_rConnectContext = rConnectContext;
        mf_rLogger = rConnectContext. mf_rLogger;
    }
    private final ConnectContext  mf_rConnectContext;
    private final Logger  mf_rLogger;

    /**
     * create a server representation
     * @return  server representation
     * @throws IOException  in case communication with the server failed
     */
    public ServerImpl  getServer ()
        throws IOException
    {
        // Besorge mir eine Socket
        if (mf_rLogger. connect)
            mf_rLogger. connect (this
                    , "Connect to " + mf_rConnectContext. mf_rName);
        Socket  rSocket = mf_rConnectContext. createSocket ();
        // Die Socket ist jetzt da.
        if (mf_rLogger. connect)
            mf_rLogger. connect (this
                    , "Local address is " + rSocket. getLocalSocketAddress ());

        try
        {
            return connectServer (rSocket);
        }
        catch (IOException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, e);

            if (mf_rLogger. connect)
                mf_rLogger. connect (this
                        , "Close socket due to previous exception ...");
            closeSocket (rSocket);

            throw e;
        }
    }
    private void  closeSocket (Socket rSocket)
    {
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "CLOSE socket ...");
        try
        {
            rSocket. close ();

            if (mf_rLogger. connect)
                mf_rLogger. connect (this, "... closed socket");
        }
        catch (IOException e)
        {
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, "... exception closing socket");
            if (mf_rLogger. connect)
                mf_rLogger. connect (this, e);
        }
    }

    private ServerImpl  connectServer (Socket rSocket)
        throws IOException
    {
        Tube  rTube = mf_rConnectContext. createTube (rSocket);

        // Lese Server-ID
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "READ server ID ...");
        ID  rID = rTube. readID ();
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "... read server ID as " + rID);

        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Demand transport");
        // Erzeuge Anschluß
        rTube. writeTransport (Protocol. TUBE_Transport);

        // Hole Anschlußnummer
        int  iTransport = rTube. readTransport ();
            // Fehlermeldung kann hier nicht auftreten
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Transport is " + iTransport);

        // Lege nun einen Server-Repräsentanten an
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Create transport");
        ServerImpl  rServerImpl;
        {
            TransportContext  rTransportContext
                = new TransportContext (mf_rConnectContext, rID, iTransport);
            Transport  rTransport = new Transport (rTransportContext);
            rTransport. connect (rTube);

            rServerImpl = new ServerImpl (mf_rConnectContext, rID, rTransport);
        }

        // Gib den Server-Repräsentanten an den Aufrufer zurück.
        return rServerImpl;
    }

}
