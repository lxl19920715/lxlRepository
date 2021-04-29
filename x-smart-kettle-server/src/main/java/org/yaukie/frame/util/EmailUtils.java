package org.yaukie.frame.util;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: lxl
 * @create: 2021/04/18 19/59/503
 */
@Slf4j
public class EmailUtils {

//                地址	        端口（不带SSL）	端口（带SSL）
//    POP3服务器	pop.139.com	    110	            995
//    SMTP服务器	smtp.139.com	25	            465
//    IMAP服务器	imap.139.com	143	            993
    /** 是否发送邮件 */
    public static String emailType;
    /** 临时发送邮件用户id */
    public static String userId;
    /** 临时作业描述 */
    public static String jobDescription;
    /** 临时kjb路径 */
    public static String kjbPath;

    /**
     * 使用配置表配置发件人，发送邮件
     * @param account 发件邮箱账号
     * @param authorizationCode 发件邮箱授权码
     * @param smthHost SMTP服务器地址
     * @param senderName 发件人名称
     * @param to 收件人邮箱
     * @param title 邮件标题
     * @param messageText 邮件内容
     * @param userName 收件人姓名
     * @param defaultPort 默认端口
     * @param sslPort ssl端口
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Boolean sendEmail(String account,String authorizationCode,String smthHost,String senderName,
                                    String to, String title, String messageText,String userName,String defaultPort,String sslPort) throws Exception {

        Properties props = new Properties();

        //创建参数配置, 用于连接邮件服务器的参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", smthHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.port",defaultPort); //默认端口

        //设置连接超时时间
        props.setProperty("mail.smtp.connectiontimeout","10000");
        props.setProperty("mail.smtp.timeout", "10000");
        // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
        //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
        //     取消下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。


        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助)
        if (null != sslPort){
//            final String smtpPort = "465";
            props.setProperty("mail.smtp.port", sslPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", sslPort);
            props.put("mail.smtp.ssl.enable",true);
        }


        //根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        //设置为debug模式, 可以查看详细的发送 log
        session.setDebug(true);

        //创建邮件
        Transport transport = session.getTransport();
        try {
            MimeMessage message = createEmail(session,account,senderName,to,title,messageText,userName);
            //使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            transport.connect(account, authorizationCode);
            //发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());

        } catch (Exception e) {
            log.error("发送邮件出现异常,原因为:",e );
        }finally {
            if (null != transport) {
                //关闭连接
                transport.close();
            }
        }


        return true;
    }


    /**
     * 生成邮件
     * @param session
     * @param from 发送人邮箱
     * @param senderName 发件人名称
     * @param to 接收人邮箱
     * @param title 邮件标题
     * @param messageText 邮件内容
     * @param userName 收件人姓名
     * @return
     */
    public static MimeMessage createEmail(Session session, String from,String senderName,String to, String title, String messageText, String userName) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        //设置发件人 From: 发件人
        message.setFrom(new InternetAddress(from, senderName, "UTF-8"));
        //设置收件人 To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO,new InternetAddress(to, userName, "UTF-8"));
        //设置邮件标题
        message.setSubject(title,"UTF-8");
        //设置邮件内容
        message.setContent(messageText,"text/html;charset=UTF-8");
        //设置发送时间
        message.setSentDate(new Date());
        //保存设置
        message.saveChanges();
        return message;
    }

//    public static void main(String[] args) {
//        //测试
//        try {
//            sendEmail("190709769@qq.com","继续测试邮件","继续测试邮件","继续测试邮件人员");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     *验证是否为邮箱格式
     * @param email
     * @return boolean
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }
    public static void main(String[] args) {
        String msg = "youxiang@yahoo.com.tw";
        System.out.println(isEmail(msg));
    }


}
