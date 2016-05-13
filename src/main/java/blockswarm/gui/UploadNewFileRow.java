
package blockswarm.gui;

/**
 *
 * @author cal
 */
public class UploadNewFileRow
{
    private String uploadFilename, tags;

    public UploadNewFileRow(String uploadFilename, String tags)
    {
        this.uploadFilename = uploadFilename;
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

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }
}
