package identity.bootstrap;

import identity.AgentIdentity;
import identity.loader.IdentityLoader;
import identity.service.IdentityService;
import identity.source.file.FileIdentitySource;

import java.nio.file.Path;

public class IdentityBootstrap {


    public static IdentityService initialize(){

        FileIdentitySource identitySource = new FileIdentitySource(

                Path.of(
                        System.getProperty("user.home"),
                        ".telemetry")

        );

        IdentityLoader identityLoader = new IdentityLoader(identitySource);

        IdentityService identityService = new IdentityService(identityLoader, identitySource);


        identityService.initialize();

        return identityService;

    }


}

