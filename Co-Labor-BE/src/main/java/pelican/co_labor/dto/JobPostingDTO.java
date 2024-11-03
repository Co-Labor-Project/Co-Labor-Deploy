package pelican.co_labor.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JobPostingDTO {

    // 채용정보
    private String jobDescription; // 업무내용
    private String applicantRequirements; // 지원자격
    private String preferredQualifications; // 우대사항
    private String applicationMethod; // 접수방법

    // 근무조건
    private String workingDays; // 근무요일
    private String workingHours; // 근무시간
    private String workingPeriod; // 근무기간
    private String salary; // 급여
}
