package com.github.pedrobaachini.demospringbatch

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.sql.DataSource

@Configuration
class DatasourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun springDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    fun appDataSource(): DataSource = DataSourceBuilder.create().build()
}