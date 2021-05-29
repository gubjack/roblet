// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.BufferedOutputStream;
import  java.io.ByteArrayOutputStream;
import  java.io.InputStream;

import org.roblet.client.Logger;

import  java.io.IOException;


/**
 * @author Hagen Stanek
 */
class  ResourceThread
    extends Thread
{
    ResourceThread (TransportContext rTransportContext, Queues rQueues
                    , Tube.ResourceData rResourceData)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + "=" + rResourceData. mf_iCount);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = rQueues;
        mf_iCount = rResourceData. mf_iCount;
        mf_abyteResourceString = rResourceData. mf_abyteResourceString;
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final int  mf_iCount;
    private final byte[]  mf_abyteResourceString;

    // Thread
    public void  run ()
    {
        if (mf_rLogger. call)
            mf_rLogger. call (this, "START");
        try
        {
            work ();
        }
        finally
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, "END");
        }
    }
    private void  work ()
    {
        String  strResource = null;
        try
        {
            strResource = Convert. bytes2str (mf_abyteResourceString);
            if (mf_rLogger. call)
                mf_rLogger. call (this
                        , "Requested resource is '" + strResource + "'");

            ByteArrayOutputStream  rByteArrayOutputStream
                    = new ByteArrayOutputStream ();
            BufferedOutputStream  rBufferedOutputStream
                = new BufferedOutputStream (rByteArrayOutputStream);

            if (mf_rLogger. call)
                mf_rLogger. call (this
                        , "Load resource '" + strResource + "' ...");
            loadResource (strResource, rBufferedOutputStream);
            if (mf_rLogger. call)
                mf_rLogger. call (this
                        , "... loaded resource '" + strResource + "'");

            byte[]  abyteResource
                    = rByteArrayOutputStream. toByteArray ();
            Packet  packet
                = new ResourceResultPacket (mf_rLogger
                        , mf_iCount, strResource, abyteResource);

            if (mf_rLogger. call)
                mf_rLogger. call (this, "Append " + packet);
            mf_rQueues. append (packet);
        }
        catch (Exception e)
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, e);
//            e. printStackTrace ();
            try
            {
                byte[]  abyteException = Convert. object2bytes (e);
                Packet  packet
                    = new ResourceExceptionPacket (mf_rLogger
                            , mf_iCount, strResource, abyteException);
                mf_rQueues. append (packet);
            }
            catch (IOException e2)
            {
                if (mf_rLogger. call)
                    mf_rLogger. call (this, e2);
                // TODO:  Unverfängliche Ausnahme schicken
            }
        }
    }
    private void  loadResource (String strResource
                        , BufferedOutputStream rBufferedOutputStream)
        throws IOException
    {
        String  strFile = strResource;
        // Erzeuge einen gepufferten Eingabestrom
        InputStream  is = getClass (). getClassLoader ()
                                                . getResourceAsStream (strFile);
        if (is == null)
            throw new IOException ("Resource '" + strFile + "' not found");

        // Puffer
        int  i = 4 * 1024;
        byte []  ba = new byte [i];

        // Hole Daten
        {
            for (;;)
            {
                int  iRead = is. read (ba, 0, i);
                if (iRead == -1)
                    // EOF encountered
                    break;
                rBufferedOutputStream. write (ba, 0, iRead);
            }
        }

        // Schließe die Ströme
        is. close ();
        rBufferedOutputStream. close ();
        //TODO:  Sollte man die Ströme nicht in ein finally packen?
    }
}
