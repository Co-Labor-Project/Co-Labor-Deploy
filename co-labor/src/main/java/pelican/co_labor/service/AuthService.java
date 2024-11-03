package pelican.co_labor.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;
import pelican.co_labor.domain.labor_user.LaborUser;
import pelican.co_labor.dto.auth.EnterpriseUserDTO;
import pelican.co_labor.dto.auth.LaborUserDTO;
import pelican.co_labor.repository.enterprise.EnterpriseRepository;
import pelican.co_labor.repository.enterprise_user.EnterpriseUserRepository;
import pelican.co_labor.repository.labor_user.LaborUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LaborUserRepository laborUserRepository;
    private final EnterpriseUserRepository enterpriseUserRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final SessionRepository sessionRepository;

    public void registerLaborUser(LaborUserDTO laborUserDTO) {
        LaborUser laborUser = LaborUser.toLaborUser(laborUserDTO);
        laborUser.setPassword(BCrypt.hashpw(laborUser.getPassword(), BCrypt.gensalt()));
        laborUserRepository.save(laborUser);
    }

    public void registerEnterpriseUser(EnterpriseUserDTO enterpriseUserDTO) {
        EnterpriseUser enterpriseUser = EnterpriseUser.toEnterpriseUser(enterpriseUserDTO);
        enterpriseUser.setPassword(BCrypt.hashpw(enterpriseUser.getPassword(), BCrypt.gensalt()));
        enterpriseUserRepository.save(enterpriseUser);
    }

    public void saveEnterpriseUser(EnterpriseUser enterpriseUser) {
        enterpriseUserRepository.save(enterpriseUser);
    }

    public boolean authenticateUser(String username, String password) {
        Optional<LaborUser> byLaborUserId = laborUserRepository.findByLaborUserId(username);

        if (byLaborUserId.isPresent()) {
            LaborUser laborUser = byLaborUserId.get();
            return BCrypt.checkpw(password, laborUser.getPassword());
        } else {
            Optional<EnterpriseUser> byEnterpriseUserId = Optional.ofNullable(enterpriseUserRepository.findByEnterpriseUserId(username));
            if (byEnterpriseUserId.isPresent()) {
                EnterpriseUser enterpriseUser = byEnterpriseUserId.get();
                return BCrypt.checkpw(password, enterpriseUser.getPassword());
            }
        }

        return false;
    }

    public Optional<?> getUser(String username) {
        Optional<LaborUser> byLaborUserID = laborUserRepository.findByLaborUserId(username);

        if (byLaborUserID.isPresent()) {
            return byLaborUserID.map(user -> {
                LaborUserDTO laborUserDTO = new LaborUserDTO();
                laborUserDTO.setUsername(user.getLaborUserId());    // 메서드명 변경
                laborUserDTO.setEmail(user.getEmail());
                laborUserDTO.setName(user.getName());
                return laborUserDTO;
            });
        } else {
            Optional<EnterpriseUser> byEnterpriseUserID = Optional.ofNullable(enterpriseUserRepository.findByEnterpriseUserId(username));
            if (byEnterpriseUserID.isPresent()) {
                return byEnterpriseUserID.map(user -> {
                    EnterpriseUserDTO enterpriseUserDTO = new EnterpriseUserDTO();
                    enterpriseUserDTO.setUsername(user.getEnterprise_user_id());    // 메서드명 변경 아직 안 함.
                    enterpriseUserDTO.setEmail(user.getEmail());
                    enterpriseUserDTO.setName(user.getName());
                    enterpriseUserDTO.setEnterprise(user.getEnterprise());
                    return enterpriseUserDTO;
                });
            }
        }

        return Optional.empty();
    }

    public Optional<LaborUser> findLaborUserById(String userId) {
        return laborUserRepository.findByLaborUserId(userId);
    }


    public Optional<EnterpriseUser> findEnterpriseUserById(String enterpriseUserId) {
        return Optional.ofNullable(enterpriseUserRepository.findByEnterpriseUserId(enterpriseUserId));
    }


    public String getUserType(String username) {
        Optional<LaborUser> byLaborUserId = laborUserRepository.findByLaborUserId(username);

        if (byLaborUserId.isPresent()) {
            return "labor_user";
        } else {
            Optional<EnterpriseUser> byEnterpriseUserId = Optional.ofNullable(enterpriseUserRepository.findByEnterpriseUserId(username));

            if (byEnterpriseUserId.isPresent()) {
                return "enterprise_user";
            }

            return null;
        }
    }

    public Optional<Enterprise> findEnterpriseById(String enterpriseId) {
        return enterpriseRepository.findByEnterpriseId(enterpriseId);
    }

    public Optional<Map<String, Object>> getCurrentUser(String sessionId) {
        // 세션 저장소에서 해당 세션을 조회
        Session session = sessionRepository.findById(sessionId);

        if (session != null && session.getAttribute("username") != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", session.getAttribute("username"));
            userInfo.put("userType", session.getAttribute("userType"));
            return Optional.of(userInfo);
        } else {
            return Optional.empty();
        }
    }

    public void deleteSession(String jssesionId) {
        sessionRepository.deleteById(jssesionId);
    }
}