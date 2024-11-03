package pelican.co_labor.domain.job;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long job_id;

    @ManyToOne
    @JoinColumn(name = "enterprise_user_id")
    private EnterpriseUser enterpriseUser;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(columnDefinition = "TEXT")
    private String jobRole;

    @Column(columnDefinition = "TEXT")
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String employmentType;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = true)
    private String address2;

    @Column(nullable = true)
    private String address3;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(nullable = false)
    private int views;

    @Column(name = "dead_date", nullable = false)
    private LocalDate deadDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime modified_at;

    @Column(nullable = true)
    private String imageName;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        created_at = now;
        modified_at = now;
    }

    @PreUpdate
    protected void onUpdate() {
        modified_at = LocalDateTime.now();
    }

}
