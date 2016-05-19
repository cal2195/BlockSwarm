
package blockswarm.gui;

/**
 *
 * @author cal
 */
public class UploadNewFileRow
{
    private String uploadFilename, filepath, tags;

    public UploadNewFileRow(String uploadFilename, String filepath, String tags)
    {
        this.uploadFilename = uploadFilename;
        this.filepath = filepath;
        this.tags = tags;
    }

    public String getUploadFilename()
    {
        return uploadFilename;
    }

    public void setUploadFilename(String uploadFilename)
    {
        this.uploadFilename = uploadFilename;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }
}
