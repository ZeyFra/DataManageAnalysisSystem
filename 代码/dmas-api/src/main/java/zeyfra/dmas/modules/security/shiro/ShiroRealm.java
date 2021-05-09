package zeyfra.dmas.modules.security.shiro;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import zeyfra.dmas.modules.security.shiro.jwt.JwtToken;
import zeyfra.dmas.modules.security.shiro.jwt.JwtUtils;
import zeyfra.dmas.modules.security.user.dto.response.UserInfoDTO;
import zeyfra.dmas.modules.security.user.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author ZeyFra
 * @date 2021/2/16 13:32
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    private UserService userService;

    @Autowired
    @Lazy
    public void setUserService(UserService UserService){
        this.userService = UserService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 详细授权认证
     * @param principals
     * @return
     */
    @ApiOperation(value = "详细授权认证")
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        if (principals != null) {
            UserInfoDTO user = (UserInfoDTO) principals.getPrimaryPrincipal();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 查找用户角色
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        info.setRoles(new HashSet<>(roles));
        log.info("---------校验详细权限完成");
        return info;
    }

    /**
     * 校验用户的账号密码是否正确
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @ApiOperation(value = "校验用户的账号密码是否正确")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        if (token == null) {
            throw new AuthenticationException("token为空!");
        }

        log.info("---------校验用户："+token);
        // 校验token有效性
        UserInfoDTO user = this.checkToken(token);
        return new SimpleAuthenticationInfo(user, token, getName());
    }


    /**
     * 校验Token的有效性
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @ApiOperation(value = "校验Token的有效性")
    public UserInfoDTO checkToken(String token) throws AuthenticationException {

        // 从token中获取用户名
        String username = JwtUtils.getUsername(token);
        log.info("---------用户名： "+ username);

        if (username == null) {
            throw new AuthenticationException("无效的token");
        }

        // 查找登录用户对象
        UserInfoDTO user = userService.getUserInfo(token);

        // 校验token是否失效
        if (!JwtUtils.verify(token, username)) {
            throw new AuthenticationException("登陆失效，请重试登陆!");
        }

        return user;
    }



    /**
     * 清除当前用户的权限认证缓存
     * @param principals
     */
    @Override
    @ApiOperation(value = "清除当前用户的权限认证缓存")
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

}