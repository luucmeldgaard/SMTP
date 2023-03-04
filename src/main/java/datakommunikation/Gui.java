package datakommunikation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Gui extends JFrame {

    private int WIDTH = 400;
    private int HEIGHT = 600;

    private int BOXHEIGHT = 30;

    private int BOXWIDTH = (int) (WIDTH*0.8);

    private MailClient mailClient;

    private JButton sendButton;
    private JTextField mailFrom;
    private JTextField mailTo;
    private JTextField subject;
    private JTextArea message;
    private JLabel label;

    public Gui(MailClient mailClient) {

        this.mailClient = mailClient;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("SMTP client");
        this.setResizable(false);

        mailFrom = new JTextField("Mail fra: ");
        mailFrom.setBounds((WIDTH-BOXWIDTH)/2,(12+BOXHEIGHT)*1, BOXWIDTH, BOXHEIGHT);

        mailFrom.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (mailFrom.getText().equals("Mail fra: ")) {
                    mailFrom.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (mailFrom.getText().isEmpty()) {
                    mailFrom.setText("Mail fra: ");
                }
            }
        });

        mailTo = new JTextField("Mail til: ");
        mailTo.setBounds((WIDTH-BOXWIDTH)/2,(12+BOXHEIGHT)*2, BOXWIDTH, BOXHEIGHT);

        mailTo.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (mailTo.getText().equals("Mail til: ")) {
                    mailTo.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (mailTo.getText().isEmpty()) {
                    mailTo.setText("Mail til: ");
                }
            }
        });

        subject = new JTextField("emne");
        subject.setBounds((WIDTH-BOXWIDTH)/2, (12+BOXHEIGHT)*3, BOXWIDTH, BOXHEIGHT);
        subject.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (subject.getText().equals("emne")) {
                    subject.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (subject.getText().isEmpty()) {
                    subject.setText("emne");
                }
            }
        });

        message = new JTextArea("Skriv besked...");
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setBounds((WIDTH-BOXWIDTH)/2, (12+BOXHEIGHT)*4, BOXWIDTH, BOXHEIGHT*10);
        message.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (message.getText().equals("Skriv besked...")) {
                    message.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (message.getText().isEmpty()) {
                    message.setText("Skriv besked...");
                }
            }
        });

        label = new JLabel("Måske");
        label.setBounds((WIDTH-BOXWIDTH)/2, 20, BOXWIDTH, BOXHEIGHT);

        sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setBounds((WIDTH-BOXWIDTH)/2, (20+BOXHEIGHT)*10, BOXWIDTH, BOXHEIGHT);
        sendButton.addActionListener(e -> sendRequest());

        this.add(label);
        this.add(sendButton);
        this.add(mailFrom);
        this.add(mailTo);
        this.add(message);
        this.add(subject);
        this.setLayout(null);
        this.setVisible(true);

    }

    private void removeText(String emne) {
        if (subject.getText().equals("emne")) {
            subject.setText("");
        }
    }

    public void sendRequest() {
        sendButton.setEnabled(false);
        mailClient.messageToSend("Subject: " + subject.getText() + "\n" + message.getText(), mailFrom.getText(), mailTo.getText());
        //mailFrom.setText("");
        //mailTo.setText("");
        sendButton.setEnabled(true);
    }

    public void displayErrorMessage(String errorMessage) {
        label.setText(errorMessage);
    }

    public String getErrorMessage() {
        return label.getText();
    }

}
