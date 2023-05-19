import WhiteboardUser.WhiteboardClient;
import interfaces.WhiteboardClientRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class WhiteboardClientRemoteServant extends UnicastRemoteObject implements WhiteboardClientRemote {
    private WhiteboardClient client;

    protected WhiteboardClientRemoteServant(WhiteboardClient client) throws RemoteException {
        super();
        this.client = client;
    }

    @Override
    public void addChatMessage(String username, String message) throws RemoteException {

    }

    @Override
    public ArrayList<String> syncChatMessages() throws RemoteException {
        return null;
    }
}
