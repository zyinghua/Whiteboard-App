// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class WhiteboardUser {
    private boolean isManager;
    private String username;
    private File specifiedFilePath;
    private BoardPanel boardPanel;
    private DefaultListModel<String> chatListModel;
    private DefaultListModel<String> currUserListModel;

    public WhiteboardUser(boolean isManager, String username) {
        this.isManager = isManager;
        this.username = username;
        this.specifiedFilePath = null;
        this.boardPanel = new BoardPanel();
        this.chatListModel = new DefaultListModel<>();
        this.currUserListModel = new DefaultListModel<>();
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

    public void addChatMessage(String username, String message) {
        this.chatListModel.addElement(username + ": " + message);
    }

    public void addChatMessage(String message) {
        this.chatListModel.addElement(this.username + "(You) : " + message);
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
}