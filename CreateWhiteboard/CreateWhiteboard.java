// Author: Yinghua Zhou
// Student ID: 1308266

package CreateWhiteboard;

import WhiteboardUser.WhiteboardGUI;
import WhiteboardUser.WhiteboardManager;
import interfaces.WhiteboardServerRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import Utils.Utils;

public class CreateWhiteboard {
    public static final String USAGE = "Usage: java -jar CreateWhiteboard.jar <port> <username>";
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid parameters. " + USAGE);
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            String username = args[1];

            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException();
            }

            WhiteboardManager manager = new WhiteboardManager("[Manager] " + username);

            Registry registry = LocateRegistry.createRegistry(port);
            WhiteboardServerRemote board_remote = new WhiteboardServerRemoteServant(manager);
            registry.bind(Utils.RMI_WHITEBOARD_SERVER_NAME, board_remote);

            System.out.println("Whiteboard created on port " + port + " with manager username: " + username + ".\n");

            WhiteboardGUI board = new WhiteboardGUI(manager);
            board.setVisible(true);

        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("[Server failed to start] Port must be an integer.\n");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(USAGE);
            System.err.println("[Server failed to start] Port must be between 0 and 65535.\n" + e.getMessage());
            System.exit(1);
        } catch (RemoteException e){
            System.err.println("[Server failed to start] Port already in use.\n" + e.getMessage());
            System.exit(1);
        }catch (AlreadyBoundException e){
            System.err.println("[Server failed to start] The board object binding name already in the RMI Registry.\n" + e.getMessage());
            System.exit(1);
        }
    }
}
