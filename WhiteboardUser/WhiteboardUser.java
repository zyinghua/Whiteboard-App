package WhiteboardUser;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import interfaces.IRemoteBoard;
import Utils.Utils;

public class WhiteboardUser {
    private boolean isManager;
    private String username;
    private String specifiedFilePath;
    private String specifiedFileName;
    private int currentMode;
    private Point remoteStart;
    private Point remoteEnd;
    private String remoteMode;
    private IRemoteBoard remoteBoard;
    private Color remoteColor;
    DefaultListModel chatModel;

    private BufferedImage image;
    private String textDraw;
    private String remoteTextDraw;

    public WhiteboardUser(boolean isManager, String username) {
        this.isManager = isManager;
        this.username = username;
        this.currentMode = Utils.MODE_DEFAULT_CURSOR;
    }


    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public String getSpecifiedFilePath() {
        return specifiedFilePath;
    }

    public void setSpecifiedFilePath(String specifiedFilePath) {
        this.specifiedFilePath = specifiedFilePath;
    }

    public String getSpecifiedFileName() {
        return specifiedFileName;
    }

    public void setSpecifiedFileName(String specifiedFileName) {
        this.specifiedFileName = specifiedFileName;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }
}