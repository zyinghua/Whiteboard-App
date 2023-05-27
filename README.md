# WhiteboardApp
A shared whiteboard app which people can interact, draw and chat at realtime. Implemented in Java with RMI.

To run the application, in out/artifacts, there are two jar folders:
out/artifacts/CreateWhiteboard_jar: run cmd: java -jar CreateWhiteboard.jar <port no.> <manager username> (note: default ip address is 'localhost')
out/artifacts/JoinWhiteboard_jar: run cmd: java -jar JoinWhiteboard.jar <ip address> <port no.> <client username> (note: the ip address and port no. are the manager's who runs CreateWhiteboard.jar before this command)
