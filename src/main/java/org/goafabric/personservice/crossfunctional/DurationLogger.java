package org.goafabric.personservice.crossfunctional;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Priority(2020)
@Interceptor
@DurationLog
@Slf4j
public class DurationLogger {
    @AroundInvoke
    Object logInvocation(InvocationContext context) throws Exception {
        final long startTime = System.currentTimeMillis();
        final Object ret;
        try {
            ret = context.proceed();
        } finally {
            log.info("{} took {}ms for user {} , tenant {}", toString(context.getMethod()),
                    System.currentTimeMillis() - startTime, HttpInterceptor.getUserName(), HttpInterceptor.getTenantId());
        }
        return ret;
    }

    private String toString(final Method method) {
        final String parameterTypes = Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
                method.getName(), parameterTypes);
    }

}