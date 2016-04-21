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
    private String searchFilename, searchFilehash, searchBlocks, searchAvailability, searchFilesize, searchPeers, searchDownloadSpeed, searchUploadSpeed;

    public SearchFileRow(String searchFilename, String searchFilehash, String searchBlocks, String searchAvailability, String searchFilesize, String searchPeers, String searchDownloadSpeed, String searchUploadSpeed)
    {
        this.searchFilename = searchFilename;
        this.searchFilehash = searchFilehash;
        this.searchBlocks = searchBlocks;
        this.searchAvailability = searchAvailability;
        this.searchFilesize = searchFilesize;
        this.searchPeers = searchPeers;
        this.searchDownloadSpeed = searchDownloadSpeed;
        this.searchUploadSpeed = searchUploadSpeed;
    }

    public String getSearchDownloadSpeed()
    {
        return searchDownloadSpeed;
    }

    public void setSearchDownloadSpeed(String searchDownloadSpeed)
    {
        this.searchDownloadSpeed = searchDownloadSpeed;
    }

    public String getSearchUploadSpeed()
    {
        return searchUploadSpeed;
    }

    public void setSearchUploadSpeed(String searchUploadSpeed)
    {
        this.searchUploadSpeed = searchUploadSpeed;
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
