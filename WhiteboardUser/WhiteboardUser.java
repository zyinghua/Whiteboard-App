package WhiteboardUser;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import interfaces.IRemoteBoard;
import Utils.Utils;

public class WhiteboardUser {
    private boolean isManager;
    private String username;
    private File specifiedFilePath;
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
        this.specifiedFilePath = null;
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
}