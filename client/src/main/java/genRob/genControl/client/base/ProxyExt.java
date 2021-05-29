// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.base;

import  genRob.genControl.client.protocol.Transport;
import  genRob.genControl.client.server.Invoke;
import  java.lang.reflect.InvocationHandler;
import  java.lang.reflect.InvocationTargetException;
import  java.lang.reflect.Method;
import  java.lang.reflect.Proxy;

import  org.roblet.client.UnexpectedException;
import org.roblet.client.Logger;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  ProxyExt
    implements InvocationHandler
{
    ProxyExt (Logger rLogger, String strPrefix
                , Transport rTransport, int iSlot, Class<?> rClass)
    {
        mf_rLogger = rLogger;
        mf_strPrefix = strPrefix;
        mf_rTransport = rTransport;
        mf_iSlot = iSlot;
        mf_strClass = rClass. getName ();
    }
    private final Logger  mf_rLogger;
    private final String  mf_strPrefix;
    private final Transport  mf_rTransport;
        // Diese Referenz wird auch gehalten, damit die referenzierte
        // Transport-Instanz nicht aufgeräumt wird (solange diese
        // Instanz noch referenziert wird).
    private final int  mf_iSlot;
    private final String  mf_strClass;

    // InvocationHandler
    public Object  invoke (Object proxy, Method method, Object[] args)
        throws Throwable
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this
                , mf_strPrefix + " ENTER invoke @" + mf_iSlot + " " + method);

        try
        {
            // Name der Methode
            String  strMethod = method. getName ();
            // Typen der Parameter
            Class<?>[]  aClass_params = method. getParameterTypes ();

            // Wenn es der Standard-Vergleich ist, ...
            if (    strMethod. equals ("equals")
                &&  aClass_params.length == 1
                &&  aClass_params[0]. equals (Object.class))
            {
                Object  arg0 = args[0];
                if (    arg0 == null
                    ||  ! Proxy. isProxyClass (arg0. getClass ()))
                    return Boolean.FALSE;

                // ... so wird Gleichheit nur bei identischer Instanz angenommen
                return Boolean. valueOf (arg0 == this);
            }
            // Als Hash wird der Hash der Instanz angenommen
            if (    strMethod. equals ("hashCode")
                &&  aClass_params.length == 0)
                return Integer. valueOf (System. identityHashCode (this));
            // Lesbare Zeichenkette wird der Klassenname zuzüglich Hash
            if (    strMethod. equals ("toString")
                &&  aClass_params.length == 0)
                return getClass (). getName (). toString ()
                                + '@' + Integer. toHexString (
                                            System. identityHashCode (this));

            String[]  astrType;
            {
                astrType = new String [aClass_params.length];
                for (int  i = 0;  i < astrType.length;  ++i)
                    astrType [i] = aClass_params[i]. getName ();
            }
            Invoke  rInvoke
                        = new Invoke (mf_strClass, strMethod, astrType, args);

            try
            {
                return mf_rTransport. invoke (mf_iSlot, rInvoke);
            }
            // Die direkt von der fernen Methode geworfene Ausnahme wird
            // hier direkt geworfen.
            catch (InvocationTargetException e)
            {
                throw e. getCause ();
            }
            catch (MarshalException | SlotException e)
            {
                throw e;
            }
            // Sämtliche andere Ausnahmen werden in eine Laufzeitausnahme
            // verpackt.
            catch (Exception e)
            {
                throw new UnexpectedException (e);
            }
        }
        finally
        {
        if (mf_rLogger. base)
            mf_rLogger. base (this
                , mf_strPrefix + " LEAVE invoke @" + mf_iSlot + " " + method);
        }
    }
}
