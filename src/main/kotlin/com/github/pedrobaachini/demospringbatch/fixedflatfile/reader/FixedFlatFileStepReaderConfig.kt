package com.github.pedrobaachini.demospringbatch.fixedflatfile.reader

import com.github.pedrobaachini.demospringbatch.fixedflatfile.domain.Client
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.transform.Range
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

@Configuration
class FixedFlatFileStepReaderConfig {

    @Bean
    @StepScope
    fun fixedFlatFileStepReader(
        @Value("#{jobParameters['clientsFile']}") clientsFile: Resource
    ): FlatFileItemReader<Client> {
        return FlatFileItemReaderBuilder<Client>()
            .name("fixedFlatFileStepReader")
            .resource(clientsFile)
            .fixedLength()
            .columns(Range(1, 10), Range(11, 20), Range(21, 23), Range(24, 43))
            .names("name", "lastName", "age", "email")
            .targetType(Client::class.java)
            .build()
    }
}