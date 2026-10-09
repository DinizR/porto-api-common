package systems.porto.api.auth;

import java.util.Optional;

/**
 * Local login accounts. Used by a {@link IdentityProviderKind#LOCAL} provider.
 * External IdP plugins typically do not implement this.
 *
 * <p>Secret hashes stay inside the implementation; callers use {@link #verifySecret}.
 */
public interface IdentityStore {

    Optional<IdentityAccount> findByUsername(String username);

    Optional<IdentityAccount> findBySubject(String subject);

    /**
     * Compare the presented secret with the stored hash. Implementations choose
     * the algorithm; the host may expose {@link SecretHasher} for that work.
     */
    boolean verifySecret(String username, String secret);

    /**
     * Replace the stored hash and clear password expiry. {@code newHash} is already
     * produced by the host {@link SecretHasher}.
     */
    void replaceSecret(String username, String newHash);
}
