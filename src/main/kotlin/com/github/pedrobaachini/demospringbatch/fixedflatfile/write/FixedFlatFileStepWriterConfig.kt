package com.github.pedrobaachini.demospringbatch.fixedflatfile.write

import com.github.pedrobaachini.demospringbatch.fixedflatfile.domain.Client
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FixedFlatFileStepWriterConfig {

    @Bean
    fun fixedFlatFileStepWriter() = ItemWriter<Client> { items -> items.forEach(::println) }
//    fun fixedFlatFileStepWriter() = ItemWriter<Client> { items ->
//        items.forEach {
//            if (it.name.equals("Maria")) throw Exception() else println(it)
//        }
//    }
}