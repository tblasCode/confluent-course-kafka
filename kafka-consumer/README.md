# Kafka Consumer

# Getting started

## Consumer Groups

## Processing guarantees
https://www.confluent.io/blog/exactly-once-semantics-are-possible-heres-how-apache-kafka-does-it/
https://kafka.apache.org/documentation/#semantics

- No guarantee — No explicit guarantee is provided, so consumers may process messages once, multiple times or never at all.
- At most once — This is “best effort” delivery semantics. Consumers will receive and process messages exactly once or not at all.
- At least once — Consumers will receive and process every message, but they may process the same message more than once.
- Effectively once — Also contentiously known as exactly once, this promises consumers will process every message once.


# Commands

**Read event:**

```
docker-compose exec broker kafka-console-consumer \ 
  \ --topic quickstart-events 
  \ --from-beginning 
  \ --bootstrap-server 
  \ localhost:9092 
        
```

# References

- https://docs.confluent.io/platform/current/quickstart/cos-docker-quickstart.html
- https://github.com/simplesteph/kafka-beginners-course/blob/master/kafka-basics/src/main/java/kafka/tutorial1/ConsumerDemoGroups.java