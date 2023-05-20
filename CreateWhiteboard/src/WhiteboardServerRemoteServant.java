// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardManager;
import interfaces.WhiteboardServerRemote;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class WhiteboardServerRemoteServant extends UnicastRemoteObject implements WhiteboardServerRemote {
    private WhiteboardManager manager;

    public WhiteboardServerRemoteServant(WhiteboardManager manager) throws RemoteException {
        super();
        this.manager = manager;
    }

    @Override
    public int joinWhiteboard(String username) throws RemoteException {
        return manager.joinWhiteboard(username);
    }

    @Override
    public DefaultListModel<String> getCurrUserListModel() throws RemoteException {
        return manager.getCurrUserListModel();
    }

    @Override
    public DefaultListModel<String> getChatListModel() throws RemoteException {
        return manager.getChatListModel();
    }

    @Override
    public byte[] getWhiteboardImageInBytes() throws IOException {
        return manager.getBoardPanel().getBoardImagesInBytes();
    }
}
