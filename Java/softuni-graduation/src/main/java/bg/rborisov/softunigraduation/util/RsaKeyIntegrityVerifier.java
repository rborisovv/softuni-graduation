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
        final byte[] privateRsaKeyBytes = Files.readAllBytes(Path.of(this.privateKeyPath.getURI()));
        final byte[] publicRsaKeyBytes = Files.readAllBytes(Path.of(this.publicKeyPath.getURI()));
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        final byte[] privateRsaKeyHash = messageDigest.digest(privateRsaKeyBytes);
        final byte[] publicRsaKeyHash = messageDigest.digest(publicRsaKeyBytes);

        final String privateRsaKeySha256 = Files.readString(Path.of(this.resourceLoader
                .getResource("classpath:keys/private_key.sha256").getURI())).trim();

        final String publicRsaKeySha256 = Files.readString(Path.of(this.resourceLoader
                .getResource("classpath:keys/public_key.sha256").getURI())).trim();

        if (!privateRsaKeySha256.equals(bytesToHex(privateRsaKeyHash))) {
            throw new RsaKeyIntegrityViolationException();
        }

        if (!publicRsaKeySha256.equals(bytesToHex(publicRsaKeyHash))) {
            throw new RsaKeyIntegrityViolationException();
        }
    }

    public static String bytesToHex(final byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0f];
        }
        return new String(hexChars);
    }
}