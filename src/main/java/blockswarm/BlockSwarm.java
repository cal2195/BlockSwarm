package blockswarm;

import blockswarm.gui.FXMLController;
import blockswarm.network.cluster.Node;
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
    
    public BlockSwarm()
    {
        
    }

    public BlockSwarm(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("BlockSwarm " + Bootloader.VERSION);
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
        node.getWorkerPool().shutdown();
        node.getPeer().shutdown();
        node.getDatabase().disconnect();
    }
}
