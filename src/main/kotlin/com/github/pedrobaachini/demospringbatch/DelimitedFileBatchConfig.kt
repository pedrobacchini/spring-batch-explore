package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DelimitedFileBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    fun delimitedFileJob(delimitedFileStep: Step) = jobBuilderFactory.get("delimitedFileJob")
        .start(delimitedFileStep)
        .incrementer(RunIdIncrementer())
        .build()
}