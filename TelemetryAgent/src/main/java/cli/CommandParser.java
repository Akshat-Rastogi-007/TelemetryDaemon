package cli;


import cli.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {

    private final Map<String, CommandLineFactory> commands = new HashMap<>();

    public CommandParser() {

        register("start", StartCommand::new);
        register("version", args -> new VersionCommand());
        register("help", args -> new HelpCommand());

    }

    private void register(String command, CommandLineFactory factory) {
        commands.put(command, factory);
    }

    public Command parse(String[] args){

        if (args.length == 0)
            return new HelpCommand();

        String commandName = args[0].toLowerCase();

        CommandLineFactory factory = commands.get(commandName);

        if (factory == null)
            return new HelpCommand();


        return factory.create(args);

    }

}
