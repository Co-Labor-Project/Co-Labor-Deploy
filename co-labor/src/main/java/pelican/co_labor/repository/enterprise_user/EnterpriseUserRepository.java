package pelican.co_labor.repository.enterprise_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise.EnterpriseEng;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;

import java.util.List;

@Repository
public interface EnterpriseUserRepository extends JpaRepository<EnterpriseUser, String> {
    @Query("SELECT e FROM EnterpriseUser e WHERE e.enterprise_user_id = :enterpriseUserID")
    EnterpriseUser findByEnterpriseUserId(@Param("enterpriseUserID") String enterpriseUserID);

    @Query("SELECT e FROM EnterpriseUser e WHERE e.enterprise = :enterprise")
    List<EnterpriseUser> findByEnterprise(@Param("enterprise") Enterprise enterprise);

    @Query("SELECT e FROM EnterpriseUser e WHERE e.enterpriseEng = :enterprise")
    List<EnterpriseUser> findByEnterpriseEng(@Param("enterprise") EnterpriseEng enterprise);

    @Query("SELECT e.enterprise.id FROM EnterpriseUser e WHERE e.enterprise_user_id = :userId")
    String findEnterpriseIDByUserId(@Param("userId") String userId);


    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EnterpriseUser e WHERE e.enterprise_user_id = :id")
    boolean existsByEnterprise_user_id(@Param("id") String enterprise_user_id);

}
