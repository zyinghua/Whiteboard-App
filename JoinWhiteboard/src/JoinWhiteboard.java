// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardGUI;
import WhiteboardUser.WhiteboardManager;
import interfaces.WhiteboardServerRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Utils.Utils;

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

            if (username.isEmpty()) {
                System.out.println(USAGE);
                System.err.println("Username cannot be empty.\n");
                System.exit(1);
            }

            System.out.println("Joined whiteboard on port " + port + " with username: " + username + ".\n");

        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("Port must be an integer.\n");
            System.exit(1);
//        } catch (RemoteException e){
//            System.err.println("[Server failed to start] Port already in use.\n");
//            System.exit(1);
//        }catch (AlreadyBoundException e){
//            System.err.println("[Server failed to start] The board object binding name already in the RMI Registry.\n" + e.getMessage());
//            System.exit(1);
        }
    }
}
