package zeyfra.dmas.modules.security.shiro.aop;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;


import zeyfra.dmas.modules.Constant;
import zeyfra.dmas.modules.security.shiro.jwt.JwtToken;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static zeyfra.dmas.aspect.utils.InjectUtil.restError;

/**
 * @author ZeyFra
 * @date 2021/2/16 13:33
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 执行登录认证
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            executeLogin(request, response);
            return true;
        } catch (Exception e) {
            // 写出统一错误信息
            restError((HttpServletResponse) response);
            return false;
        }
    }


    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Constant.TOKEN);

        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }
}
