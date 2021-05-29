// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.server;

import  java.io.Serializable;


/**
 * Represents a remote invocation.
 * @author Hagen Stanek
 */
public class  Invoke
    implements Serializable
{

    /**
     * Initialize with values necessary to perform a remote invocation.
     * @param strClass class to be called on
     * @param strMethod method to be called
     * @param astrType types of the arguments
     * @param aoArgs content of the arguments
     */
    public  Invoke (String strClass, String strMethod
            , String[] astrType
            , Object[] aoArgs)
    {
        mf_strClass = strClass;
        mf_strMethod = strMethod;
        mf_astrType = astrType;
        mf_aoArgs = aoArgs;
    }

    /** class to be called on */
    final public String  mf_strClass;
    /** method to be called */
    final public String  mf_strMethod;
    /** types of the arguments */
    final public String[]  mf_astrType;
    /** content of the arguments */
    final public Object[]  mf_aoArgs;

    public String  toString ()
    {
        StringBuffer  sbParameters = new StringBuffer ();
        for (int  i = 0;  i < mf_astrType.length;  ++i)
        {
            if (i > 0)
                sbParameters. append (", ");
            sbParameters. append (mf_astrType[i]);
            sbParameters. append (" ");
            sbParameters. append (mf_aoArgs[i]);
        }
        return mf_strClass + '.' + mf_strMethod + '(' + sbParameters + ')';
    }

}
