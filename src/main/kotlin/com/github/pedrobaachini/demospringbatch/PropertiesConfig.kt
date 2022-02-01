package com.github.pedrobaachini.demospringbatch

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.FileSystemResource

@Configuration
class PropertiesConfig {

    @Bean
    fun config() = PropertySourcesPlaceholderConfigurer()
        .apply { this.setLocation(FileSystemResource("/etc/config/demospringbatch/application.properties")) }
}