package com.account.account;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    public String username;
    
    public String email;

    private byte[] password;

    private byte[] salt;

    private static byte[] pepper = HexFormat.of().parseHex("2f3e4322181546e1965d41d79f2bfc5f5c8ce745ca07585d");

    public Account() {
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.salt = generateSalt16Byte();
        this.password = Hash(password);
    }

    public boolean isPassCorrect(String pass){
        return (base64Encoding(this.password).equals(base64Encoding(Hash(pass))));
    }

    private byte[] Hash(String toHash){
        Instant start = Instant.now();

        int opsLimit = 10;
        int memLimit = 66536;
        int outputLength = 32;
        int parallelism = 1;
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(opsLimit)
                .withMemoryAsKB(memLimit)
                .withParallelism(parallelism)
                .withSalt(salt)
                .withSecret(pepper);
        Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(toHash.getBytes(StandardCharsets.UTF_8), result, 0, result.length);

        Instant end = Instant.now();
        System.out.println(base64Encoding(result));
        System.out.println(String.format("Hashing took %s ms", ChronoUnit.MILLIS.between(start, end)));

        return result;
    }

    private byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private String base64Encoding(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }
}
