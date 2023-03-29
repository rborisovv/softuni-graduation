package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dao.VoucherRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.PasswordChangeDto;
import bg.rborisov.softunigraduation.dto.UserDto;
import bg.rborisov.softunigraduation.dto.VoucherDto;
import bg.rborisov.softunigraduation.enumeration.NotificationStatus;
import bg.rborisov.softunigraduation.enumeration.RoleEnum;
import bg.rborisov.softunigraduation.events.PasswordResetPublisher;
import bg.rborisov.softunigraduation.exception.AbsentPasswordTokenException;
import bg.rborisov.softunigraduation.exception.PasswordTokenExpiredException;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.util.HttpResponseConstructor;
import bg.rborisov.softunigraduation.util.JwtProvider;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.USERNAME_OR_PASSWORD_INCORRECT;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;
import static bg.rborisov.softunigraduation.common.Messages.PASSWORD_CHANGED_SUCCESSFULLY;
import static bg.rborisov.softunigraduation.common.Messages.PASSWORD_RESET_EMAIL;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordResetPublisher passwordResetPublisher;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final VoucherRepository voucherRepository;

    public AuthService(final UserRepository userRepository, final JwtProvider jwtProvider, final PasswordTokenRepository passwordTokenRepository, final PasswordResetPublisher passwordResetPublisher, final PasswordEncoder passwordEncoder, final ModelMapper modelMapper, final VoucherRepository voucherRepository) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordResetPublisher = passwordResetPublisher;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.voucherRepository = voucherRepository;
    }

    public boolean isAdmin(String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        String username = this.jwtProvider.getSubject(token);

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_OR_PASSWORD_INCORRECT));

        return user.getRole().getName().equalsIgnoreCase(RoleEnum.ADMIN.name());
    }

    public ResponseEntity<HttpResponse> resetPassword(final String email) {
        this.passwordResetPublisher.publishPasswordResetRequest(email);
        HttpResponse httpResponse = HttpResponseConstructor.construct(HttpStatus.OK,
                String.format(PASSWORD_RESET_EMAIL, email), NotificationStatus.SUCCESS.name());

        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public ResponseEntity<HttpResponse> changePassword(final PasswordChangeDto passwordChangeDto) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        preActivePasswordResetRequestCheck(passwordChangeDto.getToken());

        PasswordToken passwordToken = this.passwordTokenRepository.findByToken(passwordChangeDto.getToken()).orElseThrow(AbsentPasswordTokenException::new);
        User user = passwordToken.getUser();
        user.setPassword(this.passwordEncoder.encode(passwordChangeDto.getPassword()));

        HttpResponse httpResponse = HttpResponseConstructor.construct(HttpStatus.OK,
                PASSWORD_CHANGED_SUCCESSFULLY, NotificationStatus.SUCCESS.name());
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    public void preActivePasswordResetRequestCheck(String token) throws AbsentPasswordTokenException, PasswordTokenExpiredException {
        Optional<PasswordToken> optionalPasswordToken = this.passwordTokenRepository.findByToken(token);

        if (optionalPasswordToken.isEmpty()) {
            throw new AbsentPasswordTokenException();
        }
        LocalDateTime expireDate = optionalPasswordToken.get().getExpireDate();

        if (expireDate.isBefore(LocalDateTime.now())) {
            throw new PasswordTokenExpiredException();
        }
    }

    public Boolean hasActivePasswordRequest(String token) {
        Optional<PasswordToken> passwordToken = this.passwordTokenRepository.findByToken(token);

        if (passwordToken.isEmpty()) {
            return false;
        }

        LocalDateTime expireDate = passwordToken.get().getExpireDate();
        return expireDate.isAfter(LocalDateTime.now());
    }

    public Set<UserDto> loadAllUsers() {
        return this.userRepository.findAll().stream().map(user -> {
            UserDto userDto = this.modelMapper.map(user, UserDto.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String birthDate = formatter.format(user.getBirthDate());
            userDto.setBirthDate(birthDate);

            return userDto;
        }).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<VoucherDto> fetchAllVouchers() {
        return this.voucherRepository.findAll().stream().map(voucher -> {
                    final VoucherDto voucherDto = this.modelMapper.map(voucher, VoucherDto.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    voucherDto.setCreationDate(formatter.format(voucher.getCreationDate()));
                    voucherDto.setExpirationDate(formatter.format(voucher.getExpirationDate()));
                    return voucherDto;
                }).sorted(Comparator.comparing(VoucherDto::getCreationDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
