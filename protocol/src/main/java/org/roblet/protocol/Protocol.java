// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.protocol;


/**
 * Basic definitions for the interoperation of roblet client and server.
 * 
 * @author Hagen Stanek
 */
public class  Protocol
{

    /** standard TCP port number of a server. */
    public final static int  STANDARD_PORT_NUMBER = 2001;


    // link

    /** no operation - ignore */
    public final static byte  LINK_NoOperation = 0;
    /** protocol version 1 */
    public final static byte  LINK_Version1 = 1;
    /** protocol version 2 */
    public final static byte  LINK_Version2 = 2;
    /** protocol version 3 */
    public final static byte  LINK_Version3 = 3;
    /** protocol version 4 */
    public final static byte  LINK_Version4 = 4;
    /** protocol version 5 */
    public final static byte  LINK_Version5 = 5;


    // transport

    /** tranport ID request */
    public final static int  TUBE_Transport = 0;
    /** the transport is not known */
    public final static int  TUBE_TransportUnknown = -1;


    // tube

    /** package ID for a package confirmation */
    public final static byte  TUBE_Confirm = 0;

    /** package ID for a slot claim */
    public final static byte  TUBE_Slot = 1;
    /** package ID for the result of a slot claim */
    public final static byte  TUBE_SlotResult = 2;
    /** package ID for an exception as the result of a slot claim */
    public final static byte  TUBE_SlotException = 3;

    /** package ID for a roblet run */
    public final static byte  TUBE_Run = 4;
    /** package ID for the result of a roblet run */
    public final static byte  TUBE_RunResult = 5;
    /** package ID for an exception as the result of a roblet run */
    public final static byte  TUBE_RunException = 6;

    /** package ID for a resource request */
    public final static byte  TUBE_Resource = 7;
    /** package ID for the result of a resource request */
    public final static byte  TUBE_ResourceResult = 8;
    /** package ID for an exception as the result of a resource request */
    public final static byte  TUBE_ResourceException = 9;

    /** package ID for a stream write request */
    public final static byte  TUBE_Write = 10;
    /** package ID for the result of a stream write request */
    public final static byte  TUBE_WriteResult = 11;
    /** package ID for an exception as the result of a stream write request */
    public final static byte  TUBE_WriteException = 12;

    /** package ID for a method invocation */
    public final static byte  TUBE_Invoke = 13;
    /** package ID for the result of a method invocation */
    public final static byte  TUBE_InvokeResult = 14;
    /** package ID for an exception as the result of a method invocation */
    public final static byte  TUBE_InvokeException = 15;

}
