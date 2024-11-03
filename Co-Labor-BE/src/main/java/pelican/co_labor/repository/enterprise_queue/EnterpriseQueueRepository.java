package pelican.co_labor.repository.enterprise_queue;

import org.springframework.data.jpa.repository.JpaRepository;
import pelican.co_labor.domain.enterprise_queue.EnterpriseQueue;

public interface EnterpriseQueueRepository extends JpaRepository<EnterpriseQueue, String> {
}
