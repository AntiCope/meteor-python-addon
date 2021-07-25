package cloudburst.pythonaddon;

import cloudburst.pythonaddon.pyclasses.PyModule;
import cloudburst.pythonaddon.utils.PathUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import org.python.antlr.op.Mod;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class PythonSystem {
    public static final PythonSystem INSTANCE = new PythonSystem();
    private final Path pythonHome;
    private final PySystemState systemState;

    public PythonSystem() {
        pythonHome = MeteorClient.FOLDER.toPath().resolve("python");
        systemState = new PySystemState();
        systemState.path.append(Py.newString(pythonHome.toString()));
    }

    public void init() {
        createPythonHome();

        Modules modules = Modules.get();
        PathUtils.getFiles(pythonHome.resolve("modules")).forEach(file -> {
            modules.add(new PyModule(file.getName().toString()));
        });
    }

    public PythonInterpreter createInterpreter() {
        return new PythonInterpreter(null, systemState);
    }

    public void execFile(String path, PythonInterpreter python) {
        OutputStream err_stream = new ByteArrayOutputStream();
        try {
            python.setErr(err_stream);

            python.execfile(pythonHome.resolve(path).toString());
        } catch (Exception e) {
            ChatUtils.error("Python", e.getMessage());
            return;
        }
        String err;
        err = err_stream.toString();
        if (!err.isEmpty()) {
            PythonAddon.LOG.error(err);
            ChatUtils.error("Python", err);
        }
    }

    public void execFile(String path) {
        execFile(path, createInterpreter());
    }


    private void createPythonHome() {
        if (pythonHome.toFile().isDirectory()) return;

        if (pythonHome.toFile().isFile()) pythonHome.toFile().delete();
        pythonHome.toFile().mkdirs();

        // Folder structure
        pythonHome.resolve("modules").toFile().mkdirs();
        pythonHome.resolve("commands").toFile().mkdirs();
        pythonHome.resolve("hud").toFile().mkdirs();

        // TODO: Create basic template or files
    }

    public boolean modulesExist() {
        if (!pythonHome.toFile().isDirectory()) return false;
        return !PathUtils.isEmpty(pythonHome.resolve("modules"));
    }
}
