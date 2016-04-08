package blockswarm;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.SuperNode;
import java.io.File;
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
        configureLogger(Level.ALL);
        //Here we go!
//        Database database = new Database();
//        database.connect();
//        database.initialise();
//        database.disconnect();
        if (args.length == 0)
        {
            Node node = new Node();
            node.setupNode();
        } else if (args[0].equals("super"))
        {
            SuperNode superNode = new SuperNode();
            superNode.setupSuperNode();
        } else if (args[0].equals("upload"))
        {
            Node node = new Node();
            node.setupNode();
            FileHandler fileHandler = new FileHandler(node);
            fileHandler.uploadFile(new File(args[1]));
        }
    }

    public static void configureLogger(Level logLevel)
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
        topLogger.setLevel(logLevel);
        consoleHandler.setLevel(logLevel);
    }
}
