package rewardsdining.security;

import java.text.ParseException;
import java.util.Collection;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

public class JwtOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
	
	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
	
	private OpaqueTokenIntrospector delegate;
            
    
    private JwtDecoder jwtDecoder = new NimbusJwtDecoder(new ParseOnlyJWTProcessor());

	
    public JwtOpaqueTokenIntrospector(OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
		this.jwtGrantedAuthoritiesConverter = null;
		
		OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerProperties.getOpaquetoken();
		delegate = new NimbusOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(),
				opaqueToken.getClientSecret());
	}

	
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
        try {
            Jwt jwt = this.jwtDecoder.decode(token);
            Collection<GrantedAuthority> authorities = null;
            return new DefaultOAuth2AuthenticatedPrincipal(jwt.getClaims(), authorities);
        } catch (JwtException ex) {
            throw new OAuth2IntrospectionException(ex.getMessage());
        }
    }

    private static class ParseOnlyJWTProcessor extends DefaultJWTProcessor<SecurityContext> {
    	public JWTClaimsSet process(SignedJWT jwt, SecurityContext context)
    			throws BadJOSEException, JOSEException {
            try {
				return jwt.getJWTClaimsSet();
			} catch (ParseException e) {
				return new JWTClaimsSet.Builder().build();
			}
        }
    }
}