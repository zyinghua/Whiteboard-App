// Author: Yinghua Zhou
// Student ID: 1308266

import WhiteboardUser.WhiteboardGUI;
import WhiteboardUser.WhiteboardClient;
import remotes.WhiteboardUserRemoteServant;
import remotes.WhiteboardServerRemote;

import Utils.Utils;
import Utils.ServerCode;

import java.awt.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;
import javax.swing.*;

public class JoinWhiteboard {
    public static final String USAGE = "Usage: java -jar JoinWhiteboard.jar <Server IP Address> <port> <username>";

    private static class WaitingDialog extends JDialog {
        // Waiting Dialog implementation for the moment of client waiting for manager's response
        private final JLabel messageLabel;
        private final JProgressBar progressBar;
        private final JButton cancelButton;

        public WaitingDialog(String message, Future<Integer> future) {
            messageLabel = new JLabel(message, SwingConstants.CENTER);

            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);

            cancelButton = new JButton("Cancel");
            JPanel buttonPanel = new JPanel();  // create a new JPanel for the button
            buttonPanel.setLayout(new FlowLayout()); // FlowLayout centers the button
            buttonPanel.add(cancelButton); // add the button to the new JPanel

            cancelButton.addActionListener(e -> {
                future.cancel(true);
                dispose();
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Creates a vertical gap of 5 pixels
            panel.add(messageLabel);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Creates a vertical gap of 10 pixels
            panel.add(progressBar);
            panel.add(Box.createRigidArea(new Dimension(0, 5))); // Creates a vertical gap of 5 pixels
            panel.add(buttonPanel); // add the buttonPanel instead of the button directly

            getContentPane().add(panel);
            setSize(350, 130);
            setLocationRelativeTo(null);
            setModal(true);
            setTitle("Waiting");
        }

        public void setMessage(String message) {
            messageLabel.setText(message);
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Invalid parameters. " + USAGE);
            System.exit(1);
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            String address = args[0];
            int port = Integer.parseInt(args[1]);
            String username = args[2];

            if (port < 0 || port > 65535) {
                System.out.println(USAGE);
                System.err.println("Port must be between 0 and 65535.\n");
                System.exit(1);
            }

            Registry registry = LocateRegistry.getRegistry(address, port); // Get the registry
            WhiteboardServerRemote board_remote = (WhiteboardServerRemote) registry.lookup(Utils.RMI_WHITEBOARD_SERVER_NAME); // Get the remote object and use as if it's local

            WhiteboardClient client = new WhiteboardClient(username, board_remote);
            WhiteboardUserRemoteServant client_remote = new WhiteboardUserRemoteServant(client); // encapsulate with the remote

            Future<Integer> future = executor.submit(() -> board_remote.joinWhiteboard(username, client_remote)); // Submit the task to the executor

            WaitingDialog waitingDialog = new WaitingDialog("Please wait for the manager to grant access...", future);
            new Thread(() -> {
                waitingDialog.setVisible(true);
            }).start();

            int access = future.get(); // Wait for the task to finish and get the manager's response, may throw cancellation exception if the user cancels the task via the cancel button.
            executor.shutdown();

            new Thread(waitingDialog::dispose).start();

            if (access == ServerCode.JOIN_ACCEPTED) {
                // Manager accepted the join request
                client.obtainWhiteboardChannelInfo(); // get the user list, chat list, remotes of peers, and the board info.
                WhiteboardGUI board = new WhiteboardGUI(client);
                board.setVisible(true);
                System.out.println("Joined whiteboard on IP Address: " + address + " | Port No.: " + port + " with username: " + username + ".\n");
            } else if (access == ServerCode.JOIN_DENIED_USERNAME_ALREADY_EXISTS) {
                JOptionPane.showMessageDialog(null, "Username already exists, please try another one.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Username already exists, please try another one.\n");
                System.exit(1);
            } else if (access == ServerCode.JOIN_DENIED_BY_MANAGER) {
                JOptionPane.showMessageDialog(null, "Join access denied by the whiteboard manager.", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Join access denied by the whiteboard manager from IP Address: " + address + " | Port No.: " + port + ". Attempted to join with username: " + username + ".\n");
                System.exit(1);
            } else {
                // should never be reached
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("Port must be an integer.\n");
            System.exit(1);
        } catch (RemoteException e){
            String err_msg = "Cannot connect with the whiteboard server, this can due to server is not up yet or your IP Address and Port No. don't match. Please try again later.";
            JOptionPane.showMessageDialog(null, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(err_msg);
            System.exit(1);
        } catch (NotBoundException e) {
            JOptionPane.showMessageDialog(null, "Something wrong with the server side, cannot properly connect with the server.", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Cannot find the named remote object in the RMI registry.\n");
            System.exit(1);
        } catch (CancellationException e) {
            System.out.println("Whiteboard join request cancelled.");
            System.exit(0);
        } catch (InterruptedException | ExecutionException e) {
            String err_msg = "Something wrong when waiting for the manager's response, please try again.";
            JOptionPane.showMessageDialog(null, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(err_msg);
            System.exit(1);
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdown();
            }
        }
    }
}
