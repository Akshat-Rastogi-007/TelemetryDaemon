import cli.CommandParser;
import cli.command.Command;

public class TelemetryAgent {

    public static void main(String[] args) {

        CommandParser parser = new CommandParser();

        Command command = parser.parse(args);

        command.execute();

    }
}
