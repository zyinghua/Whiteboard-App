package WhiteboardUser;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import interfaces.IRemoteBoard;

public class WhiteboardUser {
    private String mode;
    private Point start;
    private Point end;
    private Point remoteStart;
    private Point remoteEnd;
    private String remoteMode;
    private IRemoteBoard remoteBoard;
    public final String DRAWLINE = "drawLine";
    public final String FREEDRAW = "freeDraw";
    public final String DRAWREC = "drawRec";
    public final String DRAWCIRCLE = "drawCircle";
    public final String DRAWTRI = "drawTri";
    public final String DRAWTEXT = "drawText";
    public final String NOTHING = "";
    private String name;
    private Color color;
    private Color remoteColor;
    DefaultListModel chatModel;
    Graphics g;
    private boolean isManager;
    private String fileName;
    private BufferedImage image;
    private String textDraw;
    private String remoteTextDraw;
}