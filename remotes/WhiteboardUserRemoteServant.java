package remotes;// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardUser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardClientRemoteServant extends UnicastRemoteObject implements WhiteboardClientRemote {
    private WhiteboardUser user;

    public WhiteboardClientRemoteServant(WhiteboardUser user) throws RemoteException {
        super();
        this.user = user;
    }

    @Override
    public void addChatMessage(String username, String message) throws RemoteException {
        user.addChatMessage(username, message);
    }

    @Override
    public void addNewUser(String username) throws RemoteException {

    }
}
