/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm;

import blockswarm.files.blocks.Blocker;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class FileTest
{
    public FileTest()
    {
        try
        {
            Blocker.splitFile(new File("test.zip"));
            Blocker.mergeFiles(Blocker.listOfFilesToMerge(new File("heap/1dfc0fab43bb524274c3a5f71f01cecd1c3c2895.003")), new File("result.zip"));
        } catch (IOException ex)
        {
            Logger.getLogger(FileTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
