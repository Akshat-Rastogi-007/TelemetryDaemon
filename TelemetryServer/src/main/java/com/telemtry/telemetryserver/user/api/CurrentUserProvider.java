package com.telemtry.telemetryserver.user.api;

import com.telemtry.telemetryserver.user.domain.model.User;

public interface CurrentUserProvider {

    User currentUser();

}
