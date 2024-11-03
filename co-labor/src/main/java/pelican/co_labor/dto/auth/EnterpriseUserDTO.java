package pelican.co_labor.dto.auth;

import lombok.*;
import pelican.co_labor.domain.enterprise.Enterprise;
import pelican.co_labor.domain.enterprise_user.EnterpriseUser;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnterpriseUserDTO {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String name;
    private Enterprise enterprise;

    public static EnterpriseUserDTO toEnterpriseUserDTO(EnterpriseUser enterpriseUser) {
        EnterpriseUserDTO enterpriseUserDTO = new EnterpriseUserDTO();
        enterpriseUserDTO.setUsername(enterpriseUser.getEnterprise_user_id());
        enterpriseUserDTO.setPassword(enterpriseUser.getPassword());
        enterpriseUserDTO.setEmail(enterpriseUser.getEmail());
        enterpriseUserDTO.setName(enterpriseUser.getName());
        enterpriseUserDTO.setEnterprise(enterpriseUser.getEnterprise());
        return enterpriseUserDTO;
    }
}
