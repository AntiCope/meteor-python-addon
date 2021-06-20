package cloudburst.pythonaddon.python;

import meteordevelopment.meteorclient.systems.modules.Modules;

public class ModuleFactory {
    public static void createModule(String name, String description) {
        ModuleBase module = new ModuleBase(name, description);
        Modules.get().add(module);
    }
}
