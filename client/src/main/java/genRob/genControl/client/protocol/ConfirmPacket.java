// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  ConfirmPacket
    extends Packet
{
    ConfirmPacket (Logger rLogger, int iConfirm)
    {
        mf_rLogger = rLogger;
        mf_iConfirm = iConfirm;
    }
    private final Logger  mf_rLogger;
    private final int  mf_iConfirm;
    void  send (Tube rTube)
        throws IOException
    {
        if (mf_rLogger. transport  &&  mf_rLogger. confirm)
            mf_rLogger. transport (this, "WRITE Confirm packet for #"
                                            + mf_iConfirm + " ...");
        rTube. writePacketConfirm (mf_iConfirm);
        if (mf_rLogger. transport  &&  mf_rLogger. confirm)
            mf_rLogger. transport (this, "... wrote Confirm packet for #"
                                            + mf_iConfirm);
    }
    public String  toString ()
    {
        return "ConfirmPacket for #" + mf_iConfirm;
    }

}
