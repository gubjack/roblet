// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.base;

import  genRob.genControl.client.connect.ConnectContext;
import  genRob.genControl.client.connect.Connector;
import  genRob.genControl.client.connect.Name;
import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * Encapsulate getting a server representation from an address
 * @author Hagen Stanek
 */
public class  ServerMap
{

    /**
     * initialise with the clients context
     * @param rClientContext  clients context
     */
    public  ServerMap (ClientContext rClientContext)
    {
        mf_rClientContext = rClientContext;
        mf_rLogger = rClientContext. mf_rLogger;
        mf_iDelayMS = rClientContext. mf_iDelayMS;
    }
    private final ClientContext  mf_rClientContext;
    private final Logger  mf_rLogger;
    private final int  mf_iDelayMS;

    /**
     * provide a server representation for an address (name)
     * @param rName  Server-Name
     * @return  Server-Repräsentant
     * @throws InterruptedException  Bei Unterbrechungsanforderung
     */
    public ServerImpl  getServer (Name rName)
        throws InterruptedException
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this, "getServer (" + rName + ") - TRY");

        try
        {
            for (;;)
            {
                try
                {
                    return connectAndWaitServer (rName);
                }
                catch (IOException e)
                {
                    if (mf_rLogger. base)
                        mf_rLogger. base (this, e);
                }

                synchronized (this)
                {
                    if (mf_rLogger. base)
                        mf_rLogger. base (this, "WAIT 3s before retry ...");
                    wait (3000);
                    if (mf_rLogger. base)
                        mf_rLogger. base (this, "... waited 3s before retry");
                }
            }
        }
        finally
        {
            if (mf_rLogger. base)
                mf_rLogger. base (this, "getServer (" + rName + ") - FINALLY");
        }
    }

    /**
     * @param rName  Server-Name
     * @return  Server-Repräsentant
     * @throws IOException  Bei Fehlern in der Kommunikation
     * @throws InterruptedException  Bei Unterbrechungsanforderung
     */
    private ServerImpl  connectAndWaitServer (Name rName)
        throws IOException, InterruptedException
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this, "getServer (" + rName + ")");

        // Simulation von Netzwerkverzögerungen
        Thread. sleep (mf_iDelayMS);

        // Erzeuge Verbindungskontext
        ConnectContext  rConnectContext
                        = new ConnectContext (mf_rClientContext, rName);

        // Erzeuge Objekt zum Verbindungsaufbau
        Connector  rConnector = new Connector (rConnectContext);

        return rConnector. getServer ();
    }

}
