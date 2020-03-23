package examples.service.parameters;

import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

/**
 * Ignite Service with parameters
 */
public class ExampleServiceImpl implements Service, ExampleService  {
    /** */
    @IgniteInstanceResource
    private Ignite ignite;

    String property1;

    Integer property2;

    public ExampleServiceImpl() {
    }

    /** {@inheritDoc} */
    @Override public void cancel(ServiceContext ctx) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void init(ServiceContext ctx) throws Exception {
        System.out.println("Property1:" + property1);
        System.out.println("Property2:" + property2);

    }

    /** {@inheritDoc} */
    @Override public void execute(ServiceContext ctx) throws Exception {
        System.out.println("Property1:" + property1);
        System.out.println("Property2:" + property2);
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public Integer getProperty2() {
        return property2;
    }

    public void setProperty2(Integer property2) {
        this.property2 = property2;
    }

    @Override public Long exampleMethod() {
        return null;
    }
}