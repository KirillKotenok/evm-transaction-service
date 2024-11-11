To build an Application image from the Dockerfile, run the following command:
```bash
docker build -t evm-transaction-service .
```

Create test network for application
```bash
docker network create test-network
```

To run the application, you need to have a PostgreSQL database running. You can run a PostgreSQL database using Docker.
<br>Run PostgreSQL docker container with the following command and replace credentials if needed:
```bash
docker run --name evm-transaction-service-db --network test-network -e POSTGRES_USER=testUser -e POSTGRES_PASSWORD=testPassword -e POSTGRES_DB=EVM_TRANSACTION_SERVICE -p 5432:5432 -d postgres:latest
```

Run Kafka with Zookeeper command, change  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 if want to test 
or debug using intellij
```bash
docker run -d --name zookeeper --network test-network -p 2181:2181 zookeeper:3.5
docker run -d --name kafka --network test-network -p 9092:9092 \
    -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
    -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092 \
    -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT \
    -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
    confluentinc/cp-kafka:latest
```

To run the container on local port 7777, replace EVM_NODE_URL and EVM_API_KEY with your configuration data if needed and run the following command. Use -d to run the container in the background:
```bash
docker run --network test-network -p 7777:8080 \
    -e DB_URL=jdbc:postgresql://evm-transaction-service-db:5432/EVM_TRANSACTION_SERVICE \
    -e DB_USER=testUser \
    -e DB_PASSWORD=testPassword \
    -e EVM_NODE_URL=wss://mainnet.infura.io/ws/v3/ \
    -e EVM_API_KEY=92dfff1edc2f4396a096f2135c9ec58d \
    -e KAFKA_SERVER=kafka:9092 \
    evm-transaction-service:latest
```

Healthcheck endpoint: http://localhost:8080/actuator/health