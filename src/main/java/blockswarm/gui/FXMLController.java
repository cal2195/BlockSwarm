package blockswarm.gui;

import blockswarm.database.Database;
import blockswarm.database.entries.FileEntry;
import blockswarm.files.FileHandler;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.io.File;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JFileChooser;

public class FXMLController implements Initializable
{

    @FXML
    TableView<Map> searchTable, downloadTable;
    @FXML
    TextArea statTextArea;

    Node node;

    public void addSearchFiles(ArrayList<FileEntry> files)
    {
        List list = new ArrayList();
        for (FileEntry file : files)
        {
            list.add(new SearchFileRow(file.filename, file.filehash, "" + file.totalBlocks, "" + file.availability, "0", "0"));
        }
        searchTable.setItems(FXCollections.observableList(list));
    }

    public void addDownloadFiles(ArrayList<NodeFileInfo> files)
    {
        List list = new ArrayList();
        for (NodeFileInfo file : files)
        {
            NodeFileInfo current = node.getDatabase().getFiles().getFileInfo(file.hash);
            FileEntry info = node.getDatabase().getFiles().getFile(file.hash);
            list.add(new DownloadFileRow(info.filename, info.filehash, current.blocks.cardinality() + "/" + info.totalBlocks, "" + info.availability, "0", "0"));
        }
        downloadTable.setItems(FXCollections.observableList(list));
    }

    @FXML
    public void updateSearch()
    {
        ArrayList<FileEntry> files = new ArrayList<>();
        files.add(new FileEntry("test", "test", 1000, -1));
        addSearchFiles(files);
    }

    public void updateFileList()
    {
        addSearchFiles(node.getDatabase().getFiles().getAllFiles());
        addDownloadFiles(node.getDatabase().getDownloads().getAllDownloads());
        updateStats();
    }
    
    public void updateStats()
    {
        statTextArea.setText("Cache Size: " + node.getDatabase().getCache().cacheSize() + "\n"
                           + "Thread Pool Size: " + node.getWorkerPool().queue.size() + "\n"
                           + "Scheduled Pool Size: " + node.getWorkerPool().scheduledThreadPool.getQueue().size());
    }

    public void setNode(Node node)
    {
        this.node = node;
    }

    @FXML
    public void uploadFile()
    {
        FileHandler fileHandler = new FileHandler(node);
        JFileChooser filechooser = new JFileChooser();
        filechooser.setMultiSelectionEnabled(true);
        filechooser.showOpenDialog(null);
        for (File file : filechooser.getSelectedFiles())
        {
            fileHandler.uploadFile(file);
        }
    }

    @FXML
    public void download()
    {
        SearchFileRow file = (SearchFileRow) searchTable.getSelectionModel().getSelectedItem();
        node.getCluster().queueForDownload(file.getSearchFilehash());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        for (TableColumn column : searchTable.getColumns())
        {
            column.setCellValueFactory(new PropertyValueFactory(column.getId()));
        }
        for (TableColumn column : downloadTable.getColumns())
        {
            column.setCellValueFactory(new PropertyValueFactory(column.getId()));
        }
    }
}
