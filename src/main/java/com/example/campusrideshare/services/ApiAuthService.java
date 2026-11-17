package com.example.campusrideshare.services;


import com.example.campusrideshare.api.LoginRequestDTO;
import com.example.campusrideshare.api.TokenResponseDTO;
import com.example.campusrideshare.config.JwtProperties;
import com.example.campusrideshare.data.TokenRepository;
import com.example.campusrideshare.domain.ApiToken;
import com.example.campusrideshare.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ApiAuthService {

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public ApiAuthService(
            TokenRepository tokenRepository,
            AuthenticationManager authenticationManager,
            JwtEncoder jwtEncoder,
            JwtProperties jwtProperties) {


        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public TokenResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        User user = (User) authentication.getPrincipal();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtProperties.getTokenDuration());

        UUID tokenId = UUID.randomUUID();

        List<String> roles = user.getAuthorities().stream().map(authority ->
                authority.getAuthority()).toList();

        JwtClaimsSet claims = JwtClaimsSet.builder().issuer(jwtProperties.getIssuer())
                .subject(user.getUsername())
                .audience(List.of(jwtProperties.getAudience()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(tokenId.toString())
                .claim("userId",user.getId().toString())
                .claim("roles", roles)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();

        ApiToken tokenRecord = new ApiToken(tokenId, user, issuedAt, expiresAt, null);
        tokenRepository.save(tokenRecord);

        return new TokenResponseDTO(tokenValue, expiresAt);
    }

    @Transactional
    public boolean revokeToken(UUID tokenId) {
        Optional<ApiToken> lookupToken = tokenRepository.findById(tokenId);

        if (lookupToken.isEmpty()) {
            return false;
        }

        ApiToken token = lookupToken.get();
        token.revoke();

        tokenRepository.save(token);

        return true;
    }

}
