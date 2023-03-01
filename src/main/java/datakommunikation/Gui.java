package datakommunikation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    private int WIDTH = 400;
    private int HEIGHT = 600;

    public Gui() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("SMTP client");
        this.setResizable(false);

        JTextField mailFrom = new JTextField("Write here...");
        mailFrom.setBounds(0, 0, 100, 100);

        JLabel label = new JLabel("Insert text here");
        label.setBounds(100, 100, 100, 100);

        JButton sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setBounds(100, 200, 80, 25);
        sendButton.addActionListener(e -> sendRequest());


        this.add(label);
        this.add(sendButton);
        this.add(mailFrom);
        this.setLayout(null);
        this.setVisible(true);

    }

    public void sendRequest() {
        System.out.println("Message sent. ");
    }

}
