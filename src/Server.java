import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Invalid Port Number");
                System.exit(1);
            }

            // Get port number from command line
            int portNumber = Integer.parseInt(args[0]);
            Rmi stub = new RmiImpl();
            Registry registry = LocateRegistry.createRegistry(portNumber);
            registry.bind("Rmi", stub);
            System.out.println("Server is ready");
        } catch (Exception e) {
            System.out.println("Error Occurred: " + e);
        }
    }

}