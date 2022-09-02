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

Find class `SampleApplication` in package `io.dapr.spring.cloud.stream.sample.localkafka` and run it with your IDE:

```bash
......
Started SampleApplication in 1.63 seconds (JVM running for 1.874)

# SampleApplication will send a message per seconds to kafka-pubsub/topic1
Sending message, sequence 0
succeed to send event GenericMessage [payload=byte[10], headers={contentType=application/json, id=e2ac7e76-e4af-cf8b-e8f0-a80cead9fa7a, timestamp=1660619757796}]to kafka-pubsub/topic1
Sending message, sequence 1
succeed to send event GenericMessage [payload=byte[10], headers={contentType=application/json, id=8c99736a-cef8-36ba-0964-c384f3c7a3b9, timestamp=1660619759064}]to kafka-pubsub/topic1
......
# Receive messages from kafka-pubsub/topic1
Subscriber got: event body
Subscriber got: {"id":"b1ebc7b9-7053-49f0-b08a-cfdb268df674","source":"app1","type":"com.dapr.event.sent","specversion":"1.0","datacontenttype":"application/json","data":"event body","data_base64":null}
Subscriber got: event body
Subscriber got: {"id":"bb791ee8-bfaf-4c80-8b8a-22e9c2129573","source":"app1","type":"com.dapr.event.sent","specversion":"1.0","datacontenttype":"application/json","data":"event body","data_base64":null}
......
```
**Notice** : To make subscription work the user application will expose some HTTP endpoints referring to [Dapr SDK for JAVA](https://github.com/dapr/java-sdk). It requires applications to be web apps and the public URL has certain security risks. The implementation of the subscription is still under further discussion.
