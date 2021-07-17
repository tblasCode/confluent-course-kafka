# Kafka Infrastructure

## Getting started

```
docker-compose up -d

docker-compose exec broker kafka-topics --create --bootstrap-server localhost:9092  --replication-factor 1 --partitions 3 --topic my-topic 

docker-compose down
```

**Confluent Control Center:**

- http://localhost:9021/

## Minimum commands to admin the cluster

**List topics:**

```
docker-compose exec zookeeper kafka-topics \
    --list \
    --zookeeper localhost:2181

docker-compose exec zookeeper kafka-topics \
    --zookeeper localhost:2181 \
    --alter \
    --topic my-topic \
    --partitions 3 


docker-compose exec broker kafka-topics \
    --list \
    --bootstrap-server=localhost:9092 
   
docker-compose exec broker kafka-topics \
    --describe \
    --bootstrap-server=localhost:9092 \
    --topic my-topic
        
docker-compose exec zookeeper kafka-topics \
    --delete \
    --zookeeper localhost:2181 \
    --topic my-topic 

docker-compose exec broker kafka-run-class kafka.tools.GetOffsetShell \
    --broker-list localhost:9092 \
    --topic my-topic \
    --time -1     
    
docker-compose exec zookeeper kafka-configs \
    --zookeeper localhost:2181 \
    --alter \
    --entity-type topics \
    --entity-name my-topic \
    --add config retention.ms=1000
    
docker-compose exec zookeeper kafka-configs \
    --zookeeper localhost:2181 \
    --alter \
    --entity-type topics \
    --entity-name my-topic \
    --delete-config retention.ms    

docker-compose logs --f     
```


# References

- https://docs.confluent.io/platform/current/quickstart/cos-docker-quickstart.html
