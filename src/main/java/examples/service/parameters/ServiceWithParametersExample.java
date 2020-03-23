package examples.service.parameters;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

/**
 * Example shows how to set parameters to the Ignite service. Service itself configured in config/service-parameters-example.xml
 */
public class ServiceWithParametersExample {
    /**
     * Start up an empty node with example compute configuration.
     *
     * @param args Command line arguments, none required.
     * @throws IgniteException If failed.
     */
    public static void main(String[] args) throws IgniteException {
        Ignition.start("config/service-parameters-example.xml");
    }
}
