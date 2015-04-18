package org.ctu.fee.a4m39wa2.chalupa.chat.api.filters.security;

import org.jboss.resteasy.util.Base64;

import org.ctu.fee.a4m39wa2.chalupa.chat.logic.AuthenticationService;
import org.ctu.fee.a4m39wa2.chalupa.chat.model.User;
import org.ctu.fee.a4m39wa2.chalupa.chat.security.SecurityContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.List;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
@Priority(1)
public class Authenticator implements ContainerRequestFilter {

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Value
    private static class Credentials {
        private final String username;
        private final String password;
    }

    @Inject
    private SecurityContext securityContext;

    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        final List<String> authorization = requestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
        if (authorization != null && !authorization.isEmpty()) {
            final Credentials credentials = retrieveCredentials(authorization.get(0));
            if (credentials != null) {
                final User u = authenticationService.authenticate(credentials.getUsername(), credentials.getPassword());
                if (u != null) {
                    securityContext.setUser(u);
                }
            }
        }
    }

    private Credentials retrieveCredentials(String authorization) {
        if (authorization != null && authorization.startsWith(AUTHENTICATION_SCHEME)) {
            try {
                authorization = new String(Base64.decode(authorization.split(" ")[1]));
                final String[] credentials = authorization.split(":");
                if (credentials.length == 2) {
                    return new Credentials(credentials[0], credentials[1]);
                }
            } catch (IOException e) {
                log.warn("Can not retrieve basic auth credentials: {}", authorization);
            }
        }
        return null;
    }
}
