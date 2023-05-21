package WhiteboardUser;

import Utils.ServerCode;
import remotes.WhiteboardUserRemote;

import javax.swing.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class WhiteboardManager extends WhiteboardUser{
    private ArrayList<String> waitingClients;
    private HashMap<String, JOptionPane> waitingClientDialogs;

    public WhiteboardManager(String username){
        super(true, username);
        waitingClients = new ArrayList<>();
    }

    public int requestWhiteboardJoin(String username, WhiteboardUserRemote clientRemote){
        if (getCurrUserListModel().contains(username) || getWaitingClients().contains(username)){
            return ServerCode.JOIN_DENIED_USERNAME_ALREADY_EXISTS;
        }

        addWaitingClient(username);

        int option = JOptionPane.showConfirmDialog(null, "User \"" + username + "\" wants to access the whiteboard, approve?", "Whiteboard Access Request", JOptionPane.YES_NO_OPTION);

        removeWaitingClient(username);

        if(option == JOptionPane.YES_OPTION){
            for (String existingUsernames : getClientRemotes().keySet()) {
                if (!existingUsernames.equals(getUsername())) {
                    new Thread(() -> {
                        try {
                            getClientRemotes().get(existingUsernames).addNewUser(username, clientRemote);
                        } catch (RemoteException e) {
                            sendLeaveSignalRemote();
                            JOptionPane.showMessageDialog(null, "Something wrong with the remote connection to \"" + username + "\". Please restart the Whiteboard program.", "Error", JOptionPane.ERROR_MESSAGE);
                            System.exit(1);
                        }
                    }).start();
                }
            }

            addPeerInfo(username, clientRemote);

            return ServerCode.JOIN_ACCEPTED;
        }
        else{
            return ServerCode.JOIN_DENIED_BY_MANAGER;
        }
    }

    public void newBoardRemote() {
        for (String username : this.getClientRemotes().keySet()) {
            if (!username.equals(getUsername()))
            {
                new Thread (() -> {
                    try {
                        this.getClientRemotes().get(username).newBoard();
                    } catch (RemoteException e) {
                        // remove the user
                        sendLeaveSignalRemote();
                        JOptionPane.showMessageDialog(null, "Something wrong with the remote connection to \"" + username + "\". Please restart the Whiteboard program.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }).start();
            }
        }

    }

    public void loadBoardRemote() {
        for (String username : this.getClientRemotes().keySet()) {
            if (!username.equals(getUsername()))
            {
                new Thread (() -> {
                    try {
                        this.getClientRemotes().get(username).loadBoard(getBoardPanel().getBoardImagesInBytes());
                    } catch (RemoteException e) {
                        sendLeaveSignalRemote();
                        JOptionPane.showMessageDialog(null, "Something wrong with the remote connection to \"" + username + "\". Please restart the Whiteboard program.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    } catch(IOException e) {
                        // byte[] board image parse error
                        sendLeaveSignalRemote();
                        JOptionPane.showMessageDialog(null, "Something wrong with sending new whiteboard information to other users. Please restart the Whiteboard program.", "Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                }).start();
            }
        }
    }

    public void kickUserOut(String username) {
        if (getCurrUserListModel().contains(username)) {
            new Thread(() -> {
                try {
                    getClientRemotes().get(username).disconnectByManager(true);
                    sendChatMessage(username + " has been kicked out by the manager.");
                } catch(RemoteException e) {
                    removePeerInfo(username);

                    sendLeaveSignalRemote();
                    JOptionPane.showMessageDialog(null, "Something wrong when trying to kick \"" + username + "\" Out. Please restart the Whiteboard program.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }).start();
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
