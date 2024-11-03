package pelican.co_labor.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pelican.co_labor.service.JobService;

@Component
public class JobScheduler {

    private final JobService jobService;

    @Autowired
    public JobScheduler(JobService jobService) {
        this.jobService = jobService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteExpiredJobs() {
        jobService.deleteExpiredJobs();
    }
}
