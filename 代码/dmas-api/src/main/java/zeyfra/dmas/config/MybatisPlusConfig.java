package zeyfra.dmas.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import zeyfra.dmas.aspect.mybatis_plus.MyMetaObjectHandler;

import javax.sql.DataSource;

/**
 * @author ZeyFra
 * @date 2021/4/11 15:54
 */
@Configuration
@EnableTransactionManagement
@MapperScan("zeyfra.dmas.modules.**.mapper")
public class MybatisPlusConfig {

    @Bean("sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("dataSource") DataSource dataSource) throws Exception {

        // 创建MybatisConfiguration
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();

        // MybatisPlus内部过滤器
        // 将自动分页插件设置DB类型
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 添加自定义拦截器
        mybatisConfiguration.addInterceptor(mybatisPlusInterceptor);

        // 开启缓存
        mybatisConfiguration.setCacheEnabled(true);

        //创建MybatisSqlSessionFactoryBean
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();

        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);

        // 在GlobalConfig中添加sql拦截器，即相应的注解处理器
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());

        // 添加在GlobalConfig中添加sql拦截器
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);

        // 添加MybatisConfiguration
        sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);


        // 设置mapper相应的xml路径
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:zeyfra/dmas/modules/**/*.xml"));

        // 返回MybatisSqlSessionFactoryBean从而替代原生的sqlSessionFactory
        return sqlSessionFactoryBean.getObject();
    }

}
