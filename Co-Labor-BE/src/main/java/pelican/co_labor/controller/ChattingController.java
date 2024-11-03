/*
This service used datasets from 'The Open AI Dataset Project (AI-Hub, S. Korea)'. All data information can be accessed through 'AI-Hub (www.aihub.or.kr)'.
Colabr Chatbot Service is trained by AI-Hub's `법률/규정 텍스트 분석 데이터 (고도화)- 상황에 따른 판례 데이터` (https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=&topMenu=&aihubDataSe=data&dataSetSn=71723)
*/

package pelican.co_labor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pelican.co_labor.domain.chatting.Chatting;
import pelican.co_labor.domain.labor_user.LaborUser;
import pelican.co_labor.dto.CaseDocument;
import pelican.co_labor.service.AuthService;
import pelican.co_labor.service.ChattingService;
import pelican.co_labor.service.ElasticsearchService;
import pelican.co_labor.service.JsonLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Chatting", description = "법률 상담 챗봇과의 채팅 관련 API")
@RestController
@RequestMapping("/api/chatting")
public class ChattingController {

    private final ChattingService chattingService;
    private final AuthService authService;

    private final ElasticsearchService elasticsearchService;
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchController.class);

    @Value("${elasticsearch.index-name}")
    private String defaultIndexName;

    @Autowired
    public ChattingController(ChattingService chattingService, AuthService authService, ElasticsearchService elasticsearchService) {
        this.chattingService = chattingService;
        this.authService = authService;
        this.elasticsearchService = elasticsearchService;

    }

    @Operation(summary = "새 채팅 시작 API", description = "이전 메시지를 모두 삭제하고 새로운 채팅 세션을 시작합니다.")
    @PostMapping("/start-new")
    public void startNewChat(@Parameter(description = "사용자 ID") @RequestParam("userId") String userId) {

        Optional<LaborUser> optionalUser = authService.findLaborUserById(userId);
        if (optionalUser.isPresent()) {
            LaborUser user = optionalUser.get();

            // 1. 기존 채팅 기록 삭제
            chattingService.deleteAllMessagesByUser(user.getLaborUserId());

            // 새로운 채팅을 시작할 때 초기화만 하고 별도의 메시지는 받지 않음
            System.out.println("Chat history for user " + userId + " has been deleted. New session started.");
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Operation(summary = "메시지 전송 API", description = "사용자가 챗봇과 메시지를 주고받습니다.")
    @PostMapping("/send")
    public void sendMessage(
            @Parameter(description = "사용자 ID") @RequestParam("userId") String userId,
            @Parameter(description = "사용자가 전송한 메시지") @RequestParam("message") String message) {
        Optional<LaborUser> optionalUser = authService.findLaborUserById(userId);
        if (optionalUser.isPresent()) {
            LaborUser user = optionalUser.get();
            chattingService.saveUserMessage(user, message);

            message = "나 외국인 노동자인데, " + message;
            logger.info("Preprocess user message : " + message);

            String gptResponse = chattingService.getGptResponse(user.getLaborUserId(), message);

            logger.info("start GPT Call");
            chattingService.saveGptResponse(user, gptResponse);
            logger.info("end GPT Call");


            /**
             * Elasticsearch Servcie Call
             */
            try {
                Integer i=1;
                List<Map<String, Object>> results = elasticsearchService.searchDocuments(defaultIndexName, message);
                logger.info("success to call elasticsearch");
                for(Map<String, Object> result : results){
                    CaseDocument caseDocument = new CaseDocument(result);
                    logger.info("result document : " + result);
                    String savingResult = "<details>";

                    savingResult += "<summary> 👉 연관된 ";
                    savingResult += "판례를 참고하시려면 클릭해보세요! 👈</summary> ";

                    savingResult += caseDocument.get판시사항() +  "<br/>";
                    savingResult += "<br/>" + caseDocument.get판결요지() + "<br/>";
                    savingResult += "<br/>" + caseDocument.get참조조문() + "<br/>";

                    savingResult += "</details> ";

                    logger.info("savingResult : " + savingResult);
                    chattingService.saveGptResponse(user, savingResult);
                    logger.info("iteration end");

                }

            } catch (IOException e) {
                logger.error("Error during elasticsearch search");
            }
            /**
             * Elasticsearch Service Return
             */
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Operation(summary = "전체 메시지 조회 API", description = "특정 사용자가 주고받은 모든 메시지를 조회합니다.")
    @GetMapping("/all")
    public List<Chatting> getAllMessages(
            @Parameter(description = "사용자 ID") @RequestParam("userId") String userId) {
        Optional<LaborUser> optionalUser = authService.findLaborUserById(userId);
        if (optionalUser.isPresent()) {
            LaborUser user = optionalUser.get();
            return chattingService.getAllMessagesByUser(user.getLaborUserId());  // 전체 메시지 조회
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
