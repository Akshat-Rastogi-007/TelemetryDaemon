package cli.command;

public class StartCommand implements Command{

    private final String[] args;

    public StartCommand(String[] args) {
        this.args = args;
    }

    @Override
    public void execute() {
        // start command here
    }


}
