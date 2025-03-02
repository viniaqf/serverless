package com.example.serverless.serverless.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {

     private static final Logger logger = Logger.getLogger(LogginAspect.class.getName());

    @Around("execution(* com.example.serverless.serverless.service.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        logger.info(String.format("→ Starting %s in %s", methodName, className));
        long start = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - start;
            
            logger.info(String.format("← Completed %s in %dms", methodName, elapsedTime));
            return result;
        } catch (Exception e) {
            logger.severe(String.format("× Error in %s: %s", methodName, e.getMessage()));
            throw e;
        }
    }
}