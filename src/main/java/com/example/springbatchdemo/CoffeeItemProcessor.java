package com.example.springbatchdemo;

import lombok.extern.slf4j.Slf4j;
import com.example.springbatchdemo.model.Coffee;
import org.springframework.batch.item.ItemProcessor;

// ItemProcessor<Coffee, Coffee> 介面的泛型類型為"Coffee"，表示將處理"Coffee"類型的項目
@Slf4j
public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {

//    傳入參數 final Coffee coffee 是從 FlatFileItemReader<Coffee> 來的
    @Override
    public Coffee process(final Coffee coffee) throws Exception {
        String brand = coffee.getBrand().toUpperCase();
        String origin = coffee.getOrigin().toUpperCase();
        String chracteristics = coffee.getCharacteristics().toUpperCase();

        Coffee transformedCoffee = new Coffee(brand, origin, chracteristics);
        log.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);

        return transformedCoffee;
    }
}
