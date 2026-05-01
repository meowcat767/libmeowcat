package site.meowcat.system.files.compression;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class QuickZip {
    public static void zip(Path source, Path zipFile) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
            if (Files.isDirectory(source)) {
                zipDirectory(source, source, zos);
            } else {
                zipSingleFile(source, source.getFileName().toString(), zos);
            }
        }
    }

    private static void zipDirectory(Path source, Path directory, ZipOutputStream zos) throws IOException {
        for (Path path : Files.newDirectoryStream(directory)) {
            if (Files.isDirectory(path)) {
                zipDirectory(path, source, zos);
            } else {
                String entryName = source.relativize(path).toString();
                zipSingleFile(path, entryName, zos);
            }
        }
    }

    private static void zipSingleFile(Path source, String entryName, ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(entryName));
        Files.copy(
                source,
                zos
        );
        zos.closeEntry();
    }

    public static void unzip(Path source, Path targetDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(source.toFile()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path newPath = targetDir.resolve(entry.getName());
                if (!newPath.startsWith(targetDir)) {
                    throw new IOException("[libmeowcat] Entry is outside of the target directory: " + entry.getName());
                }
                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zis.closeEntry();
            }
        }
    }
}
