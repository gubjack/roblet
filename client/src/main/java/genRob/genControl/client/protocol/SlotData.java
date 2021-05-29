// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;


/**
 * Collection of data of a slot
 * @author Hagen Stanek
 */
public class SlotData
{

    SlotData (int iSlot)
    {
        mf_iSlot = iSlot;
    }
    /** slot number assigned by the server */
    public final int  mf_iSlot;

}
