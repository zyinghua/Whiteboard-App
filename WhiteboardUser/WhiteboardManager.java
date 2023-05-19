package WhiteboardUser;

import Utils.Utils;

import javax.swing.*;

public class WhiteboardManager extends WhiteboardUser{
    public WhiteboardManager(String username){
        super(true, username);
        addUser(username);
    }

    public boolean checkNameValidity(String username){
        return !getCurrUserListModel().contains(username);
    }

    public boolean joinWhiteboard(String username){
        int option = JOptionPane.showConfirmDialog(null, username + " wants to access the whiteboard, approve?", "Whiteboard Access Request", JOptionPane.YES_NO_OPTION);
        return option == JOptionPane.YES_OPTION;
    }
}
