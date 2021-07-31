package kafka.producer;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemoAvro {

  public static void main(String[] args) {

    String bootstrapServers = "127.0.0.1:9092";

    // Configuration

    Properties properties = new Properties();
    properties.setProperty(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.setProperty(AbstractKafkaAvroSerDeConfig.AUTO_REGISTER_SCHEMAS, "true");

    String schemaPath = "C:\\opt\\workspace\\jos\\confluent-course-kafka\\kafka-producer\\src\\main\\avro\\user.avsc";
    String valueSchemaString = "";

    try {
      valueSchemaString = new String(Files.readAllBytes(Paths.get(schemaPath)));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    Schema avroValueSchema = new Schema.Parser().parse(valueSchemaString);
    GenericRecord thisValueRecord = new GenericData.Record(avroValueSchema);

    thisValueRecord.put("name","AA");
    thisValueRecord.put("age",5);

    // Create the producer
    final KafkaProducer<String, GenericRecord> producer = new KafkaProducer<String, GenericRecord>(properties);

    //Shutdown behaviour
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("### Stopping Basic Producer ###");
      producer.close();
    }));

    // create a producer record
    ProducerRecord<String, GenericRecord> record =
        new ProducerRecord<String, GenericRecord>("my-topic-1", thisValueRecord);

    // send data - asynchronous
    producer.send(record);

  }
}