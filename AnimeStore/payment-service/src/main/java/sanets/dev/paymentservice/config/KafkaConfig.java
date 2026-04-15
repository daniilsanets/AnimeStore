package sanets.dev.paymentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic paymentTopic(){
        return TopicBuilder.name("payment-processed-events-topic")
                .replicas(1)
                .partitions(3)
                .build();
    }
}
