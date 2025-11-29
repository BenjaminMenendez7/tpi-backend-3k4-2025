package utnfc.isi.back.contenedoresservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración explícita para Springdoc-OpenAPI.
 * Esto asegura que la interfaz se inicialice correctamente en WebFlux
 * e integra la configuración de seguridad OAuth2/Keycloak.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Keycloak JWT";
    private static final String REALM_ISSUER_URI = "http://keycloak:8080/realms/LogisticaContenedores";
    private static final String AUTHORIZATION_URL = REALM_ISSUER_URI + "/protocol/openid-connect/auth";
    private static final String TOKEN_URL = REALM_ISSUER_URI + "/protocol/openid-connect/token";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Información General de la API
                .info(new Info()
                        .title("Contenedores Service API")
                        .version("v1.0.0")
                        .description("API para la gestión de contenedores, solicitudes y cálculo de rutas.")
                )
                // 2. Configuración de Componentes de Seguridad (OAuth2)
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .description("Autenticación basada en JWT de Keycloak")
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl(AUTHORIZATION_URL)
                                                        .tokenUrl(TOKEN_URL)
                                                        // Define los Scopes necesarios
                                                        .scopes(new Scopes()
                                                                .addString("openid", "OpenID Connect scope")
                                                                .addString("profile", "Perfil de usuario")
                                                                .addString("email", "Correo electrónico")
                                                        )
                                                )
                                        )
                        )
                )
                // 3. Requisito de Seguridad Global (Aplica a todos los endpoints por defecto)
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
