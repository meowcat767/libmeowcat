package site.meowcat.system.files;

import javax.print.DocFlavor;
import java.io.*;

public class Read {

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public String read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("[libmeowcat]: File not found: " + path);
        }
        try(InputStream inputStream = new FileInputStream(file)) {
        return readFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
