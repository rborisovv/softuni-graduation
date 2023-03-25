package bg.rborisov.softunigraduation.service;

import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class LoginAttemptService {
    private static final int MAXIMUM_NUMBER_OF_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private final LoadingCache<String, Integer> loginAttemptCache;

    private final List<String> PRIVATE_IP_ADDRESS_RANGES = Arrays.asList(
            "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16"
    );

    public LoginAttemptService(LoadingCache<String, Integer> loginAttemptCache) {
        this.loginAttemptCache = loginAttemptCache;
    }

    public void evictUserFromLoginAttemptCache(final HttpServletRequest request) {
        String clientIpAddress = validateIpAddress(request);
        loginAttemptCache.invalidate(clientIpAddress);
    }

    public void addUserToLoginAttemptCache(final HttpServletRequest request) {
        int attempts;
        String clientIpAddress = validateIpAddress(request);
        try {
            attempts = ATTEMPT_INCREMENT + loginAttemptCache.get(clientIpAddress);
            loginAttemptCache.put(clientIpAddress, attempts);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasExceededMaxAttempts(final HttpServletRequest request) {
        try {
            String clientIpAddress = this.validateIpAddress(request);
            return loginAttemptCache.get(clientIpAddress) >= MAXIMUM_NUMBER_OF_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String validateIpAddress(final HttpServletRequest request) {
        final int MAX_IP_ADDRESSES = 3;
        final String IP_ADDRESS_REGEX = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress != null) {
            List<String> ipAddresses = Arrays.stream(ipAddress.split("[,\\s]+"))
                    .filter(ip -> ip.matches(IP_ADDRESS_REGEX))
                    .filter(ip -> !isPrivateIpAddress(ip))
                    .limit(MAX_IP_ADDRESSES)
                    .toList();
            if (!ipAddresses.isEmpty()) {
                ipAddress = ipAddresses.get(0);
            } else {
                ipAddress = request.getRemoteAddr();
            }
        } else {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    private boolean isPrivateIpAddress(String ipAddress) {
        for (String cidr : PRIVATE_IP_ADDRESS_RANGES) {
            if (isIpAddressInRange(ipAddress, cidr)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIpAddressInRange(String ipAddress, String cidr) {
        String[] parts = cidr.split("/");
        int prefixLength = Integer.parseInt(parts[1]);
        int mask = prefixLength == 32 ? 0xffffffff : (1 << (32 - prefixLength)) - 1;
        int ip = ipToInt(ipAddress);
        int network = ipToInt(parts[0]) & mask;
        return ip == network;
    }

    private int ipToInt(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        int result = 0;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }
}