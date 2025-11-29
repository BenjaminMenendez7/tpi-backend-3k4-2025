package utnfc.isi.back.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(KeycloakJwtGrantedAuthoritiesConverter.class);

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        if (realmAccess == null) {
            log.warn("Keycloak-Converter: Claim 'realm_access' es nula o no existe en el JWT.");
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) realmAccess.get("roles");

        if (roles == null) {
            log.warn("Keycloak-Converter: La lista de 'roles' es nula dentro de 'realm_access'.");
            return Collections.emptyList();
        }

        log.info("Keycloak-Roles: Roles extraídos del JWT ({}): {}", jwt.getSubject(), roles);

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}