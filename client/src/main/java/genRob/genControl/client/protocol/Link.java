// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.BufferedInputStream;
import  java.io.BufferedOutputStream;
import  java.io.DataInputStream;
import  java.io.DataOutputStream;
import  java.io.InputStream;
import  java.io.IOException;
import  java.io.OutputStream;

import org.roblet.client.Logger;
import  org.roblet.protocol.Protocol;
import org.roblet.server.unit.net.Socket;


/**
 * Communication layer controlling a TCP socket.
 * @author Hagen Stanek
 */
public class  Link
{

    /**
     * Größe interner Puffer.
     */
    private final static int  mfs_iBufferSize = 16 * 1024;


    /**
     * initilise the layer
     * @param rLogger  loggere
     * @param rSocket  socket for TCP communication
     * @throws IOException  in case socket connection preparation fails
     */
    public  Link (Logger rLogger, Socket rSocket)
        throws IOException
    {
        mf_rLogger = rLogger;
        mf_rSocket = rSocket;

        {
            // Stelle sicher, daß Pakete sofort vom TCP verschickt werden
            // Deaktivierung des Nagle-Algorithmus
            mf_rSocket. setTcpNoDelay (true);

            // Passe Empfangspuffer der Socket an
            {
                int  iSize = mf_rSocket. getReceiveBufferSize ();
                if (iSize < mfs_iBufferSize)
                {
                    mf_rSocket. setReceiveBufferSize (mfs_iBufferSize);
                    iSize = mf_rSocket. getReceiveBufferSize ();
                }
                if (mf_rLogger. link)
                    mf_rLogger. link (this
                            , "Socket receive buffer size: " + iSize);
            }

            // Passe Sendepuffer der Socket an
            {
                int  iSize = mf_rSocket. getSendBufferSize ();
                if (iSize < mfs_iBufferSize)
                {
                    mf_rSocket. setSendBufferSize (mfs_iBufferSize);
                    iSize = mf_rSocket. getSendBufferSize ();
                }
                if (mf_rLogger. link)
                    mf_rLogger. link (this
                            , "Socket send buffer size: " + iSize);
            }
        }

        {
            InputStream  is = rSocket. getInputStream ();
            BufferedInputStream  bis
                            = new BufferedInputStream (is, mfs_iBufferSize);
            dis = new DataInputStream (bis);
        }
        {
            OutputStream  os = rSocket. getOutputStream ();
            BufferedOutputStream  bos
                            = new BufferedOutputStream (os, mfs_iBufferSize);
            dos = new DataOutputStream (bos);
        }
    }
    private final Logger  mf_rLogger;
    private final Socket  mf_rSocket;
    final DataInputStream  dis;
    final DataOutputStream  dos;

    /**
     * Write data to the output stream to avoid old RMI servers
     * @throws IOException  in case of writing to the output stream fails
     */
    public void  cheatRMI ()
        throws IOException
    {
        // Trickse RMI aus
        // RMI schließt die Verbindung lautlos, wenn 6 Null-Bytes kommen
        if (mf_rLogger. link)
            mf_rLogger. link (this, "WRITE 6 NoOps to detect RMI ...");
        for (int  i = 0;  i < 6;  ++i)
            dos. writeByte (Protocol.LINK_NoOperation);
        dos. flush ();
        if (mf_rLogger. link)
            mf_rLogger. link (this, "... wrote 6 NoOps to detect RMI");

        // Haben wir es mit einem RMI-Server zu tun, so wird der jetzt
        // zumachen
    }
    /**
     * Provide server available protocol versions
     * @return protocol versions
     * @throws IOException  in case of reading from the input stream fails
     */
    public byte[]  readProtocols ()
         throws IOException
    {
        if (mf_rLogger. link)
            mf_rLogger. link (this, "READ protocols ...");
        int  iCount = dis. readByte ();
        byte[]  abyteProtocol = new byte [iCount];
        for (int  i = 0;  i < iCount;  ++i)
            abyteProtocol [i] = dis. readByte ();
        if (mf_rLogger. link)
            mf_rLogger. link (this, "... read protocols");
        return abyteProtocol;
    }
    /**
     * Write the desired protocol version
     * @param byteProtocol  protocol version
     * @throws IOException  in case of writing to the output stream fails
     */
    public void  writeProtocol (byte byteProtocol)
         throws IOException
    {
        if (mf_rLogger. link)
            mf_rLogger. link (this, "WRITE protocol " + byteProtocol + " ...");
        dos. writeByte (byteProtocol);
        dos. flush ();
        if (mf_rLogger. link)
            mf_rLogger. link (this, "... wrote protocol " + byteProtocol);
    }

    void  close ()
        throws IOException
    {
        if (mf_rLogger. link)
            mf_rLogger. link (this, "CLOSE link ...");
        mf_rSocket. close ();
        if (mf_rLogger. link)
            mf_rLogger. link (this, "... closed link");
    }

}
