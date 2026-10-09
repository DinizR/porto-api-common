package systems.porto.api.auth;

import java.util.Set;

/**
 * Resolves authorization groups and permission codes for a subject.
 *
 * <p>Applications that already persist profiles/roles (for example shine-media
 * {@code roles}, {@code user_roles}, {@code role_permissions}) implement this
 * as a client adapter. External IdP plugins may instead take groups from token claims.
 */
public interface AuthorizationSource {

    Set<String> profilesFor(String subject);

    Set<String> permissionsFor(String subject);
}
