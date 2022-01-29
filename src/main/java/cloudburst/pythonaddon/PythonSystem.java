package cloudburst.pythonaddon;

import cloudburst.pythonaddon.pyclasses.PyCommand;
import cloudburst.pythonaddon.pyclasses.PyModule;
import cloudburst.pythonaddon.utils.PathUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.util.log.Log;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
        systemState.path.append(Py.newStringOrUnicode(pythonHome.toString()));
        
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
            try {
                if (module instanceof PyObject) {
                    modules.add(new PyModule((PyObject) module));
                }
            } catch (IllegalArgumentException e) {
                log.error(e);
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
            try {
                if (command instanceof PyObject) {
                    commands.add(new PyCommand((PyObject) command));
                }
            } catch (Exception e) {
                log.error(e);
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
        } catch (Exception e) {
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

        log.info("Creating python home");

        if (pythonHome.toFile().isFile()) pythonHome.toFile().delete();
        pythonHome.getParent().toFile().mkdirs();

        File zipPath = MeteorClient.FOLDER.toPath().resolve("python.zip").toFile();

        try {
            FileUtils.copyURLToFile(new URL("https://github.com/AntiCope/python-addon-lib/archive/refs/heads/master.zip"), zipPath);
            ZipFile zip = new ZipFile(zipPath);
            zip.entries().asIterator().forEachRemaining(file -> {
                String path = file.toString().replaceFirst("python-addon-lib-master", "python");
                File outFile = MeteorClient.FOLDER.toPath().resolve(path).toFile();
                if (file.isDirectory()) {
                    outFile.mkdirs();
                }
                else {
                    outFile.getParentFile().mkdirs();
                    try (InputStream zipStream = zip.getInputStream(file)) {
                        Files.copy(zipStream, outFile.toPath());
                    } catch (IOException e) {
                        log.error(e);
                    }
                    
                }
            });
            zip.close();
            
            Files.deleteIfExists(zipPath.toPath());
        } catch (IOException e) {
            log.error(e);
            return;
        }
    }
}
