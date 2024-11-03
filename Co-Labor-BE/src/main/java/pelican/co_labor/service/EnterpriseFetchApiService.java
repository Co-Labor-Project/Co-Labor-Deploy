package pelican.co_labor.service;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.repository.enterprise.EnterpriseRepository;

import java.util.regex.Pattern;

@Service
public class EnterpriseFetchApiService {

    @Value("${public.enterprise.list.key.decoded}")
    private String serviceKey;

    @Autowired
    private final EnterpriseRepository enterpriseRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public EnterpriseFetchApiService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    @PostConstruct
    public void init() {
        if (enterpriseRepository.count() == 0) {
            fetchAndSaveEnterpriseData();
        }
    }

    public void fetchAndSaveEnterpriseData() {
        String apiUrl = "http://apis.data.go.kr/1160100/service/GetCorpBasicInfoService_V2/getCorpOutline_V2";
        int numOfRows = 1000; // 한 번에 가져올 행 수
        int pageNo = 1; // 시작 페이지 번호

        while (true) {
            String urlStr = apiUrl + "?serviceKey=" + serviceKey + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo + "&resultType=json";
            ResponseEntity<String> response = restTemplate.getForEntity(urlStr, String.class);

            String responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                System.err.println("Empty response body for URL: " + urlStr);
                break;
            }

            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(responseBody);
            } catch (Exception e) {
                System.err.println("Invalid JSON response: " + responseBody);
                break;
            }

            JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

            if (items.isEmpty() || pageNo>=15) {
                break; // 데이터가 없으면 루프 종료
            }

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                if (item.isNull("corpNm")) continue;

                String enpBsadr = item.getString("enpBsadr");
                if (Pattern.matches(".*[가-힣]+.*", enpBsadr)) {
                    String[] addressParts = enpBsadr.split(" ", 3);

                    Enterprise enterprise = new Enterprise();
                    enterprise.setEnterprise_id(item.getString("bzno"));
                    enterprise.setName(item.getString("corpNm"));
                    enterprise.setType(item.getString("sicNm"));
                    enterprise.setDescription(item.getString("enpMainBizNm"));
                    enterprise.setPhone_number(item.getString("enpTlno"));

                    if (addressParts.length > 0) {
                        enterprise.setAddress1(addressParts[0]);
                    }
                    if (addressParts.length > 1) {
                        enterprise.setAddress2(addressParts[1]);
                    }
                    if (addressParts.length > 2) {
                        enterprise.setAddress3(addressParts[2]);
                    }

                    enterpriseRepository.save(enterprise); // 이미 존재하면 덮어씌움
                }
            }

            pageNo++; // 다음 페이지로 넘어감
        }
    }
}
