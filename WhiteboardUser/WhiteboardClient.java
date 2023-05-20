package WhiteboardUser;

import remotes.WhiteboardUserRemote;
import remotes.WhiteboardServerRemote;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

public class WhiteboardClient extends WhiteboardUser{
    private final WhiteboardServerRemote board_remote;

    public WhiteboardClient(String username, WhiteboardServerRemote board_remote){
        super(false, username);
        this.board_remote = board_remote;
    }

    public void obtainWhiteboardChannelInfo() {
        try {
            DefaultListModel<String> currUserListModel = board_remote.getCurrUserListModel();
            DefaultListModel<String> chatListModel = board_remote.getChatListModel();
            HashMap<String, WhiteboardUserRemote> clientRemotes = board_remote.getClientRemotes();

            byte[] boardImageInBytes = board_remote.getWhiteboardImageInBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(boardImageInBytes);
            BufferedImage boardImage = ImageIO.read(inputStream);

            this.setCurrUserListModel(currUserListModel);
            this.setChatListModel(chatListModel);
            this.setBoardImage(boardImage);
            this.setClientRemotes(clientRemotes);

        } catch (RemoteException e) {
            String err_msg = "Something wrong with getting the whiteboard information from the server, please try again later.";
            JOptionPane.showMessageDialog(null, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(err_msg);
            System.exit(1);

        } catch (IOException e) {
            // byte[] board image parse error
            String err_msg = "Something wrong with getting the whiteboard information from the server, please try again later.";
            JOptionPane.showMessageDialog(null, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(err_msg);
            System.exit(1);
        }
    }
}
