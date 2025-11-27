package utnfc.isi.back.clientesservice.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.stream.Collectors;

public class KeycloakJwtGrantedAuthoritiesConverter {

    // Este metodo convierte los roles del token JWT de Keycloak
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        // Obtener los roles desde el claim 'realm_access.roles'
        return jwt.getClaimAsStringList("realm_access.roles")
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // Prefijo ROLE_
                .collect(Collectors.toList());
    }
}
