// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  genRob.genControl.client.connect.ConnectContext;
import  genRob.genControl.client.server.ID;
import  java.util.HashMap;
import  java.util.HashSet;

import org.roblet.client.Logger;


/**
 * Context for transport environments
 * @see Transport
 * @author Hagen Stanek
 */
public class  TransportContext
{

    /**
     * Initialise the context for a transport environment.
     * @param rConnectContext  context of the connection initiation
     * @param rID  ID of the server
     * @param iTransport  number of the transport as assigned by the server
     */
    public  TransportContext (ConnectContext rConnectContext
                              , ID rID, int iTransport)
    {
        mf_rConnectContext = rConnectContext;
        mf_rLogger = rConnectContext. mf_rLogger;
        mf_rThreadGroup = rConnectContext. mf_rClientContext. mf_rThreadGroup;
        mf_rID = rID;
        mf_iTransport = iTransport;
        mf_strPrefix = rConnectContext. mf_strPrefix;
        mf_rHashMap_iSlot__HashSet_remote
                                    = new HashMap<Integer,HashSet<Object>> ();
    }
    /** context of the connection initiation */
    public final ConnectContext  mf_rConnectContext;
    /** logger */
    public final Logger  mf_rLogger;
    /** group to be used for threads */
    public final ThreadGroup  mf_rThreadGroup;
    /** server ID */
    public final ID  mf_rID;
    /** number of the transport as assigned by the server */
    public final int  mf_iTransport;
    /** prefix used for thread names */
    public final String  mf_strPrefix;
    /** maping of slots to assigned local instances */
    public final HashMap<Integer,HashSet<Object>>
                                            mf_rHashMap_iSlot__HashSet_remote;

    /**
     * Assign a local instance to a slot.
     * @param iSlot slot
     * @param object local instance
     * @param rTransport corresponding transport
     */
    public synchronized void  offerRemote (int iSlot, Object object
                                            , Transport rTransport)
    {
        Integer  rInteger_Slot = Integer. valueOf (iSlot);
        HashSet<Object>  rHashSet = mf_rHashMap_iSlot__HashSet_remote
                                                        . get (rInteger_Slot);
        if (rHashSet == null)
        {
            rHashSet = new HashSet<Object> ();
            mf_rHashMap_iSlot__HashSet_remote. put (rInteger_Slot, rHashSet);
        }
        synchronized (rHashSet)
        {
            rHashSet. add (object);
            // Setze Transport, damit auch bei aufgeräumtem Server und Fächern
            // noch TCP-Verbindungen aufgebaut werden
            m_rTransport = rTransport;
        }
    }
    /**
     * Transport wird gesetzt, wenn einem Fach ferne Instanzen zugeordnet
     * werden.&nbsp;
     * Dadurch wird der Transport nicht aufgeräumt, solange noch eine
     * ferne Instanz bereitgestellt ist und damit die TCP-Verbindung
     * sichergestellt.
     */
    @SuppressWarnings("unused")
    private Transport  m_rTransport;
    /**
     * End assignment of a local instance to a slot.
     * @param iSlot slot
     * @param object local instance
     */
    public synchronized void  revokeRemote (int iSlot, Object object)
    {
        Integer  rInteger_Slot = Integer. valueOf (iSlot);
        HashSet<Object>  rHashSet = mf_rHashMap_iSlot__HashSet_remote
                                                        . get (rInteger_Slot);
        if (rHashSet == null)
            return;
        synchronized (rHashSet)
        {
            rHashSet. remove (object);
            if (rHashSet. isEmpty ())
            {
                mf_rHashMap_iSlot__HashSet_remote. remove (rInteger_Slot);
                // Keine ferne Instanz wird mehr offeriert, weshalb dann
                // auch der Transport aufgeräumt werden könnte.
                m_rTransport = null;
            }
        }
    }
    /**
     * Provide the set of local instances assign to a slot.
     * @param iSlot slot
     * @return local instances
     */
    public synchronized HashSet<Object>  getRemoteSet (int iSlot)
    {
        Integer  rInteger_Slot = Integer. valueOf (iSlot);
        HashSet<Object>  rHashSet = mf_rHashMap_iSlot__HashSet_remote
                                                        . get (rInteger_Slot);
        return rHashSet;
    }

}
