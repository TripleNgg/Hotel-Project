package org.tripleng.likesidehotel.service;

import org.tripleng.likesidehotel.model.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    Optional<ConfirmationToken> getToken(String token);

    void setConfirmedAt(String token);
}
