package examples.kafka;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.stream.kafka.KafkaStreamer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Ignite Kafka Streamer example. It processes entries from a topic and adds them to the Ignite cache.
 *
 * To run this example:
 * <p>
 * 1) Start Zookeeper and Kafka.
 * <p>
 * 2) Create the input and output topics used by this example.
 * <pre>
 * {@code
 * $ bin/kafka-topics.sh --create --topic example_cache \
 *                    --zookeeper localhost:2181 --partitions 1 --replication-factor 1
 * }</pre>
 * <p>
 * 3) Start this example application.
 * </pre>
 * <p>
 * 4) Write some input data to the source topic (e.g. via {@code kafka-console-producer}. The already running example
 * application (step 3) will automatically process this input data and write the results to the output topic.
 *
 * Alternatively, KafkaStreamerExampleTest can be used to run everything from IDE.
 *
 */
public class KafkaStreamerExample {
    private static final String CACHE_NAME = "example_cache";

    static KafkaStreamer<String, String> kafkaStreamer;

    static Ignite ignite;

    public static void main(String[] args) throws Exception {
        startConsumer();

        final CountDownLatch latch = new CountDownLatch(10);

        latch.await(20, TimeUnit.SECONDS);

        stopConsumer();

        if (ignite.cache(CACHE_NAME).size() > 0)
            System.out.println("Cache content is " + ignite.cache(CACHE_NAME).iterator().next());

    }

    public static void startConsumer() {
        ignite = Ignition.start("config/example-default.xml");
        System.out.println();
        System.out.println(">>> Cache data loader example started.");

        IgniteCache<String, String> cache = ignite.getOrCreateCache(CACHE_NAME);

        kafkaStreamer = new KafkaStreamer<>();

        IgniteDataStreamer<String, String> stmr = ignite.dataStreamer(CACHE_NAME);
        // Allow data updates.
        stmr.allowOverwrite(true);

        kafkaStreamer.setIgnite(ignite);
        kafkaStreamer.setStreamer(stmr);

        // set the topic
        kafkaStreamer.setTopic(Collections.singletonList(CACHE_NAME));

        // set the number of threads to process Kafka streams
        kafkaStreamer.setThreads(1);

        // set Kafka consumer configurations
        Properties props = new Properties();

        props.put("bootstrap.servers",  "127.0.0.1:9092");
        props.put("group.id", "consumer");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("enable.auto.commit", false);
        props.put("auto.commit.interval.ms", "10");
        props.put("session.timeout.ms", "60000");

        kafkaStreamer.setConsumerConfig(props);

        kafkaStreamer.setMultipleTupleExtractor(record -> {
            Map<String, String> entries = new HashMap<>();


            try {
                String key = (String)record.key();
                String val = (String)record.value();

                System.err.println("Adding an entry to the cache: {key: \"" + key + "\"; value: \"" + val + "\"}");


                entries.put(key, val);
            }
            catch (Exception ex) {
                System.err.println("Unexpected error." + ex);
            }

            return entries;
        });

        kafkaStreamer.start();
        System.out.println("Kafka streamer started!");
    }

    public static void stopConsumer() throws InterruptedException {
        kafkaStreamer.getStreamer().flush();

        Thread.sleep(5000);
        System.out.println("Cache count is " + ignite.cache(CACHE_NAME).size());

        kafkaStreamer.stop();

        ignite.close();
    }
}