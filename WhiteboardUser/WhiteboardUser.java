package WhiteboardUser;
import javax.swing.*;
import java.io.File;

public class WhiteboardUser {
    private boolean isManager;
    private String username;
    private File specifiedFilePath;
    private BoardPanel boardPanel;
    private DefaultListModel<String> chatMsgListModel;
    private DefaultListModel<String> currUserListModel;

    public WhiteboardUser(boolean isManager, String username) {
        this.isManager = isManager;
        this.username = username;
        this.specifiedFilePath = null;
        this.boardPanel = new BoardPanel();
        this.chatMsgListModel = new DefaultListModel<>();
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

    public DefaultListModel<String> getChatMsgListModel() {
        return chatMsgListModel;
    }

    public void addChatMessage(String username, String message) {
        this.chatMsgListModel.addElement(username + ": " + message);
    }

    public void addChatMessage(String message) {
        this.chatMsgListModel.addElement(this.username + "(You) : " + message);
    }

    public DefaultListModel<String> getCurrUserListModel() {
        return currUserListModel;
    }

    public void addUser(String username) {
        this.currUserListModel.addElement(username);
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void sendMessage(String message) {

    }
}