package com.example.springbatchdemo.config;

import com.example.springbatchdemo.CoffeeItemProcessor;
import com.example.springbatchdemo.JobCompletionNotificationListener;
import com.example.springbatchdemo.model.Coffee;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Value("${file.input}")
    private String fileInput;

    /*
     * 讀取 csv檔案 中每一行資料，並轉換成物件 Coffee
     */
    @Bean
    public FlatFileItemReader<Coffee> reader() {

        BeanWrapperFieldSetMapper<Coffee> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Coffee.class);

        return
//                設定 FlatFileItemReader 的名稱為 "coffeeItemReader"
                new FlatFileItemReaderBuilder<Coffee>().name("coffeeItemReader")
//                設定要讀取的文件資源位置
                .resource(new ClassPathResource(fileInput))
//                指定文件的分隔符，預設文件是以逗號分隔
                .delimited()
//                指定文件中每個欄位的名稱
                .names("brand", "origin", "characteristics")
//                設定 BeanWrapperFieldSetMapper 作為 FlatFileItemReader 的欄位映射器。它將文件中的每一行轉換為一個 Coffee 物件。
//                setTargetType(Coffee.class) 指定了目標類型為 Coffee。
                .fieldSetMapper(mapper)
                .build();
    }

    /*
     * 對 reader 回傳的物件進行加工
     */
    @Bean
    public CoffeeItemProcessor processor() {
        return new CoffeeItemProcessor();
    }

    /*
     * This time around, we'll include the SQL statement needed to insert a single coffee item into our database,
     * driven by the Java bean properties of our Coffee object.
     */
//    @Bean
//    public JdbcBatchItemWriter<Coffee> writer(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<Coffee>()
//                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//                .sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
//                .dataSource(dataSource)
//                .build();
//    }

    @Bean
    public JpaItemWriter<Coffee> writer(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<Coffee> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JpaItemWriter<Coffee> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Coffee, Coffee> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }



}
