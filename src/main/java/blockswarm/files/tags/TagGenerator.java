package blockswarm.files.tags;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

/**
 *
 * @author cal
 */
public class TagGenerator
{

    public static String generateTags(String file)
    {
        String tags = "";
        
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        try (InputStream stream = new FileInputStream(file))
        {
            parser.parse(stream, handler, metadata);
        } catch (Exception ex)
        {
            Logger.getLogger(TagGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(handler.toString());
        tags += handler.toString() + "\n";
        //getting the list of all meta data elements 
        String[] metadataNames = metadata.names();

        for (String name : metadataNames)
        {
            System.out.println(name + ": " + metadata.get(name));
            tags += name + ": " + metadata.get(name) + "\n";
        }
        
        return tags;
    }
}
