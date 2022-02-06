package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.math.BigDecimal
import javax.xml.bind.annotation.XmlRootElement

//@Configuration
class ReaderXMLBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @XmlRootElement(name="trade")
    data class Trade(
        var isin: String? = null,
        var quantity: Long? = null,
        var price: BigDecimal? = null,
        var customer: String? = null
    )

    @Bean
    fun readerXmlJob(readerXmlStep: Step) = jobBuilderFactory.get("readerXmlJob")
        .start(readerXmlStep)
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    fun readerXmlStep(
        xmlReader: StaxEventItemReader<Trade>,
        tradeWrite: ItemWriter<Trade>
    ) = stepBuilderFactory.get("readerXmlStep")
        .chunk<Trade, Trade>(1)
        .reader(xmlReader)
        .writer(tradeWrite)
        .build()

    @Bean
    @StepScope
    fun xmlReader(@Value("#{jobParameters['file']}") file: Resource): StaxEventItemReader<Trade> {

        val tradeMarshaller = Jaxb2Marshaller()
        tradeMarshaller.setClassesToBeBound(Trade::class.java)

        return StaxEventItemReaderBuilder<Trade>()
            .name("xmlReader")
            .resource(file)
            .addFragmentRootElements("trade")
            .unmarshaller(tradeMarshaller)
            .build()
    }

    @Bean
    fun tradeWrite() = ItemWriter<Trade> { items -> items.forEach(::println) }
}