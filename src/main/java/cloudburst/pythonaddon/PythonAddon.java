package cloudburst.pythonaddon;

import cloudburst.pythonaddon.commands.PythonCommand;
import cloudburst.pythonaddon.utils.PathUtils;
import meteordevelopment.meteorclient.MeteorAddon;
import meteordevelopment.meteorclient.MeteorClient;

import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

public class PythonAddon extends MeteorAddon {

    public static final Logger LOG = LogManager.getLogger();
    public static final Path PYTHON_HOME = MeteorClient.FOLDER.toPath().resolve("python");
    public static final Category CATEGORY = new Category("Python", Items.YELLOW_WOOL.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing Python Addon");

        MeteorClient.EVENT_BUS.registerLambdaFactory("cloudburst.pythonaddon", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        PathUtils.createPythonHome();

        Commands.get().add(new PythonCommand());
    }

    @Override
    public void onRegisterCategories() {
        if (!PYTHON_HOME.toFile().isDirectory()) return;
        try {
            if (PathUtils.isEmpty(PYTHON_HOME)) return;
        } catch (IOException e) {
            return;
        }

        Modules.registerCategory(CATEGORY);
    }

}
