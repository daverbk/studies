package training.kafka.kafka_training.kafka

import training.kafka.kafka_training.model.UserCreatedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserConsumer {

    @KafkaListener(topics = ["user-created"])
    fun listen(event: UserCreatedEvent) {
        println("Received event: $event")
    }
}
