// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  InvokeResultPacket
    extends Packet
{
    InvokeResultPacket (Logger rLogger, int iCount, byte[] abyteObject)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_abyteObject = abyteObject;
    }
    private final Logger  mf_rLogger;
    private final int  mf_iCount;
    private final byte[]  mf_abyteObject;
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "WRITE InvokeResultPacket for #" + mf_iCount
                                            + " ...");
        rTube. writePacketInvokeResult (mf_iCount, mf_abyteObject);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this
                    , "... wrote InvokeResultPacket for #" + mf_iCount);
    }
    public String  toString ()
    {
        return "InvokeResultPacket for #" + mf_iCount; 
    }
}
