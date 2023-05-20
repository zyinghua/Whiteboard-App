// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;
import remotes.WhiteboardUserRemote;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.rmi.RemoteException;
import java.util.HashMap;

public class WhiteboardUser {
    private boolean isManager;
    private String username;
    private File specifiedFilePath;
    private BoardPanel boardPanel;
    private DefaultListModel<String> chatListModel;
    private DefaultListModel<String> currUserListModel;
    private HashMap<String, WhiteboardUserRemote> clientRemotes;

    public WhiteboardUser(boolean isManager, String username) {
        this.isManager = isManager;
        this.username = username;
        this.specifiedFilePath = null;
        this.boardPanel = new BoardPanel();
        this.chatListModel = new DefaultListModel<>();
        this.currUserListModel = new DefaultListModel<>();
        this.clientRemotes = new HashMap<>();
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public File getSpecifiedFilePath() {
        return specifiedFilePath;
    }

    public void setSpecifiedFilePath(File specifiedFilePath) {
        this.specifiedFilePath = specifiedFilePath;
    }

    public DefaultListModel<String> getChatListModel() {
        return chatListModel;
    }

    public void setChatListModel(DefaultListModel<String> chatListModel) {
        this.chatListModel = chatListModel;
    }

    public void receiveChatMessage(String username, String message) {
        this.chatListModel.addElement(username + ": " + message);
    }

    public void sendChatMessage(String message) {
        this.chatListModel.addElement(this.username + "(You) : " + message);
        sendChatMessageRemote(message);
    }

    public DefaultListModel<String> getCurrUserListModel() {
        return currUserListModel;
    }

    public void setCurrUserListModel(DefaultListModel<String> currUserListModel) {
        this.currUserListModel = currUserListModel;
    }

    public void addUser(String username) {
        this.currUserListModel.addElement(username);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public void setBoardImage(BufferedImage boardImage) {
        this.boardPanel.setBoardImage(boardImage);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, WhiteboardUserRemote> getClientRemotes() {
        return clientRemotes;
    }

    public void setClientRemotes(HashMap<String, WhiteboardUserRemote> clientRemotes) {
        this.clientRemotes = clientRemotes;
    }
    public void addClientRemote(String username, WhiteboardUserRemote clientRemote){
        clientRemotes.put(username, clientRemote);
    }

    public void sendChatMessageRemote(String message) {
        for (String username : clientRemotes.keySet()) {
            if (!username.equals(this.username)) {
                new Thread(() -> {
                    try {
                        clientRemotes.get(username).addChatMessage(this.username, message);
                    } catch (RemoteException e) {
                        if (isManager) {
                            JOptionPane.showMessageDialog(null, "Something wrong with the remote connection to " + username + ". User removed.", "Error", JOptionPane.ERROR_MESSAGE);
                            // kick the user

                        } else {
                            JOptionPane.showMessageDialog(null, "Something wrong with the remote connection, suggesting restart the program.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }).start();
            }
        }
    }
}