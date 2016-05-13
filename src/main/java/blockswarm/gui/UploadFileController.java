package blockswarm.gui;

import blockswarm.files.tags.TagGenerator;
import blockswarm.network.cluster.Node;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 *
 * @author cal
 */
public class UploadFileController implements Initializable
{

    @FXML
    TableView<Map> uploadNewFileTable;
    @FXML
    TextArea tagArea;

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

    @FXML
    public void addFiles()
    {
//        TagGenerator.generateTags(file.getAbsolutePath());
//        FileHandler fileHandler = new FileHandler(node);
        List list = new ArrayList();
        JFileChooser filechooser = new JFileChooser();
        filechooser.setMultiSelectionEnabled(true);
        filechooser.showOpenDialog(null);
        for (File file : filechooser.getSelectedFiles())
        {
            list.add(new UploadNewFileRow(file.getName(), TagGenerator.generateTags(file.getAbsolutePath())));
        }
        uploadNewFileTable.getItems().addAll(FXCollections.observableList(list));
    }

    @FXML
    public void okayPressed()
    {
        stage.close();
    }

    @FXML
    public void cancelPressed()
    {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        for (TableColumn column : uploadNewFileTable.getColumns())
        {
            column.setCellValueFactory(new PropertyValueFactory(column.getId()));
        }
        uploadNewFileTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                tagArea.setText(((UploadNewFileRow) uploadNewFileTable.getSelectionModel().getSelectedItem()).getTags());
            }
        });
    }
}
