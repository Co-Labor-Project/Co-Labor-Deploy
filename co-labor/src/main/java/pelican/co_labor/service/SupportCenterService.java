package pelican.co_labor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pelican.co_labor.domain.support_center.SupportCenter;
import pelican.co_labor.repository.support_center.SupportCenterRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class SupportCenterService {

    @Value("${public.data.api.key.encoded}")
    private String apiKeyEncoded;

    @Value("${public.data.api.key.decoded}")
    private String apiKeyDecoded;

    private final SupportCenterRepository supportCenterRepository;
    private final NaverGeocodingService naverGeocodingService;

    @Autowired
    public SupportCenterService(SupportCenterRepository supportCenterRepository, NaverGeocodingService naverGeocodingService) {
        this.supportCenterRepository = supportCenterRepository;
        this.naverGeocodingService = naverGeocodingService;
    }



    public void fetchDataFromApi() {
        supportCenterRepository.deleteAll(); // 기존 데이터 삭제

        // 인코딩된 키로 데이터 가져오기 시도
        try {
            fetchDataUsingKey(apiKeyEncoded);
        } catch (Exception e) {
            System.out.println("Failed with encoded key: " + e.getMessage());
        }

        // 디코딩된 키로 데이터 가져오기 시도
        try {
            fetchDataUsingKey(apiKeyDecoded);
        } catch (Exception e) {
            System.out.println("Failed with decoded key: " + e.getMessage());
        }
    }

    private void fetchDataUsingKey(String apiKey) {
        String url = "https://api.odcloud.kr/api/3038226/v1/uddi:6c52a2c3-b1ed-407c-ada3-8c14769beec1?page=1&perPage=100&serviceKey=" + apiKey;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<Map<String, Object>> records = (List<Map<String, Object>>) response.getBody().get("data");

            for (Map<String, Object> record : records) {
                SupportCenter supportCenter = new SupportCenter();
                supportCenter.setSupport_center_id(Long.valueOf(record.get("순번").toString()));
                supportCenter.setCenter_type(record.get("유형").toString());
                supportCenter.setName(record.get("기관명(거점센터 운영기관)").toString());
                supportCenter.setAddress(record.get("소재지").toString());
                supportCenter.setPhone(record.get("연락처").toString());

                // 네이버 지오코딩 서비스 사용하여 위도와 경도 설정
                String coordinates = naverGeocodingService.getCoordinates(supportCenter.getAddress());
                String[] latLng = coordinates.split(",");
                supportCenter.setLatitude(Double.parseDouble(latLng[0]));
                supportCenter.setLongitude(Double.parseDouble(latLng[1]));

                supportCenterRepository.save(supportCenter);
            }
        } else {
            throw new RuntimeException("Failed to fetch data from API using key: " + apiKey);
        }
    }

    public List<SupportCenter> getAllSupportCenters() {
        return supportCenterRepository.findAll();
    }
}
