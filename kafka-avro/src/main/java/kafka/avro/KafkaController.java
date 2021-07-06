package kafka.avro;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class KafkaController {

  private final Producer producer;

  @PostMapping(value = "/publish")
  public void sendMessageToKafkaTopic(@RequestParam("name") String name,
      @RequestParam("age") Integer age) {
    this.producer.sendMessage(new User(name, age));
  }
}