package bg.rborisov.softunigraduation.service;

import bg.rborisov.softunigraduation.dao.CategoryRepository;
import bg.rborisov.softunigraduation.dao.PasswordTokenRepository;
import bg.rborisov.softunigraduation.dao.UserRepository;
import bg.rborisov.softunigraduation.dao.VoucherRepository;
import bg.rborisov.softunigraduation.domain.HttpResponse;
import bg.rborisov.softunigraduation.dto.PasswordChangeDto;
import bg.rborisov.softunigraduation.dto.UserDto;
import bg.rborisov.softunigraduation.dto.VoucherDto;
import bg.rborisov.softunigraduation.enumeration.NotificationStatus;
import bg.rborisov.softunigraduation.enumeration.RoleEnum;
import bg.rborisov.softunigraduation.enumeration.VoucherTypeEnum;
import bg.rborisov.softunigraduation.events.PasswordResetPublisher;
import bg.rborisov.softunigraduation.exception.AbsentPasswordTokenException;
import bg.rborisov.softunigraduation.exception.PasswordTokenExpiredException;
import bg.rborisov.softunigraduation.exception.UserNotFoundException;
import bg.rborisov.softunigraduation.exception.VoucherByNameAlreadyPresent;
import bg.rborisov.softunigraduation.model.Category;
import bg.rborisov.softunigraduation.model.PasswordToken;
import bg.rborisov.softunigraduation.model.User;
import bg.rborisov.softunigraduation.model.Voucher;
import bg.rborisov.softunigraduation.util.HttpResponseConstructor;
import bg.rborisov.softunigraduation.util.JwtProvider;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static bg.rborisov.softunigraduation.common.ExceptionMessages.USERNAME_OR_PASSWORD_INCORRECT;
import static bg.rborisov.softunigraduation.common.JwtConstants.TOKEN_PREFIX;
import static bg.rborisov.softunigraduation.common.Messages.PASSWORD_CHANGED_SUCCESSFULLY;
import static bg.rborisov.softunigraduation.common.Messages.PASSWORD_RESET_EMAIL;
import static bg.rborisov.softunigraduation.common.Messages.VOUCHER_CREATED_SUCCESSFULLY;

@Service
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordResetPublisher passwordResetPublisher;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final VoucherRepository voucherRepository;
    private final CategoryRepository categoryRepository;
    private final CacheManager cacheManager;

    public AuthService(final UserRepository userRepository, final JwtProvider jwtProvider, final PasswordTokenRepository passwordTokenRepository, final PasswordResetPublisher passwordResetPublisher, final PasswordEncoder passwordEncoder, final ModelMapper modelMapper, final VoucherRepository voucherRepository, final CategoryRepository categoryRepository, final CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordResetPublisher = passwordResetPublisher;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.voucherRepository = voucherRepository;
        this.categoryRepository = categoryRepository;
        this.cacheManager = cacheManager;
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

        final Cache optionalCacheModel = this.cacheManager.getCache("userDetails");

        if (optionalCacheModel != null) {
            optionalCacheModel.evict(user.getUsername());
        }

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
                    voucherDto.setCreationDate(voucher.getCreationDate());
                    voucherDto.setExpirationDate(voucher.getExpirationDate());
                    return voucherDto;
                })
                .sorted(Comparator.comparing(VoucherDto::getCreationDate).thenComparing(VoucherDto::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public ResponseEntity<HttpResponse> createVoucher(final VoucherDto voucherDto) throws VoucherByNameAlreadyPresent, UserNotFoundException {
        User user;
        final Optional<Voucher> optionalVoucher = this.voucherRepository.findVoucherByName(voucherDto.getName());

        if (optionalVoucher.isPresent()) {
            throw new VoucherByNameAlreadyPresent();
        }

        final Voucher voucher = this.modelMapper.map(voucherDto, Voucher.class);
        voucher.setCreationDate(LocalDate.now());
        voucher.setType(VoucherTypeEnum.valueOf(voucherDto.getType()));

        if (voucherDto.getUser() != null) {
            final String username = voucherDto.getUser().getUsername();
            user = this.userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

            if (user.getVouchers() == null) {
                user.setVouchers(new HashSet<>());
            }

            user.getVouchers().add(voucher);
            voucher.setUser(user);
        }

        if (voucher.getCategory() != null) {
            final Optional<Category> optionalCategory;
            optionalCategory = this.categoryRepository.findCategoryByIdentifier(voucherDto.getCategory().getIdentifier());
            optionalCategory.ifPresent(voucher::setCategory);
        }

        this.voucherRepository.save(voucher);

        HttpResponse response = HttpResponseConstructor.construct(HttpStatus.OK,
                String.format(VOUCHER_CREATED_SUCCESSFULLY, voucher.getName()), NotificationStatus.SUCCESS.name());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}