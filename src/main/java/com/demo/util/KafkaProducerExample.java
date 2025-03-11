package com.demo.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerExample {
    public static void main(String args[])
    {
        String bootstrapService = "localhost:9092";
        String topic = "test-topic";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapService);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Create Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (int i=0; i <= 5; i++)
        {
            String message = "Message  " + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key"+i, message);
            producer.send(record, (recordMetadata, e) -> {
                if (e == null){
                    System.out.println("Sent -> " + record.value() + " to partition " + recordMetadata.partition());
                }else {
                    e.printStackTrace();
                }
            });
        }
        // Close producer
        producer.close();
    }
}
