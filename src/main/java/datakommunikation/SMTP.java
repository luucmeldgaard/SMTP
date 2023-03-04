package datakommunikation;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
 */
public class SMTP {

    // The socket that we use to connect to the SMTP-server
    private Socket connection;

    // Buffered reader and writer for the connection
    private BufferedReader fromServer;
    private BufferedWriter toServer;

    private static final int SMTP_PORT = 25;

    // Array of positive status codes
    private static final String[] goodStatusCodes = new String[] {"354", "250", "221", "250", "220"};


    // Initializes a connection with the SMTP-server
    // and the socket streams
    /* Create an SMTPConnection object. Create the socket and the
       associated streams. Initialize SMTP connection. */
    public SMTP() {
        this.connection = null;
        this.fromServer = null;
        this.toServer = null;

        // tests the connection
        connect();
        closeEverything(connection, fromServer, toServer);

    }


    // Establishes a connection
    private void connect() {

        try {
            System.out.println("Testing connection... ");
            this.connection = new Socket("datacomm.bhsi.xyz", SMTP_PORT);
            this.fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.toServer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

            System.out.println("Connection established. ");


            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) factory.createSocket("smtp.gmail.com", 465);
            sslSocket.startHandshake();

            this.fromServer = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            this.toServer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            toServer.write("helo localhost");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());
            System.out.println(fromServer.readLine());

            toServer.write("AUTH LOGIN");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("bHV1Y21lbGRnYWFyZHRlc3RAZ21haWwuY29t");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("ZnJsaW9tZ3RidnJrZnFncg==");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("mail from: <luucmeldgaardtest@gmail.com>");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("rcpt to: <luucmeldgaardtest@gmail.com>");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("DATA");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            toServer.write("det her kan umuligt virke");
            toServer.newLine();
            toServer.flush();

            toServer.write(".");
            toServer.newLine();
            toServer.flush();
            System.out.println(fromServer.readLine());

            /* Read a line from server and check that the reply code is 220.
               If not, throw an IOException. */

            getStatusCode(false);

            sendCommand("helo localhost", true);


            /* SMTP handshake. We need the name of the local machine.
           Send the appropriate SMTP handshake command. */
            //String localhost = "helo localhost";
            //sendCommand(localhost, false);

        } catch (IOException e) {
            System.out.println(e);
            closeEverything(connection,fromServer,toServer);
        }

    }

    /*  returns the status code
        and if it is not in the
        goodStatusCodes array,
        it will kill the connection */
    public String getStatusCode(boolean showAll) {

        String statusCode = "";

        try {

            String serverAnswer = fromServer.readLine();
            String[] serverAnswerAsArray = serverAnswer.split(" ");
            statusCode = serverAnswerAsArray[0].trim();

            if (Arrays.asList(goodStatusCodes).contains(statusCode)) {
                if (showAll) {
                    System.out.println("Server says: " + serverAnswer);
                }
                else {
                    System.out.println("Server says: " + statusCode);
                }

            }
            else {
                System.out.println("Error: " + statusCode);
                throw new IOException();
            }

        } catch (IOException e) {
            System.out.println(e);
            closeEverything(connection, fromServer, toServer);
        }

        return statusCode;
    }

    // Sends a command to the server
    public void sendCommand(String msg, boolean flush) {

        try {

            System.out.println(msg);
            toServer.write(msg);
            toServer.newLine();
            if (flush) {
                toServer.flush();
            }

        } catch (IOException e) {
            System.out.println(e);
            closeEverything(connection, fromServer, toServer);
        }

    }

        /* Fill in */
        /* Write command to server and read reply from server. */
        /* Fill in */

        /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        /* Fill in */

    // sends the mail to the server
    public void send(Envelope envelope, Message message) {
        connect();
        sendCommand("mail from: <" + envelope.getMailFrom() + ">", true);
        getStatusCode(true);
        sendCommand("rcpt to: <" + envelope.getMailTo() + ">", true);
        getStatusCode(true);
        sendCommand("DATA", true);

        String lineOfMessage = message.nextLine();
        while (lineOfMessage != null) {
            sendCommand(lineOfMessage, false);
            lineOfMessage = message.nextLine();
        }
        sendCommand(".", true);
        getStatusCode(true);
        closeEverything(connection, fromServer, toServer);

    }

    /** closes all the streams and the socket connection.
     *  Usually used after testing a connection,
     *  receiving an IOException or to end the
     *  connection after it has been used.
     *
     * @param socket - the client-connection object to the SMTP-server
     * @param fromServer - the buffered reader
     * @param toServer - the buffered writer
     */
    public void closeEverything(Socket socket, BufferedReader fromServer, BufferedWriter toServer) {
        try {
            fromServer.close();
            toServer.close();
            socket.close();
            System.out.println("successfully closed connection. ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SMTP smtp = new SMTP();
    }

}
