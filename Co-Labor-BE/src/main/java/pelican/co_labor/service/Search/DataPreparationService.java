//package pelican.co_labor.service.Search;
//
//import jakarta.annotation.PostConstruct;
//import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
//import org.deeplearning4j.models.word2vec.Word2Vec;
//import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
//import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
//import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pelican.co_labor.domain.enterprise.Enterprise;
//import pelican.co_labor.domain.job.Job;
//import pelican.co_labor.domain.review.Review;
//import pelican.co_labor.repository.enterprise.EnterpriseRepository;
//import pelican.co_labor.repository.job.JobRepository;
//import pelican.co_labor.repository.review.ReviewRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Service
//public class DataPreparationService {
//
//    private static final Logger logger = LoggerFactory.getLogger(DataPreparationService.class);
//
//    @Autowired
//    private JobRepository jobRepository;
//
//    @Autowired
//    private ReviewRepository reviewRepository;
//
//    @Autowired
//    private EnterpriseRepository enterpriseRepository;
//
//    @PostConstruct
//    public void fetchDataAndTrainModel() {
//        try {
//            logger.info("Starting data extraction and model training...");
//
//            // 각각의 새로운 필드에 접근하여 데이터를 추출합니다.
//
//            List<String> jobTitles = getSafeList(jobRepository.findAll().stream()
//                    .map(Job::getTitle)
//                    .collect(Collectors.toList()));
//
//            List<String> jobConditions = getSafeList(jobRepository.findAll().stream()
//                    .map(Job::getRequirement)
//                    .collect(Collectors.toList()));
//
//            List<String> jobDescription = getSafeList(jobRepository.findAll().stream()
//                    .map(Job::getDescription)
//                    .collect(Collectors.toList()));
//
//            logger.info("Extracting data from Review repository...");
//            List<String> reviewPros = getSafeList(reviewRepository.findAll().stream()
//                    .map(Review::getPros)
//                    .collect(Collectors.toList()));
//
//            List<String> reviewCons = getSafeList(reviewRepository.findAll().stream()
//                    .map(Review::getCons)
//                    .collect(Collectors.toList()));
//
//            List<String> reviewTitles = getSafeList(reviewRepository.findAll().stream()
//                    .map(Review::getTitle)
//                    .collect(Collectors.toList()));
//
//            logger.info("Extracting data from Enterprise repository...");
//            List<String> enterpriseDescriptions = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getDescription)
//                    .collect(Collectors.toList()));
//
//            List<String> enterpriseNames = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getName)
//                    .collect(Collectors.toList()));
//
//            List<String> enterpriseAddresses1 = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getAddress1)
//                    .collect(Collectors.toList()));
//
//            List<String> enterpriseAddresses2 = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getAddress2)
//                    .collect(Collectors.toList()));
//
//            List<String> enterpriseAddresses3 = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getAddress3)
//                    .collect(Collectors.toList()));
//
//            List<String> enterpriseTypes = getSafeList(enterpriseRepository.findAll().stream()
//                    .map(Enterprise::getType)
//                    .collect(Collectors.toList()));
//
//            // 모든 데이터를 하나의 리스트로 합치기
//            logger.info("Combining all extracted data into a single list...");
//            List<String> sentences = Stream.of(
//                            jobDescription,jobTitles, reviewPros, reviewCons, reviewTitles, enterpriseDescriptions, enterpriseNames,
//                            enterpriseAddresses1, enterpriseAddresses2, enterpriseAddresses3, enterpriseTypes
//                            )
//                    .flatMap(List::stream)
//                    .collect(Collectors.toList());
//
//            logger.info("Data extraction completed. Total number of sentences: {}", sentences.size());
//
//            // 모델 학습
//            logger.info("Starting Word2Vec model training...");
//            trainWord2VecModel(sentences);
//            logger.info("Word2Vec model training completed.");
//        } catch (Exception e) {
//            logger.error("An error occurred during data extraction and model training", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    private List<String> getSafeList(List<String> list) {
//        return list != null ? list : new ArrayList<>();
//    }
//
//    private void trainWord2VecModel(List<String> sentences) {
//        try {
//            logger.info("Initializing Word2Vec model...");
//            // Word2Vec 모델 학습
//            CollectionSentenceIterator sentenceIterator = new CollectionSentenceIterator(sentences);
//            DefaultTokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
//            tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
//
//            Word2Vec vec = new Word2Vec.Builder()
//                    .minWordFrequency(1)
//                    .iterations(5)
//                    .layerSize(200)
//                    .seed(42)
//                    .windowSize(10)
//                    .iterate(sentenceIterator)
//                    .tokenizerFactory(tokenizerFactory)
//                    .build();
//
//            logger.info("Fitting Word2Vec model...");
//            vec.fit();
//
//            logger.info("Saving Word2Vec model to word2vecModel.txt...");
//            // 모델 저장
//            WordVectorSerializer.writeWord2VecModel(vec, "word2vecModel.txt");
//            logger.info("Word2Vec model saved successfully.");
//        } catch (Exception e) {
//            logger.error("An error occurred during Word2Vec model training", e);
//            throw new RuntimeException(e);
//        }
//    }
//}
