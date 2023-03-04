package datakommunikation;

public class MailClient {

    private Gui gui;
    public MailClient() {
        this.gui = new Gui(this);
    }

    public void messageToSend(String msg, String mailFrom, String mailTo) {
        Envelope envelope = new Envelope(mailFrom, mailTo);
        if (envelope.isValid()) {
            if (gui.getErrorMessage().length() != 0) {
                gui.displayErrorMessage("");
            }
            Message message = new Message(msg);
            envelope.sendToSMTPServer(message);
        }
        else {
            gui.displayErrorMessage(envelope.getValidStatus());
        }

    }

    public static void main(String[] args) {
        MailClient mailClient = new MailClient();

    }

}
