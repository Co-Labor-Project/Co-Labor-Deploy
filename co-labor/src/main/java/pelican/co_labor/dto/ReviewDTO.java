package pelican.co_labor.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {
    private String userId;
    private String enterpriseId;

    private String title;
    private int rating;
    private int promotionRating;
    private int salaryRating;
    private int balanceRating;
    private int cultureRating;
    private int managementRating;
    private String pros;
    private String cons;
}
