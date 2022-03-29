package com.soybeany.demo;

import com.soybeany.demo.impl.PermDefineProviderImpl;
import com.soybeany.demo.impl.SessionProcessorImpl;
import com.soybeany.permx.annotation.EnablePermx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnablePermx(
        permDefineProvider = PermDefineProviderImpl.class,
        sessionProcessor = SessionProcessorImpl.class
)
@SpringBootApplication
class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
