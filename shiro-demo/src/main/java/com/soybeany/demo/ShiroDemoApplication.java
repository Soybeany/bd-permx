package com.soybeany.demo;

import com.soybeany.demo.impl.AuthListenerImpl;
import com.soybeany.demo.impl.PermDefineProviderImpl;
import com.soybeany.demo.impl.SessionProcessorImpl;
import com.soybeany.permx.core.annotation.EnablePermx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnablePermx(
        sessionProcessor = SessionProcessorImpl.class,
        permDefineProvider = PermDefineProviderImpl.class,
        authListener = AuthListenerImpl.class
)
@SpringBootApplication
class ShiroDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroDemoApplication.class, args);
    }

}
