/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockswarm.gui;

/**
 *
 * @author cal
 */
public class FileRow
{
    private String filename, blocks, availability, filesize, peers;

    public FileRow(String filename, String blocks, String availability, String filesize, String peers)
    {
        this.filename = filename;
        this.blocks = blocks;
        this.availability = availability;
        this.filesize = filesize;
        this.peers = peers;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getBlocks()
    {
        return blocks;
    }

    public void setBlocks(String blocks)
    {
        this.blocks = blocks;
    }

    public String getAvailability()
    {
        return availability;
    }

    public void setAvailability(String availability)
    {
        this.availability = availability;
    }

    public String getFilesize()
    {
        return filesize;
    }

    public void setFilesize(String filesize)
    {
        this.filesize = filesize;
    }

    public String getPeers()
    {
        return peers;
    }

    public void setPeers(String peers)
    {
        this.peers = peers;
    }

   
}
