package blockswarm;

import blockswarm.files.FileHandler;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.supernode.SuperNode;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class Bootloader
{

    public static final String VERSION =    "prealpha-0.05";
    public static final String RELEASE =    "Oh, her eyes, her eyes\n" +
                                            "Make the stars look like they're not shinin'\n" +
                                            "Her hair, her hair\n" +
                                            "Falls perfectly without her trying\n" +
                                            "She's so beautiful\n" +
                                            "And I tell her everyday...";

    public static void main(String[] args)
    {
        configureLogger(Level.FINE);
        Logger.getLogger("javafx").setLevel(Level.OFF);
        Logger.getLogger("java.awt").setLevel(Level.OFF);
        Logger.getLogger("sun.awt").setLevel(Level.OFF);
        Logger.getLogger("javax.swing").setLevel(Level.OFF);
        //Here we go!
        if (args.length == 0)
        {
            new BlockSwarm(args);
        } else if (args[0].equals("super"))
        {
            SuperNode superNode = new SuperNode();
            superNode.setupSuperNode();
        } else if (args[0].equals("headless"))
        {
            Node node = new Node();
            node.setupNode();
            if (args.length > 1)
            {
                FileHandler fileHandler = new FileHandler(node);
                fileHandler.watchFolder(new File(args[1]));
            }
        } else if (args[0].equals("version"))
        {
            System.out.println("BlockSwarm " + VERSION);
            System.out.println("--------------------------------------------");
            System.out.println(RELEASE);
            System.out.println("--------------------------------------------");
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
