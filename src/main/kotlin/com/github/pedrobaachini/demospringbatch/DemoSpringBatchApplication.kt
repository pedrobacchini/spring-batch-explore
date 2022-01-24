package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableBatchProcessing
class DemoSpringBatchApplication(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun job() = jobBuilderFactory.get("job")
        .start(
            stepBuilderFactory.get("step")
                .tasklet { _, _ ->
                    println("Olá Mundo")
                    RepeatStatus.FINISHED
                }.build()
        )
        .build()
}

fun main(args: Array<String>) {
    runApplication<DemoSpringBatchApplication>(*args)
}
