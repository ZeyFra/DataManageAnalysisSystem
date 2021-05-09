package zeyfra.dmas.modules.security.user.dto.request;

import lombok.Data;

/**
 * @author ZeyFra
 * @date 2021/4/11 17:41
 */
@Data
public class LoginRequestDTO {

    private String userName;
    private String password;

}
