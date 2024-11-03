package pelican.co_labor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseDocument {
    private String 사건명;
    private String 사건종류명;
    private String 판시사항;
    private String 판결요지;
    private String 참조조문;
    private String 사건번호;

    public CaseDocument(Map<String, Object> result) {
        this.사건명 = (String) result.getOrDefault("사건명", "");
        this.사건종류명 = (String) result.getOrDefault("사건종류명", "");
        this.판시사항 = (String) result.getOrDefault("판시사항", "");
        this.판결요지 = (String) result.getOrDefault("판결요지", "");
        this.참조조문 = (String) result.getOrDefault("참조조문", "");
        this.사건번호 = (String) result.getOrDefault("사건번호", "");
    }

    public CaseDocument() {
    }

    // 고유 문서 이름을 생성하는 메서드
    public String generateCaseDocumentId() {
        return 사건번호;
    }
}
