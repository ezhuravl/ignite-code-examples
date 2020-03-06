package examples.service.scheduler;

import java.util.Date;
import org.apache.ignite.Ignite;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Simple job that prints message.
 */
public class SimpleJob implements Job {
    public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
        Ignite ignite = (Ignite)jExeCtx.getJobDetail().getJobDataMap().get("ignite");
        System.out.println(new Date().toString() + ": Executing Job locally on node " + ignite.name());
    }
}
