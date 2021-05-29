// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  ResourceExceptionPacket
    extends Packet
{
    ResourceExceptionPacket (Logger rLogger
            , int iCount, String strResource, byte[] abyteException)
    {
        mf_rLogger = rLogger;
        mf_iCount = iCount;
        mf_strResource = strResource;
        mf_abyteException = abyteException;
    }
    private final Logger  mf_rLogger;
    public final int  mf_iCount;
    private final String  mf_strResource;
    public final byte[]  mf_abyteException;
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "WRITE ResourceExceptionPacket for #"
                                            + mf_iCount + " (" + mf_strResource + ")"
                                            + " ...");
        rTube. writePacketResourceException (mf_iCount, mf_abyteException);
        if (mf_rLogger. transport)
            mf_rLogger. transport (this, "... wrote ResourceExceptionPacket for #"
                                            + mf_iCount + " (" + mf_strResource + ")");
    }
    public String  toString ()
    {
        return "ResourceExceptionPacket for #"
                    + mf_iCount + " (" + mf_strResource + ")"; 
    }

}
