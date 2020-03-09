package examples.nodefilter.cache;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.util.AttributeNodeFilter;

/**
 * Example that demonstrates how Cache Node Filter works. It creates 2 caches with Node Filters - one using xml,
 * which is deployed on node1 only and the second using Java API, for node2 only. Attributes for nodes configured in xml.
 */
public class CacheNodeFilterExample {
    /**
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        Ignite node1 = Ignition.start("config/node-filter-example.xml");
        Ignite node2 = Ignition.start("config/node-filter-example2.xml");

        IgniteCache cache2 = node2.getOrCreateCache(cacheConfiguration());

        //Show nodes that stores cache1
        System.out.println("Nodes to store cache1: " + node1.cluster().forCacheNodes("cache1").nodes());

        //Show nodes that stores cache2
        System.out.println("Nodes to store cache2: " + node2.cluster().forCacheNodes("cache2").nodes());
    }

    /**
     * Creates cache configuration with Node Filter. This configuration can also be done in xml.
     */
    private static CacheConfiguration cacheConfiguration() {
        return new CacheConfiguration("cache2")
            .setCacheMode(CacheMode.REPLICATED)
            .setNodeFilter(new AttributeNodeFilter("store_cache2", "true"));
    }
}
