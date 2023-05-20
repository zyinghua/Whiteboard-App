// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardUserRemote extends Remote {
    void addChatMessage(String username, String message) throws RemoteException;
    void addNewUser(String username, WhiteboardUserRemote userRemote) throws RemoteException;
    void removeUserInfo(String username) throws RemoteException;
    void disconnectByManager(boolean isKickedOut) throws RemoteException;
    void newBoard() throws RemoteException;
}
