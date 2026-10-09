package systems.porto.api.auth;

/**
 * Host password hashing. Implementations live in the host so plugins do not
 * depend on Spring {@code PasswordEncoder} or an equivalent Quarkus/Micronaut API.
 */
public interface SecretHasher {

    String hash(String rawSecret);

    boolean matches(String rawSecret, String storedHash);
}
