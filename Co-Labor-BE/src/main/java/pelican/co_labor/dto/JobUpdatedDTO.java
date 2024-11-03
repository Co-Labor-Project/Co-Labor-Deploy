package pelican.co_labor.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class JobUpdatedDTO {
    private String title;
    private String description;
    private String requirement;
    private String jobRole;
    private String experience;
    private String employmentType;
    private String address1;
    private String address2;
    private String address3;
    private String skills;
    private LocalDate deadDate;
}
