package pelican.co_labor.repository.labor_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pelican.co_labor.domain.labor_user.LaborUser;

import java.util.Optional;

@Repository
public interface LaborUserRepository extends JpaRepository<LaborUser, Long> {
    Optional<LaborUser> findByLaborUserId(String laborUserId);
    boolean existsByLaborUserId(String laborUserId);
    boolean existsByEmail(String Email);
}
