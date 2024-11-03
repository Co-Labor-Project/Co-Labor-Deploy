package pelican.co_labor.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pelican.co_labor.controller.ElasticsearchController;
import pelican.co_labor.dto.CaseDocument;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {

    private final ElasticsearchClient esClient;

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    private static final int BATCH_SIZE = 32;
    private static final int MAX_RESULTS = 2; // 결과 수 제한

    @Value("${elasticsearch.index-name}")
    private String defaultIndexName;

    @Autowired
    public ElasticsearchService(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    public void createIndexWithMapping(String indexName) throws IOException {
        if (!esClient.indices().exists(e -> e.index(indexName)).value()) {
            Map<String, Object> settings = Map.of(
                    "analysis", Map.of(
                            "analyzer", Map.of(
                                    "nori_analyzer", Map.of(
                                            "type", "custom",
                                            "tokenizer", "nori_tokenizer"
                                    )
                            ),
                            "tokenizer", Map.of(
                                    "nori_tokenizer", Map.of(
                                            "type", "nori_tokenizer"
                                    )
                            )
                    )
            );

            Map<String, Property> properties = new HashMap<>();
            properties.put("사건명", Property.of(p -> p.text(t -> t.analyzer("nori_analyzer"))));
            properties.put("사건종류명", Property.of(p -> p.text(t -> t.analyzer("nori_analyzer"))));
            properties.put("판시사항", Property.of(p -> p.text(t -> t.analyzer("nori_analyzer"))));
            properties.put("판결요지", Property.of(p -> p.text(t -> t.analyzer("nori_analyzer"))));
            properties.put("참조조문", Property.of(p -> p.text(t -> t.analyzer("nori_analyzer"))));
            properties.put("사건번호", Property.of(p -> p.keyword(k -> k)));

            String settingsJson = new JacksonJsonpMapper().objectMapper().writeValueAsString(settings);

            CreateIndexResponse createResponse = esClient.indices().create(c -> c
                    .index(indexName)
                    .settings(s -> s.withJson(new StringReader(settingsJson)))
                    .mappings(m -> m.properties(properties))
            );

            if (!createResponse.acknowledged()) {
                throw new RuntimeException("Failed to create index with mapping");
            }
        }
    }

    public void bulkIndexDocuments(String indexName, List<CaseDocument> documents) throws IOException {
        logger.info("Starting bulk indexing for {} documents", documents.size());
        createIndexWithMapping(indexName);
        logger.info("Index created or already exists: {}", indexName);

        List<CaseDocument> filteredDocuments = filterAndPrepareDocuments(documents);
        logger.info("Filtered documents: {}", filteredDocuments.size());

        int totalDocuments = filteredDocuments.size();
        int batches = (int) Math.ceil((double) totalDocuments / BATCH_SIZE);

        for (int i = 0; i < batches; i++) {
            int fromIndex = i * BATCH_SIZE;
            int toIndex = Math.min(fromIndex + BATCH_SIZE, totalDocuments);
            List<CaseDocument> batch = filteredDocuments.subList(fromIndex, toIndex);
            logger.info("Processing batch {} of {}, size: {}", i+1, batches, batch.size());

            bulkIndexBatch(indexName, batch);
        }
        logger.info("Bulk indexing completed");
    }

    private List<CaseDocument> filterAndPrepareDocuments(List<CaseDocument> documents) {
        List<CaseDocument> filteredDocuments = new ArrayList<>();

        for (CaseDocument document : documents) {
            CaseDocument filteredDocument = new CaseDocument();
            filteredDocument.set사건명(document.get사건명() != null ? document.get사건명() : "기본 사건명");
            filteredDocument.set사건종류명(document.get사건종류명() != null ? document.get사건종류명() : "기본 사건종류명");
            filteredDocument.set판시사항(document.get판시사항() != null ? document.get판시사항() : "기본 판시사항");
            filteredDocument.set판결요지(document.get판결요지() != null ? document.get판결요지() : "기본 판결요지");
            filteredDocument.set참조조문(document.get참조조문() != null ? document.get참조조문() : "기본 참조조문");
            filteredDocument.set사건번호(document.get사건번호() != null ? document.get사건번호() : "기본 사건번호");

            filteredDocuments.add(filteredDocument);
        }

        return filteredDocuments;
    }

//    private void bulkIndexBatch(String indexName, List<CaseDocument> batch) throws IOException {
//        BulkRequest.Builder br = new BulkRequest.Builder();
//
//        for (CaseDocument document : batch) {
//            String documentId = document.generateCaseDocumentId();
//            br.operations(op -> op.index(idx -> idx.index(indexName).id(documentId).document(document)));
//        }
//
//        BulkResponse result = esClient.bulk(br.build());
//
//        if (result.errors()) {
//            List<String> errorMessages = new ArrayList<>();
//            for (BulkResponseItem item : result.items()) {
//                if (item.error() != null) {
//                    errorMessages.add(item.error().reason());
//                }
//            }
//            throw new RuntimeException("Bulk indexing failed: " + String.join(", ", errorMessages));
//        }
//    }

    private void bulkIndexBatch(String indexName, List<CaseDocument> batch) throws IOException {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (CaseDocument document : batch) {
            String documentId = document.generateCaseDocumentId();
            br.operations(op -> op
                    .index(idx -> idx
                            .index(indexName)
                            .id(documentId)
                            .document(document)
                    )
            );
        }

        logger.info("Sending bulk request for {} documents", batch.size());
        BulkResponse result = esClient.bulk(br.build());

        if (result.errors()) {
            logger.error("Bulk indexing encountered errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    logger.error("Error for document {}: {}", item.id(), item.error().reason());
                }
            }
            throw new RuntimeException("Bulk indexing failed");
        } else {
            logger.info("Bulk indexing successful. Indexed {} documents.", batch.size());
        }
    }

    public long deleteAllDocuments(String indexName) throws IOException {
        DeleteByQueryResponse response = esClient.deleteByQuery(d -> d
                .index(indexName)
                .query(q -> q.matchAll(m -> m))
        );
        return response.deleted();
    }


    public List<Map<String, Object>> searchDocuments(String indexName, String query) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(indexName)
                .query(q -> q
                        .bool(b -> b
                                .should(
                                        createFieldQuery("사건명", query),
                                        createFieldQuery("사건종류명", query),
                                        createFieldQuery("판시사항", query),
                                        createFieldQuery("판결요지", query),
                                        createFieldQuery("참조조문", query)
                                )
                                .minimumShouldMatch("75%")
                        )
                )
                .size(MAX_RESULTS)
                .sort(so -> so
                        .score(sc -> sc.order(SortOrder.Desc))
                )
        );

        SearchResponse<Map> response = esClient.search(searchRequest, Map.class);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Hit<Map> hit : response.hits().hits()) {
            results.add(hit.source());
        }

        return results;
    }

    private Query createFieldQuery(String field, String query) {
        return Query.of(q -> q
                .match(m -> m
                        .field(field)
                        .query(query)
                )
        );
    }
}