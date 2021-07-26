package cloudburst.pythonaddon;

import cloudburst.pythonaddon.pyclasses.PyCommand;
import cloudburst.pythonaddon.pyclasses.PyModule;
import cloudburst.pythonaddon.utils.PathUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class PythonSystem {
    public static final PythonSystem INSTANCE = new PythonSystem();
    public final PyList pymodules = new PyList();
    public final PyList pycommands = new PyList();
    private final Logger log = LogManager.getLogger();
    private final Path pythonHome;
    private final PySystemState systemState;

    public PythonSystem() {
        pythonHome = MeteorClient.FOLDER.toPath().resolve("python");
        systemState = new PySystemState();
        systemState.path.append(Py.newString(pythonHome.toString()));
    }

    public void init() {
        createPythonHome();


        // Initializing python modules
        PathUtils.getFiles(pythonHome.resolve("modules")).forEach(file -> {
            if (file.getName().startsWith("_")) return;
            if (!(file.getName().endsWith(".py") || file.getName().endsWith(".jy"))) return;
            log.info("Executing: " + file.getName());
            execFile(file.toString());
        });
        Modules modules = Modules.get();
        log.info("Python modules: " + pymodules.toString());
        for (Object module : pymodules) {
            if (module instanceof PyObject) {
                modules.add(new PyModule((PyObject) module));
            }
        }

        // Initializing python commands
        PathUtils.getFiles(pythonHome.resolve("commands")).forEach(file -> {
            if (file.getName().startsWith("_")) return;
            if (!(file.getName().endsWith(".py") || file.getName().endsWith(".jy"))) return;
            log.info("Executing: " + file.getName());
            execFile(file.toString());
        });
        Commands commands = Commands.get();
        log.info("Python commands: " + pycommands.toString());
        for (Object command : pycommands) {
            if (command instanceof PyObject) {
                commands.add(new PyCommand((PyObject) command));
            }
        }
    }

    public PythonInterpreter createInterpreter() {
        return new PythonInterpreter(null, systemState);
    }

    public void execFile(String path, PythonInterpreter python) {
        OutputStream err_stream = new ByteArrayOutputStream();
        try {
            python.setErr(err_stream);
            python.execfile(pythonHome.resolve(path).toString());
        } catch (PyException e) {
            ChatUtils.error("Python", e.getMessage());
            log.error(e);
            return;
        }
        String err;
        err = err_stream.toString();
        if (!err.isEmpty()) {
            log.error(err);
            ChatUtils.error("Python", err);
        }
    }

    public void execFile(String path) {
        execFile(path, createInterpreter());
    }

    private void createPythonHome() {
        if (pythonHome.toFile().isDirectory()) return;

        if (pythonHome.toFile().isFile()) pythonHome.toFile().delete();
        pythonHome.getParent().toFile().mkdirs();

        PathUtils.copy(FabricLoader.getInstance().getModContainer("python-addon").get().getRootPath().resolve("python").toString(), pythonHome.toString());

    }
}
