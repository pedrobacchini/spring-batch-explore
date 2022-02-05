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
import org.springframework.batch.item.file.transform.Range
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

//@Configuration
class FixedFlatFileBatchConfig(
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
    fun fixedFlatFileJob(readFixedFlatFileStep: Step) = jobBuilderFactory.get("fixedFlatFileJob")
        .start(readFixedFlatFileStep)
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    fun readFixedFlatFileStep(
        fixedFlatFileStepReader: ItemReader<Client>,
        fixedFlatFileStepWriter: ItemWriter<Client>
    ) = stepBuilderFactory.get("readFixedFlatFileStep")
        .chunk<Client, Client>(1)
        .reader(fixedFlatFileStepReader)
        .writer(fixedFlatFileStepWriter)
        .build()

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

    @Bean
    fun fixedFlatFileStepWriter() = ItemWriter<Client> { items -> items.forEach(::println) }
//    fun fixedFlatFileStepWriter() = ItemWriter<Client> { items ->
//        items.forEach {
//            if (it.name.equals("Maria")) throw Exception() else println(it)
//        }
//    }
}