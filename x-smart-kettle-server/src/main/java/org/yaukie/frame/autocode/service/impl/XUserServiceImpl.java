
package org.yaukie.frame.autocode.service.impl;
import org.apache.commons.lang3.StringUtils;
import org.yaukie.core.base.service.BaseService;
import org.springframework.stereotype.Service;
import org.yaukie.frame.autocode.dao.mapper.XEmailConfigMapper;
import org.yaukie.frame.autocode.dao.mapper.XUserMapper;
import org.yaukie.frame.autocode.model.XEmailConfig;
import org.yaukie.frame.autocode.model.XEmailConfigExample;
import org.yaukie.frame.autocode.model.XUser;
import org.yaukie.frame.autocode.model.XUserExample;
import org.yaukie.frame.autocode.service.api.XUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.yaukie.frame.util.EmailUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author: yuenbin
 * @create: 2021/04/14 22/55/130
 **/
@Service
@Transactional
public class XUserServiceImpl extends BaseService<XUserMapper,XUser,XUserExample> implements XUserService {

        @Autowired
        private XUserMapper xUserMapper;

        @Autowired
        private XEmailConfigMapper xEmailConfigMapper;


        /**
         * 向用户发送邮件
         * @param userId 用户id
         * @param title 邮件标题
         * @param messageText 邮件文本
         * @return
         */
        @Override
        public Boolean sendEmail(Long userId,String title,String messageText){
                XUser xUser = xUserMapper.selectByPrimaryKey(userId);
                if(null == xUser || StringUtils.isBlank(xUser.getUserEmail())){
                        return false;
                }
                try {
                        //查询邮件系统发件人
                        XEmailConfigExample xEmailConfigExample = new XEmailConfigExample();
                        List<XEmailConfig> xEmailConfigList = xEmailConfigMapper.selectByExample(xEmailConfigExample);
                        if(xEmailConfigList.isEmpty()){
                                //未找到配置
                                return false;
                        }
                        XEmailConfig emailConfig = xEmailConfigList.get(0);
                        Boolean result = EmailUtils.sendEmail(emailConfig.getSenderAccount(),emailConfig.getSenderAuthorizationCode(),emailConfig.getSmtpHost(),
                                emailConfig.getSenderName(),xUser.getUserEmail(),title,messageText,xUser.getUserName(),emailConfig.getDefaultPort(),emailConfig.getSslPort());
//                        Boolean result = EmailUtils.sendEmail(xUser.getUserEmail(),title,messageText,xUser.getUserName());
                        if(result){
                                return true;
                        }

                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                } catch(Exception exception){
                        exception.printStackTrace();
                }
                return false;
        }

        /**
         * 根据条件查询用户信息
         * @param nickName
         * @param userPhone
         * @return
         */
        @Override
        public List<XUser> getUserListByConditions(String nickName, String userPhone) {
                return xUserMapper.getUserListByConditions(nickName,userPhone);
        }


}
