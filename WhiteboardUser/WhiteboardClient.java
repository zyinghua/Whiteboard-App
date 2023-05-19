package WhiteboardUser;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class WhiteboardClient extends WhiteboardUser{

    public WhiteboardClient(String username, DefaultListModel<String> currUserListModel, DefaultListModel<String> chatListModel, BufferedImage boardImage){
        super(false, username);
        this.setCurrUserListModel(currUserListModel);
        this.setChatListModel(chatListModel);
        this.setBoardImage(boardImage);
    }
}
