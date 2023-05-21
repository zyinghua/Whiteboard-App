// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import java.awt.*;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardUserRemote extends Remote {
    void addChatMessage(String username, String message) throws RemoteException;
    void addNewUser(String username, WhiteboardUserRemote userRemote) throws RemoteException;
    void removeUserInfo(String username) throws RemoteException;
    void disconnectByManager(boolean isKickedOut) throws RemoteException;
    void newBoard() throws RemoteException;
    void loadBoard(byte[] boardImageInBytes) throws IOException;
    void draw(Color color, int strokeWidth, Point startPt, Point endPt, int mode) throws RemoteException;
    void draw(Color color, Font font, Point endPt, String text) throws RemoteException;
}
