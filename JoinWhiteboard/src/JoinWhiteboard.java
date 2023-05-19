// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardGUI;
import WhiteboardUser.WhiteboardClient;
import interfaces.WhiteboardServerRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Utils.Utils;
import javax.swing.*;

public class JoinWhiteboard {
    public static final String USAGE = "Usage: java -jar JoinWhiteboard.jar <Server IP Address> <port> <username>";
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid parameters. " + USAGE);
            System.exit(1);
        }

        try {
            String address = args[0];
            int port = Integer.parseInt(args[1]);
            String username = args[2];

            if (port < 0 || port > 65535) {
                System.out.println(USAGE);
                System.err.println("Port must be between 0 and 65535.\n");
                System.exit(1);
            }

            Registry registry = LocateRegistry.getRegistry(address, port);
            WhiteboardServerRemote board_remote = (WhiteboardServerRemote) registry.lookup(Utils.RMI_WHITEBOARD_SERVER_NAME);
            boolean access = board_remote.joinWhiteboard(username);

            if (access) {
                System.out.println("Joined whiteboard on IP Address: " + address + " | Port No.: " + port + " with username: " + username + ".\n");
                WhiteboardClient client = new WhiteboardClient(username);
                WhiteboardGUI board = new WhiteboardGUI(client);
                board.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Join access denied by the whiteboard manager.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Join access denied by the whiteboard manager from IP Address: " + address + " | Port No.: " + port + ". Attempted to join with username: " + username + ".\n");
                System.exit(1);
            }

        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("Port must be an integer.\n");
            System.exit(1);
        } catch (RemoteException e){
            String err_msg = "Cannot connect with the whiteboard server, this can due to server is not up yet or your port No. doesn't match. Please try again later.";
            JOptionPane.showMessageDialog(null, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(err_msg);
            System.exit(1);
        } catch (NotBoundException e) {
            System.err.println("Cannot find the named remote object in the RMI registry.\n");
            System.exit(1);
        }
    }
}
