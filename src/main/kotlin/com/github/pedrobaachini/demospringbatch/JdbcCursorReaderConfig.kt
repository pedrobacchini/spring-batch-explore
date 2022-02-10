package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import javax.sql.DataSource

//@Configuration
class JdbcCursorReaderConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    data class Client(
        var name: String? = null,
        var lastName: String? = null,
        var age: String? = null,
        var email: String? = null
    )

    @Bean
    fun jdbcCursorReaderJob(@Qualifier("appDataSource") appDataSource: DataSource) =
        jobBuilderFactory.get("jdbcCursorReaderJob")
            .start(
                stepBuilderFactory.get("jdbcCursorReaderStep")
                    .chunk<Client, Client>(1)
                    .reader(
                        JdbcCursorItemReaderBuilder<Client>()
                            .name("jdbcCursorReader")
                            .dataSource(appDataSource)
                            .sql("select * from client")
                            .rowMapper(BeanPropertyRowMapper(Client::class.java))
                            .build()
                    )
                    .writer { items -> items.forEach(::println) }
                    .build()
            )
            .incrementer(RunIdIncrementer())
            .build()

}