package org.yaukie.frame;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class TimeDemo {
    public static void main(String[] args)throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date beginDate = null;// 发送邮件开始时间
        Date endData = null;// 发送邮件结束时间
        try {
            beginDate = simpleDateFormat.parse("05:11:12");
            endData = simpleDateFormat.parse("11:22:55");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Time beginTime = new Time(beginDate.getTime());
        Time endTime2 = new Time(endData.getTime());
//
//        System.out.println("开始时间：" + beginTime);
//        System.out.println("结束时间：" + endTime);

        Date daa = beginTime;
//        String format = "HH:mm:ss";
        Date nowTime = simpleDateFormat.parse("05:11:11");
        Date startTime = simpleDateFormat.parse("05:11:12");
        Date endTime = simpleDateFormat.parse("16:22:55");
//        System.out.println(isEffectiveDate(nowTime, startTime, endTime));

//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
//        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        Date nowDate = new Date(System.currentTimeMillis());
        Date d = new Date();
        SimpleDateFormat sbf = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sbf.format(d));
        Date nowTime2 = sbf.parse(sbf.format(d));
        System.out.println(isEffectiveDate(nowTime2, startTime, endTime));
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
