package pl.gov.cmp.scheduler.configuration;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.gov.cmp.application.task.RejectOldApplicationsTask;

import static org.quartz.TriggerBuilder.newTrigger;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final JobConfiguration jobConfiguration;

    @Bean
    public JobDetail rejectOldApplicationsJob() {
        return JobBuilder.newJob(RejectOldApplicationsTask.class)
                .storeDurably()
                .withIdentity("reject_old_applications")
                .withDescription("Reject old applications")
                .build();
    }

    @Bean
    public Trigger rejectOldApplicationsTrigger(@Qualifier("rejectOldApplicationsJob") JobDetail rejectOldApplicationsJob) {
        var jobs = this.jobConfiguration.getJobs();
        var jobItemConfiguration = jobs.get(JobEnum.REJECT_OLD_APPLICATIONS);
        return newTrigger()
                .withIdentity("trigger")
                .forJob(rejectOldApplicationsJob)
                .withSchedule(CronScheduleBuilder.cronSchedule(jobItemConfiguration.getCron()))
                .build();
    }
}
