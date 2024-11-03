package pelican.co_labor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pelican.co_labor.domain.enterprise_queue.EnterpriseQueue;
import pelican.co_labor.repository.enterprise_queue.EnterpriseQueueRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EnterpriseRegistrationService {
    private final RestTemplate restTemplate;
    private final EnterpriseQueueRepository enterpriseQueueRepository;

    @Value("${public.enterprise.id.check.key.decoded}")
    private String serviceKey;

    public ResponseEntity<String> isValidEnterpriseId(String EnterpriseId) {
        String apiUrl = "http://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey=" + serviceKey;

        // 요청 바디 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("b_no", Collections.singletonList(EnterpriseId));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );
    }

    public void registerEnterpriseQueue(EnterpriseQueue enterpriseQueue) {
        enterpriseQueueRepository.save(enterpriseQueue);
    }
}
