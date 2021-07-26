package cloudburst.pythonaddon.pyclasses;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import org.python.core.*;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PyCommand extends Command {
    private final PyObject pycommand;

    public PyCommand(PyObject pycommand) {
        super(getName(pycommand), getDescription(pycommand));
        this.pycommand = pycommand;
    }

    private static String getName(PyObject pycommand) {
        try {
            return ((PyString) (pycommand.__getattr__("name"))).toString();
        } catch (Exception e) {
            try {
                return ((PyString) (pycommand.__getattr__("__class__").__getattr__("__name__"))).toString();
            } catch (Exception e1) {
                return "pycmd";
            }
        }
    }

    private static String getDescription(PyObject pycommand) {
        try {
            return ((PyString) (pycommand.__getattr__("description"))).toString();
        } catch (Exception e) {
            return "Python command.";
        }
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            try {
                pycommand.__getattr__("execute_command").__call__();
            } catch (PyException e) {
                error(e.getMessage());
            }
            return SINGLE_SUCCESS;
        });
        builder.then(argument("args", StringArgumentType.greedyString()).executes(ctx -> {
            try {
                pycommand.__getattr__("execute_command").__call__(Py.newString(StringArgumentType.getString(ctx, "args")));
            } catch (PyException e) {
                error(e.getMessage());
            }
            return SINGLE_SUCCESS;
        }));

    }

}
