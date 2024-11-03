package pelican.co_labor.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;

@Component
@AllArgsConstructor
public class SessionRenewalInterceptor implements HandlerInterceptor {

    private final SessionRepository sessionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Cookie[] cookies = request.getCookies();
        String sessionId = null;

        if (cookies == null) return true;
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }
        Session session = sessionRepository.findById(sessionId);

        if (session != null) {
            // 세션의 마지막 접근 시간을 갱신
            session.setLastAccessedTime(Instant.now());
            sessionRepository.save(session);
        }
        return true; // 다음 인터셉터나 핸들러로 진행
    }
}