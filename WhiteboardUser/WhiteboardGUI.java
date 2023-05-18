package WhiteboardUser;

import javax.swing.*;

import Utils.Utils;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class WhiteboardGUI extends JFrame{
    private WhiteboardUser user;
    private static final String guiName = "Whiteboard";
    private JMenuBar menuBar;
    private JRadioButtonMenuItem drawLine;
    private JRadioButtonMenuItem drawRect;
    private JRadioButtonMenuItem drawOval;
    private JRadioButtonMenuItem drawCir;
    private ColorIcon colorIcon;
    private JMenu colorMenu;
    private JMenuItem colorSelector;
    private JRadioButtonMenuItem paintText;
    private JMenu cursorMenu;
    private JRadioButtonMenuItem cursorButton;
    private BoardPanel boardPanel;
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
    private JScrollPane inputScrollPane;
    private HintTextArea inputArea;
    private JPanel inputPanel;
    private JPanel userListPanel;
    private ButtonGroup toolBtnGroup;
    private JButton sendButton;
    private JMenu shapeMenu;
    private JMenu textMenu;
    private JList<String> userList;
    private JLabel userListLabel;
    private JScrollPane userListScrollPane;

     public WhiteboardGUI() {
         initComponents();
     }

    private void initComponents() {
        initGUIComponents();
        initFundamentalComponents();

        // Set the close board action
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleBoardClose(e);
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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(boardPanel, Utils.BOARD_PANEL_WIDTH, Utils.BOARD_PANEL_WIDTH, Utils.BOARD_PANEL_WIDTH)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(userListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(inputPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                                .addContainerGap())
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addContainerGap()))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(boardPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(userListPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputPanel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addContainerGap())
        );

        pack();
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
//                try {
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        });

        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        fileOpen.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_OPEN_BOARD))));
        fileOpen.setText("Open");
        fileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try {
//
//                } catch (IOException e) {
//                    JOptionPane.showMessageDialog(null,"The file you want to " +
//                            "open does not exist!","no file",JOptionPane.WARNING_MESSAGE);
//                }
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

            }
        });

        fileMenu.add(fileNewBoard);
        fileMenu.add(fileOpen);
        fileMenu.add(fileSave);
        fileMenu.add(fileSaveAs);
        fileMenu.add(fileClose);
    }

    private void handleFileNew() {

    }

    private void handleFileSave() {

    }

    private void handleFileSaveAs() {
        try{

        }catch (NullPointerException e){

        }
    }

    private void handleFileOpen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            System.out.println("Selected folder: " + selectedFolder.getAbsolutePath());
        }
    }

    private void handleFileClose() {

    }

    private void setUpShapeMenu() {
        this.shapeMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_SHAPE))));
        this.shapeMenu.setText("Shape");

        drawLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        drawLine.setText("Line");
        drawLine.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_LINE))));
        drawLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_DRAW_LINE);
            }
        });

        drawRect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        drawRect.setText("Rectangle");
        drawRect.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_RECTANGLE))));
        drawRect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_DRAW_RECT);
            }
        });

        drawOval.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        drawOval.setText("Oval");
        drawOval.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_OVAL))));
        drawOval.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_DRAW_OVAL);
            }
        });

        drawCir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        drawCir.setText("Circle");
        drawCir.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CIRCLE))));
        drawCir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_DRAW_CIRCLE);
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
                boardPanel.setCurrColor(color);
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
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_PAINT_TEXT);
            }
        });

        textMenu.add(paintText);

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
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_FREE_DRAW);
            }
        });

        freeDrawMenu.add(freeDrawButton);

        toolBtnGroup.add(freeDrawButton);
    }

    private void setUpCursorMenu() {
        cursorMenu.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CURSOR))));
        cursorMenu.setText("Cursor");

        cursorButton.setText("Default Cursor");
        cursorButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(Utils.ICON_CURSOR))));
        cursorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                boardPanel.setCurrentMode(Utils.MODE_DEFAULT_CURSOR);
            }
        });

        cursorMenu.add(cursorButton);

        toolBtnGroup.add(cursorButton);
    }

    private void setUpBoardPanel() {
        boardPanel.setPreferredSize(new Dimension(Utils.BOARD_PANEL_WIDTH, Utils.BOARD_PANEL_HEIGHT));
    }

    private void setUpInputPanel() {
        inputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputArea.setLineWrap(true);

        inputScrollPane.setViewportView(inputArea);

        sendButton.setText("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                try {
//
//                } catch (RemoteException e) {
//                    JOptionPane.showMessageDialog(null,
//                            "Server is down, the board will close automatically!","warning",JOptionPane.WARNING_MESSAGE);
//                    System.exit(1);
//                }
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
        userListLabel.setText("Current Users:");
        userList.setModel(new AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });

        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
//                try {
//
//                } catch (RemoteException e) {
//                    JOptionPane.showMessageDialog(null,
//                            "Server is down, the board will close automatically!","warning",JOptionPane.WARNING_MESSAGE);
//                    System.exit(1);
//                }
            }
        });

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
        chatList.setModel(new AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
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

    private void handleBoardClose(WindowEvent e) {
         String msg = user.isManager() ? Utils.BOARD_CLOSE_MANAGER : Utils.BOARD_CLOSE_CLIENT;
         int option = JOptionPane.showConfirmDialog(null, msg, "exit", JOptionPane.YES_NO_OPTION);
         if (option == JOptionPane.YES_OPTION) {
             System.exit(0);
         }
    }

    private void initFundamentalComponents() {
        setTitle(guiName);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        this.colorIcon = new ColorIcon(Color.BLACK);
    }

    public void initGUIComponents(){
        this.toolBtnGroup = new ButtonGroup();
        this.boardPanel = new BoardPanel();
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
        this.colorSelector = new JMenuItem();
        this.textMenu = new JMenu();
        this.paintText = new JRadioButtonMenuItem();
        this.freeDrawMenu = new JMenu();
        this.freeDrawButton = new JRadioButtonMenuItem();
        this.cursorMenu = new JMenu();
        this.cursorButton = new JRadioButtonMenuItem();
        this.user = new WhiteboardUser(true, "manager");
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
    }

    public static void main(String[] args) {
        WhiteboardGUI board = new WhiteboardGUI();
        board.setVisible(true);

        Dimension size = board.getSize();
        size.width += Utils.BOARD_WIDTH_ADDITION;
        size.height += Utils.BOARD_HEIGHT_ADDITION;

        board.setSize(size);
    }
}
