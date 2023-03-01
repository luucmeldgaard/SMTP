package datakommunikation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    private int WIDTH = 400;
    private int HEIGHT = 600;

    private int BOXHEIGHT = 60;

    private int BOXWIDTH = (int) (WIDTH*0.8);
    private SMTP smtp;

    private JButton sendButton;
    private JTextField mailFrom;
    private JTextField mailTo;
    private JTextField message;

    public Gui(SMTP smtp) {

        this.smtp = smtp;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("SMTP client");
        this.setResizable(false);

        mailFrom = new JTextField("Write here... mailfrom");
        mailFrom.setBounds((WIDTH-BOXWIDTH)/2,(12+BOXHEIGHT)*1, BOXWIDTH, BOXHEIGHT);

        mailTo = new JTextField("Write here... mailto");
        mailTo.setBounds((WIDTH-BOXWIDTH)/2,(12+BOXHEIGHT)*2, BOXWIDTH, BOXHEIGHT);

        message = new JTextField("Write here... jtextfield");
        message.setBounds((WIDTH-BOXWIDTH)/2, (12+BOXHEIGHT)*3, BOXWIDTH, BOXHEIGHT);

        JLabel label = new JLabel("Jeg ved ikke hvad den her gør");
        label.setBounds((WIDTH-BOXWIDTH)/2, 20, BOXWIDTH, BOXHEIGHT);

        sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setBounds((WIDTH-BOXWIDTH)/2, (20+BOXHEIGHT)*4, BOXWIDTH, BOXHEIGHT);
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
        smtp.sendMessage(message.getText(), mailFrom.getText(), mailTo.getText());
        sendButton.setEnabled(true);
    }



}
