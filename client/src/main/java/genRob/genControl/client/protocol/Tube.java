// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  genRob.genControl.client.server.ID;
import  java.io.DataInputStream;
import  java.io.DataOutputStream;
import  java.io.IOException;

import org.roblet.client.Logger;
import  org.roblet.protocol.Protocol;


/**
 * Represents the tube layer of a communication talking in raw protocol
 * entities.
 * @see Protocol
 * @author Hagen Stanek
 */
public class  Tube
{

    /**
     * Just some defined value to have something to send as there is only
     * one class loader anyway used on client side.
     */
    static int  CLASSLOADER_IGNORE = -1;


    /**
     * Initialise the tube.
     * @param rLogger  logger
     * @param byteProtocol  protocol version
     * @param rCrypt  crypto layer
     */
    public  Tube (Logger rLogger, byte byteProtocol, Crypt rCrypt)
    {
        mf_rLogger = rLogger;
        mf_byteProtocol = byteProtocol;
        mf_rCrypt = rCrypt;
        dis = rCrypt. dis;
        dos = rCrypt. dos;
    }
    private final Logger  mf_rLogger;
    /** protocol version */
    public final byte  mf_byteProtocol;
    private final Crypt  mf_rCrypt;
    private final DataInputStream  dis;
    private final DataOutputStream  dos;

    /** write the server ID
     * @param rID server ID
     * @throws IOException in case of output stream problems
     */
    public void  writeID (ID rID)
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE server ID " + rID + " ...");
        dos. writeLong (rID. mf_lIDA);
        dos. writeLong (rID. mf_lIDB);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote server ID " + rID);
    }
    /** read the server ID
     * @return server ID
     * @throws IOException in case of input stream problems
     */
    public ID  readID ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ server ID ...");
        long  lIDA = dis. readLong ();
        long  lIDB = dis. readLong ();
        ID  rID = new ID (lIDA, lIDB);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read server ID");
        
        return rID;
    }

    /**
     * Close the tube
     * @throws IOException when this fails
     */
    public void  close ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "CLOSE tube ...");
        mf_rCrypt. close ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... closed tube");
    }

    /** read the transport ID
     * @return transport ID
     * @throws IOException in case of input stream problems
     */
    public int  readTransport ()
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ transport ID ...");
        int  iTransport = dis. readInt ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read transport ID " + iTransport);
        return iTransport;
    }
    /** write the transport ID
     * @param iTransport transport ID
     * @throws IOException in case of output stream problems
     */
    public void  writeTransport (int iTransport)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE transport ID " + iTransport + " ...");
        dos. writeInt (iTransport);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote transport ID " + iTransport);
    }

    /** write the number of the last received package
     * @param iReceived package number
     * @throws IOException in case of output stream problems
     */
    public void  writeReceived (int iReceived)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE last received packet " + iReceived + " ...");
        dos. writeInt (iReceived);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote last received packet " + iReceived);
    }
    /** read number of the last received packet
     * @return package number
     * @throws IOException in case of input stream problems
     */
    public int  readReceived ()
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "READ last received packet ...");
        int  iReceived = dis. readInt ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... read last received packet " + iReceived);
        return iReceived;
    }


    static interface  Data
    {
    }
    Data  readPacket ()
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet type ...");
        byte  bytePacket = dis. readByte ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet type");
        switch (bytePacket)
        {
            case Protocol. TUBE_Confirm:
                return readPacketConfirm ();
            case Protocol. TUBE_Slot:
                return readPacketSlot ();
            case Protocol. TUBE_SlotResult:
                return readPacketSlotResult ();
            case Protocol. TUBE_Run:
                return readPacketRun ();
            case Protocol. TUBE_RunResult:
                return readPacketRunResult ();
            case Protocol. TUBE_RunException:
                return readPacketRunException ();
            case Protocol. TUBE_Resource:
                return readPacketResource ();
            case Protocol. TUBE_ResourceResult:
                return readPacketResourceResult ();
            case Protocol. TUBE_ResourceException:
                return readPacketResourceException ();
            case Protocol. TUBE_Invoke:
                return readPacketInvoke ();
            case Protocol. TUBE_InvokeResult:
                return readPacketInvokeResult ();
            case Protocol. TUBE_InvokeException:
                return readPacketInvokeException ();
        }
        throw new IOException ("Unkown packet type " + bytePacket);
    }

    static class  ConfirmData
        implements Data
    {
        ConfirmData (int iConfirm)
        {
            mf_iConfirm = iConfirm;
        }
        final int  mf_iConfirm;
        public String  toString ()
        {
            return "ConfirmData(&" + mf_iConfirm + ")";
        }
    }
    private ConfirmData  readPacketConfirm ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet Confirm data ...");
        int  iConfirm = dis. readInt ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet Confirm data");
        return new ConfirmData (iConfirm);
    }
    void  writePacketConfirm (int iConfirm)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE packet Confirm(&" + iConfirm + ") ...");
        dos. writeByte (Protocol. TUBE_Confirm);
        dos. writeInt (iConfirm);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote packet Confirm(&" + iConfirm + ")");
    }

    static class  SlotData
        implements Data
    {
        SlotData ()
        {
        }
        public String  toString ()
        {
            return "SlotData()";
        }
    }
    private SlotData  readPacketSlot ()
    {
        return new SlotData ();
    }
    void  writePacketSlot ()
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE packet Slot() ...");
        dos. writeByte (Protocol. TUBE_Slot);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote packet Slot()");
    }

    static class  SlotResultData
        implements Data
    {
        SlotResultData (int iSlot)
        {
            mf_iSlot = iSlot;
        }
        final int  mf_iSlot;
        public String  toString ()
        {
            return "SlotResultData(@" + mf_iSlot + ")";
        }
    }
    private SlotResultData  readPacketSlotResult ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet SlotResult data ...");
        int  iSlot = dis. readInt ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet SlotResult data");
        return new SlotResultData (iSlot);
    }
    void  writePacketSlotResult (int iSlot)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE packet SlotResult(@" + iSlot + ") ...");
        dos. writeByte (Protocol. TUBE_SlotResult);
        dos. writeInt (iSlot);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote packet SlotResult(@" + iSlot + ")");
    }

    static class  RunData
        implements Data
    {
        RunData (int iCount, int iSlot, int iClassLoader, byte[] abyteRoblet)
        {
            mf_iCount = iCount;
            mf_iSlot = iSlot;
            mf_iClassLoader = iClassLoader;
            mf_abyteRoblet = abyteRoblet;
        }
        final int  mf_iCount;
        final int  mf_iSlot;
        final int  mf_iClassLoader;
        final byte[]  mf_abyteRoblet;
        public String  toString ()
        {
            return "RunData(#" + mf_iCount
                            + ",@" + mf_iSlot
                            + ",?" + mf_iClassLoader + ",...)";
        }
    }
    private RunData  readPacketRun ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet Run data ...");
        int  iCount = dis. readInt ();
        int  iSlot = dis. readInt ();
        int  iClassLoader = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteRoblet = new byte [iLength];
        dis. readFully (abyteRoblet);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet Run data");
        return new RunData (iCount, iSlot, iClassLoader, abyteRoblet);
    }
    void  writePacketRun (int iCount, int iSlot, byte[] abyteRoblet)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet Run(#" + iCount
                                        + ",@" + iSlot + ") ...");
        dos. writeByte (Protocol. TUBE_Run);
        dos. writeInt (iCount);
        dos. writeInt (iSlot);
        dos. writeInt (CLASSLOADER_IGNORE);
        dos. writeInt (abyteRoblet. length);
        dos. write (abyteRoblet);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet Run(#" + iCount
                                            + ",@" + iSlot + ")");
    }

    static class  RunResultData
        implements Data
    {
        RunResultData (int iCount, byte[] abyteObject)
        {
            mf_iCount = iCount;
            mf_abyteObject = abyteObject;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteObject;
        public String  toString ()
        {
            return "RunResultData(#" + mf_iCount + ",...)";
        }
    }
    private RunResultData  readPacketRunResult ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet RunResult data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteObject = new byte [iLength];
        dis. readFully (abyteObject);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet RunResult data");
        return new RunResultData (iCount, abyteObject);
    }
    void  writePacketRunResult (int iCount, byte[] abyteObject)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE packet RunResult(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_RunResult);
        dos. writeInt (iCount);
        dos. writeInt (abyteObject. length);
        dos. write (abyteObject);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote packet RunResult(#" + iCount + ")");
    }

    static class  RunExceptionData
        implements Data
    {
        RunExceptionData (int iCount, byte[] abyteException)
        {
            mf_iCount = iCount;
            mf_abyteException = abyteException;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteException;
        public String  toString ()
        {
            return "RunExceptionData(#" + mf_iCount + ",...)";
        }
    }
    private RunExceptionData  readPacketRunException ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet RunException data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteException = new byte [iLength];
        dis. readFully (abyteException);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet RunException data");
        return new RunExceptionData (iCount, abyteException);
    }
    void  writePacketRunException (int iCount, byte[] abyteException)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "WRITE packet RunException(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_RunException);
        dos. writeInt (iCount);
        dos. writeInt (abyteException. length);
        dos. write (abyteException);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... wrote packet RunException(#" + iCount + ")");
    }

    static class  ResourceData
        implements Data
    {
        ResourceData (int iCount, int iClassLoader, byte[] abyteResourceString)
        {
            mf_iCount = iCount;
            mf_iClassLoader = iClassLoader;
            mf_abyteResourceString = abyteResourceString;
        }
        final int  mf_iCount;
        final int  mf_iClassLoader;
        final byte[]  mf_abyteResourceString;
        public String  toString ()
        {
            return "ResourceData(#" + mf_iCount + ",?" + mf_iClassLoader + ",...)";
        }
    }
    private ResourceData  readPacketResource ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet Resource data ...");
        int  iCount = dis. readInt ();
        int  iClassLoader = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteResourceString = new byte [iLength];
        dis. readFully (abyteResourceString);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet Resource data");
        return new ResourceData (iCount, iClassLoader, abyteResourceString);
    }
    void  writePacketResource (int iCount, int iClassLoader
                               , byte[] abyteResourceString)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet Resource(#" + iCount + ",?" + iClassLoader
                                                + ",...) ...");
        dos. writeByte (Protocol. TUBE_Resource);
        dos. writeInt (iCount);
        dos. writeInt (iClassLoader);
        dos. writeInt (abyteResourceString. length);
        dos. write (abyteResourceString);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet Resource(#" + iCount + ",?" + iClassLoader
                                                + "...)");
    }

    static class  ResourceResultData
        implements Data
    {
        ResourceResultData (int iCount, byte[] abyteResource)
        {
            mf_iCount = iCount;
            mf_abyteResource = abyteResource;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteResource;
        public String  toString ()
        {
            return "ResourceResultData(#" + mf_iCount + ",...)";
        }
    }
    private ResourceResultData  readPacketResourceResult ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet ResourceResult data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteResource = new byte [iLength];
        dis. readFully (abyteResource);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet ResourceResult data");
        return new ResourceResultData (iCount, abyteResource);
    }
    void  writePacketResourceResult (int iCount, byte[] abyteResource)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet ResourceResult(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_ResourceResult);
        dos. writeInt (iCount);
        dos. writeInt (abyteResource. length);
        dos. write (abyteResource);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet ResourceResult(#" + iCount + ")");
    }

    static class  ResourceExceptionData
        implements Data
    {
        ResourceExceptionData (int iCount, byte[] abyteException)
        {
            mf_iCount = iCount;
            mf_abyteException = abyteException;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteException;
        public String  toString ()
        {
            return "ResourceExceptionData(#" + mf_iCount + ",...)";
        }
    }
    private ResourceExceptionData  readPacketResourceException ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet ResourceException data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteException = new byte [iLength];
        dis. readFully (abyteException);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet ResourceException data");
        return new ResourceExceptionData (iCount, abyteException);
    }
    void  writePacketResourceException (int iCount, byte[] abyteException)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet ResourceException(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_ResourceException);
        dos. writeInt (iCount);
        dos. writeInt (abyteException. length);
        dos. write (abyteException);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet ResourceException(#" + iCount + ")");
    }

    static class  InvokeData
        implements Data
    {
        InvokeData (int iCount, int iSlot, int iClassLoader, byte[] abyteInvoke)
        {
            mf_iCount = iCount;
            mf_iSlot = iSlot;
            mf_iClassLoader = iClassLoader;
            mf_abyteInvoke = abyteInvoke;
        }
        final int  mf_iCount;
        final int  mf_iSlot;
        final int  mf_iClassLoader;
        final byte[]  mf_abyteInvoke;
        public String  toString ()
        {
            return "InvokeData(#" + mf_iCount
                            + ",@" + mf_iSlot
                            + ",?" + mf_iClassLoader + ",...)";
        }
    }
    private InvokeData  readPacketInvoke ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet Invoke data ...");
        int  iCount = dis. readInt ();
        int  iSlot = dis. readInt ();
        int  iClassLoader = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteInvoke = new byte [iLength];
        dis. readFully (abyteInvoke);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet Invoke data");
        return new InvokeData (iCount, iSlot, iClassLoader, abyteInvoke);
    }
    void  writePacketInvoke (int iCount, int iSlot, byte[] abyteInvoke)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet Invoke(#" + iCount
                                        + ",@" + iSlot + ") ...");
        dos. writeByte (Protocol. TUBE_Invoke);
        dos. writeInt (iCount);
        dos. writeInt (iSlot);
        dos. writeInt (CLASSLOADER_IGNORE);
        dos. writeInt (abyteInvoke. length);
        dos. write (abyteInvoke);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet Invoke(#" + iCount
                                            + ",@" + iSlot + ")");
    }

    static class  InvokeResultData
        implements Data
    {
        InvokeResultData (int iCount, byte[] abyteObject)
        {
            mf_iCount = iCount;
            mf_abyteObject = abyteObject;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteObject;
        public String  toString ()
        {
            return "InvokeResultData(#" + mf_iCount + ",...)";
        }
    }
    private InvokeResultData  readPacketInvokeResult ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet InvokeResult data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteObject = new byte [iLength];
        dis. readFully (abyteObject);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet InvokeResult data");
        return new InvokeResultData (iCount, abyteObject);
    }
    void  writePacketInvokeResult (int iCount, byte[] abyteObject)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet InvokeResult(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_InvokeResult);
        dos. writeInt (iCount);
        dos. writeInt (abyteObject. length);
        dos. write (abyteObject);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet InvokeResult(#" + iCount + ")");
    }

    static class  InvokeExceptionData
        implements Data
    {
        InvokeExceptionData (int iCount, byte[] abyteException)
        {
            mf_iCount = iCount;
            mf_abyteException = abyteException;
        }
        final int  mf_iCount;
        final byte[]  mf_abyteException;
        public String  toString ()
        {
            return "InvokeExceptionData(#" + mf_iCount + ",...)";
        }
    }
    private InvokeExceptionData  readPacketInvokeException ()
        throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "READ packet InvokeException data ...");
        int  iCount = dis. readInt ();
        int  iLength = dis. readInt ();
        byte[]  abyteException = new byte [iLength];
        dis. readFully (abyteException);
        if (mf_rLogger. tube)
            mf_rLogger. tube (this, "... read packet InvokeException data");
        return new InvokeExceptionData (iCount, abyteException);
    }
    void  writePacketInvokeException (int iCount, byte[] abyteException)
         throws IOException
    {
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "WRITE packet InvokeException(#" + iCount + ") ...");
        dos. writeByte (Protocol. TUBE_InvokeException);
        dos. writeInt (iCount);
        dos. writeInt (abyteException. length);
        dos. write (abyteException);
        dos. flush ();
        if (mf_rLogger. tube)
            mf_rLogger. tube (this
                    , "... wrote packet InvokeException(#" + iCount + ")");
    }

}
