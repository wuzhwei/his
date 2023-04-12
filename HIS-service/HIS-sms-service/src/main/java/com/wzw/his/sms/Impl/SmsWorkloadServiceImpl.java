package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsWorkloadResult;
import com.wzw.his.mbg.mapper.SmsDeptMapper;
import com.wzw.his.mbg.mapper.SmsRegistrationRankMapper;
import com.wzw.his.mbg.mapper.SmsStaffMapper;
import com.wzw.his.mbg.mapper.SmsWorkloadRecordMapper;
import com.wzw.his.mbg.model.*;
import com.wzw.his.sms.SmsWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Service
public class SmsWorkloadServiceImpl implements SmsWorkloadService {
    @Autowired
    SmsWorkloadRecordMapper smsWorkloadRecordMapper;
    @Autowired
    SmsStaffMapper smsStaffMapper;
    @Autowired
    SmsRegistrationRankMapper smsRegistrationRankMapper;
    @Autowired
    SmsDeptMapper smsDeptMapper;
    //个人工作量统计
    @Override
    public SmsWorkloadResult workloadPersonalStatistic(Long staffId, Date date) {
        SmsWorkloadResult workload = new SmsWorkloadResult();
        workload.setWorkloadId(null);
        workload.setCreateTime(new Date());
        workload.setDate(date);
        //查询员工和挂号级别信息
        SmsStaff staff = smsStaffMapper.selectByPrimaryKey(staffId);
        if (staff == null){
            return null;
        }
        workload.setStaffId(staffId);
        workload.setStaffName(staff.getName());
        SmsRegistrationRank rank = smsRegistrationRankMapper.selectByPrimaryKey(staff.getRegistrationRankId());
        if (rank == null){
            return null;
        }
        //查询部门信息
        SmsDept dept = smsDeptMapper.selectByPrimaryKey(staff.getDeptId());
        if (dept == null){
            return null;
        }
        workload.setDeptId(dept.getId());
        workload.setDeptName(dept.getName());
        //设置处方单状态
        List<Integer> statusList = new ArrayList<>();
        statusList.add(2);
        statusList.add(3);
        //设置起始时间
        Date startDate = getBeginDate(date);
        //查询开立成药


        return workload;
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
        SmsWorkloadRecord beforeRecord = addAll(workloadRecordList);
        //合并两个
        lastResult.setMedicineAmount(lastResult.getMedicineAmount().add(beforeRecord.getMedicineAmount()));
        lastResult.setHerbalAmount(lastResult.getHerbalAmount().add(beforeRecord.getHerbalAmount()));
        lastResult.setCheckAmount(lastResult.getCheckAmount().add(beforeRecord.getCheckAmount()));
        lastResult.setTestAmount(lastResult.getTestAmount().add(beforeRecord.getTestAmount()));
        lastResult.setDispositionAmount(lastResult.getDispositionAmount().add(beforeRecord.getDispositionAmount()));
        lastResult.setRegistrationAmount(lastResult.getRegistrationAmount().add(beforeRecord.getRegistrationAmount()));
        lastResult.setAmount(lastResult.getAmount().add(beforeRecord.getAmount()));

        lastResult.setRegistrationNum(lastResult.getRegistrationNum()+beforeRecord.getRegistrationNum());
        lastResult.setExcuteCheckAmount(lastResult.getExcuteCheckAmount().add(beforeRecord.getExcuteCheckAmount()));
        lastResult.setExcuteTestAmount(lastResult.getExcuteTestAmount().add(beforeRecord.getExcuteTestAmount()));
        lastResult.setExcuteDispositionAmount(lastResult.getExcuteDispositionAmount().add(beforeRecord.getExcuteDispositionAmount()));
        lastResult.setExcuteNum(lastResult.getExcuteNum()+beforeRecord.getExcuteNum());


        return lastResult;
    }
    //求和
    private SmsWorkloadRecord addAll(List<SmsWorkloadRecord> recordList) {
        SmsWorkloadRecord resultRecord = new SmsWorkloadRecord();

        resultRecord.setMedicineAmount(new BigDecimal(0));
        resultRecord.setHerbalAmount(new BigDecimal(0));
        resultRecord.setCheckAmount(new BigDecimal(0));
        resultRecord.setTestAmount(new BigDecimal(0));
        resultRecord.setDispositionAmount(new BigDecimal(0));
        resultRecord.setRegistrationAmount(new BigDecimal(0));
        resultRecord.setAmount(new BigDecimal(0));

        resultRecord.setRegistrationNum(new Long(0));
        resultRecord.setExcuteCheckAmount(new BigDecimal(0));
        resultRecord.setExcuteTestAmount(new BigDecimal(0));
        resultRecord.setExcuteDispositionAmount(new BigDecimal(0));
        resultRecord.setExcuteNum(new Long(0));
        for (SmsWorkloadRecord record : recordList) {
            resultRecord.setMedicineAmount(resultRecord.getMedicineAmount().add(record.getMedicineAmount()));
            resultRecord.setHerbalAmount(resultRecord.getHerbalAmount().add(record.getHerbalAmount()));
            resultRecord.setCheckAmount(resultRecord.getCheckAmount().add(record.getCheckAmount()));
            resultRecord.setTestAmount(resultRecord.getTestAmount().add(record.getTestAmount()));
            resultRecord.setDispositionAmount(resultRecord.getDispositionAmount().add(record.getDispositionAmount()));
            resultRecord.setRegistrationAmount(resultRecord.getRegistrationAmount().add(record.getRegistrationAmount()));
            resultRecord.setAmount(resultRecord.getAmount().add(record.getAmount()));

            resultRecord.setRegistrationNum(resultRecord.getRegistrationNum()+record.getRegistrationNum());
            resultRecord.setExcuteCheckAmount(resultRecord.getExcuteCheckAmount().add(record.getExcuteCheckAmount()));
            resultRecord.setExcuteTestAmount(resultRecord.getExcuteTestAmount().add(record.getExcuteTestAmount()));
            resultRecord.setExcuteDispositionAmount(resultRecord.getExcuteDispositionAmount().add(record.getExcuteDispositionAmount()));
            resultRecord.setExcuteNum(resultRecord.getExcuteNum()+record.getExcuteNum());

        }
        return resultRecord;
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
