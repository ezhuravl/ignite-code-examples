package examples.service.scheduler;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * Example that demonstrates how to deploy Scheduler as a Cluster Singleton Service in order to guarantee failure safety.
 * Service configuration can be found in config/service-example.xml, but also it can be done using java API.
 * First, the service is deployed to node 1, and after the node stops, it is redeployed to node 2.
 */
public class FailureSafeSchedulerExample {
    /**
     * Executes example.
     *
     * @param args Command line arguments, none required.
     * @throws Exception If example execution failed.
     */
    public static void main(String[] args) throws Exception {
        //Start 2 server nodes in the same JVM
        Ignite node1 = Ignition.start("config/service-example.xml");
        Ignite node2 = Ignition.start(((IgniteConfiguration)Ignition.loadSpringBean("config/service-example.xml", "ignite.cfg")).setIgniteInstanceName("node2"));

        //Wait for the service to be deployed and executed a couple of times on the node1
        Thread.sleep(10000);

        //Stop the node1 to show that scheduler will be executed on the node2
        node1.close();
    }
}
