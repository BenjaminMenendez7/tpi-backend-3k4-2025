package utnfc.isi.back.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        // 1. Obtener el mapa de acceso al realm (realm_access)
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        // 2. Verificar si la claim existe antes de intentar acceder a ella
        if (realmAccess == null || !realmAccess.containsKey("roles")) {
            return Collections.emptyList();
        }

        // 3. Obtener la lista de roles
        // Se hace un casting seguro a List<String>
        List<String> roles = (List<String>) realmAccess.get("roles");

        // 4. Transformar los roles a GrantedAuthorities
        return roles.stream()
                .map(role -> "ROLE_" + role) // IMPORTANTE: Agregamos el prefijo 'ROLE_'
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
