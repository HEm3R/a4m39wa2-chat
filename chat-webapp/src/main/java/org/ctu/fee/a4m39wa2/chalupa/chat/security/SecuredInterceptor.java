package org.ctu.fee.a4m39wa2.chalupa.chat.security;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Secured
@Interceptor
public class SecuredInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object authorize(InvocationContext ctx) throws Exception {
        if (securityContext.getUser() == null) {
            throw new AuthenticationException();
        }

        final BusinessRole[] declaredRoles = getExpectedBusinessRoles(ctx.getMethod());
        if (declaredRoles.length == 0 || hasRole(declaredRoles)) {
            return ctx.proceed();
        }

        throw new AuthorizationException();
    }

    private boolean hasRole(BusinessRole[] declaredRoles) {
        List<BusinessRole> userRoles = securityContext.getUser().getBusinessRoles();
        for (BusinessRole role : declaredRoles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    private BusinessRole[] getExpectedBusinessRoles(Method method) {
        Secured secured = method.getAnnotation(Secured.class);
        if (secured == null) {
            secured = method.getDeclaringClass().getAnnotation(Secured.class);
            if (secured == null) {
                throw new RuntimeException(
                        "@Secured not found on method " + method.getName() + " or its declaring class " + method.getClass().getName()
                );
            }
        }
        return secured.value();
    }
}
