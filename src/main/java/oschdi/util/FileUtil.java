package oschdi.util;

import oschdi.parser.RecipeSaver;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    private FileUtil() {}

    public static String getResourcePath() {
        ClassLoader classLoader = RecipeSaver.class.getClassLoader();
        String resourcePath = "resourcePath.txt"; // Change this to the actual path

        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                String fileContents = new String(buffer);
                return fileContents;
            } else {
                System.out.println("Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
