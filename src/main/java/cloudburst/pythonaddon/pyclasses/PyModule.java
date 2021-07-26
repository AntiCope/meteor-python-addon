package cloudburst.pythonaddon.pyclasses;

import cloudburst.pythonaddon.PythonAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import org.python.core.*;


public class PyModule extends Module {
    private final PyObject pymodule;

    public PyModule(PyObject pymodule) {
        super(PythonAddon.CATEGORY, getName(pymodule), getDescription(pymodule));
        this.pymodule = pymodule;
    }

    @Override
    public void onActivate() {
        try {
            pymodule.__getattr__("on_activate").__call__();
        } catch (PyException e) {
            error(e.getMessage());
        }
    }

    @Override
    public void onDeactivate() {
        try {
            pymodule.__getattr__("on_deactivate").__call__();
        } catch (PyException e) {
            error(e.getMessage());
        }
    }

    private static String getName(PyObject pymodule) {
        try {
            return ((PyString)(pymodule.__getattr__("name"))).toString();
        } catch (PyException e) {
            try {
                return ((PyString)(pymodule.__getattr__("__class__").__getattr__("__name__"))).toString();
            } catch (PyException e1) {
                return "python-module";
            }
        }
    }

    private static String getDescription(PyObject pymodule) {
        try {
            return ((PyString)(pymodule.__getattr__("description"))).toString();
        } catch (PyException e) {
            return "Python module.";
        }
    }
}
