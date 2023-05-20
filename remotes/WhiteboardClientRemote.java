// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardClientRemote extends Remote {
    void addChatMessage(String username, String message) throws RemoteException;
    void addNewUser(String username) throws RemoteException;


}
