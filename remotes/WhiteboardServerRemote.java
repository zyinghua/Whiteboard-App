// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import javax.swing.*;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface WhiteboardServerRemote extends Remote {
    int joinWhiteboard(String username, WhiteboardUserRemote client_remote) throws RemoteException;
    void cancelJoinWhiteboard(String username) throws RemoteException;
    DefaultListModel<String> getCurrUserListModel() throws RemoteException;
    DefaultListModel<String> getChatListModel() throws RemoteException;
    HashMap<String, WhiteboardUserRemote> getClientRemotes() throws RemoteException;
    byte[] getWhiteboardImageInBytes() throws IOException;
}