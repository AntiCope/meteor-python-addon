package cloudburst.pythonaddon.python;

import cloudburst.pythonaddon.PythonAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;

public class ModuleBase extends Module {
    public ModuleBase(String name, String description) {
        super(PythonAddon.CATEGORY, name, description);
    }
}
