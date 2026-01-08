package training.kafka.kafka_training.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import training.kafka.kafka_training.kafka.UserProducer
import training.kafka.kafka_training.model.UserCreatedEvent


@RestController
@RequestMapping("/kafka")
class KafkaController(
    private val userProducer: UserProducer
) {

    @PostMapping("/publish")
    fun publishMessage(@RequestBody message: UserCreatedEvent): String {
        userProducer.send(message)
        return "Message published successfully!"
    }
}