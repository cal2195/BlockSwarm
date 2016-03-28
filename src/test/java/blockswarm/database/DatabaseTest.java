/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database;

import blockswarm.BlockSwarm;
import java.io.File;
import java.util.Arrays;
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
        BlockSwarm.configureLogger();
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
    public void checkCache()
    {
        assertTrue("Checking cache table exists!", database.tableExists("cache"));
        database.getCache().putBlock("1234567891234567891", 0, "Hello world".getBytes());
        assertEquals("Testing putBlock()!", "[72, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100]", Arrays.toString(database.getCache().getBlock("1234567891234567891", 0)));
        assertNull("Testing non existant block!", database.getCache().getBlock("1234567891234567891", 12));
    }
}
