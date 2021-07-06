package kafka.avro;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog(topic = "Consumer Logger")
public class Consumer {

  @KafkaListener(
      topics = "#{'${topic.name}'}",
      groupId = "simple-consumer"
  )
  public void consume(User record) {
    log.info(String.format("Consumed message -> %s", record));
  }
}