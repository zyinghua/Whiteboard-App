// Author: Yinghua Zhou
// Student ID: 1308266

package interfaces;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface WhiteboardServerRemote extends Remote {
    boolean checkNameValidity(String username) throws RemoteException;
    boolean joinWhiteboard(String username) throws RemoteException;
    DefaultListModel<String> getCurrUserListModel() throws RemoteException;
    DefaultListModel<String> getChatListModel() throws RemoteException;
    byte[] getWhiteboardImageInBytes() throws IOException;
}