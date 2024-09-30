package ru.matthew.aop.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.matthew.aop.TimedAspect;

@Configuration
public class AutoConfigure {
    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }
}
