package datakommunikation;

import java.util.Base64;
import java.util.Scanner;

// Kun til SSL og TLS - ikke en del af programmet
public class Base64Converter {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String username = scan.nextLine();
        String password = scan.nextLine();

        // Encode the username and password as base64
        String encodedUsername = Base64.getEncoder().encodeToString(username.getBytes());
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

        System.out.println("Encoded username: " + encodedUsername);
        System.out.println("Encoded password: " + encodedPassword);
    }

}

// til google server: luucmeldgaardtest@gmail.com:  bHV1Y21lbGRnYWFyZHRlc3RAZ21haWwuY29t
// til google server: frliomgtbvrkfqgr, base64:     ZnJsaW9tZ3RidnJrZnFncg==
