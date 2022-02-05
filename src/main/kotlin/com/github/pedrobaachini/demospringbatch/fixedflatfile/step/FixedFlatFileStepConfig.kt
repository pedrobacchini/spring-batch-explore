package com.github.pedrobaachini.demospringbatch.fixedflatfile.step

import com.github.pedrobaachini.demospringbatch.fixedflatfile.domain.Client
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FixedFlatFileStepConfig(
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun readFixedFlatFileStep(
        fixedFlatFileStepReader: ItemReader<Client>,
        fixedFlatFileStepWriter: ItemWriter<Client>
    ) = stepBuilderFactory.get("readFixedFlatFileStep")
        .chunk<Client, Client>(1)
        .reader(fixedFlatFileStepReader)
        .writer(fixedFlatFileStepWriter)
        .build()
}