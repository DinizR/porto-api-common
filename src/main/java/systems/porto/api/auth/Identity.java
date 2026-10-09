package systems.porto.api.auth;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Authenticated subject resolved from a local account or an external IdP token.
 *
 * <p>{@code profiles} are authorization groups (the application may persist them as
 * roles). {@code permissions} are grants through those profiles. {@code *} is
 * all access; {@code cash-flow:*} is every action on that entity.
 */
public record Identity(
    String subject,
    String username,
    String email,
    Set<String> profiles,
    Set<String> permissions,
    Map<String, String> attributes
) {

    public Identity {
        profiles = profiles == null ? Set.of() : Set.copyOf(profiles);
        permissions = compactPermissions(permissions);
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }

    public boolean hasProfile(final String profile) {
        return profile != null && profiles.contains(profile);
    }

    /**
     * {@code *} matches every code. {@code entity:*} matches every action on
     * that entity. Leaf codes match only themselves.
     */
    public boolean hasPermission(final String permission) {
        if (permission == null || permission.isBlank()) {
            return false;
        }
        if (permissions.contains("*") || permissions.contains(permission)) {
            return true;
        }
        int colon = permission.indexOf(':');
        if (colon <= 0) {
            return false;
        }
        return permissions.contains(permission.substring(0, colon) + ":*");
    }

    public boolean passwordExpired() {
        return "true".equalsIgnoreCase(attributes.getOrDefault("password_expired", "false"));
    }

    static Set<String> compactPermissions(final Set<String> granted) {
        if (granted == null || granted.isEmpty()) {
            return Set.of();
        }
        if (granted.contains("*")) {
            return Set.of("*");
        }
        Set<String> entityWildcards = new LinkedHashSet<>();
        for (String code : granted) {
            if (code != null && code.endsWith(":*")) {
                entityWildcards.add(code);
            }
        }
        Set<String> compacted = new LinkedHashSet<>();
        for (String code : granted) {
            if (code == null || code.isBlank()) {
                continue;
            }
            int colon = code.indexOf(':');
            if (colon > 0 && !code.endsWith(":*")
                && entityWildcards.contains(code.substring(0, colon) + ":*")) {
                continue;
            }
            compacted.add(code);
        }
        return Set.copyOf(compacted);
    }
}
