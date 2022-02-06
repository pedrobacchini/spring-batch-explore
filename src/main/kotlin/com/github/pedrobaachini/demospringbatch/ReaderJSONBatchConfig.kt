package com.github.pedrobaachini.demospringbatch

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.json.JacksonJsonObjectReader
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import java.util.function.Function


@Configuration
class ReaderJSONBatchConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    data class Movie(
        var title: String? = null,
        var year: Long? = null,
        var cast: List<String> = listOf(),
        var genres: List<String> = listOf()
    )

    data class MovieGenre(
        var title: String? = null,
        var genres: String? = null
    )

    @Bean
    fun readerJsonJob() =
        jobBuilderFactory.get("readerJsonJob")
            .start(
                stepBuilderFactory.get("readerJsonStep")
                    .chunk<Movie, MovieGenre>(10)
                    .reader(
                        JsonItemReaderBuilder<Movie>()
                            .name("movieJsonItemReader")
                            .jsonObjectReader(JacksonJsonObjectReader(Movie::class.java))
                            .resource(FileSystemResource("files/movies.json"))
                            .build()
                    )
                    .processor(Function<Movie, MovieGenre> { movie ->
                        MovieGenre(
                            movie.title,
                            movie.genres.toString()
                        )
                    })
                    .writer(
                        FlatFileItemWriterBuilder<MovieGenre>()
                            .name("movieGenreWriter")
                            .resource(FileSystemResource("out/movies.csv"))
                            .delimited()
                            .delimiter(",")
                            .names("title", "genres")
                            .build()
                    )
                    .build()
            )
            .incrementer(RunIdIncrementer())
            .build()
}