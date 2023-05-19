// Author: Yinghua Zhou
// Student ID: 1308266

package WhiteboardUser;

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



        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("[Server failed to start] Port must be an integer.\n");
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(USAGE);
            System.err.println("[Server failed to start] Port must be between 0 and 65535.\n" + e.getMessage());
            System.exit(1);
        }
    }
}
