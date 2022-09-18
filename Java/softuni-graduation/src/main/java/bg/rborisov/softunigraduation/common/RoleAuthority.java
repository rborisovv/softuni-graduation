package bg.rborisov.softunigraduation.common;

import java.util.Set;

import static bg.rborisov.softunigraduation.enumeration.AuthorityEnum.*;

public class RoleAuthority {
    public static final Set<String> USER_AUTHORITIES = Set.of(READ.name(), CREATE.name(), UPDATE.name(), DELETE.name());
}