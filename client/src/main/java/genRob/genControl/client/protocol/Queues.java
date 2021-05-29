// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.IOException;

import org.roblet.client.Logger;
import org.roblet.client.SlotException;


/**
 * @author Hagen Stanek
 */
class  Queues
{

    Queues (Logger rLogger)
    {
        mf_rLogger = rLogger;
    }
    private final Logger  mf_rLogger;

    private int  m_iSend, m_iConfirmed, m_iReceived;
    private Packet  unsend, sending, send, confirmed;

    synchronized void  append (Packet packet)
    {
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Append packet to unsend queue: "
                                        + packet);
            if (unsend == null)
                unsend = packet;
            else
            {
                Packet  last = unsend;
                while (last. next != null)
                    last = last. next;
                last. next = packet;
            }
            if (mf_rLogger. queues)
                log_queues ();
            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "NOTIFY sender");
            notify ();
    }
    synchronized void  confirm ()
    {
        ++m_iReceived;
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Insert confirm #" + m_iReceived);
        insertConfirm ();
    }
    synchronized void  insertConfirm ()
    {
        int  iConfirm = m_iReceived;
        if (mf_rLogger. queues)
            mf_rLogger. queues (this
                    , "Insert confirm packet for #" + iConfirm);
        Packet  packet = new ConfirmPacket (mf_rLogger, iConfirm);
        if (unsend == null)
            unsend = packet;
        else
            if (unsend instanceof ConfirmPacket)
            {
                packet. next = unsend. next;
                unsend = packet;
            }
            else
            {
                packet. next = unsend;
                unsend = packet;
            }
        if (mf_rLogger. queues)
            log_queues ();
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "NOTIFY sender");
        notify ();
    }
    void  confirm (int iConfirm)
        throws InterruptedException
    {
        while (iConfirm >  m_iConfirmed)
        {
            synchronized (mf_rObject)
            {
                while (send == null)    // Schleife gegen Spurious-Wakeups
                {
                    if (mf_rLogger. queues)
                        mf_rLogger. queues (this
                                , "WAIT for sender"
                                    + " to finish sending the"
                                    + " package to be confirmed");
                    mf_rObject. wait ();
                    if (mf_rLogger. queues)
                        mf_rLogger. queues (this
                                , "... finished waiting"
                                    + " for for sender"
                                    + " to finish sending the"
                                    + " package to be confirmed");
                }
            }
            synchronized (this)
            {
                confirmNext ();
                ++m_iConfirmed;
            }
        }
        if (mf_rLogger. queues)
            log_queues ();
    }
    private final Object  mf_rObject = new Object ();
    private synchronized void  confirmNext ()
    {
        if (mf_rLogger. queues)
            mf_rLogger. queues (this
                    , "Confirm packet " + send);
        if (    ! (send instanceof ResourceResultPacket)
            &&  ! (send instanceof ResourceExceptionPacket)
            &&  ! (send instanceof InvokeResultPacket)
            &&  ! (send instanceof InvokeExceptionPacket)
            )
        {
            if (confirmed == null)
            {
                confirmed = send;
                send = send. next;
                confirmed. next = null;
            }
            else
            {
                Packet  last = confirmed;
                while (last. next != null)
                    last = last. next;
                last. next = send;
                last = send;
                send = send. next;
                last. next = null;
            }
        }
        else
            send = send. next;
    }
    void  loopSend (Tube rTube)
        throws InterruptedException, IOException
    {
        if (mf_rLogger. queues)
            log_queues ();
        for (;;)
        {
            prepareSend ();

            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "Send packet: " + sending);
            sending. send (rTube);

            finishSend ();
            synchronized (mf_rObject)
            {
                if (mf_rLogger. queues)
                    mf_rLogger. queues (this
                        , "NOTIFY possibly waiting receiver to confirm");
                mf_rObject. notify ();
            }

            if (mf_rLogger. queues)
                log_queues ();
        }
    }
    private synchronized void  prepareSend ()
        throws InterruptedException
    {
        while (unsend == null)    // Schleife gegen Spurious-Wakeups
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this
                        , "WAIT unsend packages ...");
            wait ();
            if (mf_rLogger. queues)
                mf_rLogger. queues (this
                    , "... finished waiting for unsend packages");
        }
        sending = unsend;
        unsend = unsend. next;
        sending. next = null;
        if (mf_rLogger. queues)
            log_queues ();
    }
    private synchronized void  finishSend ()
    {
        if (sending instanceof ConfirmPacket)
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this
                        , "Confirm packet successfully send");
        }
        else
        {
            ++m_iSend;
            if (mf_rLogger. queues)
                mf_rLogger. queues (this
                        , "Packet #" +  m_iSend + " successfully send");

            if (send == null)
                send = sending;
            else
            {
                Packet  last = send;
                while (last. next != null)
                    last = last. next;
                last. next = sending;
            }
        }
        sending = null;
    }
    @SuppressWarnings("unchecked")
    synchronized <T extends Packet>
                            T  getWaitingPacket (Class<T> rClass, int iCount)
    {
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Retrieve waiting package");
        Packet  loop = confirmed,  loopLast = null;
        while (! loop. isLike (rClass, iCount))
        {
            loopLast = loop;
            loop = loop. next;
            // TODO:  Fehler in der Implementierung der
            // Gegenseite führt hier zu einer NullPointerException
        }
        if (loopLast == null)
            confirmed = loop. next;
        else
            loopLast. next = loop. next;
        if (mf_rLogger. queues)
            log_queues ();
        return (T) loop;
    }
    int  getReceived ()
    {
        return m_iReceived;
    }
    synchronized void  connect (int iReceived)
    {
        if (mf_rLogger. queues)
            log_queues ();

        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Remove confirm packages");
        if (sending instanceof ConfirmPacket)
            sending = null;
        if (unsend instanceof ConfirmPacket)
            unsend = unsend. next;
        if (mf_rLogger. queues)
            log_queues ();

        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Confirm packages");
        while (iReceived >  m_iConfirmed)
        {
            confirmNext ();
            ++m_iConfirmed;
        }
        if (mf_rLogger. queues)
            log_queues ();

        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "Shift unsend packages");
        if (sending != null)
        {
            sending. next = unsend;
            unsend = sending;
            sending = null;
        }
        while (send != null)
        {
            Packet  last = send,  prev = null;
            while (last. next != null)
            {
                prev = last;
                last = last. next;
            }
            if (prev == null)
                send = null;
            else
                prev. next = null;
            last. next = unsend;
            unsend = last;
            --m_iSend;
        }
        if (mf_rLogger. queues)
            log_queues ();
    }
    synchronized void  cancel (SlotException rSlotException)
    {
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "cancel packages");

        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "unsend:");
        Packet  packet = unsend;
        while (packet != null)
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "\t" + packet);
            packet. cancel (rSlotException);
            packet = packet. next;
        }
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "sending:");
        packet = sending;
        while (packet != null)
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "\t" + packet);
            packet. cancel (rSlotException);
            packet = packet. next;
        }
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "send:");
        packet = send;
        while (packet != null)
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "\t" + packet);
            packet. cancel (rSlotException);
            packet = packet. next;
        }
        if (mf_rLogger. queues)
            mf_rLogger. queues (this, "confirmed:");
        packet = confirmed;
        while (packet != null)
        {
            if (mf_rLogger. queues)
                mf_rLogger. queues (this, "\t" + packet);
            packet. cancel (rSlotException);
            packet = packet. next;
        }
    }
    private synchronized void  log_queues ()
    {
        mf_rLogger. queues (this, "unsend:");
        Packet  packet = unsend;
        while (packet != null)
        {
            mf_rLogger. queues (this, "\t" + packet);
            packet = packet. next;
        }
        mf_rLogger. queues (this, "sending:");
        packet = sending;
        while (packet != null)
        {
            mf_rLogger. queues (this, "\t" + packet);
            packet = packet. next;
        }
        mf_rLogger. queues (this, "send:");
        packet = send;
        while (packet != null)
        {
            mf_rLogger. queues (this, "\t" + packet);
            packet = packet. next;
        }
        mf_rLogger. queues (this, "confirmed:");
        packet = confirmed;
        while (packet != null)
        {
            mf_rLogger. queues (this, "\t" + packet);
            packet = packet. next;
        }
        mf_rLogger. queues (this, "send at #" + m_iSend
                                + " - confirmed at #" + m_iConfirmed
                                + " - received at #" + m_iReceived);
    }
}
