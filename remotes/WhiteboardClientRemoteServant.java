package remotes;// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardClientRemoteServant extends UnicastRemoteObject implements WhiteboardClientRemote {
    private WhiteboardClient client;

    public WhiteboardClientRemoteServant(WhiteboardClient client) throws RemoteException {
        super();
        this.client = client;
    }

    @Override
    public void addChatMessage(String username, String message) throws RemoteException {

    }

    @Override
    public void addNewUser(String username) throws RemoteException {

    }
}
