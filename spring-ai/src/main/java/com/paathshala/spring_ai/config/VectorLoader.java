package com.paathshala.spring_ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class VectorLoader {

    @Value("classpath:/src/main/resources/static/India_Constitution.pdf")
    private Resource pdfResource;
}
