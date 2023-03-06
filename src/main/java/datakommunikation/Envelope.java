package datakommunikation;

import java.io.File;
import java.util.regex.*;

public class Envelope {

    SMTP smtp;
    String mailFrom;
    String mailTo;

    String validStatus;

    private boolean SSL;

    public Envelope(String mailFrom, String mailTo, boolean SSL) {
        this.smtp = null;
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.validStatus = null;
        this.SSL = SSL;
    }

    public void sendEnvelope(Message message, File includedFile, boolean withAttachment, String... credentials) {
        if (SSL) {
            smtp = new SMTP(true, credentials);
            smtp.send(this, message, includedFile, withAttachment, credentials);
        }
        else {
            smtp = new SMTP(false);
            smtp.send(this, message, includedFile, withAttachment);
        }
    }

    public boolean checkEnvelope(String encodedUsername, String encodedPassword) {

        SMTP testConnection;
        if (SSL) {
            testConnection = new SMTP(true, encodedUsername, encodedPassword);
        }
        else {
            testConnection = new SMTP(false);
        }
        return testConnection.getSuccessfulLastSession();
    }


    public boolean isValid() {
        int maxAddressLength = 254;

        // example@something.com
        String validMailPattern = "^[^.-].+@.+\\..*[^.-]";
        Pattern p = Pattern.compile(validMailPattern);

        for (String address : new String[] {mailFrom, mailTo}) {

            if (address.length() > maxAddressLength) {
                this.validStatus = "The address: " + address + " is too long. ";
                return false;
            }
            else if (address.length() == 0) {
                this.validStatus = "Address field left empty. ";
                return false;
            }

            Matcher m = p.matcher(address);

            if (m.matches()) {
                this.validStatus = "valid";

            } else {
                this.validStatus = address + " is invalid";
                return false;
            }
        }
        return true;
    }

    public String getValidStatus() {
        return this.validStatus;
    }

    public String getMailFrom() {
        return this.mailFrom;
    }

    public String getMailTo() {
        return this.mailTo;
    }

    public static void main(String[] args) {
        Envelope envelope = new Envelope("example@edu.dk", "anotherexample@dtu.dk", false);
        System.out.println(envelope.isValid());
        System.out.println(envelope.getValidStatus());
    }

}
