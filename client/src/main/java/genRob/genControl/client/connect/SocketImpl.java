// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.roblet.server.unit.net.Socket;


/**
 * @author Hagen Stanek
 */
class  SocketImpl
    implements Socket
{

    SocketImpl ()
    {
        socket = new java.net.Socket ();
    }
    SocketImpl (Proxy proxy)
    {
        socket = new java.net.Socket (proxy);
    }
    SocketImpl (String host, int port)
        throws UnknownHostException, IOException
    {
        socket = new java.net.Socket (host, port);
    }
    SocketImpl (InetAddress address, int port)
        throws IOException
    {
        socket = new java.net.Socket (address, port);
    }
    SocketImpl (String host, int port, InetAddress localAddr, int localPort)
        throws IOException
    {
        socket = new java.net.Socket (host, port, localAddr, localPort);
    }
    SocketImpl (InetAddress address, int port
                , InetAddress localAddr, int localPort)
        throws IOException
    {
        socket = new java.net.Socket (address, port, localAddr, localPort);
    }
    private final java.net.Socket  socket;

    public void  connect (SocketAddress endpoint)
        throws IOException
    {
        socket. connect (endpoint);
    }
    public void  connect (SocketAddress endpoint, int timeout)
        throws IOException
    {
        socket. connect (endpoint, timeout);
    }

    public void  bind (SocketAddress bindpoint)
        throws IOException
    {
        socket. bind (bindpoint);
    }

    public InetAddress  getInetAddress ()
    {
        return socket. getInetAddress ();
    }
    public InetAddress  getLocalAddress ()
    {
        return socket. getLocalAddress ();
    }
    public int  getPort ()
    {
        return socket. getPort ();
    }
    public int  getLocalPort ()
    {
        return socket. getLocalPort ();
    }
    public SocketAddress  getRemoteSocketAddress ()
    {
        return socket. getRemoteSocketAddress ();
    }
    public SocketAddress  getLocalSocketAddress ()
    {
        return socket. getLocalSocketAddress ();
    }

    public SocketChannel  getChannel ()
    {
        return socket. getChannel();
    }
    public InputStream  getInputStream ()
        throws IOException
    {
        return socket. getInputStream ();
    }
    public OutputStream  getOutputStream ()
        throws IOException
    {
        return socket. getOutputStream ();
    }

    public void  setTcpNoDelay (boolean on)
        throws SocketException
    {
        socket. setTcpNoDelay (on);
    }
    public boolean  getTcpNoDelay ()
        throws SocketException
    {
        return socket. getTcpNoDelay ();
    }

    public void  setSoLinger (boolean on, int linger)
        throws SocketException
    {
        socket. setSoLinger (on, linger);
    }
    public int  getSoLinger ()
        throws SocketException
    {
        return socket. getSoLinger ();
    }

    public void  sendUrgentData (int data)
        throws IOException
    {
        socket. sendUrgentData (data);
    }

    public void  setOOBInline (boolean on)
        throws SocketException
    {
        socket. setOOBInline (on);
    }
    public boolean  getOOBInline ()
        throws SocketException
    {
        return socket. getOOBInline ();
    }

    public void  setSoTimeout (int timeout)
        throws SocketException
    {
        socket. setSoTimeout (timeout);
    }
    public int  getSoTimeout ()
        throws SocketException
    {
        return socket. getSoTimeout ();
    }

    public void  setSendBufferSize (int size)
        throws SocketException
    {
        socket. setSendBufferSize (size);
    }
    public int  getSendBufferSize ()
        throws SocketException
    {
        return socket. getSendBufferSize ();
    }

    public void  setReceiveBufferSize (int size)
        throws SocketException
    {
        socket. setReceiveBufferSize (size);
    }
    public int  getReceiveBufferSize ()
        throws SocketException
    {
        return socket. getReceiveBufferSize ();
    }

    public void  setKeepAlive (boolean on)
        throws SocketException
    {
        socket. setKeepAlive (on);
    }
    public boolean  getKeepAlive ()
        throws SocketException
    {
        return socket. getKeepAlive ();
    }

    public void  setTrafficClass (int tc)
        throws SocketException
    {
        socket. setTrafficClass (tc);
    }
    public int  getTrafficClass ()
        throws SocketException
    {
        return socket. getTrafficClass ();
    }

    public void  setReuseAddress (boolean on)
        throws SocketException
    {
        socket. setReuseAddress (on);
    }
    public boolean  getReuseAddress ()
        throws SocketException
    {
        return socket. getReuseAddress ();
    }

    public void  close ()
        throws IOException
    {
        socket. close ();
    }

    public void  shutdownInput()
        throws IOException
    {
        socket. shutdownInput ();
    }
    public void  shutdownOutput ()
        throws IOException
    {
        socket. shutdownOutput ();
    }

    public boolean  isConnected ()
    {
        return socket. isConnected ();
    }
    public boolean  isBound ()
    {
        return socket. isBound ();
    }
    public boolean  isClosed ()
    {
        return socket. isClosed ();
    }
    public boolean  isInputShutdown ()
    {
        return socket. isInputShutdown ();
    }
    public boolean  isOutputShutdown ()
    {
        return socket. isOutputShutdown ();
    }

    public void  setPerformancePreferences (int connectionTime, int latency
                                            , int bandwidth)
    {
        socket. setPerformancePreferences (connectionTime, latency, bandwidth);
    }

    public <T> Socket  setOption (SocketOption<T> name, T value)
        throws IOException
    {
        socket. setOption (name, value);
        return this;
    }
    public <T> T  getOption (SocketOption<T> name)
        throws IOException
    {
        return socket. getOption (name);
    }
    public Set<SocketOption<?>>  supportedOptions ()
    {
        return socket. supportedOptions ();
    }

}
