package pelican.co_labor.domain.enterprise_user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise.EnterpriseEng;
import pelican.co_labor.dto.auth.EnterpriseUserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "enterprise_user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class EnterpriseUser {

    @Id
    private String enterprise_user_id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;
    @ManyToOne
    @JoinColumn(name = "enterprise_eng_id")
    private EnterpriseEng enterpriseEng;


    public static EnterpriseUser toEnterpriseUser(EnterpriseUserDTO enterpriseUserDTO) {
        EnterpriseUser enterpriseUser = new EnterpriseUser();
        enterpriseUser.setEnterprise_user_id(enterpriseUserDTO.getUsername());
        enterpriseUser.setPassword(enterpriseUserDTO.getPassword());
        enterpriseUser.setName(enterpriseUserDTO.getName());
        enterpriseUser.setEmail(enterpriseUserDTO.getEmail());
        return enterpriseUser;
    }

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
