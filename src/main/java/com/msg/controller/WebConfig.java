package com.msg.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.msg.controller.StringToTimestampConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToTimestampConverter stringToTimestampConverter;

    public WebConfig(StringToTimestampConverter stringToTimestampConverter) {
        this.stringToTimestampConverter = stringToTimestampConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToTimestampConverter);
    }
}
