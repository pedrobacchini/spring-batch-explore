package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.jdbc.core.BeanPropertyRowMapper
import java.sql.SQLException
import javax.sql.DataSource

@Configuration
class JdbcCursorReaderConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    @Qualifier("appDataSource") val appDataSource: DataSource
) {

    data class Client(
        var name: String? = null,
        var lastName: String? = null,
        var age: String? = null,
        var email: String? = null
    )

    @Bean
    fun jdbcCursorReaderJob() =
        jobBuilderFactory.get("jdbcCursorReaderJob")
            .start(
                stepBuilderFactory.get("jdbcCursorReaderStep")
                    .chunk<Client, Client>(11)
                    .reader(skipExceptionReader())
                    .writer { items -> items.forEach(::println) }
                    .faultTolerant()
                    .skip(UncategorizedSQLException::class.java)
                    .skipLimit(2)
                    .build()
            )
            .incrementer(RunIdIncrementer())
            .build()

    @Bean
    fun jdbcCursorReader() = JdbcCursorItemReaderBuilder<Client>()
        .name("jdbcCursorReader")
        .dataSource(appDataSource)
        .sql("select * from client")
        .rowMapper(BeanPropertyRowMapper(Client::class.java))
        .build()

    @Bean
    fun skipExceptionReader() = JdbcCursorItemReaderBuilder<Client>()
        .name("skipExceptionReader")
        .dataSource(appDataSource)
        .sql("select * from client")
        .rowMapper { rs, _ ->
            if (rs.row == 11)
                throw SQLException("Finish execution - invalid client ${rs.getString("email")}")
            else
                Client().apply {
                    name = rs.getString("name")
                    lastName = rs.getString("lastName")
                    age = rs.getString("age")
                    email = rs.getString("email")
                }
        }
        .build()

}