
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
            //new TestFileSendServer(65321);
            new TestFileReceiveServer().testGetFile();
        } catch (Exception ex)
        {
            Logger.getLogger(BlockSwarm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
