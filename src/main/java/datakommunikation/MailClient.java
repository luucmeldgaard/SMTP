package datakommunikation;

import java.util.Base64;

public class MailClient {

    private Gui gui;
    private String encodedUsername;
    private String encodedPassword;
    private boolean SSL;

    public MailClient() {
        this.gui = new Gui(this);
        this.SSL = false;
    }

    public void messageToSend(String msg, String mailFrom, String mailTo) {
        Envelope envelope = new Envelope(mailFrom, mailTo, SSL);
        if (envelope.isValid()) {
            if (gui.getErrorMessage().length() != 0) {
                gui.displayErrorMessage("");
            }
            Message message = new Message(msg);
            if (SSL) {
                envelope.sendEnvelope(message, encodedUsername, encodedPassword);
            }
            else {
                envelope.sendEnvelope(message);
            }
        }
        else {
            gui.displayErrorMessage(envelope.getValidStatus());
        }

    }

    public void login(String username, String password) {
        encodedUsername = Base64.getEncoder().encodeToString(username.getBytes());
        encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        if (username.equals("test") && password.equals("test")) {
            encodedUsername = "bHV1Y21lbGRnYWFyZHRlc3RAZ21haWwuY29t";
            encodedPassword = "ZnJsaW9tZ3RidnJrZnFncg==";
        }

        Envelope envelope = new Envelope(null, null, true);
        boolean loginSessionsSuccess = envelope.checkEnvelope(encodedUsername, encodedPassword);
        System.out.println("loginsession: " + loginSessionsSuccess);
        if (loginSessionsSuccess) {
            SSL = true;
            gui.closeSetupWindows();
        }
        else {
            SSL = false;
            gui.displayErrorMessage("Invalid Credentials");
        }



    }

    public static void main(String[] args) {
        MailClient mailClient = new MailClient();

    }

    // "bHV1Y21lbGRnYWFyZHRlc3RAZ21haWwuY29t"
    // "ZnJsaW9tZ3RidnJrZnFncg=="

}
