// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.connect;

import  genRob.genControl.client.base.ClientContext;
import  genRob.genControl.client.protocol.Crypt;
import  genRob.genControl.client.protocol.Link;
import  genRob.genControl.client.protocol.Tube;

import  java.io.IOException;

import org.roblet.client.Logger;
import  org.roblet.protocol.Protocol;
import org.roblet.server.unit.net.Socket;


/**
 * data and functions for connection to a server
 * @author Hagen Stanek
 */
public class  ConnectContext
{

    /**
     * initialise using clients context and the name of the server
     * @param rClientContext  clients context
     * @param rName  servers name
     */
    public  ConnectContext (ClientContext rClientContext, Name rName)
    {
        mf_rClientContext = rClientContext;
        mf_rLogger = rClientContext. mf_rLogger;
        int  iServer = rClientContext. getServerNumber ();
        mf_strPrefix = rClientContext. mf_rThreadGroup. getName ()
                            + "s" + iServer;
        mf_rName = rName;
    }
    /** clients context */
    public final ClientContext  mf_rClientContext;
    /** logger */
    public final Logger  mf_rLogger;
    /** prefix to be used for thread names */
    public final String  mf_strPrefix;
    /** communcation address of the server */
    public final Name  mf_rName;


    Socket  createSocket ()
        throws IOException
    {
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "CREATE socket to " + mf_rName + " ...");
        Socket  rSocket =  mf_rClientContext.mf_rNetUnit == null
                ?  new SocketImpl (mf_rName. mf_strHost, mf_rName. mf_iPort)
                :  mf_rClientContext.mf_rNetUnit
                        . getSocket (mf_rName. mf_strHost, mf_rName. mf_iPort);
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "... created socket to " + mf_rName);
        return rSocket;
    }

    Tube  createTube (Socket rSocket)
        throws IOException
    {
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Create link layer");
        Link  rLink = new Link (mf_rLogger, rSocket);

        // Protokollverhandlung
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Negotiate protocol");
        byte  byteProtocol = Protocol. LINK_NoOperation;
        {
            // RMI-Behandlung
            rLink. cheatRMI ();

            // Lese verfügbare Protokolle
            byte[]  abyteProtocol = rLink. readProtocols ();

            // Schaue nach bekannten Protokollen
            int  iCount = abyteProtocol. length;
            for (int  i = 0;  i < iCount;  ++i)
            {
                if (abyteProtocol [i] == Protocol.LINK_Version5)
                {
                    byteProtocol = Protocol. LINK_Version5;
                    break;
                        // Besser kann es nicht kommen (ein höheres Protokoll
                        // verstehen wir nicht)
                }
            }

            // Wurde ein "bekanntes" Protokoll geschickt?
            if (byteProtocol == Protocol. LINK_NoOperation)
                throw new IOException ("No suitable protocol version found");

            // Sende "ausgewähltes" Protokoll
            rLink. writeProtocol (byteProtocol);
        }
        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Using protocol " + byteProtocol);

        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Create cryptography layer");
        Crypt  rCrypt = new Crypt (rLink);

        if (mf_rLogger. connect)
            mf_rLogger. connect (this, "Create tube layer");
        Tube  rTube = new Tube (mf_rLogger, byteProtocol, rCrypt);

        return rTube;
    }

}
