package Utils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class test extends JPanel {

    private BufferedImage image;
    private Graphics2D graphics2D;
    private int currentX, currentY, oldX, oldY;

    public test() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                oldX = e.getX();
                oldY = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                if (graphics2D != null) {
                    graphics2D.drawLine(oldX, oldY, currentX, currentY);
                }
                repaint();
                oldX = currentX;
                oldY = currentY;
            }
        });
    }

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_RGB);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        graphics2D.setPaint(Color.black);
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Draw");
        test drawPanel = new test();
        frame.add(drawPanel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
