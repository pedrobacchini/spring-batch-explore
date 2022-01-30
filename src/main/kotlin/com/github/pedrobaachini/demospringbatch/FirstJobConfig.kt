package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//@Configuration
class FirstJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun job() = jobBuilderFactory.get("imprimeOlaJob")
        .start(
            stepBuilderFactory.get("step")
                .tasklet(impimieOlaTasklet(null))
                .build()
        )
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    @StepScope
    fun impimieOlaTasklet(@Value("#{jobParameters['nome']}") nome: String?) = Tasklet { _, _ ->
        println("Olá, $nome")
        RepeatStatus.FINISHED
    }
}