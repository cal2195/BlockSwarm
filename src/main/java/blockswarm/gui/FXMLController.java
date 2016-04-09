package blockswarm.gui;

import blockswarm.database.Database;
import blockswarm.database.entries.FileEntry;
import blockswarm.network.cluster.Node;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController implements Initializable
{

    @FXML
    TableView<Map> searchTable;
    
    Node node;

    public void addFiles(ArrayList<FileEntry> files)
    {
        List list = new ArrayList();
        for (FileEntry file : files)
        {
            list.add(new FileRow(file.filename, file.filehash, "" + file.totalBlocks, "" + file.availability, "0", "0"));
        }
        searchTable.setItems(FXCollections.observableList(list));
    }
    
    @FXML
    public void updateSearch()
    {
        ArrayList<FileEntry> files = new ArrayList<>();
        files.add(new FileEntry("test", "test", 1000, -1));
        addFiles(files);
    }
    
    public void updateFileList()
    {
        addFiles(node.getDatabase().getFiles().getAllFiles());
    }

    public void setNode(Node node)
    {
        this.node = node;
    }
    
    @FXML
    public void download()
    {
        FileRow file = (FileRow) searchTable.getSelectionModel().getSelectedItem();
        node.getCluster().downloadFile(file.getFilehash());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        for (TableColumn column : searchTable.getColumns())
        {
            column.setCellValueFactory(new PropertyValueFactory(column.getId()));
        }
    }
}
