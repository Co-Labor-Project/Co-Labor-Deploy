package pelican.co_labor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pelican.co_labor.domain.review.Review;
import pelican.co_labor.repository.review.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(String review_id) {
        return reviewRepository.findById(Long.parseLong(review_id)).orElse(null);
    }

    public List<Review> getReviewByEnterpriseId(String enterprise_id) {
        return reviewRepository.getReviewByEnterpriseId(Long.parseLong(enterprise_id));
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review updateReview(String reviewId, Review reviewDetails) {
        // 리뷰를 ID로 조회
        Review existingReview = reviewRepository.findById(Long.parseLong(reviewId)).orElse(null);

        if (existingReview != null) {
            // 필드 업데이트: null이 아닌 값만 업데이트
            if (reviewDetails.getPros() != null) existingReview.setPros(reviewDetails.getPros());
            if (reviewDetails.getCons() != null) existingReview.setCons(reviewDetails.getCons());
            if (reviewDetails.getRating() != 0) existingReview.setRating(reviewDetails.getRating());
            if (reviewDetails.getPromotion_rating() != 0)
                existingReview.setPromotion_rating(reviewDetails.getPromotion_rating());
            if (reviewDetails.getSalary_rating() != 0)
                existingReview.setSalary_rating(reviewDetails.getSalary_rating());
            if (reviewDetails.getBalance_rating() != 0)
                existingReview.setBalance_rating(reviewDetails.getBalance_rating());
            if (reviewDetails.getCulture_rating() != 0)
                existingReview.setCulture_rating(reviewDetails.getCulture_rating());
            if (reviewDetails.getManagement_rating() != 0)
                existingReview.setManagement_rating(reviewDetails.getManagement_rating());
            if (reviewDetails.getLike_count() != 0) existingReview.setLike_count(reviewDetails.getLike_count());
            if (reviewDetails.getTitle() != null) existingReview.setTitle(reviewDetails.getTitle());

            // 리뷰를 업데이트하여 저장
            return reviewRepository.save(existingReview);
        }

        // 리뷰가 존재하지 않으면 null 반환
        return null;
    }

    public void deleteReview(String review_id) {
        reviewRepository.deleteById(Long.parseLong(review_id));
    }
}
