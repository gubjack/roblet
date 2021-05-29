// SPDX-License-Identifier: LGPL-2.1
// (C) Copyright Hagen Stanek. All rights reserved.

package genRob.genControl.client.connect;

import  java.io.Serializable;

import  org.roblet.protocol.Protocol;


/**
 * communication address of server
 * @author Hagen Stanek
 */
public class  Name
    implements Serializable
{

    /**
     * Extrahiert und vermerkt Host-Name und Port-Nummer aus dem angegebenen
     * Server-Namen.&nbsp;
     * Ist kein Doppelpunkt im Server-Namen, so wird der Standard-Port
     * <CODE>org.roblet.Main#STANDARD_PORT_NUMBER</CODE> angenommen.&nbsp;
     * Ist der Server-Name <CODE>null</CODE> oder dessen Länge Null, so wird
     * die Loopback-Netzwerkschnittstelle "localhost" ebenfalls mit
     * Standard-Port angenommen.
     * <P>
     * ACHTUNG:&nbsp;
     * Die Aufspaltung nach Host-Name und Port-Nummer erfolgt einfach anhand
     * des Doppelpunktes ohne weitere Handlungen.&nbsp;
     * Der Aufrufer muß selbst sicherstellen, daß Host-Name und Port-Nummer
     * den Konventionen (DNS, IPv4, IPv6 etc.) bzw. dem Bereich (normalerweise
     * 1 bis 65535) entsprechen, also der Server-Name tatsächlich einen
     * existierenden Server anspricht.&nbsp;
     * Einzig für den Fall, daß nach dem Doppelpunkt keine ganze Zahl steht,
     * wird der Host-Name gleich dem Server-Namen gesetzt und das Port
     * als <CODE>org.roblet.Main#STANDARD_PORT_NUMBER</CODE> angenommen.
     * 
     * @param strName  Server-Name der Form "host:port" oder "host"
     */
    public  Name (String strName)
    {
        // Nehme Standard-Port an, wenn loopback gefordert ist
        if (strName == null)
        {
            mf_strHost = null;
            mf_iPort = Protocol.STANDARD_PORT_NUMBER;
            return;
        }

        // Bestimme Position des Doppelpunkts
        int  iColon = strName. lastIndexOf (':');

        // Nehme Standard-Port an, wenn kein Doppelpunkt vorliegt
        if (iColon == -1)
        {
            mf_strHost = strName;
            mf_iPort = Protocol.STANDARD_PORT_NUMBER;
            return;
        }

        // Extrahiere anhand des Doppelpunktes ohne weitere Prüfung bzw.
        // Fehlerbehandlung
        String  strHost = strName. substring (0, iColon);
        int  iPort;
        try
        {
            String  strPort = strName. substring (iColon + 1);
            iPort = Integer. parseInt (strPort);
        }
        catch (NumberFormatException e)
        {
            iPort = Protocol.STANDARD_PORT_NUMBER;
            strHost = strName;
        }
        mf_strHost = strHost;
        mf_iPort = iPort;
    }
    /** server host name or IP address */
    public final String  mf_strHost;
    /** server port */
    public final int  mf_iPort;

    public String  toString ()
    {
        return mf_strHost + ":" + mf_iPort;
    }

    public boolean  equals (Object rObject)
    {
        if (! (rObject instanceof Name))
            return false;
        Name  rName = (Name) rObject;
        return  mf_strHost. equals (rName. mf_strHost)
                &&  mf_iPort == rName. mf_iPort;
    }
    public int  hashCode ()
    {
        return mf_strHost. hashCode () + mf_iPort;
    }

}
