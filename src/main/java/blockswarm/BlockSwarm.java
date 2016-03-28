package blockswarm;

import blockswarm.database.Database;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
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
        configureLogger();
        //Here we go!
        Database database = new Database();
        database.connect();
        database.initialise();
        database.disconnect();
    }

    public static void configureLogger()
    {
        //get the top Logger:
        Logger topLogger = java.util.logging.Logger.getLogger("");

        // Handler for console (reuse it if it already exists)
        Handler consoleHandler = null;
        //see if there is already a console handler
        for (Handler handler : topLogger.getHandlers())
        {
            if (handler instanceof ConsoleHandler)
            {
                //found the console handler
                consoleHandler = handler;
                break;
            }
        }

        if (consoleHandler == null)
        {
            //there was no console handler found, create a new one
            consoleHandler = new ConsoleHandler();
            topLogger.addHandler(consoleHandler);
        }
        
        //set the console handler to fine:
        topLogger.setLevel(Level.FINEST);
        consoleHandler.setLevel(Level.FINEST);
    }
}
