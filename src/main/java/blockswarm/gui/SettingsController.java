package blockswarm.gui;

import blockswarm.network.cluster.Node;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 *
 * @author cal
 */
public class SettingsController implements Initializable
{

    @FXML
    Spinner downloadLimit, uploadLimit, cacheLimit;

    Node node;

    public void setNode(Node node)
    {
        this.node = node;
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
        if (verifySettings())
        {
            saveSettings();
        }
    }
    
    @FXML
    public void cancelPressed()
    {
        updateSettings();
    }

    public boolean verifySettings()
    {
        try
        {
            if ((int) downloadLimit.getValueFactory().getValue() < 0)
            {
                throw new NumberFormatException();
            }
            if ((int) uploadLimit.getValueFactory().getValue() < 0)
            {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Value Found!");
            alert.setHeaderText("Invalid Value!");
            alert.setContentText("All speed limits must be whole numbers greater than 0, in KB/s. (0 = unlimited)");
            alert.showAndWait();
            return false;
        }
        try
        {
            if ((int) cacheLimit.getValueFactory().getValue() < 2000)
            {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Value Found!");
            alert.setHeaderText("Invalid Value!");
            alert.setContentText("Cache Limit must be a number greater than 2000!");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public void saveSettings()
    {
        node.getDatabase().getSettings().put("downloadLimit", "" + downloadLimit.getValueFactory().getValue());
        node.getDatabase().getSettings().put("uploadLimit", "" + uploadLimit.getValueFactory().getValue());
        node.getDatabase().getSettings().put("cacheLimit", "" + cacheLimit.getValueFactory().getValue());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        downloadLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0, 10));
        uploadLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000000, 0, 10));
        cacheLimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, Integer.MAX_VALUE, 10000, 100));
    }
}
