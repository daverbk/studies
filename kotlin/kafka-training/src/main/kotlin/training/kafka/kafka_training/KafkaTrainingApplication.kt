package training.kafka.kafka_training

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaTrainingApplication

fun main(args: Array<String>) {
    runApplication<KafkaTrainingApplication>(*args)
}
