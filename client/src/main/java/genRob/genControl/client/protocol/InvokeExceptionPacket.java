// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  InvokeExceptionPacket
    extends Packet
{
    InvokeExceptionPacket (Logger rLogger, int iCount, byte[] abyteException)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_abyteException = abyteException;
    }
    private final Logger  mf_rLogger;
    private final int  mf_iCount;
    private final byte[]  mf_abyteException;
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "WRITE InvokeExceptionPacket for #" + mf_iCount + " ...");
        rTube. writePacketInvokeException (mf_iCount, mf_abyteException);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "... wrote InvokeExceptionPacket for #" + mf_iCount);
    }
    public String  toString ()
    {
        return "InvokeExceptionPacket for #" + mf_iCount; 
    }
}
