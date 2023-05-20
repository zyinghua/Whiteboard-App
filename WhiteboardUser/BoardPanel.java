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

public class BoardPanel extends JPanel {
    private int currentMode;
    private int strokeWidth;
    private int graphicsFontSize;
    private Color currColor;
    private BufferedImage boardImage;
    private Graphics2D graphics2D;
    private Point mouseStartPt, mouseEndPt;

    public BoardPanel() {
        initComponents();
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
                        graphics2D.setPaintMode(); // XOR free
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
                    }
                }

                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {  // Account for single discrete mouse action
            public void mousePressed(MouseEvent e) {
                updateStartPt(e);
            }
            public void mouseReleased(MouseEvent e) {
                if(currentMode == Utils.MODE_PAINT_TEXT) {
                    updateEndPt(e);
                    draw();
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
        if (checkPtsValidity()) {
            if(getCurrentMode() == Utils.MODE_FREE_DRAW){
                graphics2D.drawLine(mouseStartPt.x, mouseStartPt.y, mouseEndPt.x, mouseEndPt.y);
            }else if(getCurrentMode() == Utils.MODE_DRAW_LINE){
                graphics2D.drawLine(mouseStartPt.x, mouseStartPt.y, mouseEndPt.x, mouseEndPt.y);
            }else if(getCurrentMode() == Utils.MODE_DRAW_RECT){
                graphics2D.drawRect(getShapeStartPt().x, getShapeStartPt().y, Math.abs(mouseEndPt.x - mouseStartPt.x), Math.abs(mouseEndPt.y - mouseStartPt.y));
            }else if(getCurrentMode() == Utils.MODE_DRAW_OVAL){
                graphics2D.drawOval(getShapeStartPt().x, getShapeStartPt().y, Math.abs(mouseEndPt.x - mouseStartPt.x), Math.abs(mouseEndPt.y - mouseStartPt.y));
            }else if(getCurrentMode() == Utils.MODE_DRAW_CIRCLE){
                int width = Math.abs(mouseStartPt.x - mouseEndPt.x);
                int height = Math.abs(mouseStartPt.y - mouseEndPt.y);
                int diameter = Math.max(width, height);
                graphics2D.drawOval(getShapeStartPt().x, getShapeStartPt().y, diameter, diameter);
            } else if(getCurrentMode() == Utils.MODE_PAINT_TEXT){
                String text = JOptionPane.showInputDialog(null, "Please enter the text");
                if(text != null){
                    graphics2D.drawString(text, mouseEndPt.x, mouseEndPt.y);
                }
            }

            repaint();
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

    private Point getShapeStartPt() {
        return new Point(Math.min(mouseStartPt.x, mouseEndPt.x), Math.min(mouseStartPt.y, mouseEndPt.y));
    }

    private boolean checkPtsValidity() {
        return (mouseStartPt != null) && (mouseEndPt != null);
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
