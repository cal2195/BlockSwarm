package blockswarm.gui;

import blockswarm.database.entries.FileEntry;
import blockswarm.files.FileHandler;
import blockswarm.info.NodeFileInfo;
import blockswarm.network.cluster.Node;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javax.swing.JFileChooser;

public class FXMLController implements Initializable
{

    @FXML
    TableView<Map> searchTable, downloadTable;
    @FXML
    TextArea statTextArea;
    BitSet selection = new BitSet();

    Node node;

    public void addSearchFiles(ArrayList<FileEntry> files)
    {
        List list = new ArrayList();
        for (FileEntry file : files)
        {
            NodeFileInfo current = node.getDatabase().getFiles().getFileInfo(file.filehash);
            list.add(new SearchFileRow(file.filename, file.filehash, current.blocks.cardinality() + "/" + file.totalBlocks, "" + file.availability, "0", "0"));
        }
        BitSet toSelect = (BitSet) selection.clone();
        searchTable.getItems().clear();
        searchTable.getItems().addAll(FXCollections.observableList(list));
        searchTable.sort();
        for (int i = toSelect.nextSetBit(0); i > -1; i = toSelect.nextSetBit(i + 1))
        {
            searchTable.getSelectionModel().select(i);
        }
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
        downloadTable.getItems().clear();
        downloadTable.getItems().addAll(FXCollections.observableList(list));
        downloadTable.sort();
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
        try
        {
            statTextArea.setText("Cache Size: " + node.getDatabase().getCache().cacheSize() + "\n"
                    + "Thread Pool Size: " + node.getWorkerPool().queue.size() + "\n"
                    + "Scheduled Pool Size: " + node.getWorkerPool().scheduledThreadPool.getQueue().size() + "\n"
                    + "Cluster Size (Verified): " + node.getPeer().peerBean().peerMap().all().size() + "\n"
                    + "Cluster Size (Unverified): " + node.getPeer().peerBean().peerMap().allOverflow().size() + "\n"
                    + "Peers: \n" + node.getPeer().peerBean().peerMap().all().toString() + "\n");
        } catch (Exception e)
        {
        }
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
        for (Iterator<Map> it = searchTable.getSelectionModel().getSelectedItems().iterator(); it.hasNext();)
        {
            SearchFileRow file = (SearchFileRow) it.next();
            node.getCluster().queueForDownload(file.getSearchFilehash());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        searchTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        searchTable.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>()
        {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Integer> c)
            {
                c.next();
                for (Integer row : c.getAddedSubList())
                {
                    selection.set(row);
                }
                for (Integer row : c.getRemoved())
                {
                    selection.clear(row);
                }
            }
        });
        searchTable.setOnDragOver(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                Dragboard db = event.getDragboard();
                if (db.hasFiles())
                {
                    event.acceptTransferModes(TransferMode.COPY);
                } else
                {
                    event.consume();
                }
            }
        });
        searchTable.setOnDragDropped(new EventHandler<DragEvent>()
        {
            @Override
            public void handle(DragEvent event)
            {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles())
                {
                    FileHandler fileHandler = new FileHandler(node);
                    success = true;
                    for (File file : db.getFiles())
                    {
                        System.out.println("Uploading " + file.getName());
                        fileHandler.uploadFile(file);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
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
