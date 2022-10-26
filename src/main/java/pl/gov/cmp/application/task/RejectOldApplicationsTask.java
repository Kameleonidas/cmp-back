package pl.gov.cmp.application.task;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import pl.gov.cmp.application.service.ApplicationRejectionService;

@Component
@Slf4j
@AllArgsConstructor
public class RejectOldApplicationsTask extends QuartzJobBean {

    private final ApplicationRejectionService applicationRejectionService;

    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Rejection task initiated");
        try {
            applicationRejectionService.rejectOldApplications();
            log.info("Rejection task completed");
        } catch (Exception e) {
            JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
            log.error("{} error occured", jobKey);
            throw new JobExecutionException(e);
        }
    }
}