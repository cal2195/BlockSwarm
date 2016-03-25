/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.test;

import blockswarm.database.Database;
import org.junit.Test;
import static org.junit.Assert.*;

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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void checkCache()
    {
        assertTrue("Checking cache table exists!", database.tableExists("cach"));
    }
}
