// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  ResourceResultPacket
    extends Packet
{
    ResourceResultPacket (Logger rLogger
            , int iCount, String strResource, byte[] abyteResource)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_strResource = strResource;
        mf_abyteResource = abyteResource;
    }
    private final Logger  mf_rLogger;
    public final int  mf_iCount;
    private final String  mf_strResource;
    public final byte[]  mf_abyteResource;
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "WRITE ResourceResultPacket for #"
                                            + mf_iCount + " (" + mf_strResource + ")"
                                            + " ...");
        rTube. writePacketResourceResult (mf_iCount, mf_abyteResource);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "... wrote ResourceResultPacket for #"
                                            + mf_iCount + " (" + mf_strResource + ")");
    }
    public String  toString ()
    {
        return "ResourceResultPacket for #"
                    + mf_iCount + " (" + mf_strResource + ")"; 
    }

}
