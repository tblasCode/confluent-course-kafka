# Kafka producer

# Getting started

## ACK

## Idempotent Producers
https://www.cloudkarafka.com/blog/apache-kafka-idempotent-producer-avoiding-message-duplication.html

```
producer = Producer({'bootstrap.servers': ‘localhost:9092’,
'message.send.max.retries': 10000000,
'enable.idempotence': True})
```

Limitation 1: Acks=All
For one, you can only use acks=all. If you set acks to 0 or 1 then you’ll see the following error

Limitation 2: max.in.flight.requests.per.connection <= 5
You must either leave max.in.flight.requests.per.connection (alias max.in.flight) left unset so that it be automatically set for you or manually set it to 5 or less. If you leave it explicitly set to a value higher than 5 you’ll see:

````
cimpl.KafkaException: KafkaError{code=_INVALID_ARG,val=-186,str="Failed to create producer: `max.in.flight` must be set <= 5 when `enable.idempotence` is true"}
````


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