package WhiteboardUser;

import Utils.Utils;
import Utils.ServerCode;

import javax.swing.*;
import java.util.ArrayList;

public class WhiteboardManager extends WhiteboardUser{
    private ArrayList<String> waitingClients;

    public WhiteboardManager(String username){
        super(true, username);
        addUser(username);
        waitingClients = new ArrayList<>();
    }

    public int joinWhiteboard(String username){
        if (getCurrUserListModel().contains(username) || getWaitingClients().contains(username)){
            return ServerCode.JOIN_DENIED_USERNAME_ALREADY_EXISTS;
        }

        addWaitingClient(username);

        int option = JOptionPane.showConfirmDialog(null, username + " wants to access the whiteboard, approve?", "Whiteboard Access Request", JOptionPane.YES_NO_OPTION);

        removeWaitingClient(username);

        return option == JOptionPane.YES_OPTION ? ServerCode.JOIN_ACCEPTED : ServerCode.JOIN_DENIED_BY_MANAGER;
    }

    public void addWaitingClient(String username){
        waitingClients.add(username);
    }

    public void removeWaitingClient(String username){
        waitingClients.remove(username);
    }

    public ArrayList<String> getWaitingClients(){
        return waitingClients;
    }
}
