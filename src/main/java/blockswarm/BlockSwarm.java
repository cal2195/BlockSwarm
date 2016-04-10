package blockswarm;

import blockswarm.files.FileHandler;
import blockswarm.gui.FXMLController;
import blockswarm.network.cluster.Node;
import blockswarm.network.cluster.SuperNode;
import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cal
 */
public class BlockSwarm extends Application
{
    Node node;

    public static void main(String[] args)
    {
        configureLogger(Level.FINE);
        //Here we go!
//        Database database = new Database();
//        database.connect();
//        database.initialise();
//        database.disconnect();
        if (args.length == 0)
        {
//            Node node = new Node();
//            node.setupNode();
            launch(args);
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

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("BlockSwarm");
        stage.setScene(scene);
        stage.show();

        FXMLController gui = loader.getController();
        node = new Node(gui);
        node.setupNode();
    }

    @Override
    public void stop()
    {
        System.out.println("Stage is closing");
        node.getPeer().shutdown();
        System.exit(0);
    }
}
