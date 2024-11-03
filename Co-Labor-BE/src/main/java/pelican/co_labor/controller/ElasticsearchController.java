package pelican.co_labor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pelican.co_labor.service.ElasticsearchService;
import pelican.co_labor.service.JsonLoader;
import pelican.co_labor.dto.CaseDocument;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/elasticsearch")
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchController.class);

    @Value("${elasticsearch.index-name}")
    private String defaultIndexName;

    @Autowired
    public ElasticsearchController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    @PostMapping("/bulk-index-directory")
    public ResponseEntity<String> bulkIndexFromDirectory(@RequestParam String directoryPath) {
        logger.info("Received request to bulk index from directory: {}", directoryPath);
        try {
            List<CaseDocument> documents = JsonLoader.loadJsonFromDirectory(directoryPath);
            logger.info("Loaded {} documents from directory", documents.size());
            if (documents.isEmpty()) {
                logger.warn("No documents loaded from directory");
                return ResponseEntity.ok("No documents found to index.");
            }

            elasticsearchService.bulkIndexDocuments(defaultIndexName, documents);
            String message = "Batch indexing from directory completed. Attempted to index " + documents.size() + " documents.";
            logger.info(message);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            logger.error("Error during bulk indexing", e);
            return ResponseEntity.internalServerError().body("Error during bulk indexing: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllDocuments() {
        try {
            long deletedCount = elasticsearchService.deleteAllDocuments(defaultIndexName);
            return ResponseEntity.ok("Deleted " + deletedCount + " documents");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error deleting documents: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDocuments(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Map<String, Object>> results = elasticsearchService.searchDocuments(defaultIndexName, query);
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error searching documents: " + e.getMessage());
        }
    }
}