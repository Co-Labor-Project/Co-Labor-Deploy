package pelican.co_labor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pelican.co_labor.dto.CaseDocument;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    private static final Logger logger = LoggerFactory.getLogger(JsonLoader.class);

    public static List<CaseDocument> loadJsonFromDirectory(String directoryPath) throws IOException {
        logger.info("Loading JSON files from directory: {}", directoryPath);
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            logger.error("Directory does not exist or is not a directory: {}", directoryPath);
            throw new IOException("Invalid directory path: " + directoryPath);
        }

        List<CaseDocument> documents = new ArrayList<>();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) {
            logger.warn("No JSON files found in directory: {}", directoryPath);
            return documents;
        }

        logger.info("Found {} JSON files", files.length);
        ObjectMapper objectMapper = new ObjectMapper();
        for (File file : files) {
            logger.debug("Processing file: {}", file.getName());
            try {
                CaseDocument document = objectMapper.readValue(file, CaseDocument.class);
                documents.add(document);
            } catch (IOException e) {
                logger.error("Error reading file: {}", file.getName(), e);
            }
        }
        logger.info("Loaded {} documents", documents.size());
        return documents;
    }
}