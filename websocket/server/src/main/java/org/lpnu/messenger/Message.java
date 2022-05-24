package org.lpnu.messenger;

/**
 * POJO representing the chat message
 */
public class Message {
    private String text;
    private String username;
    private String avatar;

    public Message(){

    }

    public Message(String text , String username, String avatar) {
        this.text = text;
        this.username = username;
        this.avatar = avatar;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
