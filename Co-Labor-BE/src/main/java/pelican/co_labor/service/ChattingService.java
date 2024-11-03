/*
This service used datasets from 'The Open AI Dataset Project (AI-Hub, S. Korea)'. All data information can be accessed through 'AI-Hub (www.aihub.or.kr)'.
Colabr Chatbot Service is trained by AI-Hub's `법률/규정 텍스트 분석 데이터 (고도화)- 상황에 따른 판례 데이터` (https://www.aihub.or.kr/aihubdata/data/view.do?currMenu=&topMenu=&aihubDataSe=data&dataSetSn=71723)
*/

package pelican.co_labor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import pelican.co_labor.controller.ChattingController;
import pelican.co_labor.domain.chatting.Chatting;
import pelican.co_labor.domain.labor_user.LaborUser;
import pelican.co_labor.repository.chatting.ChattingRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChattingService {


    private static final Logger logger = LoggerFactory.getLogger(ChattingService.class);

    private final ChattingRepository chattingRepository;
    private final OpenAIChatService openAIChatService;
    private final RestTemplate restTemplate;


    @Autowired
    public ChattingService(ChattingRepository chattingRepository, OpenAIChatService openAIChatService, RestTemplate restTemplate) {
        this.chattingRepository = chattingRepository;
        this.openAIChatService = openAIChatService;
        this.restTemplate = restTemplate;
    }

    // 새로운 메서드: 사용자의 모든 메시지 삭제
    @Transactional
    public void deleteAllMessagesByUser(String laborUserId) {
        List<Chatting> chats = chattingRepository.findByLaborUser_LaborUserId(laborUserId);
        chattingRepository.deleteAll(chats);  // 사용자와 관련된 모든 채팅을 삭제
    }

    @Transactional
    public Chatting saveUserMessage(LaborUser laborUser, String userMessage) {
        Chatting chatting = new Chatting();
        chatting.setLaborUser(laborUser);
        chatting.setContent(userMessage);
        chatting.setMy_message(true);
        return chattingRepository.save(chatting);
    }

    @Transactional
    public Chatting saveGptResponse(LaborUser laborUser, String gptResponse) {
        Chatting chatting = new Chatting();
        chatting.setLaborUser(laborUser);
        chatting.setContent(gptResponse);
        chatting.setMy_message(false);
        return chattingRepository.save(chatting);
    }
    // 기존 코드
    public List<Chatting> getAllMessagesByUser(String laborUserId) {
        return chattingRepository.findByLaborUser_LaborUserId(laborUserId);
    }

//    // 기존 코드 모두 기억해서 오류 발생
//    public String getGptResponse(String userId, String userMessage) {
//
//        List<Chatting> userChats = getAllMessagesByUser(userId);
//        String previousResponses = userChats.stream()
//                .map(Chatting::getContent)
//                .collect(Collectors.joining(" "));
//
//        // previousResponses에서 userMessage와 겹치는 부분 제거
//        String cleanedResponses = removeOverlap(previousResponses, userMessage);
//        String prev = cleanedResponses + "\n\nThe above is a conversation I had with you before. If it's empty, ignore it, but if it is, remember it.\n\n";
//
//        String summaryPrompt = prev + "\nCondition 0 - The English in the prompt is the setting to be referenced in the answer, and the Korean is the actual question. \nCondition 1 - Your role is a legal chatbot consulting with a foreign worker. \nCondition 2 - Now the foreign worker will ask you a question about legal advice, and you must answer the question according to conditions 3 and 4. \nCondition 3 - First paragraph: The main answer to the question. This includes various contents such as solutions, advice, etc. Second paragraph: The law, case law, etc. related to the question. \nCondition 4 - Answer in three paragraphs using indentation and do not put subtitles before the paragraphs.\n"
//                + "Legal Advice Questions: " + userMessage + "\nPlease answer legal advice questions in Korean according to conditions 1, 2, 3, 4, and 5. \nIf the legal advice question is not relevant, please reply that you did not understand and that you need to ask the correct question.";
//
//        String gptResponse = openAIChatService.getGptResponse(summaryPrompt);
//        String output = gptResponse.replace("\n", "<br>");
//        return output;
//    }

//    // 새로운 메서드: 대화의 처음 3문장만 가져오는 요약 기능 추가
//    private String summarizeMessages(List<Chatting> recentChats) {
//        return recentChats.stream()
//                .map(chat -> getFirstThreeSentences(chat.getContent()))  // 각 대화의 처음 3문장만 가져옴
//                .collect(Collectors.joining(" "));
//    }
//
//    // 각 메시지에서 처음 3문장만 가져오는 메서드
//    private String getFirstThreeSentences(String content) {
//        // 문장 단위로 분리 (기본적으로 '.'으로 구분)
//        String[] sentences = content.split("\\. ");
//
//        // 처음 3개의 문장만 추출, 만약 문장이 3개 미만이면 있는 만큼 가져옴
//        return Arrays.stream(sentences)
//                .limit(3)
//                .collect(Collectors.joining(". ")) + ".";  // 마지막 문장 뒤에 '.' 추가
//    }

    // 241017 최근 메시지를 가져오는 메서드 추가
    public List<Chatting> getRecentMessagesByUser(String laborUserId, int messageCount) {
        List<Chatting> allMessages = chattingRepository.findByLaborUser_LaborUserId(laborUserId);
        return allMessages.stream()
                .skip(Math.max(0, allMessages.size() - messageCount))
                .collect(Collectors.toList());
    }


    // 241017 GPT 호출 시 최근 4개의 메시지만 포함하도록 수정
    public String getGptResponse(String userId, String userMessage) {
        try {
            List<Chatting> recentChats = getRecentMessagesByUser(userId, 4);
            String recentMessages = recentChats.stream()
                    .map(Chatting::getContent)
                    .collect(Collectors.joining(" "));

            String prev = recentMessages + "\n\nThe above is a conversation I had with you before. If it's empty, ignore it, but if it is, remember it.\n\n";

            String summaryPrompt = prev + "\nCondition 0 - The English in the prompt is the setting to be referenced in the answer, and the Korean is the actual question. \nCondition 1 - Your role is a legal chatbot consulting with a foreign worker. \nCondition 2 - Now the foreign worker will ask you a question about legal advice, and you must answer the question according to conditions 3 and 4. \nCondition 3 - First paragraph: The main answer to the question. This includes various contents such as solutions, advice, etc. Second paragraph: The law, case law, etc. related to the question. \nCondition 4 - Answer in three paragraphs using indentation and do not put subtitles before the paragraphs.\n"
                    + "Legal Advice Questions: " + userMessage + "\nPlease answer legal advice questions in Korean according to conditions 1, 2, 3, 4, and 5.";

            return openAIChatService.getGptResponse(summaryPrompt).replace("\n", "<br>");
        } catch (Exception e) {
            logger.error("Error while fetching GPT response: ", e);
            throw new RuntimeException("Failed to get GPT response", e);
        }
    }



    private String removeOverlap(String previousResponses, String userMessage) {
        // 이전 응답에서 사용자 메시지를 제거
        if (previousResponses.contains(userMessage)) {
            // 겹치는 부분이 있으면 제거
            return previousResponses.replace(userMessage, "").trim();
        }
        // 겹치는 부분이 없으면 이전 응답 반환
        return previousResponses;
    }
}