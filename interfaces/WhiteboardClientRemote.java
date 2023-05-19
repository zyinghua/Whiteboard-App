// Author: Yinghua Zhou
// Student ID: 1308266

package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface WhiteboardClientRemote extends Remote {
    void addChatMessage(String username, String message) throws RemoteException;
    ArrayList<String> syncChatMessages() throws RemoteException;

}
