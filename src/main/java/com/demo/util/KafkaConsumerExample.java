package com.demo.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    public static void main(String arg[]) {


        // Kafka broker details
        String bootstrapServers = "localhost:9092";
        String topic = "test-topic";
        String groupId = "my-consumer-group";

        // Kafka consumer configuration
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("auto.offset.reset","earliest");

        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));
        // polling the messages
        while (true) {
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String,String> record: records) {
                System.out.println("Received -> Key: " + record.key() + ", Value: " + record.value() + ", Partition: " + record.partition());
            }
        }

    }
}
