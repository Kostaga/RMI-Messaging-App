public class Message implements java.io.Serializable {

    private boolean isRead;
    private final String sender;
    private final String receiver;
    private final String body;

    private final int messageId;
    private static int counter = 0;


    public Message(String sender, String receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
        this.isRead = false;
        this.messageId = counter;
        counter++;
    }

    public String getSender() {
        return sender;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getBody() {
        return body;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }




}
