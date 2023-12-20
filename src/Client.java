import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    public static void main(String[] args) {
        int portNumber;
        String ipAddress;
        int fnID;
        int authToken;
        try {
            // args[0] = IpAddress
            // args[1] = portNumber
            // args[2] = fnID
            // args[3] = rest of the arguments

//            double a = Integer.parseInt(args[0]);
//            double b = Integer.parseInt(args[1]);
            if (args.length < 4) {
                System.out.println("Not enough number of arguments");
                System.exit(1);
            }

            // Get port,ip,fnID number from command line
            ipAddress = args[0];
            portNumber = Integer.parseInt(args[1]);
            fnID = Integer.parseInt(args[2]);

            if (portNumber < 1000 || portNumber > 9999) {
                System.out.println("Invalid Port Number");
                System.exit(1);
            }

            if (fnID < 1 || fnID > 6) {
                System.out.println("Invalid Function ID");
                System.exit(1);
            }

            //ID:1 = Create Account
            //ID:2 = Show Accounts
            //ID:3 = Send Message
            //ID:4 = Show Messages
            //ID:5 = Read Message
            //ID:6 = Delete Message

            // connect to the RMI registry
            Registry rmiRegistry = LocateRegistry.getRegistry(ipAddress, portNumber);
            // get reference for remote object
            Rmi stub = (Rmi) rmiRegistry.lookup("Rmi");

            switch (fnID) {

                case 1: // Creating account
                    String username = args[3];
                    authToken = stub.createAccount(username);
                    if (authToken == -1) {
                        System.out.println("Invalid Username");
                    } else if (authToken == 0) {
                        System.out.println("Username already exists");
                    } else {
                        System.out.println(authToken);
                    }
                    break;

                case 2: // Showing accounts
                    authToken = Integer.parseInt(args[3]);
                    String usernames = stub.showAccounts(authToken);
                    System.out.println(usernames);
                    break;

                case 3: // Sending message
                    authToken = Integer.parseInt(args[3]);
                    String recipient = args[4];
                    String body = args[5];
                    String messageSent = stub.sendMessage(authToken, recipient, body);
                    System.out.println(messageSent);

                    break;

                case 4: // Showing inbox
                    authToken = Integer.parseInt(args[3]);
                    String messages = stub.showInbox(authToken);
                    System.out.println(messages);
                    break;

                case 5:
                    authToken = Integer.parseInt(args[3]);
                    int messageId2 = Integer.parseInt(args[4]);
                    String message = stub.readMessage(authToken, messageId2);
                    if (message == null) {
                        System.out.println("Invalid Message ID");
                    } else {
                        System.out.println(message);
                    }
                    break;

                case 6:
                    authToken = Integer.parseInt(args[3]);
                    int messageId3 = Integer.parseInt(args[4]);
                    String result = stub.deleteMessage(authToken, messageId3);
                    if (result == null) {
                        System.out.println("Invalid Message ID");
                    } else {
                        System.out.println(result);
                    }
                    break;
            }


        } catch (Exception e) {
            System.out.println("Error " + e);
        }
    }
}