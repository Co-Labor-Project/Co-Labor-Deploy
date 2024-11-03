package pelican.co_labor.repository.enterprise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise.EnterpriseEng;

import java.util.List;

@Repository
public interface EnterpriseEngRepository extends JpaRepository<EnterpriseEng, String> {
    @Query("SELECT e FROM EnterpriseEng e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.address1) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.address2) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.address3) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.type) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Enterprise> searchEnterprises(@Param("keyword") String keyword);
}
