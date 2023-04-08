# How to run demo

## Preparation

Before running this demo, please ensure that you have the following software installed:

- jdk 8
- maven 3.8.*
- docker / docker-compose
- dapr cli

### dapr install

```bash
dapr init
```

## Save items

In this demo, we will use redis as component for configuration. After dapr is installed, redis will automatically run as a container.

Check it by:

```bash
# check if redis is running in docker
$ docker ps
CONTAINER ID   IMAGE                             COMMAND                  CREATED         STATUS                    PORTS                                                                      NAMES
...
71a5880ea24a   redis               "docker-entrypoint.s…"   3 months ago   Up 5 days             0.0.0.0:6379->6379/tcp, :::6379->6379/tcp             dapr_redis                               zoo1
...
```

Using the Redis CLI connect to the redis instance and save your configuration item:
```bash
$ redis-cli -p 6379 
$ MSET test "hello!||1" orderId1 "111||1" orderId2 "222||1"
```

## Start dapr sidecar
```bash
$ cd spring-cloud-config-dapr/samples/configuration
$ dapr run --app-port 3501 --app-id orderprocessing --app-protocol grpc  --dapr-http-port 3500 --dapr-grpc-port 50001 --components-path ./components
```

Check the log to see if dapr runtime start successfully. 

## Start Application
Find class `SampleApplication` in package `io.dapr.spring.cloud.config.sample.configuration` and run it with your IDE. Then you will see logs like this:
```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.6.8)

2022-11-01 11:05:49.776  INFO 25284 --- [           main] i.d.s.c.c.s.c.SampleApplication          : Starting SampleApplication using Java 17.0.4 on Zhikun with PID 25284 (/home/hzk/configure/spring-cloud-dapr/spring-cloud-config-dapr/samples/configuration/target/classes started by hzk in /home/hzk/configure/spring-cloud-dapr)
2022-11-01 11:05:49.778  INFO 25284 --- [           main] i.d.s.c.c.s.c.SampleApplication          : No active profile set, falling back to 1 default profile: "default"
2022-11-01 11:05:50.397  INFO 25284 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=3a6262a3-99ca-3348-9448-8bd60e7c069f
2022-11-01 11:05:50.685  INFO 25284 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2022-11-01 11:05:50.695  INFO 25284 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-11-01 11:05:50.696  INFO 25284 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.63]
2022-11-01 11:05:50.833  INFO 25284 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-11-01 11:05:50.833  INFO 25284 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1004 ms
2022-11-01 11:05:52.233  INFO 25284 --- [           main] o.s.cloud.commons.util.InetUtils         : Cannot determine local hostname
2022-11-01 11:05:52.306  INFO 25284 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-11-01 11:05:53.308  INFO 25284 --- [           main] o.s.cloud.commons.util.InetUtils         : Cannot determine local hostname
2022-11-01 11:05:53.317  INFO 25284 --- [           main] i.d.s.c.c.s.c.SampleApplication          : Started SampleApplication in 5.511 seconds (JVM running for 5.833)
```

access port 8080 in your command line and you will get the configuration items in redis
```bash
$ curl http://127.0.0.1:8080
test:hello!
```