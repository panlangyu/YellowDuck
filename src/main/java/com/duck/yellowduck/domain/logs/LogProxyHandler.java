package com.duck.yellowduck.domain.logs;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(-5)
public class LogProxyHandler
{
    public Object aroundJoinPoint(ProceedingJoinPoint pjp)
    {
        Object result = null;
        Log.beforeInvoke(pjp.getTarget().getClass(), pjp.getSignature().getName(), pjp.getArgs());
        try
        {
            result = pjp.proceed();
            Log.returnInvoke(result);
        }
        catch (Throwable e)
        {
            Log.throwableInvoke(new Object[] { pjp.getTarget().getClass().getSimpleName(), pjp.getSignature().getName(), e.getMessage(),
                    Arrays.toString(new StackTraceElement[] {e.getStackTrace()[0], e.getStackTrace()[1], e
                            .getStackTrace()[2], e.getStackTrace()[3], e.getStackTrace()[5], e.getStackTrace()[6], e.getStackTrace()[7], e.getStackTrace()[8] }) });
        }
        return result;
    }

    public Object around(ProceedingJoinPoint pjp)
    {
        Object result = null;
        Log.beforeInvoke(pjp.getTarget().getClass(), pjp.getSignature().getName(), pjp.getArgs());
        try
        {
            result = pjp.proceed();
            Log.returnInvoke(result);
        }
        catch (Throwable e)
        {
            Log.throwableInvoke(new Object[] { "[result = exception: {%s}]", e.getMessage() });
        }
        return result;
    }

    public void doBeforeInServiceLayer(JoinPoint joinPoint) {}

    public void doAfterInServiceLayer(JoinPoint joinPoint) {}

    public void webLog(JoinPoint joinPoint) {}
}
