
package blockswarm;

import blockswarm.database.Database;

/**
 *
 * @author cal
 */
public class BlockSwarm
{
    

    public static void main(String[] args)
    {
        //Here we go!
        Database database = new Database();
        database.connect();
        database.disconnect();
    }
}
