/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class FolderWatcher implements Runnable
{

    private WatchService folderWatcher;
    FileHandler fileHandler;

    public FolderWatcher(File path, FileHandler fileHandler)
    {
        this.fileHandler = fileHandler;
        setup(path);
    }

    private void setup(File path)
    {
        try
        {
            Path toWatch = Paths.get(path.getAbsolutePath());
            if (toWatch == null)
            {
                throw new UnsupportedOperationException("Directory not found");
            }

            folderWatcher = toWatch.getFileSystem().newWatchService();

            Thread th = new Thread(this, "FolderWatcher");
            th.start();

            toWatch.register(folderWatcher, ENTRY_CREATE);
        } catch (IOException ex)
        {
            Logger.getLogger(FolderWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        try
        {
            WatchKey key = folderWatcher.take();
            while (key != null)
            {
                for (WatchEvent event : key.pollEvents())
                {
                    if (event.kind() == ENTRY_CREATE)
                    {
                        Path dir = (Path) key.watchable();
                        Path fullPath = dir.resolve((Path) event.context());
                        fileHandler.uploadFile(fullPath.toFile(), "folder watcher upload");
                    }
                }
                key.reset();
                key = folderWatcher.take();
            }
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("Stopping thread");
    }
}
