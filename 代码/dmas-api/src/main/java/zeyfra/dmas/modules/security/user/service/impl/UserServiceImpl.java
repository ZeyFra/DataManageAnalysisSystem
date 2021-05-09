package zeyfra.dmas.modules.security.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import zeyfra.dmas.core.api.ApiError;
import zeyfra.dmas.core.exception.ServiceException;
import zeyfra.dmas.core.utils.PasswordHandler;
import zeyfra.dmas.modules.security.shiro.jwt.JwtUtils;
import zeyfra.dmas.modules.security.user.dto.response.UserInfoDTO;
import zeyfra.dmas.modules.security.user.entity.User;
import zeyfra.dmas.modules.security.user.mapper.UserMapper;
import zeyfra.dmas.modules.security.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public UserInfoDTO login(String userName, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUserName, userName);

        User user = this.getOne(wrapper);
        if(user == null){
            throw new ServiceException(ApiError.ERROR_90010001);
        }

        boolean check = PasswordHandler.checkPass(password,"18146710901", user.getPassword());
        if(!check){
            throw new ServiceException(ApiError.ERROR_90010002);
        }

        return this.setToken(user);
    }

    @Override
    public void logout(String token) {
        // 仅退出当前会话
        SecurityUtils.getSubject().logout();
    }

    @Override
    public UserInfoDTO getUserInfo(String token) {

        // 获得会话
        String userName = JwtUtils.getUsername(token);

        // 校验结果
        boolean check = JwtUtils.verify(token, userName);

        if(!check){
            throw new ServiceException(ApiError.ERROR_90010001);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUserName, userName);

        User user = this.getOne(wrapper, false);

        if(user == null){
            throw new ServiceException(ApiError.ERROR_10010002);
        }

        return this.setToken(user);
    }

    /**
     * 保存会话信息
     * @param user
     * @return UserInfoDTO
     */
    private UserInfoDTO setToken(User user){

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(user.getUserName());

        // 生成Token
        String token = JwtUtils.sign(userInfoDTO.getUserName());
        userInfoDTO.setToken(token);

        // 填充角色
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        userInfoDTO.setRoles(roles);

        return userInfoDTO;
    }



}
