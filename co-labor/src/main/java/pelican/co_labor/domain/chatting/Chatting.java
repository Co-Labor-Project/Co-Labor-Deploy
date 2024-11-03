package pelican.co_labor.domain.chatting;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pelican.co_labor.domain.labor_user.LaborUser;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chatting")
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatting_id;

    @ManyToOne
    @JoinColumn(name = "labor_user_id")
    private LaborUser laborUser;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean my_message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
}
