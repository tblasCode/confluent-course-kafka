# Kafka Infrastructure

# Getting started

```
docker-compose up -d

docker-compose exec broker kafka-topics \
  --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 1 \
  --topic my-topic

docker-compose down
```

# Minimum commands to admin the cluster

**List topics:**

```
docker-compose exec zookeeper kafka-topics \
    --list \
    --zookeeper localhost:2181

docker-compose exec broker kafka-topics \
    --list \
    --bootstrap-server=localhost:9092 
   
docker-compose exec broker kafka-topics \
    --describe \
    --bootstrap-server=localhost:9092 \
    --topic my-topic
```


# References

- https://docs.confluent.io/platform/current/quickstart/cos-docker-quickstart.html