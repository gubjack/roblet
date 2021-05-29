// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.base;

import  genRob.genControl.client.Slot;
import genRob.genControl.client.protocol.SlotData;
import  genRob.genControl.client.protocol.Transport;
import  genRob.genControl.client.server.ExecutionException;
import  java.lang.reflect.Proxy;
import  org.roblet.Roblet;
import org.roblet.client.Logger;
import org.roblet.client.MarshalException;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
@SuppressWarnings("deprecation")
class  SlotImpl
    implements Slot
{

    SlotImpl (Logger rLogger, int iDelayMS, String strPrefix
                ,Transport rTransport)
        throws InterruptedException, SlotException
    {
        mf_rLogger = rLogger;
        mf_iDelayMS = iDelayMS;
        mf_strPrefix = strPrefix;
        mf_rTransport = rTransport;

        // Simulation von Netzwerkverzögerungen
        Thread. sleep (iDelayMS);

        // Benutze Transporter, um die Fach zu bekommen
        SlotData  rSlotData = mf_rTransport. getSlot ();
        mf_iSlot = rSlotData. mf_iSlot;

        if (mf_rLogger. base)
        {
            mf_strDebugPrefix = mf_strPrefix + "-@" + mf_iSlot;
            mf_rLogger. base (this, mf_strDebugPrefix + " SlotImpl ()");
        }
        else
            mf_strDebugPrefix = null;
    }
    private final Logger  mf_rLogger;
    private final int  mf_iDelayMS;
    private final String  mf_strPrefix;
    private final Transport  mf_rTransport;
    /** Die Fach-Nummer, die vom Server kam. */
    private final int  mf_iSlot;
    private final String  mf_strDebugPrefix;

    // Object
    protected void  finalize ()
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this, mf_strDebugPrefix + " finalize ()");
    }

    /**
     * Gibt die Fach-Number und den Server-Namen zurück.
     * <p>
     * Die Form ist dann:
     * <blockquote><tt>
     * Slot <i>Nummer</i> of <i>host:port</i>
     * </tt></blockquote>
     * @return Fach in lesbarer Form
     */
    // Object
/*
    public String  toString ()
    {
        StringBuffer  sb = new StringBuffer ();
        sb. append (mfs_strSlot);
        sb. append (mf_iSlot);
        sb. append (mfs_strOf);
        sb. append (mf_rConnection. getServer (). getName ());
        return  sb. toString ();
    }
    private final static String  mfs_strSlot = "Slot ";
    private final static String  mfs_strOf = " of ";
*/

    public void  close ()
    {
        mf_rTransport. close (new SlotException ("slot forced to close"));
    }


    // Slot
    public <RESULT> RESULT  run (Roblet<RESULT> rRoblet)
        throws InterruptedException, MarshalException
                , SlotException, Exception
    {
        if (mf_rLogger. base)
            mf_rLogger. base (this, mf_strDebugPrefix
                                    + " run (" + rRoblet + ") - begin");

        try
        {
            // Simulation von Netzwerkverzögerungen
            Thread. sleep (mf_iDelayMS);

            RESULT  rObject = mf_rTransport. run (mf_iSlot, rRoblet);

            return rObject;
        }
        // Fange die vom Server verpackten Ausführungsausnahmen ab, damit ihr
        // Ursprung direkt geworfen werden kann.
        catch (ExecutionException e)
        {
            Throwable  t = e. getCause ();

            // Werfe die Ausnahme des Roblet-Endes direkt.
            if (t instanceof Exception)
            {
                // Füge lokalen Teil zum Stack hinzu
                appendLocalStack (t);

                // Wirf erneut
                throw (Exception) t;
            }

            // Falls doch im Server mal ein Fehler verpackt wurde.
            throw e;
        }
        finally
        {
            if (mf_rLogger. base)
                mf_rLogger. base (this, mf_strDebugPrefix
                                        + " run (" + rRoblet + ") - end");
        }
    }
    /**
     * Fügt dem Stack einer Ausnahme eines Roblets noch die lokalen Anteil
     * ohne Implementierungsdetails hinzu.
     * @param t  Ausnahme des Roblets, die angepaßt wird
     */
    private void  appendLocalStack (Throwable t)
    {
        // Besorge den Stack der Ausnahme des Roblets
        StackTraceElement[]  aste_t = t. getStackTrace ();
        int  l = aste_t. length;

        // Erzeuge Ausnahme, um lokalen Stack zu erhalten
        Throwable  t2 = new Throwable ();
        t2. fillInStackTrace ();
        StackTraceElement[]  aste_t2 = t2. getStackTrace ();
        // Die Länge dieses Stacks reduziert sich um diese Methode und
        // die von run(), da sie uninteressante Implementierungsdetails
        // darstellen.
        int  l2 = aste_t2. length - 2;

        // Erzeuge neuen Stack mit den originalen Elementen und den
        // lokalen ohne die Implementierungsdetails.
        StackTraceElement[]  aste_new = new StackTraceElement [l + l2];
        for (int  i = 0;  i < l;  ++i)
            aste_new [i] = aste_t [i];
        for (int  i = l;  i < l + l2;  ++i)
            aste_new [i] = aste_t2 [(i - l) + 2];

        // Setze neuen Stack
        t. setStackTrace (aste_new);
    }

    // Slot
    public Object  obtainProxy (Class<?> clazz)
        throws SlotException
    {
        mf_rTransport. checkForClosed ();

        return Proxy. newProxyInstance (getClass (). getClassLoader ()
                                            , new Class[] { clazz }
                                            , new ProxyExt (mf_rLogger
                                                            , mf_strPrefix
                                                            , mf_rTransport
                                                            , mf_iSlot
                                                            , clazz));
    }

    // Slot
    public void  offerRemote (Object object)
        throws SlotException
    {
        mf_rTransport. offerRemote (mf_iSlot, object);
    }
    // Slot
    public void  revokeRemote (Object object)
        throws SlotException
    {
        mf_rTransport. revokeRemote (mf_iSlot, object);
    }

}
