package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

//@Configuration
class FirstJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun job() = jobBuilderFactory.get("printHelloJob")
        .start(
            stepBuilderFactory.get("step")
                .tasklet(printHelloTasklet(null))
                .build()
        )
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    @StepScope
    fun printHelloTasklet(@Value("#{jobParameters['name']}") name: String?) = Tasklet { _, _ ->
        println("Hello, $name")
        RepeatStatus.FINISHED
    }
}