package kafka.producer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {

  public static void main(String[] args) {

    String bootstrapServers = "127.0.0.1:9092";

    // Configuration
    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // Create the producer
    final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

    //Shutdown behaviour
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("### Stopping Basic Producer ###");
      producer.close();
    }));

    // create a producer record
    ProducerRecord<String, String> record =
        new ProducerRecord<String, String>("first_topic", "hello world");

    // send data - asynchronous
    producer.send(record);

  }
}