package com.telemtry.telemetryserver.user.application.pat;

import com.telemtry.telemetryserver.common.exception.InternalResourceCorruptionError;
import com.telemtry.telemetryserver.common.exception.ResourceNotFoundException;
import com.telemtry.telemetryserver.common.infrastructure.hash.Hasher;
import com.telemtry.telemetryserver.common.infrastructure.secureKeyGenerator.SecureKeyGenerator;
import com.telemtry.telemetryserver.user.api.CurrentUserProvider;
import com.telemtry.telemetryserver.user.api.request.PersonalAccessTokenRequestDto;
import com.telemtry.telemetryserver.user.api.response.PersonalAccessTokenResponseDto;
import com.telemtry.telemetryserver.user.application.user.UserService;
import com.telemtry.telemetryserver.user.domain.model.PersonalAccessToken;
import com.telemtry.telemetryserver.user.domain.model.User;
import com.telemtry.telemetryserver.user.infrastructure.repository.pat.JpaPatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalAccessTokenService {

    private final JpaPatRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final Hasher hasher;
    private final SecureKeyGenerator secureKeyGenerator;


    public PersonalAccessTokenService(JpaPatRepository repository,
                                      @Qualifier("userSecurityCurrentUserProvider")
                                      CurrentUserProvider currentUserProvider,
                                      @Qualifier("sha256Hasher")
                                      Hasher hasher,
                                      @Qualifier("secureRandomKeyGenerator")
                                      SecureKeyGenerator secureKeyGenerator) {
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.hasher = hasher;
        this.secureKeyGenerator = secureKeyGenerator;
    }


    @Transactional
    public String createToken(PersonalAccessTokenRequestDto dto) {

        PersonalAccessToken personalAccessToken = mapToToken(dto);

        User user = currentUserProvider.currentUser();

        personalAccessToken.setOwner(user);

        String patKey = "pat_" + secureKeyGenerator.generateSecureKey().replace("-", "");

        String hashedKey = hasher.getHash(patKey);

        personalAccessToken.setTokenHash(hashedKey);

        repository.save(personalAccessToken);

        return patKey;
    }

    public List<PersonalAccessTokenResponseDto> mapToResponseDto(List<PersonalAccessToken> tokenList){

        return tokenList.stream().map(

                this::mapToResponseToken

        ).toList();

    }

    public Optional<PersonalAccessToken> findByTokenHashWithOwner(String token) {

        String hashedToken = hasher.getHash(token);

        return repository.findByTokenHashWithOwner(hashedToken);

    }

    public boolean isExpiredOrRevoked(PersonalAccessToken personalAccessToken){

        return personalAccessToken.getRevoked() || personalAccessToken.getExpirationDateAndTime().isBefore(LocalDateTime.now());

    }


    private PersonalAccessTokenResponseDto mapToResponseToken(PersonalAccessToken token){

       return new PersonalAccessTokenResponseDto(
                token.getPublicId(),
                token.getTokenName(),
                token.getCreationDate(),
                token.getExpirationDateAndTime(),
                token.getRevoked()

        );


    }

    private PersonalAccessToken mapToToken(PersonalAccessTokenRequestDto dto ){

        PersonalAccessToken personalAccessToken = new PersonalAccessToken();

        personalAccessToken.setTokenName(dto.getTokenName());
        personalAccessToken.setExpirationDateAndTime(dto.getExpirationDate());
        personalAccessToken.setRevoked(Boolean.FALSE);

        return personalAccessToken;

    }



    public void revokePat(String tokenId) {


        PersonalAccessToken personalAccessToken = repository.findByPublicId(tokenId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("PAT token with id " + tokenId + " not found")
                );

        personalAccessToken.setRevoked(Boolean.TRUE);
        repository.save(personalAccessToken);
    }
}
