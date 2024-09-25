package ru.matthew.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
@Slf4j
public class TimedAspect {

    @Around("@annotation(ru.matthew.aspect.Timed) || @within(ru.matthew.aspect.Timed)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();

        long startTime = System.nanoTime();

        Object result = joinPoint.proceed();

        long executionTime = System.nanoTime() - startTime;

        log.info("Метод {}.{} выполнен за {} наносекунд", className, methodName, executionTime);

        return result;
    }
}
