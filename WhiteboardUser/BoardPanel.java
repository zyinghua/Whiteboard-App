// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;

import Utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class BoardPanel extends JPanel {
    private final WhiteboardUser user;
    private int currentMode;
    private int strokeWidth;
    private int graphicsFontSize;
    private Color currColor;
    private BufferedImage boardImage;  // All drawing is done on this image first, then rendered to the panel
    private Graphics2D graphics2D;
    private Point mouseStartPt, mouseEndPt;

    public BoardPanel(WhiteboardUser user) {
        initComponents();
        this.user = user;
    }

    private void initComponents() {
        setBackground(Utils.BOARD_BACKGROUND_COLOR);
        setDoubleBuffered(false);
        setDefaultValues();

        addMouseMotionListener(new MouseMotionAdapter() {  // Account for continuous mouse action
            public void mouseDragged(MouseEvent e) {
                if(graphics2D != null)
                {
                    if (getCurrentMode() == Utils.MODE_FREE_DRAW) {
                        updateEndPt(e);
                        draw();
                        updateStartPt(e);
                    }else if(getCurrentMode() == Utils.MODE_DRAW_LINE
                            || getCurrentMode() == Utils.MODE_DRAW_RECT
                            || getCurrentMode() == Utils.MODE_DRAW_OVAL
                            || getCurrentMode() == Utils.MODE_DRAW_CIRCLE){

                        graphics2D.setXORMode(Color.WHITE);
                        draw();

                        updateEndPt(e);
                        draw();
                        graphics2D.setPaintMode(); // XOR free
                    }
                }

                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {  // Account for single discrete mouse action
            public void mousePressed(MouseEvent e) {
                if (mouseStartPt == null)  // Does not take concurrent left-right mouse pressed
                {
                    updateStartPt(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if(currentMode == Utils.MODE_PAINT_TEXT || currentMode == Utils.MODE_FREE_DRAW) {
                    updateEndPt(e);
                    draw();
                }
                else if (currentMode == Utils.MODE_DRAW_LINE
                        || currentMode == Utils.MODE_DRAW_RECT
                        || currentMode == Utils.MODE_DRAW_OVAL
                        || currentMode == Utils.MODE_DRAW_CIRCLE) {
                    if (checkPtsValidity(mouseStartPt, mouseEndPt)) {
                        graphics2D.setXORMode(Color.WHITE); // cancel the previous draw
                        draw();

                        graphics2D.setPaintMode();  // use paint mode to finalise the shape draw
                        draw();
                        sendDrawSignalRemote(mouseStartPt, mouseEndPt, getCurrentMode()); // draw only when released for shapes
                    }
                }

                resetPts();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (boardImage == null) {
            boardImage = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
            initGraphics();
            clear();
        }

        g.drawImage(boardImage, 0, 0, null); // board_image by itself nothing to do with the panel, rendering here.
    }

    public void clear() {
        graphics2D.setColor(getBackground());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setColor(currColor);
    }

    public void initGraphics() {
        graphics2D = (Graphics2D) boardImage.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(getStrokeWidth()));
        graphics2D.setFont(new Font("TimesRoman", Font.PLAIN, graphicsFontSize));
        graphics2D.setColor(currColor);
    }

    private void draw() {
        if (checkPtsValidity(mouseStartPt, mouseEndPt)) {
            if(getCurrentMode() == Utils.MODE_FREE_DRAW){
                graphics2D.drawLine(mouseStartPt.x, mouseStartPt.y, mouseEndPt.x, mouseEndPt.y);
                sendDrawSignalRemote(mouseStartPt, mouseEndPt, getCurrentMode());
            }else if(getCurrentMode() == Utils.MODE_DRAW_LINE){
                graphics2D.drawLine(mouseStartPt.x, mouseStartPt.y, mouseEndPt.x, mouseEndPt.y);
            }else if(getCurrentMode() == Utils.MODE_DRAW_RECT){
                Point shapeStartPt = getShapeStartPt(mouseStartPt, mouseEndPt);
                graphics2D.drawRect(shapeStartPt.x, shapeStartPt.y, Math.abs(mouseEndPt.x - mouseStartPt.x), Math.abs(mouseEndPt.y - mouseStartPt.y));
            }else if(getCurrentMode() == Utils.MODE_DRAW_OVAL){
                Point shapeStartPt = getShapeStartPt(mouseStartPt, mouseEndPt);
                graphics2D.drawOval(shapeStartPt.x, shapeStartPt.y, Math.abs(mouseEndPt.x - mouseStartPt.x), Math.abs(mouseEndPt.y - mouseStartPt.y));
            }else if(getCurrentMode() == Utils.MODE_DRAW_CIRCLE) {
                int width = Math.abs(mouseStartPt.x - mouseEndPt.x);
                int height = Math.abs(mouseStartPt.y - mouseEndPt.y);
                int diameter = Math.max(width, height);
                Point shapeStartPt = getShapeStartPt(mouseStartPt, mouseEndPt);
                graphics2D.drawOval(shapeStartPt.x, shapeStartPt.y, diameter, diameter);
            } else if(getCurrentMode() == Utils.MODE_PAINT_TEXT) {
                String text = JOptionPane.showInputDialog(null, "Please enter the text");
                if(text != null){
                    graphics2D.drawString(text, mouseEndPt.x, mouseEndPt.y);
                    sendDrawSignalRemote(graphics2D.getFont(), mouseEndPt, text);
                }
            }
        }

        repaint();
    }

    public void drawRemote(Color color, int strokeWidth, Point startPt, Point endPt, int mode)  // receiver method
    {
        if(checkPtsValidity(startPt, endPt))
        {
            BufferedImage temp_bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D temp_g2d = (Graphics2D) temp_bi.getGraphics();
            temp_g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            temp_g2d.setStroke(new BasicStroke(strokeWidth));
            temp_g2d.setColor(color);

            if(mode == Utils.MODE_FREE_DRAW){
                temp_g2d.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);
            }else if(mode == Utils.MODE_DRAW_LINE){
                temp_g2d.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);
            }else if(mode == Utils.MODE_DRAW_RECT){
                Point shapeStartPt = getShapeStartPt(startPt, endPt);
                temp_g2d.drawRect(shapeStartPt.x, shapeStartPt.y, Math.abs(endPt.x - startPt.x), Math.abs(endPt.y - startPt.y));
            }else if(mode == Utils.MODE_DRAW_OVAL){
                Point shapeStartPt = getShapeStartPt(startPt, endPt);
                temp_g2d.drawOval(shapeStartPt.x, shapeStartPt.y, Math.abs(endPt.x - startPt.x), Math.abs(endPt.y - startPt.y));
            }else if(mode == Utils.MODE_DRAW_CIRCLE){
                int width = Math.abs(startPt.x - endPt.x);
                int height = Math.abs(startPt.y - endPt.y);
                int diameter = Math.max(width, height);
                Point shapeStartPt = getShapeStartPt(startPt, endPt);
                temp_g2d.drawOval(shapeStartPt.x, shapeStartPt.y, diameter, diameter);
            }

            graphics2D.drawImage(temp_bi, 0, 0, null);

            repaint();
        }
    }

    public void drawRemote(Color color, Font font, Point endPt, String text)  // receiver method
    {
        if((endPt != null) && (text != null) && (!text.isEmpty()))
        {
            BufferedImage temp_bi = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D temp_g2d = (Graphics2D) temp_bi.getGraphics();
            temp_g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            temp_g2d.setFont(font);
            temp_g2d.setColor(color);

            temp_g2d.drawString(text,endPt.x, endPt.y);

            graphics2D.drawImage(temp_bi, 0, 0, null);

            repaint();
        }
    }

    public void sendDrawSignalRemote(Point startPt, Point endPt, int mode)  // sender method
    {
        for (String username : this.user.getClientRemotes().keySet()) {
            if (!username.equals(this.user.getUsername()))
            {
                new Thread(() -> {
                    try {
                        this.user.getClientRemotes().get(username).draw(currColor, strokeWidth, startPt, endPt, mode);
                    } catch (RemoteException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        }
    }

    public void sendDrawSignalRemote(Font font, Point endPt, String text)  // sender method
    {
        for (String username : this.user.getClientRemotes().keySet()) {
            if (!username.equals(this.user.getUsername()))
            {
                new Thread(() -> {
                    try {
                        this.user.getClientRemotes().get(username).draw(currColor, font, endPt, text);
                    } catch (RemoteException ignored) {

                    }
                }).start();
            }
        }
    }



    private void resetPts() {
        mouseStartPt = null;
        mouseEndPt = null;
    }

    private void updateStartPt(MouseEvent e) {
        mouseStartPt = e.getPoint();
    }

    private void updateEndPt(MouseEvent e) {
        mouseEndPt = e.getPoint();
    }

    private Point getShapeStartPt(Point startPt, Point endPt) {
        return new Point(Math.min(startPt.x, endPt.x), Math.min(startPt.y, endPt.y));
    }

    private boolean checkPtsValidity(Point startPt, Point endPt) {
        return (startPt != null) && (endPt != null);
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        graphics2D.setStroke(new BasicStroke(strokeWidth));
    }

    public int getGraphicsFontSize() {
        return graphicsFontSize;
    }

    public void setGraphicsFontSize(int graphicsFontSize) {
        this.graphicsFontSize = graphicsFontSize;
        graphics2D.setFont(new Font("TimesRoman", Font.PLAIN, graphicsFontSize));
    }

    public Color getCurrColor() {
        return currColor;
    }

    public void setCurrColor(Color currColor) {
        this.currColor = currColor;
        this.graphics2D.setColor(currColor);
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;

        if (currentMode == Utils.MODE_DEFAULT_CURSOR) {
            setCursor(Cursor.getDefaultCursor());
        } else if (currentMode == Utils.MODE_FREE_DRAW) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == Utils.MODE_DRAW_LINE) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == Utils.MODE_DRAW_RECT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == Utils.MODE_DRAW_OVAL) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == Utils.MODE_DRAW_CIRCLE) {
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        } else if (currentMode == Utils.MODE_PAINT_TEXT) {
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
    }

    public BufferedImage getBoardImage() {
        return boardImage;
    }

    public byte[] getBoardImagesInBytes() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(boardImage, "png", baos);
        return baos.toByteArray();
    }

    public void setBoardImageFromBytes(byte[] boardImageInBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(boardImageInBytes);
        this.boardImage = ImageIO.read(inputStream); // Now boardImage is updated
        initGraphics(); // Gotta re-init the graphics2D object to inline with the new boardImage
        repaint(); // Now paint the new boardImage to the actual panel
    }

    private void setDefaultValues() {
        this.strokeWidth = Utils.DEFAULT_STROKE_WIDTH;
        this.graphicsFontSize = Utils.DEFAULT_FONT_SIZE;
        this.currentMode = Utils.MODE_DEFAULT_CURSOR;
        this.currColor = Color.BLACK;
        this.mouseStartPt = null;
        this.mouseEndPt = null;
    }

    public void clearBoard() {
        clear();
        repaint();
    }

    public void setBoard(Image new_image) {
        clear(); // Deal with transparent new image

        graphics2D.drawImage(new_image, 0, 0, null); // boardImage is updated
        repaint(); // Now update the actual graphics of the GUI Panel
    }
}
