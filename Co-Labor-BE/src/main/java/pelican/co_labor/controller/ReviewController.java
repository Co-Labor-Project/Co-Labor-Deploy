package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.labor_user.LaborUser;
import pelican.co_labor.domain.review.Review;
import pelican.co_labor.dto.ReviewDTO;
import pelican.co_labor.service.AuthService;
import pelican.co_labor.service.EnterpriseService;
import pelican.co_labor.service.ReviewService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Review", description = "기업 리뷰 관련 API")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;
    private final EnterpriseService enterpriseService;

    @Autowired
    public ReviewController(ReviewService reviewService, AuthService authService, EnterpriseService enterpriseService) {
        this.reviewService = reviewService;
        this.authService = authService;
        this.enterpriseService = enterpriseService;
    }

    private String extractJSessionIdFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Optional<Map<String, Object>> getSessionUser(HttpServletRequest httpServletRequest) {
        String sessionId = extractJSessionIdFromCookies(httpServletRequest.getCookies());
        if (sessionId == null) {
            return Optional.empty();
        }
        return authService.getCurrentUser(sessionId);
    }

    @Operation(summary = "모든 리뷰 조회", description = "등록된 모든 기업 리뷰를 조회합니다.")
    @GetMapping("/all")
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @Operation(summary = "특정 리뷰 조회", description = "리뷰 ID로 특정 기업 리뷰를 조회합니다.")
    @GetMapping
    public Review getReviewById(
            @Parameter(description = "리뷰 ID") @RequestParam("reviewId") String review_id) {
        return reviewService.getReviewById(review_id);
    }

    @Operation(summary = "특정 기업의 모든 리뷰 조회", description = "기업 ID로 해당 기업의 모든 리뷰를 조회합니다.")
    @GetMapping("/{enterpriseId}")
    public List<Review> getReviewsByEnterpriseId(
            @Parameter(description = "기업 ID") @PathVariable("enterpriseId") String enterprise_id) {
        return reviewService.getReviewByEnterpriseId(enterprise_id);
    }

    @Operation(summary = "리뷰 작성", description = "특정 기업에 대한 리뷰를 작성합니다.")
    @PostMapping("/{enterpriseId}")
    public ResponseEntity<Object> addReview(
            @Parameter(description = "기업 ID") @PathVariable("enterpriseId") String enterprise_id,
            @RequestBody ReviewDTO reviewDTO, HttpServletRequest httpServletRequest) {

        // 세션에서 사용자 정보 가져오기
        Optional<Map<String, Object>> userSession = getSessionUser(httpServletRequest);
        if (userSession.isEmpty()) {
            // 세션이 없을 경우 401 상태와 함께 명확한 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인되지 않았습니다. 세션이 만료되었거나 유효하지 않습니다."));
        }

        String username = userSession.get().get("username").toString();
        String userType = userSession.get().get("userType").toString();

        // 노동자만 리뷰를 작성할 수 있도록 제한
        if (!"labor_user".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "리뷰를 작성할 권한이 없습니다. 노동자만 작성할 수 있습니다."));
        }

        Optional<LaborUser> laborUserOpt = authService.findLaborUserById(username);
        if (laborUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "사용자를 찾을 수 없습니다."));
        }

        LaborUser laborUser = laborUserOpt.get();
        Optional<Enterprise> enterpriseOpt = enterpriseService.getEnterpriseById(enterprise_id);
        if (enterpriseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "해당 기업을 찾을 수 없습니다."));
        }

        Enterprise enterprise = enterpriseOpt.get();

        Review review = new Review();
        review.setLaborUser(laborUser);
        review.setEnterprise(enterprise);
        review.setTitle(reviewDTO.getTitle());
        review.setRating(reviewDTO.getRating());
        review.setPromotion_rating(reviewDTO.getPromotionRating());
        review.setSalary_rating(reviewDTO.getSalaryRating());
        review.setBalance_rating(reviewDTO.getBalanceRating());
        review.setCulture_rating(reviewDTO.getCultureRating());
        review.setManagement_rating(reviewDTO.getManagementRating());
        review.setPros(reviewDTO.getPros());
        review.setCons(reviewDTO.getCons());
        review.setLike_count(0); // 기본 값 설정

        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @Operation(summary = "리뷰 수정", description = "특정 리뷰 ID에 해당하는 리뷰를 수정합니다.")
    @PatchMapping("/{review_id}")
    public ResponseEntity<Object> updateReview(
            @Parameter(description = "리뷰 ID") @PathVariable("review_id") String review_id,
            @RequestBody Review review, HttpServletRequest httpServletRequest) {

        // 세션에서 사용자 정보 가져오기
        Optional<Map<String, Object>> userSession = getSessionUser(httpServletRequest);
        if (userSession.isEmpty()) {
            // 세션이 없을 경우 401 상태와 함께 명확한 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인되지 않았습니다. 세션이 만료되었거나 유효하지 않습니다."));
        }

        String username = userSession.get().get("username").toString();
        String userType = userSession.get().get("userType").toString();

        // 노동자만 리뷰를 수정할 수 있도록 제한
        if (!"labor".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "리뷰를 수정할 권한이 없습니다. 노동자만 수정할 수 있습니다."));
        }

        Review existingReview = reviewService.getReviewById(review_id);
        if (existingReview == null || !existingReview.getLaborUser().getLaborUserId().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "리뷰를 수정할 권한이 없습니다. 본인이 작성한 리뷰만 수정할 수 있습니다."));
        }

        return ResponseEntity.ok(reviewService.updateReview(review_id, review));
    }

    @Operation(summary = "리뷰 삭제", description = "특정 리뷰 ID에 해당하는 리뷰를 삭제합니다.")
    @DeleteMapping("/{review_id}")
    public ResponseEntity<Object> deleteReview(
            @Parameter(description = "리뷰 ID") @PathVariable("review_id") String review_id,
            HttpServletRequest httpServletRequest) {

        // 세션에서 사용자 정보 가져오기
        Optional<Map<String, Object>> userSession = getSessionUser(httpServletRequest);
        if (userSession.isEmpty()) {
            // 세션이 없을 경우 401 상태와 함께 명확한 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인되지 않았습니다. 세션이 만료되었거나 유효하지 않습니다."));
        }

        String username = userSession.get().get("username").toString();
        String userType = userSession.get().get("userType").toString();

        // 노동자만 리뷰를 삭제할 수 있도록 제한
        if (!"labor".equals(userType)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "리뷰를 삭제할 권한이 없습니다. 노동자만 삭제할 수 있습니다."));
        }

        Review existingReview = reviewService.getReviewById(review_id);
        if (existingReview == null || !existingReview.getLaborUser().getLaborUserId().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "리뷰를 삭제할 권한이 없습니다. 본인이 작성한 리뷰만 삭제할 수 있습니다."));
        }

        reviewService.deleteReview(review_id);
        return ResponseEntity.ok(Map.of("message", "리뷰가 성공적으로 삭제되었습니다."));
    }
}
