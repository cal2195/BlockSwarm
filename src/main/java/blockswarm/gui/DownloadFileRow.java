
package blockswarm.gui;

/**
 *
 * @author cal
 */
public class DownloadFileRow
{
    private String downloadFilename, downloadFilehash, downloadBlocks, downloadAvailability, downloadFilesize, downloadPeers, downloadDownSpeed, downloadUpSpeed;

    public DownloadFileRow(String downloadFilename, String downloadFilehash, String downloadBlocks, String downloadAvailability, String downloadFilesize, String downloadPeers, String downloadDownSpeed, String downloadUpSpeed)
    {
        this.downloadFilename = downloadFilename;
        this.downloadFilehash = downloadFilehash;
        this.downloadBlocks = downloadBlocks;
        this.downloadAvailability = downloadAvailability;
        this.downloadFilesize = downloadFilesize;
        this.downloadPeers = downloadPeers;
        this.downloadDownSpeed = downloadDownSpeed;
        this.downloadUpSpeed = downloadUpSpeed;
    }

    public String getDownloadDownSpeed()
    {
        return downloadDownSpeed;
    }

    public void setDownloadDownSpeed(String downloadDownSpeed)
    {
        this.downloadDownSpeed = downloadDownSpeed;
    }

    public String getDownloadUpSpeed()
    {
        return downloadUpSpeed;
    }

    public void setDownloadUpSpeed(String downloadUpSpeed)
    {
        this.downloadUpSpeed = downloadUpSpeed;
    }

    public String getDownloadFilename()
    {
        return downloadFilename;
    }

    public void setDownloadFilename(String downloadFilename)
    {
        this.downloadFilename = downloadFilename;
    }

    public String getDownloadFilehash()
    {
        return downloadFilehash;
    }

    public void setDownloadFilehash(String downloadFilehash)
    {
        this.downloadFilehash = downloadFilehash;
    }

    public String getDownloadBlocks()
    {
        return downloadBlocks;
    }

    public void setDownloadBlocks(String downloadBlocks)
    {
        this.downloadBlocks = downloadBlocks;
    }

    public String getDownloadAvailability()
    {
        return downloadAvailability;
    }

    public void setDownloadAvailability(String downloadAvailability)
    {
        this.downloadAvailability = downloadAvailability;
    }

    public String getDownloadFilesize()
    {
        return downloadFilesize;
    }

    public void setDownloadFilesize(String downloadFilesize)
    {
        this.downloadFilesize = downloadFilesize;
    }

    public String getDownloadPeers()
    {
        return downloadPeers;
    }

    public void setDownloadPeers(String downloadPeers)
    {
        this.downloadPeers = downloadPeers;
    }
    
    
}
