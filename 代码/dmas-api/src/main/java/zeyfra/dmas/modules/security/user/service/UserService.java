package zeyfra.dmas.modules.security.user.service;

import zeyfra.dmas.modules.security.user.entity.User;
import zeyfra.dmas.modules.security.user.dto.response.UserInfoDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
public interface UserService extends IService<User> {

    UserInfoDTO login(String userName, String password);

    UserInfoDTO getUserInfo(String token);

    void logout(String token);
}
