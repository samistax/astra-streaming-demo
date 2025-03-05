package com.samistax;

import com.samistax.dto.PriceUpdateEvent;
import com.samistax.dto.SampleEvent;
import org.apache.pulsar.client.api.Schema;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.pulsar.annotation.EnablePulsar;
import org.springframework.pulsar.core.DefaultSchemaResolver;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.pulsar.core.SchemaResolver;



@EnablePulsar
@SpringBootApplication
//@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class Application {


    /*@Bean
    public SchemaResolver.SchemaResolverCustomizer<DefaultSchemaResolver> schemaResolverCustomizer() {
        return (schemaResolver) -> {
            schemaResolver.addCustomSchemaMapping(SampleEvent.class, Schema.JSON(SampleEvent.class));
            schemaResolver.addCustomSchemaMapping(PriceUpdateEvent.class, Schema.JSON(PriceUpdateEvent.class));
        };
    }

    */

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
