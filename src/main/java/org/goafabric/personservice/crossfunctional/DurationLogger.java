package org.goafabric.personservice.crossfunctional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Priority(2020)
@Interceptor
@DurationLog
public class DurationLogger {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        var startTime = System.currentTimeMillis();
        final Object ret;
        try {
            ret = context.proceed();
        } finally {
            log.info("{} took {}ms for user: {} , tenant: {}", toString(context.getMethod()),
                    System.currentTimeMillis() - startTime, HttpInterceptor.getUserName(), HttpInterceptor.getTenantId());
        }
        return ret;
    }

    private String toString(final Method method) {
        var parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
                method.getName(), parameterTypes);
    }

}