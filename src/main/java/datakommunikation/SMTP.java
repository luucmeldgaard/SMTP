package datakommunikation;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
 */
public class SMTP {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private BufferedWriter toServer;

    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the
       associated streams. Initialize SMTP connection. */
    public SMTP(Envelope envelope) throws IOException {
        this.connection = null;
        this.fromServer = null;
        this.toServer = null;

        try {
            this.connection = new Socket("datacomm.bhsi.xyz", 2526);
            this.fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.toServer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

	/* Read a line from server and check that the reply code is 220.
	   If not, throw an IOException. */
            try {
                fromServer.readLine();
            } catch (IOException e) {
                System.out.println(e);
            }

        /* SMTP handshake. We need the name of the local machine.
	   Send the appropriate SMTP handshake command. */
            String localhost = "helo";
            sendCommand(localhost, false);

            isConnected = true;
        } catch (IOException e) {
            System.out.println(e);
            closeEverything(connection,fromServer,toServer);
        }
    }

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String msg, boolean showAll) {

        try {
            if (msg.equals("data")) {
                toServer.write("data");
                toServer.newLine();
                toServer.flush();
                String answer = fromServer.readLine();
                System.out.println(answer);
                Scanner scan = new Scanner(System.in);
                while (true) {
                    msg = scan.nextLine();
                    if (msg.equals(".")) {
                        System.out.println("Trying to send message...");
                        toServer.write("." + CRLF);
                        toServer.flush();
                        Thread.sleep(3000); // Pause for 3 seconds
                        break;
                    }
                    else {
                        toServer.write(msg);
                        toServer.newLine();
                    }
                }
            }

            else {
                System.out.println("has been run");
                toServer.write(msg);
                toServer.newLine();
                toServer.flush();
            }

            String answer = fromServer.readLine();

            if (!showAll) {
                String[] answerCode = answer.split(" ");
                System.out.println(answerCode[0]);
            } else {
                System.out.println(answer);
            }

        } catch (IOException e) {
            System.out.println(e);
            closeEverything(connection, fromServer, toServer);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /* Fill in */
        /* Write command to server and read reply from server. */
        /* Fill in */

        /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        /* Fill in */
    }

    /**
     *
     * @param socket
     * @param fromServer
     * * @param toServer
     */
    public void closeEverything(Socket socket, BufferedReader fromServer, BufferedWriter toServer) {
        try {
            socket.close();
            fromServer.close();
            toServer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws IOException {
        SMTP smtp = new SMTP(null);
        Gui gui = new Gui();
        Scanner scan = new Scanner(System.in);
        String promptCommand;
        while (true) {
            promptCommand = scan.nextLine();
            smtp.sendCommand(promptCommand, true);
        }
    }

}
