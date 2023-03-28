package bg.rborisov.softunigraduation.config;

import bg.rborisov.softunigraduation.util.LoginCacheModel;
import bg.rborisov.softunigraduation.util.RsaKeyProviderFactory;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static bg.rborisov.softunigraduation.common.JwtConstants.JWT_ALGORITHM;
import static java.util.concurrent.TimeUnit.MINUTES;

@Configuration
public class ApplicationBeanConfiguration {
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String TEMPLATE_RESOLVER_PREFIX = "classpath:/templates/";
    private static final String TEMPLATE_RESOLVER_SUFFIX = ".html";

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix(TEMPLATE_RESOLVER_PREFIX);
        templateResolver.setSuffix(TEMPLATE_RESOLVER_SUFFIX);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(CHARACTER_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public RSAKeyProvider rsaKeyProvider() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        final byte[] privateKeyBytes = Files.readAllBytes(Paths.get("/home/dev/Desktop/demo/softuni-graduation/Java/softuni-graduation/src/main/resources/keys/private_key.pem"));
        final byte[] publicKeyBytes = Files.readAllBytes(Paths.get("/home/dev/Desktop/demo/softuni-graduation/Java/softuni-graduation/src/main/resources/keys/public_key.pem"));

        final byte[] decodedPrivateKey = decodeRsaKeyContent(privateKeyBytes);
        final byte[] decodedPublicKey = decodeRsaKeyContent(publicKeyBytes);
        final PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(decodedPrivateKey);
        final X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(decodedPublicKey);

        final KeyFactory keyFactory = KeyFactory.getInstance(JWT_ALGORITHM);
        final RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateSpec);
        final RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicSpec);
        return new RsaKeyProviderFactory(privateKey, publicKey);
    }

    @Bean
    @SuppressWarnings("all")
    public LoadingCache<String, LoginCacheModel<Integer>> loginAttemptCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(15, MINUTES)
                .maximumSize(100)
                .build(new CacheLoader<>() {
                    @Override
                    public LoginCacheModel<Integer> load(String key) {
                        return new LoginCacheModel<>(0);
                    }
                });
    }

    private byte[] decodeRsaKeyContent(final byte[] rsaKeyBytes) {
        final String rsaKeyContent = new String(rsaKeyBytes, StandardCharsets.UTF_8)
                .replaceAll("\\n+", StringUtils.EMPTY)
                .replace("-----BEGIN PRIVATE KEY-----", StringUtils.EMPTY)
                .replace("-----BEGIN PUBLIC KEY-----", StringUtils.EMPTY)
                .replace("-----END PRIVATE KEY-----", StringUtils.EMPTY)
                .replace("-----END PUBLIC KEY-----", StringUtils.EMPTY);

        return Base64.getDecoder().decode(rsaKeyContent);
    }
}