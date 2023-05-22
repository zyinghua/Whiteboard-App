// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;

import Utils.ServerCode;
import remotes.WhiteboardUserRemote;

import javax.swing.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.*;

public class WhiteboardManager extends WhiteboardUser{
    private final HashMap<String, JoinExecutor> waitingClients;

    public WhiteboardManager(String username){
        super(true, username);
        waitingClients = new HashMap<>();
    }

    public int requestWhiteboardJoin(String username, WhiteboardUserRemote clientRemote){
        if (getCurrUserListModel().contains(username) || this.waitingClients.containsKey(username)){
            return ServerCode.JOIN_DENIED_USERNAME_ALREADY_EXISTS;
        }

        try {
            JoinExecutor joinExecutor = new JoinExecutor(username);
            addWaitingClient(username, joinExecutor);

            int option = joinExecutor.getOption();

            removeWaitingClient(username);

            if(option == JOptionPane.YES_OPTION)
            {
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
            else
            {
                return ServerCode.JOIN_DENIED_BY_MANAGER;
            }
        } catch (InterruptedException | ExecutionException e) {
            removeWaitingClient(username);
            return ServerCode.JOIN_CANCELLED_DUE_TO_MANAGER_ERROR;
        } catch (CancellationException e) {
            removeWaitingClient(username);
            return ServerCode.JOIN_CANCELLED_BY_CLIENT;
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
        if (waitingClients.containsKey(username)){
            waitingClients.get(username).cancelJoin();
        }
    }

    public void addWaitingClient(String username, JoinExecutor joinExecutor){
        waitingClients.put(username, joinExecutor);
    }

    public void removeWaitingClient(String username){
        waitingClients.get(username).shutdown();
        waitingClients.remove(username);
    }

    private static class JoinExecutor {
        private final ExecutorService executor;
        private final Future<Integer> future;
        private final JOptionPane joinOptionPane;
        private final JDialog joinDialog;

        public JoinExecutor(String username) {
            executor = Executors.newSingleThreadExecutor();
            joinOptionPane = new JOptionPane("User \"" + username + "\" wants to access the whiteboard, approve?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
            joinDialog = joinOptionPane.createDialog("Join Request");
            future = executor.submit(() -> {
                joinDialog.setVisible(true);
                return (int) joinOptionPane.getValue();
            });
        }

        public int getOption() throws InterruptedException, ExecutionException, CancellationException {
            return future.get();
        }

        public void cancelJoin() {
            joinDialog.setVisible(false);
            joinDialog.dispose();
            future.cancel(true);
        }

        public void shutdown() {
            executor.shutdown();
        }
    }
}
