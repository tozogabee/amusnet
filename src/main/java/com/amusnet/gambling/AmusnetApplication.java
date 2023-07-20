package com.amusnet.gambling;

import com.amusnet.gambling.controller.GamblingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@SpringBootApplication
public class AmusnetApplication {

    public static void main(String[] args) {
        //SpringApplication.run(AmusnetApplication.class, args);
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext();
        context.scan("com.amusnet.gambling");

        context.refresh();

        GamblingService gamblingService = context.getBean(GamblingService.class);

        double result = gamblingService.sumGGR();
        log.info(String.valueOf(result));

        context.close();
    }

}
