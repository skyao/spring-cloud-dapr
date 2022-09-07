# How to run local-kafka demo

## preparation

Before running this demo, please ensure that you have the following software installed:

- jdk 8
- maven 3.8.*
- docker / docker-compose
- dapr cli

### dapr install

```bash
dapr init
```

## Start kafka 

In this demo, we will use kafka as component for pubsub. 

start a single kafka cluster by docker-compose

```bash
$ mkdir kafka
$ cd kafka
$ git clone https://github.com/conduktor/kafka-stack-docker-compose.git
$ cd kafka-stack-docker-compose
$ docker-compose -f zk-single-kafka-single.yml up
......
kafka1    | [2022-08-05 05:53:59,001] INFO KafkaConfig values: 
kafka1    | 	advertised.listeners = INTERNAL://kafka1:19092,EXTERNAL://127.0.0.1:9092,DOCKER://host.docker.internal:29092
......
```

Check it by:

```bash
# check if kafka is running with docker
$ docker ps
CONTAINER ID   IMAGE                             COMMAND                  CREATED         STATUS                    PORTS                                                                      NAMES
340a4e85beca   confluentinc/cp-kafka:7.2.0       "/etc/confluent/dock…"   5 minutes ago   Up 5 minutes              0.0.0.0:9092->9092/tcp, 0.0.0.0:9999->9999/tcp, 0.0.0.0:29092->29092/tcp   kafka1
48e7c08f2ff6   confluentinc/cp-zookeeper:7.2.0   "/etc/confluent/dock…"   5 minutes ago   Up 5 minutes              2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp                                 zoo1

# verify kafka 9092 port
$ nc  -zv  127.0.0.1 9092
```

## Start dapr sidecar

```bash
# start dapr runtime without app1
cd spring-cloud-stream-dapr/samples/local-kafka 
dapr run --app-port 8888 --app-id app1 --app-protocol grpc --dapr-http-port 3501 --dapr-grpc-port 50001 --components-path=./components
```

Check the log to see if dapr runtime start successfully. 


## start sample app

Find class `SampleApplication` in package `io.dapr.spring.cloud.stream.sample.localkafka` and run it with your IDE. Then you will see logs like this:

```bash
......
Started SampleApplication in 1.63 seconds (JVM running for 1.874)

......
Sending message, sequence 29
succeed to send event GenericMessage [payload=byte[13], headers={contentType=application/json, id=308b580d-d684-cdcc-1281-3399708a3acd, timestamp=1662516457376}]to kafka-pubsub/topic1

Message received : GenericMessage [payload=event body 29, headers={contentType=application/json, id=4dde7c71-65ca-10d0-d26d-69cbbc45abdb, timestamp=1662516457381}]

Sending message, sequence 30
succeed to send event GenericMessage [payload=byte[13], headers={contentType=application/json, id=a9305d7a-86af-4ceb-b363-a56924670d65, timestamp=1662516458382}]to kafka-pubsub/topic1

Message received : GenericMessage [payload=event body 30, headers={contentType=application/json, id=1b351355-fdaa-373d-f184-4305c972f5c2, timestamp=1662516458389}]

Sending message, sequence 31
succeed to send event GenericMessage [payload=byte[13], headers={contentType=application/json, id=c2b0cb09-ddea-8c2a-bb9c-c3d0987c0a23, timestamp=1662516459389}]to kafka-pubsub/topic1

Message received : GenericMessage [payload=event body 31, headers={contentType=application/json, id=420cf68c-6218-01bb-3b1c-c5c1433c5b05, timestamp=1662516459394}]
......
```

