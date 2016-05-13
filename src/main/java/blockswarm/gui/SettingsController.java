package blockswarm.gui;

import blockswarm.network.cluster.Node;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 *
 * @author cal
 */
public class SettingsController implements Initializable
{

    @FXML
    Spinner downloadLimit, uploadLimit, cacheLimit;

    Node node;
    Stage stage;

    public void setNode(Node node)
    {
        this.node = node;
    }
    
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public void updateSettings()
    {
        downloadLimit.getValueFactory().setValue(node.getDatabase().getSettings().getInt("downloadLimit", "0"));
        uploadLimit.getValueFactory().setValue(node.getDatabase().getSettings().getInt("uploadLimit", "0"));
        cacheLimit.getValueFactory().setValue(node.getDatabase().getSettings().getInt("cacheLimit", "10000"));
    }

    @FXML
    public void okayPressed()
    {
        saveSettings();
        updateNodeSettings();
        stage.close();
    }

    @FXML
    public void cancelPressed()
    {
        updateSettings();
        stage.close();
    }

    public void saveSettings()
    {
        node.getDatabase().getSettings().put("downloadLimit", "" + downloadLimit.getValueFactory().getValue());
        node.getDatabase().getSettings().put("uploadLimit", "" + uploadLimit.getValueFactory().getValue());
        node.getDatabase().getSettings().put("cacheLimit", "" + cacheLimit.getValueFactory().getValue());
    }
    
    public void updateNodeSettings()
    {
        node.getTrafficLimiter().setWriteLimit(node.getDatabase().getSettings().getInt("uploadLimit", "0"));
        node.getTrafficLimiter().setReadLimit(node.getDatabase().getSettings().getInt("downloadLimit", "0"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        downloadLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0, 10));
        setupSpinner(downloadLimit);
        uploadLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0, 10));
        setupSpinner(uploadLimit);
        cacheLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, Integer.MAX_VALUE, 10000, 100));
        setupSpinner(cacheLimit);
    }

    private void setupSpinner(Spinner spinner)
    {
        // hook in a formatter with the same properties as the factory
        TextFormatter formatter = new TextFormatter(spinner.getValueFactory().getConverter(), spinner.getValueFactory().getValue());
        spinner.getEditor().setTextFormatter(formatter);
        // bidi-bind the values
        spinner.getValueFactory().valueProperty().bindBidirectional(formatter.valueProperty());
    }
}
