/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.network.cluster;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 *
 * @author cal
 */
public class PeerAddress {
    InetSocketAddress address;
    
    public PeerAddress(InetSocketAddress address)
    {
        this.address = address;
    }
    
    public InetSocketAddress inetAddress()
    {
        return address;
    }
}
