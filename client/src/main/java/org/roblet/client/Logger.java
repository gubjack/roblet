// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.client;


/**
 * Derive this class and implement the abstract methods to be able to obtain
 * log information.
 * <P>
 * In this library every logging demand is implemented the following way:
 * <BLOCKQUOTE><PRE>
 *  if (logger.base)
 *      logger.base (this, "message");
 * </PRE></BLOCKQUOTE>
 * 
 * @author Hagen Stanek
 */
public abstract class  Logger
{

    /** Tells if the corresponding log method should be called.
     * @see #base(Object, Object)*/
    public volatile boolean  base;
    /** log basic information
     * @param caller calling instance
     * @param message information
     * @see #base */
    public abstract void  base (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #call(Object, Object)*/
    public volatile boolean  call;
    /** log function call information
     * @param caller calling instance
     * @param message information
     * @see #call */
    public abstract void  call (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #confirm(Object, Object)*/
    public volatile boolean  confirm;
    /** log transport package confirmation information
     * @param caller calling instance
     * @param message information
     * @see #confirm */
    public abstract void  confirm (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #connect(Object, Object)*/
    public volatile boolean  connect;
    /** log transport establishment information
     * @param caller calling instance
     * @param message information
     * @see #connect */
    public abstract void  connect (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #link(Object, Object)*/
    public volatile boolean  link;
    /** log transport link information
     * @param caller calling instance
     * @param message information
     * @see #link */
    public abstract void  link (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #queues(Object, Object)*/
    public volatile boolean  queues;
    /** log transport package queues information
     * @param caller calling instance
     * @param message information
     * @see #queues */
    public abstract void  queues (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #transport(Object, Object)*/
    public volatile boolean  transport;
    /** log transport information
     * @param caller calling instance
     * @param message information
     * @see #transport */
    public abstract void  transport (Object caller, Object message);

    /** Tells if the corresponding log method should be called.
     * @see #tube(Object, Object)*/
    public volatile boolean  tube;
    /** log transport tube information
     * @param caller calling instance
     * @param message information
     * @see #tube */
    public abstract void  tube (Object caller, Object message);

}
