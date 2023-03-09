package datakommunikation;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import javax.sound.sampled.*;

public class Gui extends JFrame {

    private int WIDTH = 400;
    private int HEIGHT = 700;

    private int BOXHEIGHT = 30;

    private int BOXWIDTH = (int) (WIDTH*0.8);

    private MailClient mailClient;

    private static File includedFile;

    private JButton sendButton;
    private JButton selectAttatchmentButton;
    private JTextField mailFrom;
    private JTextField mailTo;
    private JTextField subject;
    private JTextArea message;
    private JLabel label;
    private JCheckBox rickroll;
    private JFrame gmailGui;
    JFrame serverGui;

    public Gui(MailClient mailClient) {

        this.mailClient = mailClient;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("SMTP client");
        this.setResizable(false);

        mailFrom = new JTextField("luucmeldgaardtest@gmail.com");
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

        mailTo = new JTextField("luucmeldgaard@outlook.com");
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

        label = new JLabel("SMTP CLIENT");
        label.setBounds((WIDTH-BOXWIDTH)/2, 20, BOXWIDTH, BOXHEIGHT);

        rickroll = new JCheckBox("secret attachment");
        rickroll.setBounds((WIDTH-BOXWIDTH)/2, (20+BOXHEIGHT)*10, BOXWIDTH, BOXHEIGHT);

        sendButton = new JButton("Send");
        sendButton.setFocusable(false);
        sendButton.setBounds((WIDTH-BOXWIDTH)/2, (20+BOXHEIGHT)*12, BOXWIDTH, BOXHEIGHT);
        sendButton.addActionListener(e -> sendRequest());

        selectAttatchmentButton = new JButton("Vælg billede af Hansi (kun Hansi plz)");
        selectAttatchmentButton.setFocusable(false);
        selectAttatchmentButton.setBounds((WIDTH-BOXWIDTH)/2, (20+BOXHEIGHT)*11, BOXWIDTH, BOXHEIGHT);
        selectAttatchmentButton.addActionListener(e -> openFileExplorer());



        this.add(label);
        this.add(sendButton);
        this.add(selectAttatchmentButton);
        this.add(mailFrom);
        this.add(mailTo);
        this.add(message);
        this.add(subject);
        this.add(rickroll);
        this.setLayout(null);
        this.setVisible(true);

        this.setEnabled(false);

        serverSelectGui();

    }

    private void openFileExplorer(){
        if (rickroll.isSelected()) {
            displayErrorMessage("rickroll is selected");
        }
        else {
            FileDialog fd = new FileDialog(new JFrame());
            fd.setDirectory(".\\hansi");
            fd.setVisible(true);
            File[] f = fd.getFiles();
            try {
                String filePath = fd.getFiles()[0].getAbsolutePath();
                if (f.length > 0) {
                    System.out.println("File is " + filePath);
                    includedFile = f[0];
                }
                int extensionStart = filePath.lastIndexOf(".");
                String extension = filePath.substring(extensionStart, filePath.length());
                System.out.println(extension);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e + ": no file selected");
            }
        }
        rickroll.setEnabled(false);
    }

    private void serverSelectGui() {
        serverGui = new JFrame("Choose connection");

        serverGui.setResizable(false);
        serverGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel infoText = new JLabel("Choose a connection");
        infoText.setHorizontalAlignment(SwingConstants.CENTER);



        JButton dataComm = new JButton("Datacomm");
        dataComm.setPreferredSize(new Dimension(80, 25));
        dataComm.setFocusable(false);
        dataComm.addActionListener(e -> closeSetupWindows());

        JButton googleMail = new JButton("Gmail");
        googleMail.setPreferredSize(new Dimension(100, 25));
        googleMail.setFocusable(false);
        googleMail.addActionListener(e -> gmailSetup());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 10));
        buttonPanel.setLayout(new GridLayout(1, 1));
        buttonPanel.add(dataComm);
        buttonPanel.add(googleMail);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 10));
        panel.add(infoText, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        serverGui.add(panel, BorderLayout.CENTER);
        serverGui.pack();
        serverGui.setVisible(true);

    }

    private void gmailSetup() {

        gmailGui = new JFrame("Login to Gmail");
        gmailGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gmailGui.setResizable(false);

        JTextField addressField = new JTextField("Address field, ex: example@gmail.com ...");
        addressField.setPreferredSize(new Dimension(400, 20));

        addressField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (addressField.getText().equals("Address field, ex: example@gmail.com ...")) {
                    addressField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (addressField.getText().isEmpty()) {
                    addressField.setText("Address field, ex: example@gmail.com ...");
                }
            }
        });

        JTextField passwordField = new JTextField("password ...");
        passwordField.setPreferredSize(new Dimension(400, 20));

        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("password ...")) {
                    passwordField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("password ...");
                }
            }
        });

        JButton login = new JButton("Login");
        login.setPreferredSize(new Dimension(100, 25));
        login.setFocusable(false);
        login.addActionListener(e -> mailClient.login(addressField.getText(), passwordField.getText()));


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 10));
        panel.setLayout(new GridLayout(0, 1));

        panel.add(addressField);
        panel.add(passwordField);
        panel.add(login);

        gmailGui.add(panel);
        gmailGui.pack();
        gmailGui.setVisible(true);

    }

    private void removeText(String emne) {
        if (subject.getText().equals("emne")) {
            subject.setText("");
        }
    }

    public void sendRequest() {
        sendButton.setEnabled(false);

        playSound("swoosh-sound-effect-By-Tuna.wav");

        if (rickroll.isSelected()) {
            includedFile = new File(".\\hansi\\importantFile.mp4");
        }

        boolean withAttachment;
        if (includedFile != null) {
            withAttachment = true;
        }
        else {
            withAttachment = false;
        }

        mailClient.messageToSend("Subject: " + subject.getText() + "\n" + message.getText(), mailFrom.getText(), mailTo.getText(), withAttachment, includedFile);
        //mailFrom.setText("");
        //mailTo.setText("");
        sendButton.setEnabled(true);
    }

    public void displayErrorMessage(String errorMessage) {
        label.setText(errorMessage);
        label.setVisible(true);

        Timer timer = new Timer(2500, e -> {
            label.setText("SMTP CLIENT");
            label.setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public String getErrorMessage() {
        return label.getText();
    }

    public void closeSetupWindows() {
        if (gmailGui != null) {
            gmailGui.setVisible(false);
        }
        serverGui.setVisible(false);
        this.setEnabled(true);
        this.setVisible(true);
    }

    public void playSound(String soundName){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
