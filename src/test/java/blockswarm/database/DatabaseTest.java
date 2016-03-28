/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.database;

import java.io.File;
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
        database.getCache().putBlock("1234567891234567891", 0, new byte[1024 * 1024]);
    }
}
