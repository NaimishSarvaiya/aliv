package com.iotsmartaliv.model;

import com.iotsmartaliv.apiCalling.models.Broadcast;

import java.io.Serializable;
import java.util.ArrayList;

public class BroadcastDocumentFolder implements Serializable {

    private String documentFolderTitle;
    private ArrayList<Broadcast> broadcasts;
    private String readStatus;

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getDocumentFolderTitle() {
        return documentFolderTitle;
    }

    public void setDocumentFolderTitle(String documentFolderTitle) {
        this.documentFolderTitle = documentFolderTitle;
    }

    public ArrayList<Broadcast> getBroadcasts() {
        return broadcasts;
    }

    public void setBroadcasts(ArrayList<Broadcast> broadcasts) {
        this.broadcasts = broadcasts;
    }
}
