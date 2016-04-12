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
public class SearchFileRow
{
    private String searchFilename, searchFilehash, searchBlocks, searchAvailability, searchFilesize, searchPeers;

    public SearchFileRow(String filename, String filehash, String blocks, String availability, String filesize, String peers)
    {
        this.searchFilename = filename;
        this.searchFilehash = filehash;
        this.searchBlocks = blocks;
        this.searchAvailability = availability;
        this.searchFilesize = filesize;
        this.searchPeers = peers;
    }

    public String getSearchFilename()
    {
        return searchFilename;
    }

    public void setSearchFilename(String searchFilename)
    {
        this.searchFilename = searchFilename;
    }

    public String getSearchFilehash()
    {
        return searchFilehash;
    }

    public void setSearchFilehash(String searchFilehash)
    {
        this.searchFilehash = searchFilehash;
    }

    public String getSearchBlocks()
    {
        return searchBlocks;
    }

    public void setSearchBlocks(String searchBlocks)
    {
        this.searchBlocks = searchBlocks;
    }

    public String getSearchAvailability()
    {
        return searchAvailability;
    }

    public void setSearchAvailability(String searchAvailability)
    {
        this.searchAvailability = searchAvailability;
    }

    public String getSearchFilesize()
    {
        return searchFilesize;
    }

    public void setSearchFilesize(String searchFilesize)
    {
        this.searchFilesize = searchFilesize;
    }

    public String getSearchPeers()
    {
        return searchPeers;
    }

    public void setSearchPeers(String searchPeers)
    {
        this.searchPeers = searchPeers;
    }
    
    
}
