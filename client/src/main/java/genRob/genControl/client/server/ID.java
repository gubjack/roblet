// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.server;

import  java.io.Serializable;
import  java.security.SecureRandom;


/**
 * The ID of a server.
 * @author Hagen Stanek
 */
public class  ID
    implements Serializable
{

    /** from RDK 1.3 */
    static final long serialVersionUID = 5292082860865386868L;

    /** create an ID from given values
     * @param lIDA value A
     * @param lIDB value B */
    public  ID (long lIDA, long lIDB)
    {
        mf_lIDA = lIDA;
        mf_lIDB = lIDB;
    }
    /** create a unique ID */
    public  ID ()
    {
        SecureRandom  rSecureRandom = new SecureRandom ();
        byte[]  ab = new byte [16];
        rSecureRandom. nextBytes (ab);
        long  l = 0;
        for (int  i = 0;  i < 8;  ++i)
            l = (l << 8) | (ab [i] & 0xFF);
        mf_lIDA = l;
        l = 0;
        for (int  i = 8;  i < 16;  ++i)
            l = (l << 8) | (ab [i] & 0xFF);
        mf_lIDB = l;
    }
    /** values A and B */
    public final long  mf_lIDA,  mf_lIDB;
    public String  toString ()
    {
        return hex (mf_lIDA >> 32) + "-" + hex (mf_lIDA)
                + "-" + hex (mf_lIDB >> 32) + "-" + hex (mf_lIDB);
    }
    private static String  hex (long l)
    {
        long  hi = 0x100000000L;
        long  lo = 0x0FFFFFFFFL;
        return Long.toHexString (hi | (l & lo)). substring (1);
    }

    public boolean  equals (Object rObject)
    {
        if (! (rObject instanceof ID))
            return false;
        ID  rID = (ID) rObject;
        if (mf_lIDA != rID. mf_lIDA)
            return false;
        if (mf_lIDB != rID. mf_lIDB)
            return false;
        return true;
    }
    public int  hashCode ()
    {
        return (int) ((mf_lIDA >> 32) ^ mf_lIDA ^ (mf_lIDB >> 32) ^ mf_lIDB);
    }

}
