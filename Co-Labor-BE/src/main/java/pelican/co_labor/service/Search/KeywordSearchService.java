//package pelican.co_labor.service.Search;
//
//import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
//import org.deeplearning4j.models.word2vec.Word2Vec;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class KeywordSearchService {
//
//    private static final Logger logger = LoggerFactory.getLogger(KeywordSearchService.class);
//    private Word2Vec word2Vec;
//
//    public KeywordSearchService() {
//        try {
//            loadModel();
//        } catch (Exception e) {
//            logger.error("An error occurred while loading the Word2Vec model", e);
//        }
//    }
//
//    private void loadModel() {
//        File modelFile = new File("word2vecModel.txt");
//        if (modelFile.exists()) {
//            logger.info("Loading Word2Vec model from file...");
//            word2Vec = WordVectorSerializer.readWord2VecModel(modelFile);
//            logger.info("Word2Vec model loaded successfully.");
//        } else {
//            logger.warn("Word2Vec model file not found. Please train the model first.");
//        }
//    }
//
//    public List<String> searchSimilarWords(String keyword) {
//        if (word2Vec == null) {
//            logger.warn("Word2Vec model is not loaded. Please train the model first.");
//            return Collections.emptyList();
//        }
//
//        if (!word2Vec.hasWord(keyword)) {
//            logger.warn("Keyword '{}' not found in Word2Vec model vocabulary.", keyword);
//            return Collections.emptyList();
//        }
//
//        List<String> similarWords = word2Vec.wordsNearest(keyword, 10).stream()
//                .filter(word -> word.matches("[가-힣]+"))  // Filter to include only Korean words
//                .collect(Collectors.toList());
//
//        // Add the original keyword to the result list
//        if (!similarWords.contains(keyword) && keyword.matches("[가-힣]+")) {
//            similarWords.add(keyword);
//        }
//
//        logger.info("Similar words for '{}': {}", keyword, similarWords);
//        return similarWords;
//    }
//}
