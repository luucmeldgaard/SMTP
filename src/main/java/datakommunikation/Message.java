package datakommunikation;

import java.util.Arrays;

public class Message {

    int currentLine;
    String message;
    String[] completeMessage;

    /** An object that can be used as the complete message
     * to be send to an SMTP-server, including header and main-text
     *
     * @param msg - the complete DATA (message) to be sent
     */
    public Message(String msg) {
        this.currentLine = 0;
        this.completeMessage = msg.split("\n");

    }

    /*  Loops through the message line by line
        returns null when there are no more lines
        in the message */
    public String nextLine() {
        if (currentLine == completeMessage.length) {
            return null;
        }

        String line = completeMessage[currentLine];
        currentLine += 1;
        return line;
    }

}
