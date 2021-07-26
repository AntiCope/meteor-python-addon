package cloudburst.pythonaddon.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PathUtils {
    public static boolean isEmpty(Path path) {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                return !directory.iterator().hasNext();
            } catch (IOException e) {
                return true;
            }
        }
        return false;
    }

    public static List<File> getFiles(Path path) {
        if (!Files.isDirectory(path)) return new ArrayList<>();
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
            List<File> files = new ArrayList<>();
            for (Path p: directory) {
                if (p.toFile().isFile()) files.add(p.toFile());
            }
            return files;
        } catch (IOException e) {
        }
        return new ArrayList<>();
    }

    public static void copy(String from, String to) {
        try {
            Files.walk(Paths.get(from))
                .forEach(fromFile -> {
                    Path dest = Paths.get(to.toString(), fromFile.toString().substring(from.toString().length()));
                    try {
                        Files.copy(fromFile, dest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
