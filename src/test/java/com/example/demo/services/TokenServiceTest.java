package com.example.demo.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.ApplicationConfigTest;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TokenServiceTest extends ApplicationConfigTest {

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${token.expiration}")
    private long tokenExpiration;
    @Value("${timezone.offset}")
    private String timezoneOffSet;

    User USER_RECORD = User.builder()
            .name("name")
            .email("email")
            .password("password")
            .role(Role.PAYEE)
            .CPF("cpf")
            .build();

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(USER_RECORD, "id", UUID.randomUUID());
    }

    @Test
    void create_givenUser_shouldGenerateToken() {
        String token = tokenService.generateToken(USER_RECORD);

        assertThat(token).isNotNull();
    }

    @Test
    void create_givenCorrectClaims_shouldGenerateTokenWithValidClaims() {
        String token = tokenService.generateToken(USER_RECORD);

        DecodedJWT decodedToken = JWT.decode(token);

        assertThat(decodedToken.getSubject()).isEqualTo(USER_RECORD.getEmail());
        assertThat(decodedToken.getClaim("id").asString())
                .isEqualTo(USER_RECORD.getId().toString());
    }

    @Test
    void create_givenCorrectExpiration_shouldGenerateTokenWithValidClaims () {
        String token = tokenService.generateToken(USER_RECORD);

        DecodedJWT decodedToken = JWT.decode(token);
        Date expiration = decodedToken.getExpiresAt();
        Date expectedExpiration = Date.from(LocalDateTime.now().plusSeconds(tokenExpiration)
                .toInstant(ZoneOffset.of(timezoneOffSet)));

        long toleranceMilliseconds = 1000;
        long expirationTime = expiration.getTime();
        long expectedExpirationTime = expectedExpiration.getTime();

        assertThat(expirationTime).isCloseTo(expectedExpirationTime, within(toleranceMilliseconds));
    }

    @Test
    void create_givenCorrectSignature_shouldGenerateTokenWithValidClaims() {
        String token = tokenService.generateToken(USER_RECORD);

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }

    @Test
    void getSubject_givenUser_shouldReturnTheSubject() {
        String token = JWT.create()
                .withIssuer("JWT Issuer")
                .withSubject(USER_RECORD.getEmail())
                .withClaim("id", USER_RECORD.getId().toString())
                .withExpiresAt(LocalDateTime.now()
                        .plusDays(1)
                        .toInstant(ZoneOffset.of(timezoneOffSet))
                ).sign(Algorithm.HMAC256(jwtSecret));

        String retrievedSubject = tokenService.getSubject(token);
        assertThat(retrievedSubject).isEqualTo(USER_RECORD.getEmail());
    }
}