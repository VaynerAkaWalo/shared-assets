package com.vaynerakawalo.springobservability.logging.advice;

import com.vaynerakawalo.springobservability.logging.annotation.Egress;
import com.vaynerakawalo.springobservability.logging.model.Outcome;
import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Aspect
@Component
@RequiredArgsConstructor
public class EgressAdvice {

    private final Clock clock;

    @Around("@annotation(com.vaynerakawalo.springobservability.logging.annotation.Egress)")
    public Object egressCall(ProceedingJoinPoint jp) throws Throwable {
        var startTime = clock.millis();
        try {
            ThreadContextProperty.TYPE.putString(Type.INGRESS.getDisplayName());
            ThreadContextProperty.METHOD.putString(getMethod(jp));
            ThreadContextProperty.TARGET_SERVICE.putString(getService(jp));

            var result = jp.proceed(jp.getArgs());

            ThreadContextProperty.OUTCOME.putString(Outcome.SUCCESS.getDisplayName());
            return result;
        } catch (Throwable t) {
            ThreadContextProperty.OUTCOME.putString(Outcome.ERROR.getDisplayName());
            ThreadContextProperty.ERROR.putString(t.getClass().getSimpleName());
            var rootCause = ExceptionUtils.getRootCause(t);
            ThreadContextProperty.CAUSE.putString(rootCause.getClass().getSimpleName());
            throw t;
        } finally {
            ThreadContextProperty.TOTAL_DURATION.putLong(clock.millis() - startTime);
        }
    }

    private String getService(ProceedingJoinPoint jp) {
        var signature = (MethodSignature) jp.getSignature();
        var clazz = signature.getDeclaringType();
        var annotation = clazz.getAnnotation(Egress.class);

        if (annotation == null || StringUtils.isEmpty(((Egress) annotation).service())) {
            return clazz.getSimpleName();
        }

        return ((Egress) annotation).service();
    }

    private String getMethod(ProceedingJoinPoint jp) {
        var signature = (MethodSignature) jp.getSignature();
        var method = signature.getMethod();

        var annotation = method.getAnnotation(Egress.class);
        if (annotation == null) {
            return method.getName();
        }
        return annotation.method();
    }
}
