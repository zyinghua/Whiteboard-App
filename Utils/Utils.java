// Author: Yinghua Zhou
// Student ID: 1308266

package Utils;

import javax.swing.*;
import java.awt.*;

public class Utils {
    public static final String RMI_WHITEBOARD_SERVER_NAME = "WhiteBoard";
    public static final Color BOARD_BACKGROUND_COLOR = Color.WHITE;
    public static final int BOARD_WIDTH = 1300;
    public static final int BOARD_HEIGHT = 800;
    public static final int BOARD_MENU_BAR_HEIGHT = 35;
    public static final String BOARD_CLOSE_MANAGER = "You are the manager, closing the board will terminate the program and force all clients to exit, do you want to continue? (Please make sure to save the board if needed)";
    public static final String BOARD_CLOSE_CLIENT = "Are you sure to exit the board?";
    public static final String ICON_PATH = "/icons/";
    public static final String ICON_FILE = ICON_PATH + "file.png";
    public static final String ICON_NEW_BOARD = ICON_PATH + "new_board.png";
    public static final String ICON_OPEN_BOARD = ICON_PATH + "open_board.png";
    public static final String ICON_SAVE_BOARD = ICON_PATH + "save.png";
    public static final String ICON_SAVE_AS_BOARD = ICON_PATH + "save_as.png";
    public static final String ICON_CLOSE_BOARD = ICON_PATH + "close_board.png";
    public static final String ICON_COLOR = ICON_PATH + "color.png";
    public static final String ICON_SHAPE = ICON_PATH + "shape.png";
    public static final String ICON_LINE = ICON_PATH + "line.png";
    public static final String ICON_RECTANGLE = ICON_PATH + "rectangle.png";
    public static final String ICON_CIRCLE = ICON_PATH + "circle.png";
    public static final String ICON_OVAL = ICON_PATH + "oval.png";
    public static final String ICON_TEXT = ICON_PATH + "text.png";
    public static final String ICON_DRAW = ICON_PATH + "draw.png";
    public static final String ICON_PEN = ICON_PATH + "pen.png";
    public static final String ICON_CURSOR = ICON_PATH + "cursor.png";
    public static final String ICON_PEN_STROKE_WIDTH = ICON_PATH + "g_stroke.png";
    public static final String ICON_FONT_SIZE = ICON_PATH + "font_size.png";
    public static final int MODE_DEFAULT_CURSOR = 0;
    public static final int MODE_DRAW_LINE = 1;
    public static final int MODE_DRAW_CIRCLE = 2;
    public static final int MODE_DRAW_OVAL = 3;
    public static final int MODE_DRAW_RECT = 4;
    public static final int MODE_FREE_DRAW = 5;
    public static final int MODE_PAINT_TEXT = 6;
    public static final int DEFAULT_STROKE_WIDTH = 2;
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final String NEW_BOARD_WARNING = "Create a new board will overwrite the current one, please make sure you have saved the current one as needed before you continue. Are you sure to continue?";

    public static class WaitingDialog extends JDialog {
        private boolean isCanceled;
        private final JLabel messageLabel;
        private final JProgressBar progressBar;
        private final JButton cancelButton;

        public WaitingDialog(String message) {
            isCanceled = false;

            messageLabel = new JLabel(message, SwingConstants.CENTER);

            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);

            cancelButton = new JButton("Cancel");
            JPanel buttonPanel = new JPanel();  // create a new JPanel for the button
            buttonPanel.setLayout(new FlowLayout()); // FlowLayout centers the button
            buttonPanel.add(cancelButton); // add the button to the new JPanel

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Creates a vertical gap of 5 pixels
            panel.add(messageLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Creates a vertical gap of 10 pixels
            panel.add(progressBar);
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Creates a vertical gap of 5 pixels
            panel.add(buttonPanel); // add the buttonPanel instead of the button directly

            getContentPane().add(panel);
            setSize(350, 130);
            setLocationRelativeTo(null);
            setModal(true);
            setTitle("Waiting");
        }

        public void setMessage(String message) {
            messageLabel.setText(message);
        }

        public boolean isCanceled() {
            return isCanceled;
        }
    }
}
