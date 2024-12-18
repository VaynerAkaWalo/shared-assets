package com.vaynerakawalo.springobservability.logging.advice;

import com.vaynerakawalo.springobservability.logging.annotation.Egress;
import com.vaynerakawalo.springobservability.logging.log.EgressOperationLog;
import com.vaynerakawalo.springobservability.logging.model.Outcome;
import com.vaynerakawalo.springobservability.logging.model.ThreadContextProperty;
import com.vaynerakawalo.springobservability.logging.model.Type;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Clock;

@Aspect
public class EgressAdvice {

    private final Clock clock = Clock.systemUTC();

    @Around("@annotation(com.vaynerakawalo.springobservability.logging.annotation.Egress) && execution(* *(..))")
    public Object egressCall(ProceedingJoinPoint jp) throws Throwable {
        var startTime = clock.millis();
        try {
            ThreadContextProperty.TYPE.putString(Type.EGRESS.getDisplayName());
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
            new EgressOperationLog().log();

            ThreadContextProperty.METHOD.putString(null);
            ThreadContextProperty.TARGET_URL.putString(null);
            ThreadContextProperty.TARGET_SERVICE.putString(null);
        }
    }

    private String getService(ProceedingJoinPoint jp) {
        var signature = (MethodSignature) jp.getSignature();
        var clazz = signature.getDeclaringType();
        var annotation = clazz.getAnnotation(Egress.class);

        if (annotation == null || StringUtils.equalsIgnoreCase(((Egress) annotation).service(), Egress.NOT_SET)) {
            return clazz.getSimpleName();
        }

        return ((Egress) annotation).service();
    }

    private String getMethod(ProceedingJoinPoint jp) {
        var signature = (MethodSignature) jp.getSignature();
        var method = signature.getMethod();

        var annotation = method.getAnnotation(Egress.class);
        if (annotation == null || StringUtils.equalsIgnoreCase(annotation.method(), Egress.NOT_SET)) {
            return method.getName();
        }
        return annotation.method();
    }
}
