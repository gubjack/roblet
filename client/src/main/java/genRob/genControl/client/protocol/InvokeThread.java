// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  genRob.genControl.client.server.Invoke;
import  java.io.IOException;
import  java.lang.reflect.Method;
import  java.util.HashSet;
import  java.util.Iterator;

import org.roblet.client.Logger;


/**
 * @author Hagen Stanek
 */
class  InvokeThread
    extends Thread
{
    InvokeThread (TransportContext rTransportContext
                    , Queues rQueues
                    , Tube.InvokeData rInvokeData)
    {
        super (rTransportContext. mf_rThreadGroup
                , rTransportContext. mf_strPrefix
                    + "?" + rInvokeData. mf_iCount);
        mf_rLogger = rTransportContext. mf_rLogger;
        mf_rQueues = rQueues;
        mf_iCount = rInvokeData. mf_iCount;
        mf_iSlot = rInvokeData. mf_iSlot;
        mf_abyteInvoke = rInvokeData. mf_abyteInvoke;
        mf_rHashSet_remote = rTransportContext. getRemoteSet (mf_iSlot);
    }
    private final Logger  mf_rLogger;
    private final Queues  mf_rQueues;
    private final int  mf_iCount;
    private final int  mf_iSlot;
    private final byte[]  mf_abyteInvoke;
    private final HashSet<Object>  mf_rHashSet_remote;

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
        try
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, "Convert bytes to object");
            Invoke  rInvoke = Convert. bytes2object (mf_abyteInvoke);

            if (mf_rLogger. call)
                mf_rLogger. call (this, "Invoke method " + rInvoke);
            Object  rObject = invoke (rInvoke);

            if (mf_rLogger. call)
                mf_rLogger. call (this, "Convert result to bytes");
            byte[]  abyteObject = Convert. object2bytes (rObject);

            Packet  packet = new InvokeResultPacket (mf_rLogger
                                                    , mf_iCount, abyteObject);
            if (mf_rLogger. call)
                mf_rLogger. call (this, "Append " + packet);
            mf_rQueues. append (packet);
        }
        catch (Exception e)
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, e);
            try
            {
                if (mf_rLogger. call)
                    mf_rLogger. call (this, "Convert exception to bytes");
                byte[]  abyteException = Convert. object2bytes (e);

                Packet  packet = new InvokeExceptionPacket (mf_rLogger
                                                            , mf_iCount
                                                            , abyteException);
                if (mf_rLogger. call)
                    mf_rLogger. call (this, "Append " + packet);
                mf_rQueues. append (packet);
            }
            catch (IOException e2)
            {
                if (mf_rLogger. call)
                    mf_rLogger. call (this, e2);
                // TODO:  Unverfängliche Ausnahme schicken
            }
        }
        // TODO:  Speicherprobleme noch detaillierter behandeln
        catch (OutOfMemoryError e)
        {
            if (mf_rLogger. call)
                mf_rLogger. call (this, e);
            try
            {
                Exception  eX = new RuntimeException ("", e);
                if (mf_rLogger. call)
                    mf_rLogger. call (this, "Convert exception to bytes");
                byte[]  abyteException = Convert. object2bytes (eX);

                Packet  packet = new InvokeExceptionPacket (mf_rLogger
                                                            , mf_iCount
                                                            , abyteException);
                if (mf_rLogger. call)
                    mf_rLogger. call (this, "Append " + packet);
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
    private Object  invoke (Invoke rInvoke)
        throws Exception
    {
        if (mf_rHashSet_remote == null)
            throw new RuntimeException ("Implementation not found");
        Object  oImplementation = null;
        synchronized (mf_rHashSet_remote)
        {
            for (Iterator<Object>  it = mf_rHashSet_remote. iterator ()
                    ;  it. hasNext ()
                    ;  )
            {
                Object  rObject = it. next ();
                Class<? extends Object>  rClass = rObject. getClass ();
                Class<? extends Object>[]
                                aClass_interface = rClass. getInterfaces ();
                for (int  i = 0;  i < aClass_interface.length;  ++i)
                {
                    Class<? extends Object>
                                        rClass_interface = aClass_interface [i];
                    if (rClass_interface. getName (). equals (
                                                        rInvoke.mf_strClass))
                    {
                        oImplementation = rObject;
                        break;
                    }
                }
                if (oImplementation != null)
                    break;
            }
        }
        if (oImplementation == null)
            throw new RuntimeException ("Implementation not found");

        Method  rMethod_invoke = null;
        {
            Method[]  aMethod = oImplementation. getClass (). getMethods ();
            for (int  i = 0;  i < aMethod.length;  ++i)
            {
                Method  rMethod = aMethod [i];
                if (rMethod. getName (). equals (rInvoke. mf_strMethod))
                {
                    Class<? extends Object>[]
                                        aClass = rMethod. getParameterTypes ();
                    if (aClass.length != rInvoke. mf_astrType.length)
                        break;
                    boolean  bOK = true;
                    for (int  j = 0;  j < aClass.length;  ++j)
                    {
                        if (! aClass[j]. getName (). equals (
                                                rInvoke. mf_astrType[j]))
                        {
                            bOK = false;
                            break;
                        }
                    }
                    if (bOK)
                    {
                        rMethod_invoke = rMethod;
                        break;
                    }
                }
            }
        }
        if (rMethod_invoke == null)
            throw new RuntimeException ("Method not found");

        Object  oResult = rMethod_invoke. invoke (oImplementation
                                                , rInvoke. mf_aoArgs);
        return oResult;
    }
}
