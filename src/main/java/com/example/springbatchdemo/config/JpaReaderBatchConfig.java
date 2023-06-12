package com.example.springbatchdemo.config;

import com.example.springbatchdemo.CoffeeItemProcessor;
import com.example.springbatchdemo.JobCompletionNotificationListener;
import com.example.springbatchdemo.UpdateCoffeeWriter;
import com.example.springbatchdemo.model.Coffee;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JpaReaderBatchConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


//    @Bean
//    public ItemReader<Coffee> reader() {
//        JpaCursorItemReader<Coffee> reader = new JpaCursorItemReader<>();
//        reader.setEntityManagerFactory(entityManagerFactory);
//        reader.setQueryString("SELECT e FROM Coffee e");
//        return reader;
//    }

    @Bean
    public JpaPagingItemReader reader() {
        return new JpaPagingItemReaderBuilder<Coffee>()
                .name("coffeeReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Coffee c")
                .pageSize(2)
                .build();
    }

    @Bean
    public CoffeeItemProcessor processor() {
        return new CoffeeItemProcessor();
    }

//    @Bean
//    public JpaItemWriter<Coffee> writer(EntityManagerFactory entityManagerFactory) {
//        JpaItemWriter<Coffee> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }

    @Bean
    public ItemWriter<Coffee> writer(){
        return new UpdateCoffeeWriter();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemWriter<Coffee> writer) {
        return new StepBuilder("myStep", jobRepository)
                .<Coffee, Coffee> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step mystep) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(mystep)
                .end()
                .build();
    }
}
