// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import WhiteboardUser.WhiteboardManager;

import javax.swing.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class WhiteboardServerRemoteServant extends UnicastRemoteObject implements WhiteboardServerRemote {
    private WhiteboardManager manager;

    public WhiteboardServerRemoteServant(WhiteboardManager manager) throws RemoteException {
        super();
        this.manager = manager;
    }

    @Override
    public int joinWhiteboard(String username, WhiteboardUserRemote client_remote) throws RemoteException {
        return manager.requestWhiteboardJoin(username, client_remote);
    }

    @Override
    public void cancelJoinWhiteboard(String username) throws RemoteException {
        manager.cancelJoinWhiteboard(username);
    }

    @Override
    public DefaultListModel<String> getCurrUserListModel() throws RemoteException {
        return manager.getCurrUserListModel();
    }

    @Override
    public DefaultListModel<String> getChatListModel() throws RemoteException {
        return manager.getChatListModel();
    }

    @Override
    public HashMap<String, WhiteboardUserRemote> getClientRemotes() throws RemoteException {
        return manager.getClientRemotes();
    }

    @Override
    public byte[] getWhiteboardImageInBytes() throws IOException {
        return manager.getBoardPanel().getBoardImagesInBytes();
    }
}
