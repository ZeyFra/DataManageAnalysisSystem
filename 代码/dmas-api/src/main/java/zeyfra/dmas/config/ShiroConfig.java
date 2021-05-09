package zeyfra.dmas.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zeyfra.dmas.modules.security.shiro.ShiroRealm;
import zeyfra.dmas.modules.security.shiro.aop.JwtFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置类
 * @author ZeyFra
 * @date 2021/2/16 13:27
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 拦截器
        Map<String, String> map = new LinkedHashMap<>();

        // 需要排除的一些接口
        map.put("/dmas/api/user/login", "anon");
        map.put("/dmas/api/system/user/register", "anon");

        // 获取网站基本信息
        map.put("/exam/api/sys/config/detail", "anon");

        map.put("/", "anon");
        map.put("/v2/**", "anon");
        map.put("/static/css/**", "anon");
        map.put("/static/js/**", "anon");
        map.put("fonts/**", "anon");
        map.put("img/**", "anon");
        map.put("/doc.html", "anon");
        map.put("/**/*.js", "anon");
        map.put("/**/*.css", "anon");
        map.put("/**/*.html", "anon");
        map.put("/**/*.svg", "anon");
        map.put("/**/*.pdf", "anon");
        map.put("/**/*.jpg", "anon");
        map.put("/**/*.png", "anon");
        map.put("/**/*.ico", "anon");

        // 字体
        map.put("/**/*.ttf", "anon");
        map.put("/**/*.woff", "anon");
        map.put("/**/*.woff2", "anon");
        map.put("/druid/**", "anon");
        map.put("/swagger-ui.html", "anon");
        map.put("/swagger**/**", "anon");
        map.put("/webjars/**", "anon");

        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        filterMap.put("jwt", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        map.put("/**", "jwt");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }
}
