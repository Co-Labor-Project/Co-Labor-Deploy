//package pelican.co_labor.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import pelican.co_labor.domain.enterprise.Enterprise;
//import pelican.co_labor.domain.job.Job;
//import pelican.co_labor.domain.review.Review;
//import pelican.co_labor.repository.enterprise.EnterpriseRepository;
//import pelican.co_labor.repository.job.JobRepository;
//import pelican.co_labor.repository.review.ReviewRepository;
//import pelican.co_labor.service.Search.SearchService;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//public class SearchControllerTest {
//
//    @Mock
//    private JobRepository jobRepository;
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @Mock
//    private EnterpriseRepository enterpriseRepository;
//
//    @Mock
//    private SearchService searchService;
//
//    @InjectMocks
//    private SearchController searchController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testSearch() {
//        // 더미 Job 데이터 생성
//        Job job1 = new Job();
//        job1.setTitle("Software Engineer");
//        job1.setDescription("Develop and maintain software solutions.");
//        job1.setGender("Any");
//        job1.setAge("Any");
//        job1.setViews(100);
//        job1.setDead_date(java.time.LocalDateTime.now().plusDays(30));
//        job1.setCreated_at(java.time.LocalDateTime.now());
//        job1.setModified_at(java.time.LocalDateTime.now());
//
//        Job job2 = new Job();
//        job2.setTitle("Data Scientist");
//        job2.setDescription("Analyze and interpret complex data sets.");
//        job2.setGender("Any");
//        job2.setAge("Any");
//        job2.setViews(150);
//        job2.setDead_date(java.time.LocalDateTime.now().plusDays(45));
//        job2.setCreated_at(java.time.LocalDateTime.now());
//        job2.setModified_at(java.time.LocalDateTime.now());
//
//        List<Job> jobs = Arrays.asList(job1, job2);
//
//        // 더미 Review 데이터 생성
//        Review review1 = new Review();
//        review1.setTitle("Great Place to Work");
//        review1.setRating(5);
//        review1.setPromotion_rating(4);
//        review1.setSalary_rating(3);
//        review1.setBalance_rating(5);
//        review1.setCulture_rating(4);
//        review1.setManagement_rating(4);
//        review1.setPros("Great work-life balance and culture.");
//        review1.setCons("Salary could be higher.");
//        review1.setLike_count(10);
//        review1.setCreated_at(java.time.LocalDateTime.now());
//        review1.setModified_at(java.time.LocalDateTime.now());
//
//        Review review2 = new Review();
//        review2.setTitle("Challenging Environment");
//        review2.setRating(4);
//        review2.setPromotion_rating(3);
//        review2.setSalary_rating(4);
//        review2.setBalance_rating(3);
//        review2.setCulture_rating(4);
//        review2.setManagement_rating(3);
//        review2.setPros("Great learning opportunities.");
//        review2.setCons("Work-life balance can be challenging.");
//        review2.setLike_count(8);
//        review2.setCreated_at(java.time.LocalDateTime.now());
//        review2.setModified_at(java.time.LocalDateTime.now());
//
//        List<Review> reviews = Arrays.asList(review1, review2);
//
//        // 더미 Enterprise 데이터 생성
//        Enterprise enterprise1 = new Enterprise();
//        enterprise1.setName("Tech Company");
//        enterprise1.setAddress("123 Tech Street");
//        enterprise1.setDescription("Leading tech company in the industry.");
//
//        Enterprise enterprise2 = new Enterprise();
//        enterprise2.setName("Data Corp");
//        enterprise2.setAddress("456 Data Avenue");
//        enterprise2.setDescription("Innovative data solutions provider.");
//
//        List<Enterprise> enterprises = Arrays.asList(enterprise1, enterprise2);
//
//        // Mock 객체의 동작 정의
//        when(searchService.searchJobs("tech")).thenReturn(jobs);
//        when(searchService.searchReviews("tech")).thenReturn(reviews);
//        when(searchService.searchEnterprises("tech")).thenReturn(enterprises);
//
//        // 메소드 호출 및 결과 검증
//        Map<String, Object> response = searchController.search("tech");
//
//        // AssertJ를 사용한 JSON 검증
//        assertThat(response).isNotNull();
//        assertThat(response.get("jobs")).isInstanceOf(List.class);
//        assertThat(response.get("reviews")).isInstanceOf(List.class);
//        assertThat(response.get("enterprises")).isInstanceOf(List.class);
//
//        List<?> jobList = (List<?>) response.get("jobs");
//        List<?> reviewList = (List<?>) response.get("reviews");
//        List<?> enterpriseList = (List<?>) response.get("enterprises");
//
//        assertThat(jobList).hasSize(2);
//        assertThat(reviewList).hasSize(2);
//        assertThat(enterpriseList).hasSize(2);
//
//        assertThat(jobList.get(0)).isInstanceOf(Job.class);
//        assertThat(((Job) jobList.get(0)).getTitle()).isEqualTo("Software Engineer");
//        assertThat(((Job) jobList.get(1)).getTitle()).isEqualTo("Data Scientist");
//
//        assertThat(reviewList.get(0)).isInstanceOf(Review.class);
//        assertThat(((Review) reviewList.get(0)).getTitle()).isEqualTo("Great Place to Work");
//        assertThat(((Review) reviewList.get(1)).getTitle()).isEqualTo("Challenging Environment");
//
//        assertThat(enterpriseList.get(0)).isInstanceOf(Enterprise.class);
//        assertThat(((Enterprise) enterpriseList.get(0)).getName()).isEqualTo("Tech Company");
//        assertThat(((Enterprise) enterpriseList.get(1)).getName()).isEqualTo("Data Corp");
//    }
//}
