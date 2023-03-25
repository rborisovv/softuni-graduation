package bg.rborisov.softunigraduation.util.validators;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

public final class ClientIpValidator {
    private static final List<String> PRIVATE_IP_ADDRESS_RANGES = Arrays.asList(
            "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16"
    );

    public static String validateIpAddress(final HttpServletRequest request) {
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

    private static boolean isPrivateIpAddress(String ipAddress) {
        for (String cidr : PRIVATE_IP_ADDRESS_RANGES) {
            if (isIpAddressInRange(ipAddress, cidr)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isIpAddressInRange(String ipAddress, String cidr) {
        String[] parts = cidr.split("/");
        int prefixLength = Integer.parseInt(parts[1]);
        int mask = prefixLength == 32 ? 0xffffffff : (1 << (32 - prefixLength)) - 1;
        int ip = ipToInt(ipAddress);
        int network = ipToInt(parts[0]) & mask;
        return ip == network;
    }

    private static int ipToInt(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        int result = 0;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }
}