package com.verhoturkin.votingsystem.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.verhoturkin.**.config", "com.verhoturkin.**.service"})
public class AppConfig {
}
