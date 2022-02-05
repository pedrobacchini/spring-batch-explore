package com.github.pedrobaachini.demospringbatch.fixedflatfile.job

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FixedFlatFileJobConfig(
    private val jobBuilderFactory: JobBuilderFactory
) {

    @Bean
    fun fixedFlatFileJob(readFixedFlatFileStep: Step) = jobBuilderFactory.get("fixedFlatFileJob")
        .start(readFixedFlatFileStep)
        .incrementer(RunIdIncrementer())
        .build()
}