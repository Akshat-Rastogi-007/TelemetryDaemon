@ApplicationModule(
        allowedDependencies = {"user::api", "common", "agent :: api"}
)
package com.telemtry.telemetryserver.security;

import org.springframework.modulith.ApplicationModule;