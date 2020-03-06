package examples.service.scheduler;

import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Scheduler service which schedules job for each 5 seconds.
 */
public class SchedulerService implements Service {
    /** Serial version UID. */
    private static final long serialVersionUID = 0L;

    /** Ignite instance. */
    @IgniteInstanceResource
    private Ignite ignite;

    private Scheduler scheduler;


    /** {@inheritDoc} */
    public void cancel(ServiceContext ctx) {
        try {
            scheduler.clear();
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println("Service was cancelled: " + ctx.name());
    }

    /** {@inheritDoc} */
    public void init(ServiceContext ctx) throws Exception {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }

    /** {@inheritDoc} */
    public void execute(ServiceContext ctx) throws Exception {
        try {
            JobDataMap arguments = new JobDataMap();

            arguments.put("ignite", ignite);

            JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).usingJobData(arguments).withIdentity("simpleJob").build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            System.out.println("SchedulerException" + e.getMessage());
        }

        System.out.println("Job was scheduled from service: " + ctx.name());
    }
}