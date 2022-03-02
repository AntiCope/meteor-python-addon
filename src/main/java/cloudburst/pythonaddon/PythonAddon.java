package cloudburst.pythonaddon;

import cloudburst.pythonaddon.commands.PythonCommand;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.MeteorClient;

import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class PythonAddon extends MeteorAddon {

    public static final Logger LOG = LoggerFactory.getLogger("PythonAddon");
    public static final Category CATEGORY = new Category("Python", Items.YELLOW_WOOL.getDefaultStack());

    @Override
    public void onInitialize() {
        LOG.info("Initializing Python Addon");

        MeteorClient.EVENT_BUS.registerLambdaFactory("cloudburst.pythonaddon", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

        PythonSystem.INSTANCE.init();

        Commands.get().add(new PythonCommand());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

}
