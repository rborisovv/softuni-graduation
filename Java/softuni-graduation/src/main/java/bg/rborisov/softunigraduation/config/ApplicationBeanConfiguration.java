package bg.rborisov.softunigraduation.config;

import bg.rborisov.softunigraduation.util.LoginCacheModel;
import bg.rborisov.softunigraduation.util.RsaKeyProviderFactory;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
    public RSAKeyProvider rsaKeyProvider() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(JWT_ALGORITHM);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return new RsaKeyProviderFactory(privateKey, publicKey);
    }

    @Bean
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
}