package cloudburst.pythonaddon.commands;

import cloudburst.pythonaddon.PythonAddon;
import cloudburst.pythonaddon.PythonSystem;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class PythonCommand extends Command {

    public PythonCommand() {
        super("python", "Execute python files", "py");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("file", StringArgumentType.greedyString()).executes(ctx -> {
            String filepath = StringArgumentType.getString(ctx, "file");
            Thread t = new Thread(() -> {
                PythonSystem.INSTANCE.execFile(filepath);
            });
            t.start();
            return SINGLE_SUCCESS;
        }));
    }

}
