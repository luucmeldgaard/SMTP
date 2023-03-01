package datakommunikation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    private int WIDTH = 400;
    private int HEIGHT = 600;
    private SMTP smtp;

    private JButton sendButton;
    private JTextField message;

    public Gui(SMTP smtp) {

        this.smtp = smtp;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("SMTP client");
        this.setResizable(false);

        JTextField mailFrom = new JTextField("Write here...");
        mailFrom.setBounds(0, 100, WIDTH, 40);

        JTextField mailTo = new JTextField("Write here...");
        mailTo.setBounds(0, 150, WIDTH, 40);

        message = new JTextField("Write here...");
        message.setBounds(0, 200, WIDTH, 200);

        JLabel label = new JLabel("Insert text here");
        label.setBounds(WIDTH/2 - 75, 25, 100, 100);

        sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setBounds(WIDTH - 100, HEIGHT - 100, 80, 25);
        sendButton.addActionListener(e -> sendRequest());


        this.add(label);
        this.add(sendButton);
        this.add(mailFrom);
        this.add(mailTo);
        this.add(message);
        this.setLayout(null);
        this.setVisible(true);

    }

    public void sendRequest() {
        sendButton.setEnabled(false);
        smtp.sendMessage(message.getText());
        sendButton.setEnabled(true);
    }



}
