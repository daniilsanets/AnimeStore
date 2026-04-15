package sanets.dev.inventoryservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public NewTopic inventoriesTopic(){
        return TopicBuilder.name("inventory-reserved-events-topic")
                .replicas(1)
                .partitions(3)
                .build();
    }
}
