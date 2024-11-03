package pelican.co_labor.repository.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pelican.co_labor.domain.job.Job;
import pelican.co_labor.domain.job.JobEng;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobEngRepository extends JpaRepository<JobEng, Long> {

    @Query("SELECT j FROM JobEng j " +
            "WHERE LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " )
    List<Job> searchJobs(@Param("keyword") String keyword);

    void deleteByDeadDateBefore(@Param("date") LocalDate date);
}