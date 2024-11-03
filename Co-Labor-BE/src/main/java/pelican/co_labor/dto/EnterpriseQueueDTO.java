package pelican.co_labor.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnterpriseQueueDTO {
    private String enterpriseId;
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String type;
    private String phoneNumber;
    private String description;
}
