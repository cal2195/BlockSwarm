/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database;

import blockswarm.BlockSwarm;
import blockswarm.database.entries.FileEntry;
import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author cal
 */
public class DatabaseTest
{

    private final Database database;

    public DatabaseTest()
    {
        BlockSwarm.configureLogger(Level.FINEST);
        database = new Database();
        database.connect();
        database.initialise();
    }

    @Before
    public void deleteFiles()
    {
        deleteFolder(new File("database"));
    }

    public static void deleteFolder(File folder)
    {
        File[] files = folder.listFiles();
        if (files != null)
        { //some JVMs return null for empty dirs
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    deleteFolder(f);
                } else
                {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    @Test
    public void testCache()
    {
        assertTrue("Checking cache table exists!", database.tableExists("cache"));
        assertTrue("Testing adding block to cache!", database.getCache().putBlock("1234567891234567891", 0, "Hello world".getBytes()));
        assertFalse("Testing adding duplicate block to cache!", database.getCache().putBlock("1234567891234567891", 0, "Don't put me there!".getBytes()));
        assertEquals("Testing putBlock()!", "[72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100]", Arrays.toString(database.getCache().getBlock("1234567891234567891", 0)));
        assertNull("Testing non existant block!", database.getCache().getBlock("1234567891234567891", 12));
    }

    @Test
    public void testFile()
    {
        assertTrue("Checking file table exists!", database.tableExists("files"));
        assertTrue("Testing adding file to database!", database.getFiles().putFile(new FileEntry("1234567891234567891", "testfile.avi", 12453)));
        assertFalse("Testing adding duplicate file to database!", database.getFiles().putFile(new FileEntry("1234567891234567891", "testfile.avi", 12453)));
        assertEquals("Testing get file!", new FileEntry("1234567891234567891", "testfile.avi", 12453).toString(), database.getFiles().getFile("1234567891234567891").toString());
        assertNull("Testing getting no existant file!", database.getFiles().getFile("this doesn't exist!"));
    }
}
