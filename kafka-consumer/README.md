# Kafka Consumer

# Getting started

**Características**
- También se denomina "subscriber", "suscriptor" o "lector"

- Puede estar subscrito a uno o más topics -> cierta independencia del broker/nodo y las particiones

- Cada consumidor es responsable de gestionar su propio offset en su partición

- Múltiples consumidores pueden leer mensajes desde el mismo topic

- Cada consumidor realiza el seguimiento de sus punteros vía tuplas (offset, partition, topic)

- Consumer lag : ¿Cuánto de lejos está el consumidor de los productores?

- Existen diferentes scripts que pueden utilizarse para conocer el offset, reasignar particiones etc.

**Funcionamiento**
- Lee los mensajes de un topic/s en el orden en el que se han producido
- Cuando un consumidor conecta a un topic conecta a un broker leader
- Para la lectura utiliza el offset de la partición asignada -> le indica el mensaje a leer 
    - Se almacena el offset del último mensaje consumido para cada partición, así un consumidor puede detenerse y reiniciar sin perder su lugar
    - Hace un seguimiento de los mensajes que ya se han consumido mediante el control de la compensación "offset" de los mensajes
- IMPORTANTE : Los consumidores no pueden leer mensajes no replicados -> el offset de mensajes menor o igual que el offset "High Watermark"
- Cuando procesa los datos debería de confirmar el offset
    - Notifican al broker cuando procesa con éxito un registro y avanza el offset
    - Si falla antes de enviar el offset de commit al broker entonces el consumidor puede continuar desde el ultimo offset comprometido
    - Si falla después de procesar el registro pero antes de enviar el commit al broker, entonces algunos mensajes pueden ser reprocesados
        - At least one : los mensajes nunca se pierden pero quizás existan duplicados
        - At most one : los mensajes son perdidos pero nunca habrá duplicados 
        - Exactly one : los mensajes sólo son entregados una y sólo una
    - Comprobación de que los mensajes (registro de entregar) son idempotentes
- Se puede ejecutar más de un consumidor en un proceso JVM usando threads /hilos -> Cada uno en su propio hilo
- Los mensajes no son quitados de la partición después de ser procesados a menos que exista algún criterio para hacerlo

## Consumer Groups
Características
Cada grupo se considera un "suscriptor" (como si fuera un sólo consumidor)
Puede ser un subscriptor de uno o más topic
Múltiples suscriptores = múltiples grupos
Cada grupo dispone de su propio offset único por partición del topic
Un grupo pueden leer desde diferentes ubicaciones en una partición
Diferentes grupos de consumidores pueden leer desde diferentes ubicaciones en una partición
Los consumidores de un grupo mantienen un balance de carga uniforme
Múltiples grupos de consumo pueden leer desde diferentes particiones de forma eficiente
Configuración
Identificador:
"Nombre" único para distinguirlo del resto de grupos
Cada consumidor puede tener un identificador de grupo
Nota : los parámetros de configuración se detallarán en los ejercicios prácticos

Funcionamiento
Consumo de mensajes :

Un mensaje es entregado a un único consumidor en un grupo de consumo
El grupo asegura que cada partición sólo sea consumida por un miembro
Cuando procesa un mensaje debería hacer un commit del offset
Si el consumidor muere, podrá iniciarse y comenzar a leer desde donde se quedó en función del offset almacenado en "_consumer_offset" o según lo analizado, otro consumidor del grupo de consumidores puede hacerse cargo
Sólo un único consumidor del mismo grupo de consumidores puede acceder a una sola partición -> múltiples consumidores puedan leer cualquier flujo de mensajes sin afectarse/interferirse
Cada consumidor en el grupo es un consumidor exclusivo de una parte "justa"/"apropiada" de las particiones
Si un nuevo consumidor entra en el grupo entonces recibe una parte de las particiones
Si un consumidor muerte , sus particiones se dividen/reparten entre los consumidores vivos "restantes" en el grupo
Los consumidores de un grupo balancean la carga al procesar registro
Un solo consumidor en el grupo se convierte en el coordinador del grupo
Si un consumidor falla antes de enviar el offset de commit al broker, entonces un consumidor diferente puede continuar desde el último offset confirmado
Si un consumidor falla después de procesar un mensaje pero antes de enviar el compromiso al broker, entonces algunos mensajes pueden ser reprocesados
Si el procesamiento de un registro tarda un tiempo, un solo consumidor puede ejecutar múltiples subprocesos para procesar registros,pero es más difícil gestionar la compensación para cada subproceso/tarea.
Si un consumidor ejecuta múltiples subprocesos, entonces dos mensajes en las mismas particiones podrían ser procesados por dos subprocesos diferentes, lo que dificulta garantizar el orden de entrega de mensajes sin una coordinación de subprocesos compleja.
Esta configuración puede ser apropiada si se está procesando una sola tarea lleva mucho tiempo, pero trata de evitarla
Si necesita ejecutar múltiples consumidores, entonces se ejecuta cada consumidor en su propio hilo. De esta manera, Kafka puede entregar los grupos de mensajes al consumidor y éste no tiene que preocuparse por solicitar el offset. Un hilo por consumidor facilita la gestión de las compensaciones. También es más sencillo gestionar la conmutación por error (cada proceso ejecuta X número de hilos de consumo), ya que puede permitir que Kafka haga el trabajo más duro.
Tipología :

Cola Tradicional : Todos los consumidores pertenecen a un sólo grupo
Difusión : Todos los consumidores pertenecen a diferentes grupos
Subscripción Lógica : Muchos consumidores son instancias de un grupo
Consideraciones :
No puede haber más consumidores que instancias
Cada consumidor lee una o más particiones de un topic
Si el nº de grupos de consumidores supera del nº de particiones, entonces los consumidores exceden el número de particiones, por lo que los consumidores adicionales estarán inactivos.Estos consumidores inactivos estarán pendientes por si se produce un fallo
Si hay más particiones que el nº grupo de consumidores, entonces algunos consumidores leerán desde más de una partición
Diagrama Conceptual : "Esquema de la plataforma en lo relacionado con grupos de consumo"

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