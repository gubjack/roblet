// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.protocol;

import  java.io.DataInputStream;
import  java.io.DataOutputStream;
//import  java.io.FilterInputStream;
//import  java.io.FilterOutputStream;
//import  java.io.InputStream;
import  java.io.IOException;
//import  java.io.OutputStream;


/**
 * communication layer to make the input and output socket streams somewhat
 * difficult to read.
 * This is no true encryption.
 * @author Hagen Stanek
 */
public class  Crypt
{

    /**
     * initalise the layer
     * @param rLink  link to be used
     */
    public  Crypt (Link rLink)
    {
        mf_rLink = rLink;
//        {
//            IStream  rIStream = new IStream (rLink. dis);
//            dis = new DataInputStream (rIStream);
//        }
//        {
//            OStream  rOStream = new OStream (rLink. dos);
//            dos = new DataOutputStream (rOStream);
//        }
        dis = new DataInputStream (rLink. dis);
        dos = new DataOutputStream (rLink. dos);
    }
    private final Link  mf_rLink;
    final DataInputStream  dis;
    final DataOutputStream  dos;

    void  close ()
        throws IOException
    {
        mf_rLink. close ();
    }


//    private class  IStream
//        extends FilterInputStream
//    {
//        IStream (InputStream rInputStream)
//        {
//            super (rInputStream);
//        }
//        public int  read ()
//            throws IOException
//        {
//            int  i = in. read ();
//            if (i == -1)
//                return -1;  // EOS
//            return i ^ 0xFF;
//        }
//        public int  read (byte[] ba, int off, int len)
//            throws IOException
//        {
//            int  i = in. read (ba, off, len);
//            if (i == -1)
//                return -1;  // EOS
//            for (int  j = 0;  j < i;  ++j)
//                ba [off + j] = (byte) (ba [off + j] ^ 0xFF);
//            return i;
//        }
//    }

//    private class  OStream
//        extends FilterOutputStream
//    {
//        OStream (OutputStream rOutputStream)
//        {
//            super (rOutputStream);
//        }
//        public void  write (int b)
//            throws IOException
//        {
//            out. write (b ^ 0xFF);
//        }
//        @SuppressWarnings("unused")
//        public void  write (int[] ba, int off, int len)
//            throws IOException
//        {
//            byte[]  ba2 = new byte [len];
//            for (int  j = 0;  j < len;  ++j)
//                ba2 [j] = (byte) (ba [off + j] ^ 0xFF);
//            out. write (ba2, 0, len);
//        }
//    }

}
