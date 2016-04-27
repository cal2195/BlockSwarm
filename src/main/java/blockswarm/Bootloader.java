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

    public static final String VERSION =    "prealpha-0.04";
    public static final String RELEASE =    "Be lost, be strange, be what those other people won't do\n" +
                                            "You know, you're right\n" +
                                            "Here you will know your might\n" +
                                            "Don't feel too weird\n" +
                                            "They're dumb for second guessing who's you";

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
