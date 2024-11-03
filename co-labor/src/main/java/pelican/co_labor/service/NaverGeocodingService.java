package pelican.co_labor.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverGeocodingService {

    @Value("${naver.map.client.id}")
    private String clientId;

    @Value("${naver.map.client.secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public NaverGeocodingService() {
        this.restTemplate = new RestTemplate();
    }

    // 1. 주소를 좌표로 변환하는 기존 메서드 (주소 → 위도, 경도)
    public String getCoordinates(String address) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + address;
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        JSONObject addressObject = jsonResponse.getJSONArray("addresses").getJSONObject(0);

        String latitude = addressObject.getString("y");
        String longitude = addressObject.getString("x");

        return latitude + "," + longitude;
    }

    // 2. 위도, 경도를 사용하여 지역명을 가져오는 메서드 (리버스 지오코딩: 위도, 경도 → 주소)
    public String getRegionName(double latitude, double longitude) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?coords=" + longitude + "," + latitude
                + "&orders=addr&output=json";
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody());

        // 리버스 지오코딩에서 받은 JSON 응답을 파싱
        JSONObject regionObject = jsonResponse.getJSONArray("results").getJSONObject(0).getJSONObject("region");

        String sido = regionObject.getJSONObject("area1").getString("name");  // 시/도
        String sigungu = regionObject.getJSONObject("area2").getString("name");  // 시/군/구

        return sido + " " + sigungu;
    }

    // 공통 헤더 생성 메서드
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        return headers;
    }
}
