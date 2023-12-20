import java.rmi.*;


public interface Rmi extends Remote {

    int createAccount(String username)
            throws RemoteException;

    String showAccounts(int authToken)
            throws RemoteException;

    String sendMessage(int authToken,String recipient,String messageBody)
            throws RemoteException;

    String showInbox(int authToken)
            throws RemoteException;

    String readMessage(int authToken,int messageId)
            throws RemoteException;

    String deleteMessage(int authToken,int messageId)
            throws RemoteException;
}

