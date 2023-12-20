import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RmiImpl extends UnicastRemoteObject implements Rmi {

    private final HashMap<Integer, Account> accounts;
    public RmiImpl() throws RemoteException {
        super();
        accounts = new HashMap<>();
    }

    // Generate a random 4 digit number
    public int generateAuthToken() {
        Random random = new Random();
        return random.nextInt(9000) + 1000;
    }

    public boolean usernameExists(String username) {
        for (Account account : accounts.values()) {
            if (account.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean invalidUsername(String username) {
        return !username.matches("[a-zA-Z]+") || username.length() == 0;
    }

    public boolean invalidAuthToken(int authToken) {
        return !accounts.containsKey(authToken);
    }

    @Override
    public int createAccount(String username) throws RemoteException {

        if (invalidUsername(username)) {
            return -1;
        }

        if(usernameExists(username)) {
            return 0;
        }

        Account user = new Account(username, generateAuthToken());
        accounts.put(user.getAuthToken(), user);
        return user.getAuthToken();
    }

    @Override
    public String showAccounts(int authToken) throws RemoteException {
        if (invalidAuthToken(authToken)) {
            return "Invalid Auth Token";
        }
        int count = 0;
        String usernames = "";
        for (Account account : accounts.values()) {
            if (!invalidAuthToken(account.getAuthToken())) {
                count++;
                usernames = usernames.concat(count + ". " + account.getUsername() + "\n");
            }
        }

        return usernames;
    }

    @Override
    public String sendMessage(int authToken, String recipient, String messageBody) throws RemoteException {
        if (!invalidAuthToken(authToken)) {
            Account sender = accounts.get(authToken);
            Account receiver = null;

            if (usernameExists(recipient)) {
                for (Account account : accounts.values()) {
                    if (account.getUsername().equals(recipient)) {
                        receiver = account;
                        Message message = new Message(sender.getUsername(), recipient, messageBody);
                        receiver.addMessage(message);
                        return "OK";
                    }

                }
        }
           return "User does not exist";
        } else {
            return "Invalid Auth Token";
        }
    }

    @Override
    public String showInbox(int authToken) throws RemoteException {
        if (invalidAuthToken(authToken)) {
            return "Invalid Auth Token";
        }

        Account user = accounts.get(authToken);
        List<Message> inbox = user.getMessageBox();

        if (inbox.isEmpty()) {
            return "Inbox is empty";
        }

        String messages = "";

        for (Message message : inbox) {
            messages = messages.concat(message.getMessageId() + ". " + "from: " + message.getSender());

            if (!message.isRead()) {
                messages = messages.concat(" *");
            }

            messages = messages.concat("\n");

        }
        return messages;
    }

    @Override
    public String readMessage(int authToken, int messageId) throws RemoteException {
        if (invalidAuthToken(authToken)) {
            return "Invalid Auth Token";
        }

        Account user = accounts.get(authToken);
        List<Message> inbox = user.getMessageBox();

        if (inbox.isEmpty()) {
            return "Inbox is empty";
        }

        for (Message message : inbox) {
            if (message.getMessageId() == messageId) {
                message.setRead(true);
                return "(" + message.getSender() + ")" + " " + message.getBody();
            }
        }
        return "Message ID does not exist";
    }

    @Override
    public String deleteMessage(int authToken, int messageId) throws RemoteException {
        if (invalidAuthToken(authToken)) {
            return "Invalid Auth Token";
        }

        Account user = accounts.get(authToken);
        List<Message> inbox = user.getMessageBox();

        if (inbox.isEmpty()) {
            return "Inbox is empty";
        }

        for (Message message : inbox) {
            if (message.getMessageId() == messageId) {
                user.removeMessage(message);
                return "OK";
            }
        }

        return "Message does not exist";

    }
}
