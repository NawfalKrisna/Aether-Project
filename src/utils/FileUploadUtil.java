package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads";

    static {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
    }

    public static String saveFile(String sourceFilePath) throws IOException {
        if (sourceFilePath == null || sourceFilePath.trim().isEmpty()) {
            throw new IOException("Source file path is empty");
        }

        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            throw new IOException("Source file does not exist: " + sourceFilePath);
        }

        String filename = sourceFile.getName();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueFilename = timestamp + "_" + filename;
        String destPath = UPLOAD_DIR + File.separator + uniqueFilename;

            try {
                Files.copy(
                        Paths.get(sourceFilePath),
                        Paths.get(destPath)
                );
                return destPath;
            } catch (IOException e) {
                throw e;
            }
    }

    public static String getFullPath(String storedPath) {
        return new File(storedPath).getAbsolutePath();
    }
}
