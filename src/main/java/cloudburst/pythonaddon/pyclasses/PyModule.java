package cloudburst.pythonaddon.pyclasses;

import cloudburst.pythonaddon.PythonAddon;
import cloudburst.pythonaddon.PythonSystem;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.util.Locale;

public class PyModule extends meteordevelopment.meteorclient.systems.modules.Module {
    private final PythonInterpreter interpreter;
    private PyObject pymodule = null;

    public PyModule(String filename) {
        super(PythonAddon.CATEGORY, createName(filename), "Python module.");

        interpreter = PythonSystem.INSTANCE.createInterpreter();
        PythonSystem.INSTANCE.execFile(filename, interpreter);
        try {
            pymodule = interpreter.eval("Module()");
        } catch (Exception e) {
            PythonAddon.LOG.error(e);
        }
    }

    @Override
    public void onActivate() {
        try {
            pymodule.__getattr__("on_activate").__call__();
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @Override
    public void onDeactivate() {
        try {
            pymodule.__getattr__("on_deactivate").__call__();
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private static String createName(String name) {
        name = name.toLowerCase(Locale.ROOT);
        name = name.replaceAll("[\s_]+", "-");
        name = name.replace(".py", "");
        name = name.replaceAll("[^a-z0-9-]", "");

        return name;
    }
}
