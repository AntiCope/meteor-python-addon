package cloudburst.pythonaddon.utils;

import java.io.IOException;
import java.nio.file.*;

import static cloudburst.pythonaddon.PythonAddon.PYTHON_HOME;

public class PathUtils {
    public static boolean isEmpty(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
                return !directory.iterator().hasNext();
            }
        }

        return false;
    }

    public static void createPythonHome() {
        if (PYTHON_HOME.toFile().isDirectory()) return;

        if (PYTHON_HOME.toFile().isFile()) PYTHON_HOME.toFile().delete();
        PYTHON_HOME.toFile().mkdirs();

        // Folder structure
        PYTHON_HOME.resolve("modules").toFile().mkdirs();
        PYTHON_HOME.resolve("commands").toFile().mkdirs();
        PYTHON_HOME.resolve("hud").toFile().mkdirs();

        // TODO: Create basic template or files
    }
}
