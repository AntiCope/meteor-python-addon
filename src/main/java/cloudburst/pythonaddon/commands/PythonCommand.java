package cloudburst.pythonaddon.commands;

import cloudburst.pythonaddon.PythonAddon;
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

    private final static SimpleCommandExceptionType IO_EXCEPTION = new SimpleCommandExceptionType(new LiteralText("Couldn't load file"));

    public PythonCommand() {
        super("python", "Execute python files", "py");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("file", StringArgumentType.greedyString()).executes(ctx -> {
            String filepath = StringArgumentType.getString(ctx, "file");
            Path file = PythonAddon.PYTHON_HOME.resolve(filepath);
            String code = "";
            try {
                code = Files.readString(file, StandardCharsets.UTF_8);
            } catch (IOException | NullPointerException e) {
                throw IO_EXCEPTION.create();
            }
            if (code == "") {
                throw IO_EXCEPTION.create();
            }
            load(code);
            return SINGLE_SUCCESS;
        }));
    }

    private void load(String code) {
        OutputStream out_stream = new ByteArrayOutputStream();
        OutputStream err_stream = new ByteArrayOutputStream();
        try ( PythonInterpreter python = new PythonInterpreter() ) {
            python.setErr(err_stream);
            python.setOut(out_stream);
            python.exec(code);
        } catch (Exception e) {
            error(e.getMessage());
            return;
        }
        String out, err;
        out = out_stream.toString();
        err = err_stream.toString();
        if (!err.isEmpty()) {
            error(err);
        }
        if (!out.isEmpty()) {
            info(out);
        }
    }
}
