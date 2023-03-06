package datakommunikation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class MimeMessage {

    int currentLine;
    String mimeVersion;
    String contentType;
    String boundary;
    String attachmentType;
    String encoding;
    String[] prepareStatement;

    String encodingBase64;
    String attachmentDisposition;
    String[] attachmentPart;
    String[] fileBase64Line;
    ArrayList<String> mimeLines;
    String fileFormat;

    public MimeMessage(Message message, File includedFile) {

        String filePath = includedFile.getAbsolutePath();
        int fileNameIndex = filePath.lastIndexOf("\\");
        String fileName = filePath.substring(fileNameIndex);
        int extensionStart = filePath.lastIndexOf(".");
        String extension = filePath.substring(extensionStart);
        System.out.println(extension);
        switch (extension) {
            case ".mp4" -> {
                this.fileFormat = "video/mp4";
            }
            case ".png"  -> {
                this.fileFormat = "image/png";
            }
            case ".jpg" -> {
                this.fileFormat = "image/jpg";
            }
            case ".zip" -> {
                this.fileFormat = "application/zip";
            }
            case ".jar" -> {
                this.fileFormat = "application/jar";
            }
        }


        this.currentLine = 0;
        this.mimeVersion = "MIME-Version: 1.0";
        this.contentType = "Content-Type: multipart/mixed; boundary=\"boundary_1234567890\"";
        this.boundary = "--boundary_1234567890";
        this.attachmentType = "Content-Type: text/plain; charset=\"utf-8";
        this.encoding = "Content-Transfer-Encoding: 8bit";
        this.prepareStatement = new String[]{mimeVersion, contentType, boundary, attachmentType, encoding, ""};

        this.contentType = "Content-Type: " + fileFormat + "; name=" + fileName;
        this.encodingBase64 = "Content-Transfer-Encoding: base64";
        this.attachmentDisposition = "Content-Disposition: attachment; filename=" + fileName;
        this.attachmentPart = new String[] {"", boundary, contentType, encodingBase64, attachmentDisposition, ""};

        try {
            byte[] png = Files.readAllBytes(Paths.get(filePath));
            String encodedPng = Base64.getEncoder().encodeToString(png);

            this.fileBase64Line = encodedPng.split("(?<=\\G.{50})");


        } catch (IOException e) {
            e.printStackTrace();
        }

        this.mimeLines = new ArrayList<>();
        mimeLines.addAll(Arrays.asList(prepareStatement));
        String lineOfMessage = message.nextLine();
        while (lineOfMessage != null) {
            mimeLines.add(lineOfMessage);
            lineOfMessage = message.nextLine();
        }
        //mimeLines.addAll(Arrays.asList(message.getCompleteMessage()));
        mimeLines.addAll(Arrays.asList(attachmentPart));
        mimeLines.addAll(Arrays.asList(fileBase64Line));
        mimeLines.add("--boundary_1234567890--");

    }

    public String nextLine() {
        if (currentLine == mimeLines.size()) {
            return null;
        }

        String line = mimeLines.get(currentLine);
        currentLine += 1;
        return line;
    }
}
