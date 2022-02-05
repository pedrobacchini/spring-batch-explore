package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

//@Configuration
class DelimitedFileBatchConfig(
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
    fun delimitedFileJob(delimitedFileStep: Step) = jobBuilderFactory.get("delimitedFileJob")
        .start(delimitedFileStep)
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    fun delimitedFileStep(
        delimitedFileReader: ItemReader<Client>,
        delimitedFileWriter: ItemWriter<Client>
    ) = stepBuilderFactory.get("delimitedFileStep")
        .chunk<Client, Client>(1)
        .reader(delimitedFileReader)
        .writer(delimitedFileWriter)
        .build()

    @Bean
    @StepScope
    fun delimitedFileReader(
        @Value("#{jobParameters['clientsFile']}") clientsFile: Resource
    ): FlatFileItemReader<Client> {
        return FlatFileItemReaderBuilder<Client>()
            .name("delimitedFileReader")
            .resource(clientsFile)
            .delimited()
            .names("name", "lastName", "age", "email")
            .targetType(Client::class.java)
            .build()
    }

    @Bean
    fun fixedFlatFileStepWriter() = ItemWriter<Client> { items -> items.forEach(::println) }
}