// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import Utils.Utils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class WhiteboardGUI extends JFrame{
    private final WhiteboardUser user;
    private static final String guiName = "Whiteboard";
    private JMenuBar menuBar;
    private JRadioButtonMenuItem drawLine;
    private JRadioButtonMenuItem drawRect;
    private JRadioButtonMenuItem drawOval;
    private JRadioButtonMenuItem drawCir;
    private JMenu shapeMenu;
    private JMenu textMenu;
    private JMenu colorMenu;
    private ColorIcon colorIcon;
    private JMenuItem colorSelector;
    private JRadioButtonMenuItem paintText;
    private JMenuItem font_size_button;
    private JMenu cursorMenu;
    private JRadioButtonMenuItem cursorButton;
    private JLabel chatLabel;
    private JList<String> chatList;
    private JScrollPane chatBoxScrollPane;
    private JPanel chatPanel;
    private JMenu fileMenu;
    private JMenuItem fileNewBoard;
    private JMenuItem fileOpen;
    private JMenuItem fileSave;
    private JMenuItem fileSaveAs;
    private JMenuItem fileClose;
    private JMenu freeDrawMenu;
    private JRadioButtonMenuItem freeDrawButton;
    private JMenuItem pen_stroke_width_button;
    private JScrollPane inputScrollPane;
    private HintTextArea inputArea;
    private JPanel inputPanel;
    private JLabel userListLabel;
    private JList<String> userList;
    private JPanel userListPanel;
    private JScrollPane userListScrollPane;
    private ButtonGroup toolBtnGroup;
    private JButton sendButton;


    public WhiteboardGUI(WhiteboardUser user) {
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        initGUIComponents();
        initFundamentalComponents();
        setSize(new Dimension(Utils.BOARD_WIDTH, Utils.BOARD_HEIGHT));

        // Set the close board action
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleBoardClose();
            }
        });

        setUpFileMenu();
        setUpShapeMenu();
        setUpColorMenu();
        setUpFreeDrawMenu();
        setUpTextMenu();
        setUpCursorMenu();

        this.menuBar.setPreferredSize(new Dimension(0, Utils.BOARD_MENU_BAR_HEIGHT));
        menuBar.add(fileMenu);
        menuBar.add(shapeMenu);
        menuBar.add(colorMenu);
        menuBar.add(freeDrawMenu);
        menuBar.add(textMenu);
        menuBar.add(cursorMenu);
        setJMenuBar(menuBar);

        setUpBoardPanel();
        setUpUserListPanel();
        setUpChatPanel();
        setUpInputPanel();

        JPanel rightPanel = new JPanel();
        BoxLayout rightLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(rightLayout);

        rightPanel.add(userListPanel);
        rightPanel.add(chatPanel);
        rightPanel.add(inputPanel);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGap(10)
                        .addComponent(user.getBoardPanel())
                        .addGap(15)
                        .addComponent(rightPanel)
                        .addGap(10)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(10, 10, 15)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(user.getBoardPanel())
                                .addComponent(rightPanel)
                        )
                        .addGap(10, 10, 15)
        );
    }

    private void setUpFileMenu() {
        fileMenu.setVisible(user.isManager());
        fileMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_FILE))));
        fileMenu.setText("File");

        fileNewBoard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        fileNewBoard.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_NEW_BOARD))));
        fileNewBoard.setText("New");
        fileNewBoard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileNew();
            }
        });

        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        fileOpen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_OPEN_BOARD))));
        fileOpen.setText("Open");
        fileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileOpen();
            }
        });

        fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        fileSave.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_SAVE_BOARD))));
        fileSave.setText("Save");
        fileSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileSave();
            }
        });

        fileSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        fileSaveAs.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_SAVE_AS_BOARD))));
        fileSaveAs.setText("Save As");
        fileSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileSaveAs();
            }
        });

        fileClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        fileClose.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CLOSE_BOARD))));
        fileClose.setText("Close Board");
        fileClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleFileClose();
            }
        });

        fileMenu.add(fileNewBoard);
        fileMenu.add(fileOpen);
        fileMenu.add(fileSave);
        fileMenu.add(fileSaveAs);
        fileMenu.add(fileClose);
    }

    private void handleFileNew() {
        int result = JOptionPane.showConfirmDialog(this, Utils.NEW_BOARD_WARNING, "New Board", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            user.getBoardPanel().clearBoard();
            ((WhiteboardManager) user).newBoardRemote();
        }
    }

    private void handleFileSave() {
        if (user.getSpecifiedFilePath() == null) {
            handleFileSaveAs();
        } else {
            try {
                ImageIO.write(user.getBoardPanel().getBoardImage(), "png", user.getSpecifiedFilePath());
                JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Save failed, an error occurred, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleFileSaveAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                    selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".png");
                }

                user.setSpecifiedFilePath(selectedFile);
                ImageIO.write(user.getBoardPanel().getBoardImage(), "png", selectedFile);
                JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Save failed, an error occurred, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleFileOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                if (!selectedFile.getName().toLowerCase().endsWith(".png")) {
                    JOptionPane.showMessageDialog(this, "Please select a file with a suffix of '.png', only a png file is accepted.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Image image = ImageIO.read(selectedFile).getScaledInstance(user.getBoardPanel().getSize().width, user.getBoardPanel().getSize().height, Image.SCALE_SMOOTH);;
                    user.getBoardPanel().setBoard(image);
                    ((WhiteboardManager) user).loadBoardRemote();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Open failed, an error occurred, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleFileClose() {
        handleBoardClose();
    }

    private void setUpShapeMenu() {
        this.shapeMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_SHAPE))));
        this.shapeMenu.setText("Shape");

        drawLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        drawLine.setText("Line");
        drawLine.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_LINE))));
        drawLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_DRAW_LINE);
            }
        });

        drawRect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        drawRect.setText("Rectangle");
        drawRect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_RECTANGLE))));
        drawRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_DRAW_RECT);
            }
        });

        drawOval.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        drawOval.setText("Oval");
        drawOval.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_OVAL))));
        drawOval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_DRAW_OVAL);
            }
        });

        drawCir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        drawCir.setText("Circle");
        drawCir.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CIRCLE))));
        drawCir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_DRAW_CIRCLE);
            }
        });

        shapeMenu.add(drawLine);
        shapeMenu.add(drawCir);
        shapeMenu.add(drawOval);
        shapeMenu.add(drawRect);

        toolBtnGroup.add(drawLine);
        toolBtnGroup.add(drawCir);
        toolBtnGroup.add(drawOval);
        toolBtnGroup.add(drawRect);
    }

    private void setUpColorMenu() {
        colorMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_COLOR))));
        colorMenu.setText("Color");

        colorSelector.setIcon(this.colorIcon);
        colorSelector.setText("Change Color");
        colorSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Please select a color", Color.BLACK);
                colorIcon.setColor(color);
                colorSelector.repaint();
                user.getBoardPanel().setCurrColor(color);
            }
        });

        colorMenu.add(colorSelector);
    }

    private void setUpTextMenu() {
        textMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_TEXT))));
        textMenu.setText("Text");

        paintText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
        paintText.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/paint_text.png"))));
        paintText.setText("Paint Text");
        paintText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_PAINT_TEXT);
            }
        });

        font_size_button.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        font_size_button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_FONT_SIZE))));
        font_size_button.setText("Font Size");

        font_size_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int min = 6;
                int max = 128;

                SpinnerNumberModel model = new SpinnerNumberModel(user.getBoardPanel().getGraphicsFontSize(), //initial value
                        min, //min
                        max, //max
                        1); //step
                JSpinner spinner = new JSpinner(model);
                int result = JOptionPane.showOptionDialog(null, spinner, "Enter a number between " + min + " and " + max, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    user.getBoardPanel().setGraphicsFontSize((int) spinner.getValue());
                }
            }
        });

        textMenu.add(paintText);
        textMenu.add(font_size_button);

        toolBtnGroup.add(paintText);
    }

    private void setUpFreeDrawMenu() {
        freeDrawMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_DRAW))));
        freeDrawMenu.setText("Draw");

        freeDrawButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        freeDrawButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_PEN))));
        freeDrawButton.setText("Pen");
        freeDrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_FREE_DRAW);
            }
        });

        pen_stroke_width_button.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        pen_stroke_width_button.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_PEN_STROKE_WIDTH))));
        pen_stroke_width_button.setText("Pen Stroke Width");

        pen_stroke_width_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int min = 1;
                int max = 20;

                SpinnerNumberModel model = new SpinnerNumberModel(user.getBoardPanel().getStrokeWidth(), //initial value
                        min, //min
                        max, //max
                        1); //step
                JSpinner spinner = new JSpinner(model);
                int result = JOptionPane.showOptionDialog(null, spinner, "Enter a number between " + min + " and " + max, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (result == JOptionPane.OK_OPTION) {
                    user.getBoardPanel().setStrokeWidth((int) spinner.getValue());
                }
            }
        });

        freeDrawMenu.add(freeDrawButton);
        freeDrawMenu.add(pen_stroke_width_button);

        toolBtnGroup.add(freeDrawButton);
    }

    private void setUpCursorMenu() {
        cursorMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CURSOR))));
        cursorMenu.setText("Cursor");

        cursorButton.setText("Default Cursor");
        cursorButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CURSOR))));
        cursorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                user.getBoardPanel().setCurrentMode(Utils.MODE_DEFAULT_CURSOR);
            }
        });

        cursorMenu.add(cursorButton);

        toolBtnGroup.add(cursorButton);
    }

    private void setUpBoardPanel() {
        user.getBoardPanel().setPreferredSize(new Dimension((int) (Utils.BOARD_WIDTH * 0.7), Utils.BOARD_HEIGHT));
    }

    private void setUpInputPanel() {
        inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputArea.setLineWrap(true);

        inputScrollPane.setViewportView(inputArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!inputArea.isEmpty()) {
                    String message = inputArea.getText();
                    user.sendChatMessage(message);
                    inputArea.setText("");
                }
            }
        });

        GroupLayout inputPanelLayout = new GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
                inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(inputScrollPane)
                        .addGroup(inputPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(sendButton)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        inputPanelLayout.setVerticalGroup(
                inputPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(inputPanelLayout.createSequentialGroup()
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputScrollPane, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                .addGap(15, 15, 15)
                                .addComponent(sendButton)
                                .addContainerGap())
        );
    }

    private void setUpUserListPanel() {
        userListLabel.setText(user.isManager() ? "Current Users (Right click to kick out):" : "Current Users:");
        userList.setModel(user.getCurrUserListModel());

        if (user.isManager()) {
            userList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        int index = userList.locationToIndex(e.getPoint());
                        if (index != -1) {
                            String username = user.getCurrUserListModel().getElementAt(index);

                            // create the popup menu
                            JPopupMenu popupMenu = new JPopupMenu();
                            JMenuItem kickMenuItem = new JMenuItem("Kick out");
                            popupMenu.add(kickMenuItem);

                            kickMenuItem.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!username.equals(user.getUsername())) {
                                        int option = JOptionPane.showConfirmDialog(null, "Are you sure to kick \""+ username + "\" out?", "Confirm", JOptionPane.YES_NO_OPTION);
                                        if(option == JOptionPane.YES_OPTION){
                                            ((WhiteboardManager) user).kickUserOut(username);
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "You cannot kick yourself out.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });

                            popupMenu.show(userList, e.getX(), e.getY());
                        }
                    }
                }
            });
        } else {
            userList.setSelectionModel(new DefaultListSelectionModel() {
                @Override
                public void setSelectionInterval(int index0, int index1) {
                    // Do nothing. This prevents items from being selected.
                }
            });
        }

        userListScrollPane.setViewportView(userList);

        GroupLayout userListPanelLayout = new GroupLayout(userListPanel);
        userListPanel.setLayout(userListPanelLayout);
        userListPanelLayout.setHorizontalGroup(
                userListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(userListScrollPane)
                        .addGroup(userListPanelLayout.createSequentialGroup()
                                .addComponent(userListLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        userListPanelLayout.setVerticalGroup(
                userListPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(userListPanelLayout.createSequentialGroup()
                                .addComponent(userListLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(userListScrollPane, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    private void setUpChatPanel() {
        chatLabel.setText("Chat:");
        chatList.setModel(user.getChatListModel());

        chatList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                // Do nothing. This prevents items from being selected.
            }
        });

        chatBoxScrollPane.setViewportView(chatList);

        GroupLayout chatPanelLayout = new GroupLayout(chatPanel);
        chatPanel.setLayout(chatPanelLayout);
        chatPanelLayout.setHorizontalGroup(
                chatPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(chatBoxScrollPane)
                        .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(chatLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        chatPanelLayout.setVerticalGroup(
                chatPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(chatPanelLayout.createSequentialGroup()
                                .addComponent(chatLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatBoxScrollPane, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    private void handleBoardClose() {
        String msg = user.isManager() ? Utils.BOARD_CLOSE_MANAGER : Utils.BOARD_CLOSE_CLIENT;
        int option = JOptionPane.showConfirmDialog(null, msg, "exit", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            user.sendLeaveSignalRemote();
            System.exit(0);
        }
    }

    private void initFundamentalComponents() {
        setTitle(guiName + " - " + user.getUsername());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
    }

    public void initGUIComponents(){
        this.toolBtnGroup = new ButtonGroup();
        this.inputScrollPane = new JScrollPane();
        this.inputArea = new HintTextArea("Type message here...");
        this.inputPanel = new JPanel();
        this.sendButton = new JButton();
        this.userListPanel = new JPanel();
        this.userListLabel = new JLabel();
        this.userListScrollPane = new JScrollPane();
        this.userList = new JList<>();
        this.chatPanel = new JPanel();
        this.chatLabel = new JLabel();
        this.chatBoxScrollPane = new JScrollPane();
        this.chatList = new JList<>();
        this.menuBar = new JMenuBar();
        this.fileMenu = new JMenu();
        this.fileNewBoard = new JMenuItem();
        this.fileOpen = new JMenuItem();
        this.fileSave = new JMenuItem();
        this.fileSaveAs = new JMenuItem();
        this.fileClose = new JMenuItem();
        this.shapeMenu = new JMenu();
        this.drawLine = new JRadioButtonMenuItem();
        this.drawRect = new JRadioButtonMenuItem();
        this.drawOval = new JRadioButtonMenuItem();
        this.drawCir = new JRadioButtonMenuItem();
        this.colorMenu = new JMenu();
        this.colorIcon = new ColorIcon(this.user.getBoardPanel().getCurrColor());
        this.colorSelector = new JMenuItem();
        this.textMenu = new JMenu();
        this.paintText = new JRadioButtonMenuItem();
        this.freeDrawMenu = new JMenu();
        this.freeDrawButton = new JRadioButtonMenuItem();
        this.cursorMenu = new JMenu();
        this.cursorButton = new JRadioButtonMenuItem();
        this.pen_stroke_width_button = new JMenuItem();
        this.font_size_button = new JMenuItem();
    }

    private static class ColorIcon implements Icon {
        private final int size = 18;
        private Color color;

        ColorIcon(Color initialColor) {
            this.color = initialColor;
        }

        void setColor(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color); // Use the current color
            g.fillOval(x, y, size, size);
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    private static class HintTextArea extends JTextArea{
        private String hint;

        public HintTextArea(String hint) {
            this.hint = hint;
            setForeground(Color.gray);
            setText(hint);
            this.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getForeground() == Color.gray) {
                        setForeground(Color.black);
                        setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setForeground(Color.gray);
                        setText(hint);
                    }
                }
            });
        }

        public void resetHint(String hint) {
            this.hint = hint;
            setForeground(Color.gray);
            setText(hint);
        }

        public boolean isEmpty() {
            return getText().isEmpty() || getForeground() == Color.gray;
        }
    }
}
