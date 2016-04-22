package blockswarm.gui;

/**
 *
 * @author cal
 */
public class UploadFileRow
{
    private String uploadFilename, uploadFilehash, uploadBlocks, uploadAvailability, uploadFilesize, uploadPeers, uploadUpSpeed;

    public UploadFileRow(String uploadFilename, String uploadFilehash, String uploadBlocks, String uploadAvailability, String uploadFilesize, String uploadPeers, String uploadUpSpeed)
    {
        this.uploadFilename = uploadFilename;
        this.uploadFilehash = uploadFilehash;
        this.uploadBlocks = uploadBlocks;
        this.uploadAvailability = uploadAvailability;
        this.uploadFilesize = uploadFilesize;
        this.uploadPeers = uploadPeers;
        this.uploadUpSpeed = uploadUpSpeed;
    }

    public String getUploadUpSpeed()
    {
        return uploadUpSpeed;
    }

    public void setUploadUpSpeed(String uploadUpSpeed)
    {
        this.uploadUpSpeed = uploadUpSpeed;
    }

    public String getUploadFilename()
    {
        return uploadFilename;
    }

    public void setUploadFilename(String uploadFilename)
    {
        this.uploadFilename = uploadFilename;
    }

    public String getUploadFilehash()
    {
        return uploadFilehash;
    }

    public void setUploadFilehash(String uploadFilehash)
    {
        this.uploadFilehash = uploadFilehash;
    }

    public String getUploadBlocks()
    {
        return uploadBlocks;
    }

    public void setUploadBlocks(String uploadBlocks)
    {
        this.uploadBlocks = uploadBlocks;
    }

    public String getUploadAvailability()
    {
        return uploadAvailability;
    }

    public void setUploadAvailability(String uploadAvailability)
    {
        this.uploadAvailability = uploadAvailability;
    }

    public String getUploadFilesize()
    {
        return uploadFilesize;
    }

    public void setUploadFilesize(String uploadFilesize)
    {
        this.uploadFilesize = uploadFilesize;
    }

    public String getUploadPeers()
    {
        return uploadPeers;
    }

    public void setUploadPeers(String uploadPeers)
    {
        this.uploadPeers = uploadPeers;
    }
}
