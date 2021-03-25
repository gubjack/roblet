// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package org.roblet.server.unit.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

import org.roblet.Unit;


/**
 * This unit provides instances of type {@link Socket}.

<P>
    Alle methods are the replacement for constructors for {@link Socket}.
</P>

 * @author Hagen Stanek
 */
public interface  NetUnit
    extends Unit
{

    /**
     * Creates an unconnected Socket.
     * <p>
     * If the application has specified a client socket implementation
     * factory, that factory's {@code createSocketImpl} method is called to
     * create the actual socket implementation. Otherwise a system-default
     * socket implementation is created.
     * @return created socket
     * @see java.net.Socket#Socket()
     */
    public Socket  getSocket ();
    /**
     * Creates an unconnected socket, specifying the type of proxy, if any,
     * that should be used regardless of any other settings.
     * <P>
     * If there is a security manager, its {@code checkConnect} method
     * is called with the proxy host address and port number
     * as its arguments. This could result in a SecurityException.
     * <P>
     * Examples:
     * <UL> <LI>{@code Socket s = new Socket(Proxy.NO_PROXY);} will create
     * a plain socket ignoring any other proxy configuration.</LI>
     * <LI>{@code Socket s = new Socket(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("socks.mydom.com", 1080)));}
     * will create a socket connecting through the specified SOCKS proxy
     * server.</LI>
     * </UL>
     *
     * @param proxy a {@link java.net.Proxy Proxy} object specifying what kind
     *              of proxying should be used.
     * @return created socket
     * @throws IllegalArgumentException if the proxy is of an invalid type
     *          or {@code null}.
     * @throws SecurityException if a security manager is present and
     *                           permission to connect to the proxy is
     *                           denied.
     * @see java.net.ProxySelector
     * @see java.net.Proxy
     * @see java.net.Socket#Socket(Proxy)
     */
    public Socket  getSocket (Proxy proxy);
    /**
     * Creates a stream socket and connects it to the specified port
     * number on the named host.
     * <p>
     * If the specified host is {@code null} it is the equivalent of
     * specifying the address as
     * {@link java.net.InetAddress#getByName InetAddress.getByName}{@code (null)}.
     * In other words, it is equivalent to specifying an address of the
     * loopback interface. </p>
     * <p>
     * If the application has specified a client socket implementation
     * factory, that factory's {@code createSocketImpl} method is called to
     * create the actual socket implementation. Otherwise a system-default
     * socket implementation is created.
     * <p>
     * If there is a security manager, its
     * {@code checkConnect} method is called
     * with the host address and {@code port}
     * as its arguments. This could result in a SecurityException.
     *
     * @param      host   the host name, or {@code null} for the loopback address.
     * @param      port   the port number.
     * @return created socket
     *
     * @throws     UnknownHostException if the IP address of
     * the host could not be determined.
     *
     * @throws     IOException  if an I/O error occurs when creating the socket.
     * @throws     SecurityException  if a security manager exists and its
     *             {@code checkConnect} method doesn't allow the operation.
     * @throws     IllegalArgumentException if the port parameter is outside
     *             the specified range of valid port values, which is between
     *             0 and 65535, inclusive.
     * @see        java.net.Socket#setSocketImplFactory(java.net.SocketImplFactory)
     * @see        java.net.SocketImpl
     * @see        java.net.SocketImplFactory#createSocketImpl()
     * @see        SecurityManager#checkConnect
     * @see java.net.Socket#Socket(String,int)
     */
    public Socket  getSocket (String host, int port)
        throws UnknownHostException, IOException;
    /**
     * Creates a stream socket and connects it to the specified port
     * number at the specified IP address.
     * <p>
     * If the application has specified a client socket implementation
     * factory, that factory's {@code createSocketImpl} method is called to
     * create the actual socket implementation. Otherwise a system-default
     * socket implementation is created.
     * <p>
     * If there is a security manager, its
     * {@code checkConnect} method is called
     * with the host address and {@code port}
     * as its arguments. This could result in a SecurityException.
     *
     * @param      address   the IP address.
     * @param      port      the port number.
     * @return created socket
     * @throws     IOException  if an I/O error occurs when creating the socket.
     * @throws     SecurityException  if a security manager exists and its
     *             {@code checkConnect} method doesn't allow the operation.
     * @throws     IllegalArgumentException if the port parameter is outside
     *             the specified range of valid port values, which is between
     *             0 and 65535, inclusive.
     * @throws     NullPointerException if {@code address} is null.
     * @see        java.net.Socket#setSocketImplFactory(java.net.SocketImplFactory)
     * @see        java.net.SocketImpl
     * @see        java.net.SocketImplFactory#createSocketImpl()
     * @see        SecurityManager#checkConnect
     * @see java.net.Socket#Socket(InetAddress,int)
     */
    public Socket  getSocket (InetAddress address, int port)
        throws IOException;
    /**
     * Creates a socket and connects it to the specified remote host on
     * the specified remote port. The Socket will also bind() to the local
     * address and port supplied.
     * <p>
     * If the specified host is {@code null} it is the equivalent of
     * specifying the address as
     * {@link java.net.InetAddress#getByName InetAddress.getByName}{@code (null)}.
     * In other words, it is equivalent to specifying an address of the
     * loopback interface. </p>
     * <p>
     * A local port number of {@code zero} will let the system pick up a
     * free port in the {@code bind} operation.</p>
     * <p>
     * If there is a security manager, its
     * {@code checkConnect} method is called
     * with the host address and {@code port}
     * as its arguments. This could result in a SecurityException.
     *
     * @param host the name of the remote host, or {@code null} for the loopback address.
     * @param port the remote port
     * @param localAddr the local address the socket is bound to, or
     *        {@code null} for the {@code anyLocal} address.
     * @param localPort the local port the socket is bound to, or
     *        {@code zero} for a system selected free port.
     * @return created socket
     * @throws     IOException  if an I/O error occurs when creating the socket.
     * @throws     SecurityException  if a security manager exists and its
     *             {@code checkConnect} method doesn't allow the connection
     *             to the destination, or if its {@code checkListen} method
     *             doesn't allow the bind to the local port.
     * @throws     IllegalArgumentException if the port parameter or localPort
     *             parameter is outside the specified range of valid port values,
     *             which is between 0 and 65535, inclusive.
     * @see        SecurityManager#checkConnect
     * @see java.net.Socket#Socket(String,int,InetAddress,int)
     */
    public Socket  getSocket (String host, int port
                            , InetAddress localAddr, int localPort)
        throws IOException;
    /**
     * Creates a socket and connects it to the specified remote address on
     * the specified remote port. The Socket will also bind() to the local
     * address and port supplied.
     * <p>
     * If the specified local address is {@code null} it is the equivalent of
     * specifying the address as the AnyLocal address
     * (see {@link java.net.InetAddress#isAnyLocalAddress InetAddress.isAnyLocalAddress}{@code ()}).
     * <p>
     * A local port number of {@code zero} will let the system pick up a
     * free port in the {@code bind} operation.</p>
     * <p>
     * If there is a security manager, its
     * {@code checkConnect} method is called
     * with the host address and {@code port}
     * as its arguments. This could result in a SecurityException.
     *
     * @param address the remote address
     * @param port the remote port
     * @param localAddr the local address the socket is bound to, or
     *        {@code null} for the {@code anyLocal} address.
     * @param localPort the local port the socket is bound to or
     *        {@code zero} for a system selected free port.
     * @return created socket
     * @throws     IOException  if an I/O error occurs when creating the socket.
     * @throws     SecurityException  if a security manager exists and its
     *             {@code checkConnect} method doesn't allow the connection
     *             to the destination, or if its {@code checkListen} method
     *             doesn't allow the bind to the local port.
     * @throws     IllegalArgumentException if the port parameter or localPort
     *             parameter is outside the specified range of valid port values,
     *             which is between 0 and 65535, inclusive.
     * @throws     NullPointerException if {@code address} is null.
     * @see        SecurityManager#checkConnect
     * @see java.net.Socket#Socket(InetAddress,int,InetAddress,int)
     */
    public Socket  getSocket (InetAddress address, int port
                            , InetAddress localAddr, int localPort)
        throws IOException;

}
