package org.yaukie.frame.config;

import bsh.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaukie.frame.autocode.dao.mapper.XUserMapper;
import org.yaukie.frame.autocode.model.*;
import org.yaukie.frame.autocode.service.api.XUserService;
import org.yaukie.frame.util.UUIDUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    /**
     * 用户mapper
     */
    @Autowired
    private XUserMapper xUserMapper;

    /**
     * 用户service
     */
    @Autowired
    private XUserService xUserService;



    /**
     * 登录 -身份认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("-------身份认证方法--------");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());

        //根据用户名密码从数据库获取用户实例
        XUserExample xUserExample = new XUserExample();
        xUserExample.createCriteria().andUserNameEqualTo(userName);
        xUserExample.createCriteria().andPasswordEqualTo(userPwd);
        List<XUser> xuserlist =  xUserMapper.selectByExample(xUserExample);
        if(xuserlist.isEmpty() || xuserlist.size() == 0){
           throw new AccountException("未找到用户");
        }
        //获取登陆人
        XUser xuser = xuserlist.get(0);

        if (userName == null ) {
            throw new AccountException("用户名不正确");
        }else if(StringUtils.isBlank(userPwd)){
            throw new AccountException("密码为空");
        } else if (!userPwd.equals(xuser.getPassword() )) {
            throw new AccountException("密码不正确");
        }

        // 更新登录用户的token
        xuser.setToken(UUIDUtils.getUUID32());
        xUserMapper.updateByPrimaryKey(xuser);

        //获取用户的权限
//        List<String> permissionList = xUserService.getPermissionByUserId(xuser.getId());
//        if(!permissionList.isEmpty()){
//            xuser.setPermissionList(permissionList);
//        }else{
//            xuser.setPermissionList(new ArrayList<String>());
//        }

        //认证器 第一个参数为用户名或用户对象 第二个参数为密码 第三个参数为当前real名称
        return new SimpleAuthenticationInfo(xuser, xuser.getPassword(),getName());
    }


    /**
     * 授权方法-权限相关
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //从session中获取到当前登录的用户对象
        XUser xuser  = (XUser) SecurityUtils.getSubject().getPrincipal();
        //创建一个授权器
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //权限集合
        Set<String> stringSet = new HashSet<>();
        info.setStringPermissions(stringSet);
        return info;
    }
}
