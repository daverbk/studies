package training.kafka.kafka_training.kafka

import training.kafka.kafka_training.model.UserCreatedEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UserProducer(
  private val kafkaTemplate: KafkaTemplate<String, UserCreatedEvent>
) {

  fun send(event: UserCreatedEvent) {
    kafkaTemplate.send("user-created", event.id, event)
  }
}