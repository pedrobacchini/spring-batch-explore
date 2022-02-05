package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.function.FunctionItemProcessor
import org.springframework.batch.item.support.IteratorItemReader
import org.springframework.context.annotation.Bean

//@Configuration
class EvenOddBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun job() = jobBuilderFactory.get("printEvenOddJob")
        .start(printEvenOddStep())
        .incrementer(RunIdIncrementer())
        .build()

    private fun printEvenOddStep() = stepBuilderFactory.get("printEvenOddStep")
        .chunk<Int, String>(10)
        .reader(countToTenReader())
        .processor(evenOrOddProcessor())
        .writer(printWriter())

        .build()

    private fun countToTenReader() = IteratorItemReader((1..10).iterator())

    private fun evenOrOddProcessor() =
        FunctionItemProcessor<Int, String> { i -> if (i % 2 == 0) "Item $i is Even" else "Item $i is Odd" }

    private fun printWriter() = ItemWriter<String> { items -> items.forEach(::println) }
}