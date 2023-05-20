package WhiteboardUser;

import Utils.ServerCode;
import remotes.WhiteboardUserRemote;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WhiteboardManager extends WhiteboardUser{
    private ArrayList<String> waitingClients;

    private HashMap<String, JOptionPane> waitingClientDialogs;

    public WhiteboardManager(String username){
        super(true, username);
        addUser(username);
        waitingClients = new ArrayList<>();
    }

    public int joinWhiteboard(String username, WhiteboardUserRemote clientRemote){
        if (getCurrUserListModel().contains(username) || getWaitingClients().contains(username)){
            return ServerCode.JOIN_DENIED_USERNAME_ALREADY_EXISTS;
        }

        addWaitingClient(username);

        int option = JOptionPane.showConfirmDialog(null, username + " wants to access the whiteboard, approve?", "Whiteboard Access Request", JOptionPane.YES_NO_OPTION);

        removeWaitingClient(username);

        if(option == JOptionPane.YES_OPTION){
            addUser(username);
            addClientRemote(username, clientRemote);

            return ServerCode.JOIN_ACCEPTED;
        }
        else{
            return ServerCode.JOIN_DENIED_BY_MANAGER;
        }
    }

    public void cancelJoinWhiteboard(String username){
        removeWaitingClient(username);
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
