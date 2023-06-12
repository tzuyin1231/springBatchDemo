package com.example.springbatchdemo;

import com.example.springbatchdemo.model.Coffee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
public class UpdateCoffeeWriter implements ItemWriter<Coffee> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void write(Chunk<? extends Coffee> chunk) throws Exception {
        log.info("Chunk: {}",String.valueOf(chunk));
        log.info(entityManager.toString());
        for(Coffee item: chunk){
            log.info("id: {}",item.getId());
            log.info(item.getOrigin());
            Coffee existingCoffee = entityManager.find(Coffee.class, item.getId());
            if(existingCoffee!=null){
                existingCoffee.setBrand(item.getBrand());
                existingCoffee.setCharacteristics(item.getCharacteristics());
                existingCoffee.setOrigin(item.getOrigin());
            }

        }

    }
}
