package cloudburst.pythonaddon.pyclasses;

import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import org.python.core.*;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PyCommand extends Command {
    private final PyObject pycommand;
    private final static DynamicCommandExceptionType PYTHON_EXCEPTION = new DynamicCommandExceptionType(o -> new LiteralText(o.toString()));

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

    private static PyObject getArgument(CommandContext<CommandSource> ctx, String name, ArgumentType<?> type) {
        if (type instanceof StringArgumentType) {
            return Py.newString(StringArgumentType.getString(ctx, name));
        } else if (type instanceof IntegerArgumentType) {
            return Py.newInteger(IntegerArgumentType.getInteger(ctx, name));
        } else if (type instanceof FloatArgumentType) {
            return Py.newFloat(FloatArgumentType.getFloat(ctx, name));
        } else if (type instanceof BoolArgumentType) {
            return Py.newBoolean(BoolArgumentType.getBool(ctx, name));
        }
        return Py.None;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        try {
            for (Object arg : (PyList) this.pycommand.__getattr__("args")) {
                PyTuple targ = (PyTuple) arg;
                ArgumentType<Object> type = (ArgumentType<Object>) targ.get(1);
                RequiredArgumentBuilder<CommandSource, ?> builder1 = RequiredArgumentBuilder.argument(targ.get(0).toString(), type);
                builder.then(builder1);
            }
            builder.executes(ctx -> {
                PyList args = new PyList();
                for (Object arg : (PyList) this.pycommand.__getattr__("args")) {
                    PyTuple targ = (PyTuple) arg;
                    ArgumentType<?> type = (ArgumentType<?>) targ.get(1);
                    args.append(getArgument(ctx, targ.get(0).toString(), type));
                }
                pycommand.__getattr__("execute_command").__call__(args);
                return SINGLE_SUCCESS;
            });
        } catch (Exception e) {
            try {
                throw PYTHON_EXCEPTION.create(e.getMessage());
            } catch (CommandSyntaxException commandSyntaxException) {
                commandSyntaxException.printStackTrace();
            }
        }

    }
}
