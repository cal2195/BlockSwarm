/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import udt.UDPEndPoint;
import udt.UDTClient;
import udt.UDTServerSocket;
import udt.UDTSession;
import udt.UDTSocket;

/**
 *
 * @author cal
 */
public class UDTTest
{
    public UDTTest()
    {
        try
        {
            UDTServerSocket svr = new UDTServerSocket(12345);
            UDTSocket skt = svr.accept();
            
            
            UDTClient client = new UDTClient(InetAddress.getLocalHost(), 12346);
            client.connect("otherone", 12345);
            
        } catch (SocketException ex)
        {
            Logger.getLogger(UDTTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(UDTTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(UDTTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
