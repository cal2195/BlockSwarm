
package blockswarm.gui;

/**
 *
 * @author cal
 */
public class DownloadFileRow
{
    private String downloadFilename, downloadFilehash, downloadBlocks, downloadAvailability, downloadFilesize, downloadPeers;

    public DownloadFileRow(String filename, String filehash, String blocks, String availability, String filesize, String peers)
    {
        this.downloadFilename = filename;
        this.downloadFilehash = filehash;
        this.downloadBlocks = blocks;
        this.downloadAvailability = availability;
        this.downloadFilesize = filesize;
        this.downloadPeers = peers;
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
