package identity.source.file;

import exceptions.IdentityException;
import identity.AgentIdentity;
import identity.source.IdentitySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class FileIdentitySource implements IdentitySource {

    private final Path identityDirectory;
    private final Path identityFile;

    public FileIdentitySource(Path identityDirectory) {
        this.identityDirectory = identityDirectory;
        this.identityFile = identityDirectory.resolve("agent.identity");    }

    @Override
    public void load(AgentIdentity agentIdentity) {

        if (!Files.exists(identityFile)) {
            throw new IdentityException("Identity file does not exist.");
        }


        Properties properties = new Properties();

        try(InputStream inputStream = Files.newInputStream(identityFile)){

            properties.load(inputStream);

            String installationId = properties.getProperty("installationId");

            String agentId = properties.getProperty("agentId");

            String agentToken = properties.getProperty("agentToken");


            agentIdentity.setInstallationId(installationId);
            agentIdentity.setAgentId(agentId);
            agentIdentity.setAgentToken(agentToken);
        }

        catch (Exception e){

            e.printStackTrace();

            throw new IdentityException("Failed to load identity.");

        }

    }

    @Override
    public void save(AgentIdentity identity) {

        try {


            Files.createDirectories(identityDirectory);
            Properties properties = new Properties();

            properties.setProperty("installationId", identity.getInstallationId());

            properties.setProperty("agentId", identity.getAgentId() == null ? " " : identity.getAgentId() );

            properties.setProperty("agentToken", identity.getAgentToken() == null ? " " : identity.getAgentToken() );



            try (OutputStream outputStream = Files.newOutputStream(identityFile)){

                properties.store(outputStream,"TELEMETRY AGENT IDENTITY");
            }


        }
        catch (Exception exception){

            exception.printStackTrace();


            throw new IdentityException(
                    "Failed to save agent identity."
            );

        }

    }



    @Override
    public boolean exists() {
        return Files.exists(identityFile);
    }

    @Override
    public void delete() {

        try {

            Files.deleteIfExists(identityFile);

        } catch (IOException exception) {

            exception.printStackTrace();

            throw new IdentityException(
                    "Failed to delete identity file."
            );

        }
    }
}
