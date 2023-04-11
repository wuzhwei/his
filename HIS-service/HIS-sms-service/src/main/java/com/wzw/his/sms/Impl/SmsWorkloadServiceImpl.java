package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsWorkloadResult;
import com.wzw.his.mbg.mapper.SmsWorkloadRecordMapper;
import com.wzw.his.mbg.model.SmsWorkloadRecord;
import com.wzw.his.mbg.model.SmsWorkloadRecordExample;
import com.wzw.his.sms.SmsWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SmsWorkloadServiceImpl implements SmsWorkloadService {
    @Autowired
    SmsWorkloadRecordMapper smsWorkloadRecordMapper;
    @Override
    public SmsWorkloadResult workloadPersonalStatistic(Long staffId, Date date) {
        return null;
    }

    @Override
    public SmsWorkloadResult queryPersonalWorkloadPeriod(Long staffId, Date startDatetime, Date endDatetime) {
        //最后一天之前的,使用between，并累计
        SmsWorkloadRecordExample workloadRecordExample = new SmsWorkloadRecordExample();
        workloadRecordExample.createCriteria().andDateBetween(getBeginDate(startDatetime),getBeginDate(endDatetime))
                .andStaffIdEqualTo(staffId)
                .andStatusEqualTo(1)
                .andTypeEqualTo(0);
        List<SmsWorkloadRecord> workloadRecordList = smsWorkloadRecordMapper.selectByExample(workloadRecordExample);
        //添加最后一天的
        SmsWorkloadResult lastResult = workloadPersonalStatistic(staffId, endDatetime);
        addAll(workloadRecordList);

        return lastResult;
    }
    //求和
    private SmsWorkloadRecord addAll(List<SmsWorkloadRecord> workloadRecordList) {

        return null;
    }

    //获取给定时间的零点
    private Date getBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //设置时分秒为0:0:0:0
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    @Override
    public SmsWorkloadResult workloadDeptStatistic(Long deptId, Date date) {
        return null;
    }

    @Override
    public SmsWorkloadResult queryDeptWorkloadPeriod(Long deptId, Date startDatetime, Date endDatetime) {
        return null;
    }

    @Override
    public List<SmsWorkloadResult> queryDeptWorkloadList(Date startDatetime, Date endDatetime) {
        return null;
    }

    @Override
    public List<SmsWorkloadResult> queryDeptPersonalWorkloadList(Long deptId, Date startDatetime, Date endDatetime) {
        return null;
    }

    @Override
    public int insertWorkload(SmsWorkloadResult smsWorkLoadResult) {
        return 0;
    }

    @Override
    public int dailyPersonalStatistic(Date date) {
        return 0;
    }

    @Override
    public int dailyDeptStatistic(Date date) {
        return 0;
    }

    @Override
    public int statistic(Date date) {
        return 0;
    }
}
