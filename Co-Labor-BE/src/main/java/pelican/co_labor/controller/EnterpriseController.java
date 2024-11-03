package pelican.co_labor.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise.EnterpriseEng;
import pelican.co_labor.domain.enterprise_queue.EnterpriseQueue;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;
import pelican.co_labor.service.AuthService;
import pelican.co_labor.service.EnterpriseFetchApiService;
import pelican.co_labor.service.EnterpriseRegistrationService;
import pelican.co_labor.service.EnterpriseService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Enterprise", description = "기업 관련 API")
@RestController
@RequestMapping("/api/enterprises")
public class EnterpriseController {

    private final AuthService authService;
    private final EnterpriseService enterpriseService;
    private final EnterpriseFetchApiService enterpriseFetchApiService;
    private final EnterpriseRegistrationService enterpriseRegistrationService;

    @Autowired
    public EnterpriseController(AuthService authService, EnterpriseService enterpriseService, EnterpriseFetchApiService enterpriseFetchApiService, EnterpriseRegistrationService enterpriseRegistrationService) {
        this.authService = authService;
        this.enterpriseService = enterpriseService;
        this.enterpriseFetchApiService = enterpriseFetchApiService;
        this.enterpriseRegistrationService = enterpriseRegistrationService;
    }

    @Operation(summary = "모든 기업 목록 조회", description = "등록된 모든 기업 정보를 조회합니다.")
    @GetMapping
    public List<Enterprise> getAllEnterprises() {
        return enterpriseService.getAllEnterprises();
    }

    @Operation(summary = "기업 정보 조회", description = "기업 ID를 기반으로 특정 기업의 정보를 조회합니다.")
    @GetMapping("/{enterprise_id}")
    public ResponseEntity<?> getEnterpriseById(
            @Parameter(description = "기업 ID") @PathVariable("enterprise_id") String enterprise_id) {
        try {
            Optional<Enterprise> enterprise = enterpriseService.getEnterpriseById(enterprise_id);
            if (enterprise.isPresent()) {
                return ResponseEntity.ok(enterprise.get());
            } else {
                throw new EnterpriseNotFoundException(enterprise_id);
            }
        } catch (EnterpriseNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "기업 데이터 Fetch", description = "외부 API로부터 데이터를 Fetch하여 기업 데이터를 업데이트합니다.")
    @GetMapping("/fetch")
    public String fetchAndSaveEnterpriseData() {
        try {
            enterpriseFetchApiService.fetchAndSaveEnterpriseData();
            return "Data fetched and saved successfully.";
        } catch (Exception e) {
            return "An error occurred while fetching and saving data: " + e.getMessage();
        }
    }

    @Operation(summary = "기업 등록", description = "새로운 기업을 등록하고, 기업 로고 이미지를 업로드합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)  // 요청이 멀티파트 폼 데이터를 처리하도록 지정
    public ResponseEntity<Map<String, Object>> createEnterprise(
            @Parameter(description = "기업 정보") @RequestPart("enterprise") Enterprise enterprise,
            @Parameter(description = "기업 로고 이미지 파일") @RequestPart("logo") MultipartFile logo,
            HttpServletRequest httpServletRequest) {
        Map<String, Object> response = new HashMap<>();

        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());
        // 세션에서 JSESSIONID를 찾을 수 없으면 401 UNAUTHORIZED 응답 반환
        if (jsessionId == null) {
            response.put("status", 0);
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 세션에서 가져온 username이 admin이 아니면 403 FORBIDDEN 응답 반환
        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        if (currentUser.isEmpty() || !"admin".equals(currentUser.get().get("username"))) {
            response.put("status", 0);
            response.put("message", "You are not authorized to create an enterprise");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            // 로고 이미지 저장
            String logoPath = enterpriseService.saveImage(logo);
            enterprise.setImageName(logoPath);

            // 엔터프라이즈 저장
            Enterprise savedEnterprise = enterpriseService.createEnterprise(enterprise);

            response.put("status", 1);
            response.put("message", "Enterprise created successfully.");
            response.put("enterprise", savedEnterprise);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", 0);
            response.put("message", "Error creating enterprise: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "기업 사용자 매핑", description = "사업자 등록 번호로 기업 사용자를 기업에 매핑합니다.")
    @PostMapping("/map")
    public ResponseEntity<Map<String, Object>> mapEnterprise(
            @Parameter(description = "기업 ID") @RequestParam("enterpriseId") String enterpriseId,
            HttpServletRequest httpServletRequest) {

        Map<String, Object> response = new HashMap<>();
        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());

        // 세션에서 JSESSIONID를 찾을 수 없으면 401 UNAUTHORIZED 응답 반환
        if (jsessionId == null) {
            response.put("status", 0);
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 세션에서 가져온 userType이 기업 사용자가 아니면 403 FORBIDDEN 응답 반환
        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        if (currentUser.isEmpty() || !"enterprise_user".equals(currentUser.get().get("userType"))) {
            response.put("status", 0);
            response.put("message", "기업회원이 아닙니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        // 회원에게 이미 등록된 기업이 있으면 200 OK 응답 반환
        if (authService.findEnterpriseUserById((String) currentUser.get().get("username")).get().getEnterprise() != null) {
            response.put("status", 3);
            response.put("message", "이미 등록된 기업이 있습니다.");
            return ResponseEntity.ok(response);
        }

        Enterprise enterprise = enterpriseService.getEnterpriseById(enterpriseId).orElse(null);
        if (enterprise == null) {
            response.put("status", 1);
            response.put("message", "등록되지 않은 기업입니다.");
            return ResponseEntity.ok(response);
        }

        EnterpriseUser enterpriseUser = authService.findEnterpriseUserById((String) currentUser.get().get("username")).orElse(null);
        enterpriseUser.setEnterprise(enterprise);
        authService.saveEnterpriseUser(enterpriseUser);
        response.put("status", 2);
        response.put("message", "기업 등록이 완료되었습니다.");

        return ResponseEntity.ok(response);
    }



    @Operation(summary = "기업 정보 수정", description = "기업 ID에 해당하는 기업 정보를 업데이트합니다.")
    @PatchMapping("/{enterprise_id}")
    public ResponseEntity<Map<String, Object>> updateEnterprise(
            @Parameter(description = "기업 ID") @PathVariable String enterprise_id,
            @Parameter(description = "수정할 기업 정보") @RequestBody Enterprise enterpriseDetails,
            HttpServletRequest httpServletRequest) {

        Map<String, Object> response = new HashMap<>();
        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());

        // 세션에서 JSESSIONID를 찾을 수 없으면 401 UNAUTHORIZED 응답 반환
        if (jsessionId == null) {
            response.put("status", 0);
            response.put("message", "No JSESSIONID found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 세션에서 가져온 user의 enterprise_id가 수정할 enterprise_id와 다르면 403 FORBIDDEN 응답 반환
        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        if (currentUser.isEmpty() || !enterprise_id.equals(currentUser.get().get("enterprise_id"))) {
            response.put("status", 0);
            response.put("message", "수정할 수 없는 기업입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            enterpriseService.updateEnterprise(enterprise_id, enterpriseDetails);
            response.put("status", 1);
            response.put("message", "기업 정보가 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 0);
            response.put("message", "기업 정보 업데이트 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "사업자 등록 상태 확인", description = "주어진 기업 ID로 사업자 등록 상태를 확인합니다.")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getBusinessStatus(
            @Parameter(description = "기업 ID") @RequestParam("enterpriseId") String enterpriseId) {
        try {
            ResponseEntity<String> responseEntity = enterpriseRegistrationService.isValidEnterpriseId(enterpriseId);

            // JSON 문자열을 JsonNode로 매핑
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseData = mapper.readTree(responseEntity.getBody());

            // tax_type 값을 추출
            String taxType = responseData.get("data").get(0).get("tax_type").asText();

            // Response 생성
            Map<String, Object> response = new HashMap<>();
            if ("국세청에 등록되지 않은 사업자등록번호입니다.".equals(taxType)) {
                response.put("status", 0);
                response.put("message", "국세청에 등록되지 않은 사업자등록번호입니다.");
            } else {
                response.put("status", 1);
                response.put("message", "사업자등록번호가 확인되었습니다.");
            }

            return ResponseEntity.status(responseEntity.getStatusCode()).body(response);
        } catch (Exception e) {
            // 예외 발생 시 에러 응답
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 0);
            errorResponse.put("message", "에러 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @Operation(summary = "기업 등록 대기열 추가", description = "새로운 기업을 등록 대기열에 추가합니다.")
    @PostMapping(value = "/queue", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> createEnterpriseQueue(
            @Parameter(description = "기업 대기열 정보") @RequestPart("enterpriseQueue") EnterpriseQueue enterpriseQueue,
            @Parameter(description = "기업 로고 이미지 파일") @RequestPart("logo") MultipartFile logo,
            HttpServletRequest httpServletRequest) {

        String jsessionId = extractJSessionIdFromCookie(httpServletRequest.getCookies());

        // 세션에서 JSESSIONID를 찾을 수 없으면 에러 응답 반환
        if (jsessionId == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "No JSESSIONID found");
            return response;
        }

        // 세션에서 가져온 userType이 enterprise_user가 아니면 에러 응답 반환
        Optional<Map<String, Object>> currentUser = authService.getCurrentUser(jsessionId);
        if (currentUser.isEmpty() || !"enterprise_user".equals(currentUser.get().get("userType"))) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "기업 회원이 아닙니다.");
            return response;
        }

        try {
            // 로고 이미지 저장
            String logoPath = enterpriseService.saveImage(logo);
            enterpriseQueue.setImageName(logoPath);

            enterpriseRegistrationService.registerEnterpriseQueue(enterpriseQueue);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Enterprise queue created successfully");
            response.put("enterpriseQueue", enterpriseQueue);
            return response;
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "An error occurred while creating enterprise queue: " + e.getMessage());
            return response;
        }
    }

    @Operation(summary = "기업 로고 조회", description = "기업 ID에 해당하는 기업의 로고 이미지를 조회합니다.")
    @GetMapping("/{enterprise_id}/logo")
    public ResponseEntity<Resource> getEnterpriseLogo(
            @Parameter(description = "기업 ID") @PathVariable("enterprise_id") String enterpriseId) {
        Optional<Enterprise> enterpriseOpt = enterpriseService.getEnterpriseById(enterpriseId);
        if (enterpriseOpt.isPresent()) {
            String logoPath = enterpriseOpt.get().getImageName();
            try {
                Path filePath = Paths.get("path/to/save/images").resolve(logoPath).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists()) {
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "영어로 기업 정보 조회", description = "등록된 모든 기업 정보를 영어로 조회합니다.")
    @GetMapping("/eng")
    public List<EnterpriseEng> getAllEnterprisesEng() {
        return enterpriseService.getAllEnterprisesEng();
    }

    @Operation(summary = "영어 기업 정보 조회", description = "영어로 기업 ID에 해당하는 특정 기업의 정보를 조회합니다.")
    @GetMapping("/eng/{enterprise_id}")
    public ResponseEntity<?> getEnterpriseEngById(
            @Parameter(description = "기업 ID") @PathVariable("enterprise_id") String enterprise_id) {
        try {
            Optional<EnterpriseEng> enterprise = enterpriseService.getEnterpriseEngById(enterprise_id);
            if (enterprise.isPresent()) {
                return ResponseEntity.ok(enterprise.get());
            } else {
                throw new EnterpriseNotFoundException(enterprise_id);
            }
        } catch (EnterpriseNotFoundException ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse {
        private int status;
        private String message;
    }

    static class EnterpriseNotFoundException extends RuntimeException {
        public EnterpriseNotFoundException(String enterpriseId) {
            super("Enterprise not found with id: " + enterpriseId);
        }
    }

    // 쿠키에서 JSESSIONID 추출하는 헬퍼 메서드
    private String extractJSessionIdFromCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
