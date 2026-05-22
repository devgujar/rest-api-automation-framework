package api.auth;


import io.restassured.specification.RequestSpecification;
import api.config.ConfigManager;

/**
 * Centralised authentication strategy applier.
 * Supported types (config key: auth.type):
 *   - none
 *   - basic    -> uses auth.username / auth.password
 *   - bearer   -> uses auth.token
 *   - oauth2   -> uses auth.oauth.token (mock/real)
 *   - apiKey   -> uses auth.header.name / auth.header.value
 */
public final class AuthProvider {

    private AuthProvider() {}

    public enum AuthType { NONE, BASIC, BEARER, OAUTH2, API_KEY }

    public static RequestSpecification applyAuthentication(RequestSpecification spec) {
        AuthType type = resolveType();
        return switch (type) {
            case BASIC   -> spec.auth().preemptive().basic(
                    ConfigManager.get("auth.username"),
                    ConfigManager.get("auth.password"));
            case BEARER  -> spec.header("Authorization", "Bearer " + ConfigManager.get("auth.token"));
            case OAUTH2  -> spec.auth().oauth2(ConfigManager.get("auth.oauth.token"));
            case API_KEY -> spec.header(ConfigManager.get("auth.header.name"),
                                        ConfigManager.get("auth.header.value"));
            case NONE    -> spec;
        };
    }

    private static AuthType resolveType() {
        String t = ConfigManager.get("auth.type", "none")
                .trim().toUpperCase().replace('-', '_');
        try { return AuthType.valueOf(t); }
        catch (IllegalArgumentException e) { return AuthType.NONE; }
    }
}

