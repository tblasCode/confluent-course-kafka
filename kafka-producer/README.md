# Kafka producer

# Getting started

**Características**
- También se denomina "publicador", "publisher", "productor", "editor" o "escritor"
- Genera un mensaje sobre un topic específico (no debería de tener en cuenta la partición utilizada o bien usar el criterio asignado) añadiéndolo por el final
- El mensaje se compone : nombre del topic, offset y el nº de partición al que enviar
- Múltiples productores pueden escribir sobre diferentes particiones del mismo topic
- Cada productor tiene su propio offset

**Configuración**

Nota : los parámetros de configuración se detallarán en los ejercicios prácticos

- Tipo de comunicación : síncrona (sync) o asíncrona (async)
  - Tener en cuenta el tiempo del evento , el tiempo de espera y el tiempo de procesamiento
- Tamaño del Batch (agrupación de mensajes)
  - Se mide en bytes totales y no en nº de mensajes
  - Nunca debe exceder la memoria total (Por defecto 16384)
  - Hay que tratar que el productor se encuentre activo la mayor parte del tiempo posible
  - Se aconseja determinar un tamaño adecuado a las características del topic y a los criterios de funcionalidad a implmentar
  - Tener un mayor tamaño (mayor cantidad de bytes de elementos que componen el byte) implica tener mayor rendimiento pero provoca mayor latencia (tiempo que lleva procesar un elemento)
- Se le puede aplicar compresión

**Carga/Reparto de trabajo :**
- Por defecto "Round-Robin"
    - Se distribuyen los mensajes uniformemente en las particiones del topic
- En base a algún criterio
    - Los mensajes se asignarán a una partición en concreto en base a unas necesidades específicas -> requiere un desarrollo custom
    - Se usará la clave del mensaje y un particionador (reglas de negocio propias) que generara un hash de la clave y lo mapeará a una partición específica en concreto (por ejemplo mediante un sistema de prioridades)
    - Se asegurará que este mapeo sea determinista -> la misma clave elige la misma partición cada vez
## ACK

**Nivel de consistencia o replicación:** (request.required.acks) -> Afecta a la durabilidad de los mensajes

- ACK=0
El productor NO esperan ningún ACK desde los brokers
Los mensajes añadidos al topic son considerados enviados
El mensaje se pierde si la partición leader se cae
No garantiza la durabilidad
  
- ACK=1
La partición leader escribe el mensaje en su log local pero sin confirmar la escritura a los followers/replicas
Si el leader falla después de enviar el ACK, entonces el mensaje puede perderse
  
- ACK=all (-1)
La partición leader espera la confirmación de escritura de todos los ISR antes de enviar el ACK al productor
Garantiza que los mensajes NO serán perdidos si uno de los ISR se encuentra vivo
Debería usar como mínimo una réplica

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