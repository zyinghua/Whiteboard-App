// Author: Yinghua Zhou
// Student ID: 1308266

package remotes;

import WhiteboardUser.WhiteboardUser;
import WhiteboardUser.WhiteboardClient;

import java.awt.*;
import java.io.IOException;
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
    public void addNewUser(String username, WhiteboardUserRemote userRemote) throws RemoteException {
        user.addPeerInfo(username, userRemote);
    }

    @Override
    public void removeUserInfo(String username) throws RemoteException {
        user.removePeerInfo(username);
    }

    @Override
    public void disconnectByManager(boolean isKickedOut) throws RemoteException {
        if(!user.isManager())
            ((WhiteboardClient) user).disconnectByManager(isKickedOut);
    }

    @Override
    public void newBoard() throws RemoteException {
        user.getBoardPanel().clearBoard();
    }

    @Override
    public void loadBoard(byte[] boardImageInBytes) throws IOException {
        user.getBoardPanel().setBoardImageFromBytes(boardImageInBytes);
    }

    @Override
    public void draw(Color color, int strokeSize, Point startPt, Point endPt, int mode) throws RemoteException {
        user.getBoardPanel().drawRemote(color, strokeSize, startPt, endPt, mode);
    }

    @Override
    public void draw(Color color, Font font, Point endPt, String text) throws RemoteException {
        user.getBoardPanel().drawRemote(color, font, endPt, text);
    }
}
