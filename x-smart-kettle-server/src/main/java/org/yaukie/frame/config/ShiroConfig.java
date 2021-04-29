package org.yaukie.frame.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 描述：shiro配置类
 *
 * @author lixiaolong
 * @create 2021-04-13-13:24
 */
@Configuration
public class ShiroConfig {

    /**
     * shiro过滤器
     * @param securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // shiro过滤器工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

//        shiroFilterFactoryBean.setLoginUrl("/login"); //设置登陆页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole"); //权限不足跳转页面

        // 过滤器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/front/**", "anon");
        filterChainDefinitionMap.put("/api/**", "anon");
        //authc:所有url都必须认证通过才可以访问
        //filterChainDefinitionMap.put("/admin/**", "authc");
        //filterChainDefinitionMap.put("/user/**", "authc");

        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截 剩余的都需要认证
        //filterChainDefinitionMap.put("/**", "authc");
        //perms权限过滤器 必须拥有某项权限才能访问对应路径
        //filterChainDefinitionMap.put("/add", "perms[user:query]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }

    /**
     * SecurityManager是Shiro核心，主要协调Shiro内部的各种安全组件
     *
     * @return DefaultWebSecurityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm());
        return defaultSecurityManager;
    }

    /**
     * Realm:用户数据和Shiro数据交互的桥梁。比如需要用户身份认证、权限认证。都是需要通过Realm来读取数据。
     * @return CustomRealm
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

}
