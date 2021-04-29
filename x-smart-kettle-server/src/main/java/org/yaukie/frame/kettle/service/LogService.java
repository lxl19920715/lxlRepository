package org.yaukie.frame.kettle.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaukie.core.base.service.BaseService;
import org.yaukie.core.util.GenCodeUtil;
import org.yaukie.frame.autocode.model.*;
import org.yaukie.frame.autocode.service.api.*;
import org.yaukie.frame.kettle.listener.XLogListener;
import org.yaukie.frame.util.DateUtils;
import org.yaukie.frame.util.EmailUtils;
import org.yaukie.xtl.cons.Constant;
import org.yaukie.xtl.cons.XJobStatus;
import org.yaukie.xtl.exceptions.XtlExceptions;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author: yuenbin
 * @Date :2020/11/3
 * @Time :20:01
 * @Motto: It is better to be clear than to be clever !
 * @Destrib:  作业 转换的日志管理
 * 作业日志监控
 * 转换日志监控
 **/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class LogService   {

    @Value("${kettle.log.file.path}")
    String logFilePath ;

    @Autowired
    private XUserService xUserService;

    @Autowired
    private XLogService xLogService ;

    @Autowired
    private XMonitorService xMonitorService ;

    @Autowired
    private XLogWarningService XLogWarningService ;

    @Autowired
    private XJobMailService xJobMailService;

    /**
     *  @Author: yuenbin
     *  @Date :2020/11/28
     * @Time :15:11
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  保存kettle异常日志,
     * 用于预警查看每个任务执行的细节情况,及时监控丢失的日志
    **/
    public void doAddLogWarning(String logChannel,String targetId,String targetName,String logFile,String error,
                                String msg,String subject,String logLevel,String logType){
        XLogWarning xLogWarning = new XLogWarning() ;
        xLogWarning.setLogId(logChannel);
        xLogWarning.setTargetId(targetId);
        xLogWarning.setTargetName(targetName);
        xLogWarning.setLogFile(logFile);
        xLogWarning.setLogError(error);
        xLogWarning.setLogMsg(msg);
        xLogWarning.setLogSubject(subject);
        xLogWarning.setLogLevel(Constant.logger(Integer.parseInt(logLevel)).getCode());
        xLogWarning.setLogType(logType);
        XLogWarningService.insertSelective(xLogWarning);

        // 判断是否发送异常邮件
        if ("1".equals(EmailUtils.emailType)) {
            SimpleDateFormat sbf = new SimpleDateFormat("HH:mm:ss");
            // 查询发送邮件表数据
            XJobMailExample xJobMailExample = new XJobMailExample();
            xJobMailExample.createCriteria().andJobIdEqualTo(targetId);
            XJobMail xJobMail = this.xJobMailService.selectFirstExample(xJobMailExample);
            try {
                Date nowDate = sbf.parse(sbf.format(new Date()));
                // 校验是否在指定发送邮件的时间内
                if (DateUtils.isEffectiveDate(nowDate,xJobMail.getBeginTime(),xJobMail.getEndTime())) {
                    // 查询job_log表
                    XLogExample xLogExample = new XLogExample() ;
                    xLogExample.createCriteria().andTargetIdEqualTo(targetId);
                    xLogExample.setOrderByClause("created_time desc");
                    XLog xLog = xLogService.selectFirstExample(xLogExample);
                    String startTime = null;
                    if (null != xLog) {
                        startTime = xLog.getStartTime();
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("作业名称: ");
                    sb.append(targetName);
                    sb.append("<br>");
                    sb.append("作业描述: ");
                    sb.append(EmailUtils.jobDescription);
                    sb.append("<br>");
                    sb.append("开始时间: ");
                    sb.append(startTime);
                    sb.append("<br>");
                    sb.append("KJB路径: ");
                    sb.append(EmailUtils.kjbPath);
                    sb.append("<br>");
                    sb.append("LOG: ");
                    sb.append("<br>");
                    sb.append(msg);
                    // 发送异常邮件
                    xUserService.sendEmail(Long.valueOf(EmailUtils.userId),targetName,sb.toString());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *  @Author: yuenbin
     *  @Date :2020/11/13
     * @Time :14:44
     * @Motto: It is better to be clear than to be clever !
     * @Destrib:  添加一个监控
    **/
    public void doAddMonitor(Map map){
        String jobId = map.get("jobId")+"";
        String transId = map.get("transId")+"";
        String type=map.get("monitorType")+"";
        String targetId= StringUtils.isNotBlank(jobId)?jobId:transId;
        XMonitorExample xMonitorExample = new XMonitorExample();
        xMonitorExample.createCriteria()
                .andTargetIdEqualTo(targetId)
                .andMonitorTypeEqualTo(type);
        XMonitor xMonitor =  xMonitorService.selectFirstExample(xMonitorExample) ;
        if(xMonitor !=null ){
            xMonitor.setTargetStatus(XJobStatus.PENDING.value());
            xMonitor.setDescription(XJobStatus.PENDING.description());
            xMonitorService.updateByExampleSelective(xMonitor, xMonitorExample);
        }else {
            xMonitor = new XMonitor();
            xMonitor.setMonitorId(GenCodeUtil.nextId());
            xMonitor.setTargetId(targetId);
            xMonitor.setTargetStatus(XJobStatus.PENDING.value());
            xMonitor.setDescription(XJobStatus.PENDING.description());
            xMonitor.setMonitorType(type);
            xMonitor.setFailCount("0");
            xMonitor.setSuccessCount("0");
            xMonitorService.insertSelective(xMonitor);
        }

    }


    /**
     *  @Author: yuenbin
     *  @Date :2020/11/13
     * @Time :14:39
     * @Motto: It is better to be clear than to be clever !
     * @Destrib: 移除监控
    **/
    private void removeMonitor(String targetId){

    }


    public void addLog(String logId,String targetId,String logType,String targetResult,String logFile ,String logText,String startTime,String endTime){
        try {
            XLogExample xLogExample = new XLogExample() ;
            xLogExample.createCriteria().andLogIdEqualTo(logId);
            XLog xLog = xLogService.selectFirstExample(xLogExample);
            if(xLog !=null){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {

                }
                xLog.setLogText(logText);
                xLog.setStopTime(endTime);
                xLog.setTargetResult(targetResult);
                xLogService.updateByExampleSelective(xLog, xLogExample);
            }else
            {
                xLog = new XLog() ;
                xLog.setLogId(logId);
                xLog.setTargetId(targetId);
                xLog.setTargetResult(targetResult);
                xLog.setLogType(logType);
                xLog.setLogText(getJobLogText(logText));
                xLog.setStartTime(startTime);
                xLog.setStopTime(endTime);
                xLog.setLogFilePath(logFile);
                xLogService.insertSelective(xLog) ;
            }

        }catch (XtlExceptions ex )
        {
           if(ex.isCausedBy(ex,SQLException.class, DateParseException.class))
            {
               log.error("作业执行日志出现异常,已记录!");
            }
        }

    }

    private String getJobLogText(String logText ){
          StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(logText)) {
            String[] lines = logText.split("\n");
            for (int i = lines.length - 1; i > 0; i--) {
                if ("null".equals(lines[i])) {
                    continue;
                }
                sb.append(lines[i]).append("\n");
            }
        }
        return sb.toString() ;
     }



}
