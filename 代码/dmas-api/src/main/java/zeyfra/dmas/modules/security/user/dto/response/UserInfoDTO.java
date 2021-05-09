package zeyfra.dmas.modules.security.user.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/4/11 17:15
 */
@Data
public class UserInfoDTO {

    @ApiModelProperty(value = "用户名", required=true)
    private String userName;

    @ApiModelProperty(value = "登录令牌", required=true)
    private String token;

    @ApiModelProperty(value = "角色列表", required=true)
    private List<String> roles;



}
