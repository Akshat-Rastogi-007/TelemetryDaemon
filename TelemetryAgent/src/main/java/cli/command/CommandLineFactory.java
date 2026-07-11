package cli.command;

@FunctionalInterface
public interface CommandLineFactory {

    Command create(String[] args);

}
