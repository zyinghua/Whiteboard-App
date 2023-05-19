// Author: Yinghua Zhou
// Student ID: 1308266

package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardServerRemote extends Remote {
    void addNewUser(String username) throws RemoteException;
    boolean joinWhiteboard(String username) throws RemoteException;
}