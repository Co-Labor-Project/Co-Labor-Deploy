package pelican.co_labor.domain.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.labor_user.LaborUser;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id;

    @ManyToOne
    @JoinColumn(name = "labor_user_id")
    private LaborUser laborUser;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private int promotion_rating;

    @Column(nullable = false)
    private int salary_rating;

    @Column(nullable = false)
    private int balance_rating;

    @Column(nullable = false)
    private int culture_rating;

    @Column(nullable = false)
    private int management_rating;

    @Column(columnDefinition = "TEXT")
    private String pros;

    @Column(columnDefinition = "TEXT")
    private String cons;

    @Column(nullable = false)
    private int like_count;

    @Column(nullable = false, updatable = false)
    private LocalDate  created_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDate.now();
        modified_at = LocalDate.now();
    }

    @Column(nullable = false)
    private LocalDate  modified_at;

    @PreUpdate
    protected void onUpdate() {
        modified_at = LocalDate.now();
    }

}
