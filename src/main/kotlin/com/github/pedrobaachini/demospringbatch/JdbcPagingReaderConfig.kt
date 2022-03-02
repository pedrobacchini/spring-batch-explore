package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

//@Configuration
class JdbcPagingReaderConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    @Qualifier(value = "appDataSource") val appDataSource: DataSource
) {

    data class Client(
        var name: String? = null,
        var lastName: String? = null,
        var age: String? = null,
        var email: String? = null
    )

    @Bean
    fun jdbcPagingReaderJob() =
        jobBuilderFactory.get("jdbcPagingReaderJob")
            .start(
                stepBuilderFactory.get("jdbcPagingReaderStep")
                    .chunk<Client, Client>(1)
                    .reader(jdbcPagingReader())
                    .writer { items -> items.forEach(::println) }
                    .build()
            )
            .incrementer(RunIdIncrementer())
            .build()

    @Bean
    fun jdbcPagingReader() = JdbcPagingItemReaderBuilder<Client>()
        .name("jdbcPagingReader")
        .dataSource(appDataSource)
        .queryProvider(
            SqlPagingQueryProviderFactoryBean().apply {
                setDataSource(appDataSource)
                setSelectClause("*")
                setFromClause("from client")
                setSortKey("email")
            }.`object`
        )
        .pageSize(2)
        .rowMapper(BeanPropertyRowMapper(Client::class.java))
        .build()
}