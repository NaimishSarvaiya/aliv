package com.iotsmartaliv.model;

/**
 * This model class is used for chat data .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class Chat {
    String send, receive;
    boolean isUser = true;

    public Chat(String send, String receive, boolean isUser) {
        this.send = send;
        this.receive = receive;
        this.isUser = isUser;
    }

    public Chat() {

    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }
}
