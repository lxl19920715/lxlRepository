package org.yaukie.frame.autocode.service.api;

import org.yaukie.core.base.service.Service;
import org.yaukie.frame.autocode.model.XUser;
import org.yaukie.frame.autocode.model.XUserExample;

import java.util.List;

/**
 * @author: yuenbin
 * @create: 2021/04/14 22/55/130
 **/
public interface XUserService extends Service<XUser,XUserExample> {

    /**
     * 根据用户id获取用户权限
     * @param id 用户主键id
     * @return lxl
     */
//    public List<String> getPermissionByUserId(Long id);

    /**
     * 向用户发送邮件
     * @param userId 用户id
     * @param title 邮件标题
     * @param messageText 邮件内容
     * @return
     */
    public Boolean sendEmail(Long userId,String title,String messageText);

    /**
     *根据条件查询用户列表
     * @param nickName
     * @param userPhone
     * @return
     */
    public List<XUser> getUserListByConditions(String nickName,String userPhone);
}
