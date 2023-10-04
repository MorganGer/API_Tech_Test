package com.test.api.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut(value = "execution(* com.test.api.*.*.*(..))")
    public void myPointcut(){
    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getClass().toString();
        String args = mapper.writeValueAsString(pjp.getArgs());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(methodName);
        log.info("Method invoked {} : {}(). Arguments : {}", className, methodName, args);
        Object object = pjp.proceed();
        stopWatch.stop();
        Long time = stopWatch.getTotalTimeMillis();
        String response = mapper.writeValueAsString(object);
        log.info("{} : {}(). Response : {}. Execution Time {} ms", className, methodName, response, time);
        return object;
    }
}
