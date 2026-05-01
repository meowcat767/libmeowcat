package site.meowcat.system.files;

import java.io.FileWriter;
import java.io.IOException;

public class Write {

    public void write(String fileName, String buffer) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
    }
}
}
