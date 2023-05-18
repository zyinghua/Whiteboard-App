package WhiteboardUser;

import Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class BoardPanel extends JPanel {
    private int currentMode;
    private int strokeWidth;
    private Color currColor;
    private BufferedImage board_image;
    private Graphics2D graphics2D;
    private Point mouseStartPt, mouseEndPt;

    public BoardPanel() {
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
                graphics2D.setPaintMode();
                updateEndPt(e);
                draw();
                resetPts();
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (board_image == null) {
            board_image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
            graphics2D = (Graphics2D) board_image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }

        g.drawImage(board_image, 0, 0, null); // board_image by itself nothing to do with the panel, binding here.
    }

    public void clear() {
        graphics2D.setPaint(getBackground());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(currColor);
        graphics2D.setStroke(new BasicStroke(getStrokeWidth()));
        repaint();
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

    private void updateBothPts(MouseEvent e) {
        updateStartPt(e);
        updateEndPt(e);
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
        this.graphics2D.setStroke(new BasicStroke(getStrokeWidth()));
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
        return board_image;
    }

    public void setBoardImage(BufferedImage board_image) {
        this.board_image = board_image;
    }

    private void setDefaultValues() {
        this.strokeWidth = 2;
        this.currentMode = Utils.MODE_DEFAULT_CURSOR;
        this.currColor = Color.BLACK;
        this.mouseStartPt = null;
        this.mouseEndPt = null;
    }

    public void clearBoard() {
        clear();
    }

    public void setBoard(Image new_image) {
        // Deal with transparent new image
        graphics2D.setPaint(getBackground());
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(currColor);

        graphics2D.drawImage(new_image, 0, 0, null);
        repaint();
    }
}
