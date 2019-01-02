package com.webforum;

import com.webforum.bo.entity.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebforumApplication.class, args);
    }

}
