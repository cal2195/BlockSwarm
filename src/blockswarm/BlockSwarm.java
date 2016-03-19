
package blockswarm;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class BlockSwarm
{
    

    public static void main(String[] args)
    {
        try
        {
            // Here we go!
            //new PeerTest();
            //new FileTest(); 
            new TestEchoServerMultiClient().testTwoClients();
        } catch (Exception ex)
        {
            Logger.getLogger(BlockSwarm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
