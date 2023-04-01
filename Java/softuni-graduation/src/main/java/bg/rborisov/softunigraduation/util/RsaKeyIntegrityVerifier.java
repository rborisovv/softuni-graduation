package bg.rborisov.softunigraduation.util;

import bg.rborisov.softunigraduation.exception.RsaKeyIntegrityViolationException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Component
public final class RsaKeyIntegrityVerifier {
    private final ResourceLoader resourceLoader;
    private final Resource privateKeyPath;
    private final Resource publicKeyPath;

    public RsaKeyIntegrityVerifier(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.privateKeyPath = this.resourceLoader.getResource("classpath:keys/private_key.pem");
        this.publicKeyPath = this.resourceLoader.getResource("classpath:keys/public_key.pem");
    }

    public void verifyRsaKeysIntegrity() throws IOException, NoSuchAlgorithmException, RsaKeyIntegrityViolationException {
        byte[] privateRsaKeyBytes = Files.readAllBytes(Path.of(this.privateKeyPath.getURI()));
        byte[] publicRsaKeyBytes = Files.readAllBytes(Path.of(this.publicKeyPath.getURI()));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] privateRsaKeyHash = digest.digest(privateRsaKeyBytes);
        byte[] publicRsaKeyHash = digest.digest(publicRsaKeyBytes);

        byte[] privateRsaKeySha256 = Files.readAllBytes(Path.of(this.resourceLoader
                .getResource("classpath:keys/private_key.sha256").getURI()));

        byte[] publicRsaKeySha256 = Files.readAllBytes(Path.of(this.resourceLoader
                .getResource("classpath:keys/public_key.sha256").getURI()));

        if (!Arrays.equals(publicRsaKeyHash, publicRsaKeySha256)){
            throw new RsaKeyIntegrityViolationException();
        }

        if (!Arrays.equals(privateRsaKeyHash, privateRsaKeySha256)) {
            throw new RsaKeyIntegrityViolationException();
        }
    }
}