package org.ctu.fee.a4m39wa2.chalupa.chat.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import java.io.Serializable;

@Authenticated
@Interceptor
public class AuthenticatedInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object authorize(InvocationContext ctx) throws Exception {
        if (securityContext.getUser() == null) {
            throw new AuthenticationException();
        }
        return ctx.proceed();
    }
}
