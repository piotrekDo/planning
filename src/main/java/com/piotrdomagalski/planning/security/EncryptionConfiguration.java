package com.piotrdomagalski.planning.security;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Used by Spring Security to encrypt passwords. Algorithm is using 'secret' fraze- in this case an enormous prime number.
 *
 * In this case I understand that it is generally a bad idea to store secret fraze hardcoded.
 * I am looking forward for your support to solve this in a better way. Security might become my obsession some day.
 */

@Configuration
public class EncryptionConfiguration {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(
                "531137992816767098689588206552468627329593117727031923199444138200403559860852242739162502265229285668889329486246501015346579337652707239409519978766587351943831270835393219031728127".getBytes());
    }
}
