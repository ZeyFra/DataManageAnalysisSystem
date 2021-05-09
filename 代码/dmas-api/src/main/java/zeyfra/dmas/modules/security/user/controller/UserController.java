package zeyfra.dmas.modules.security.user.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zeyfra.dmas.core.api.ApiRest;
import zeyfra.dmas.core.controller.BaseController;
import zeyfra.dmas.modules.security.user.dto.request.LoginRequestDTO;
import zeyfra.dmas.modules.security.user.dto.response.UserInfoDTO;
import zeyfra.dmas.modules.security.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ZeyFra
 * @since 2021-04-11
 */
@RestController
@RequestMapping("/dmas/api/user")
public class UserController extends BaseController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    /**
     * 用户登录
     * @return
     */
    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiRest<UserInfoDTO> login(@RequestBody LoginRequestDTO reqDTO) {
        UserInfoDTO respDTO = userService.login(reqDTO.getUserName(), reqDTO.getPassword());
        return super.success(respDTO);
    }

    /**
     * 用户登出
     * @return
     */
    @ApiOperation(value = "用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ApiRest logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        userService.logout(token);
        return super.success();
    }

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public ApiRest getUserInfo(HttpServletRequest request){
        String token = request.getHeader("token");
        UserInfoDTO userInfo = userService.getUserInfo(token);
        return super.success(userInfo);
    }

}

