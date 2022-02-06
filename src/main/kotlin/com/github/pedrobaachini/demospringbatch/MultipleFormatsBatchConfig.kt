package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.LineMapper
import org.springframework.batch.item.file.MultiResourceItemReader
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

//@Configuration
class MultipleFormatsBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    data class Client(
        var name: String? = null,
        var lastName: String? = null,
        var age: String? = null,
        var email: String? = null,
        var transactions: MutableList<Transactional> = mutableListOf()
    )

    data class Transactional(
        var id: String? = null,
        var description: String? = null,
        var value: Double? = null
    )

    @Bean
    fun multipleFormatsJob(multipleFormatsStep: Step) = jobBuilderFactory.get("multipleFormatsJob")
        .start(multipleFormatsStep)
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    fun multipleFormatsStep(
        multipleFilesReader: MultiResourceItemReader<Any>,
        multipleFormatsWriter: ItemWriter<Any>
    ): Step? {
        return stepBuilderFactory.get("multipleFormatsStep")
            .chunk<Any, Any>(1)
            .reader(multipleFilesReader)
            .writer(multipleFormatsWriter)
            .build()
    }

    @Bean
    @StepScope
    fun multipleFormatsReader(
        @Value("#{jobParameters['file']}") file: Resource,
        lineMapper: LineMapper<Any>
    ): FlatFileItemReader<*> {
        return FlatFileItemReaderBuilder<Any>()
            .name("multipleFormatsReader")
            .resource(file)
            .lineMapper(lineMapper)
            .build()
    }

    @Bean
    @StepScope
    fun multipleFilesReader(
        @Value("#{jobParameters['files']}") vararg files: Resource,
        multipleFormatsReader: FlatFileItemReader<Any>
    ): MultiResourceItemReader<*> {
        return MultiResourceItemReaderBuilder<Any>()
            .name("multipleFilesReader")
            .resources(*files)
            .delegate(ClientsWithTransactionsReader(multipleFormatsReader))
            .build()
    }

    @Bean
    fun multipleFormatsWriter() = ItemWriter<Any> { items -> items.forEach(::println) }

    @Bean
    fun lineMapper() = PatternMatchingCompositeLineMapper<Any>().apply {
        this.setTokenizers(tokenizers())
        this.setFieldSetMappers(fieldSetMappers())
    }

    private fun tokenizers(): Map<String, DelimitedLineTokenizer> = mapOf(
        "0*" to DelimitedLineTokenizer().apply {
            this.setNames("name", "lastName", "age", "email")
            this.setIncludedFields(1, 2, 3, 4)
        },
        "1*" to DelimitedLineTokenizer().apply {
            this.setNames("id", "description", "value")
            this.setIncludedFields(1, 2, 3)
        }
    )

    private fun fieldSetMappers(): Map<String, FieldSetMapper<Any>> = mapOf(
        "0*" to BeanWrapperFieldSetMapper<Client>().apply { this.setTargetType(Client::class.java) },
        "1*" to BeanWrapperFieldSetMapper<Transactional>().apply { this.setTargetType(Transactional::class.java) }
    ) as Map<String, FieldSetMapper<Any>>


    class ClientsWithTransactionsReader(
        private val delegate: FlatFileItemReader<Any>
    ) : ItemStreamReader<Client>, ResourceAwareItemReaderItemStream<Client> {

        var currentObject: Any? = null

        override fun open(executionContext: ExecutionContext) {
            delegate.open(executionContext)
        }

        override fun update(executionContext: ExecutionContext) {
            delegate.update(executionContext)
        }

        override fun close() {
            delegate.close()
        }

        override fun read(): Client? {
            if (currentObject == null) currentObject = delegate.read()

            val client = currentObject as Client?
            currentObject = null

            if(client != null) {
                while (peek() is Transactional) {
                    client.transactions.add(currentObject as Transactional)
                }
            }
            return client
        }

        private fun peek(): Any? {
            currentObject = delegate.read()
            return currentObject
        }

        override fun setResource(resource: Resource) {
            delegate.setResource(resource)
        }

    }
}