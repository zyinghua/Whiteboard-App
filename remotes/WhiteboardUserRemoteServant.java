package remotes;// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardUser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardUserRemoteServant extends UnicastRemoteObject implements WhiteboardUserRemote {
    private WhiteboardUser user;

    public WhiteboardUserRemoteServant(WhiteboardUser user) throws RemoteException {
        super();
        this.user = user;
    }

    @Override
    public void addChatMessage(String username, String message) throws RemoteException {
        user.receiveChatMessage(username, message);
    }

    @Override
    public void addNewUser(String username) throws RemoteException {

    }
}
