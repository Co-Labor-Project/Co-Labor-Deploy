package pelican.co_labor.domain.enterprise;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "enterprise")
public class Enterprise {

    @Id
    @Column(unique = true)
    //사업자 등록 번호
    private String enterprise_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address1;

    @Column(nullable = false)
    private String address2;

    @Column(nullable = true)
    private String address3;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String phone_number;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "image_name", nullable = true)
    private String imageName;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }


}
