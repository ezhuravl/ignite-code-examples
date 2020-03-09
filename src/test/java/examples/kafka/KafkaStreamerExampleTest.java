package examples.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for @{@link KafkaStreamerExample}. It uses Kafka from SpringBoot to run test locally.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(ports = 9092)
@SpringBootConfiguration
public class KafkaStreamerExampleTest {
    private static final String TOPIC = "example_cache";
    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private Consumer<String, String> consumer;
    @Before
    public void setUp() throws InterruptedException {
        KafkaStreamerExample.startConsumer();
        Thread.sleep(2000);
    }
    @After
    public void tearDown() {
        try {
            KafkaStreamerExample.stopConsumer();
        }
        catch (InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    @Test
    public void kafkaSetup_withTopic_ensureSendMessageIsReceived() {

        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();

        producer.send(new ProducerRecord<>(TOPIC, "KEY", "VALUE"));
        producer.flush();

        try {
            KafkaStreamerExample.main(new String[]{});
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}