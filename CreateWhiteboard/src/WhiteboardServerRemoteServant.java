// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardManager;
import interfaces.WhiteboardServerRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardServerRemoteServant extends UnicastRemoteObject implements WhiteboardServerRemote {
    private WhiteboardManager manager;

    public WhiteboardServerRemoteServant(WhiteboardManager manager) throws RemoteException {
        super();
        this.manager = manager;
    }

    @Override
    public void addNewUser(String username) throws RemoteException {

    }

    @Override
    public boolean joinWhiteboard(String username) throws RemoteException {
        return true;
    }
}
