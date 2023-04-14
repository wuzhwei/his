package com.wzw.his.bms.service.Impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wzw.his.bms.service.BmsFeeService;
import com.wzw.his.common.dto.bms.*;
import com.wzw.his.common.util.DateUtil;
import com.wzw.his.mbg.mapper.*;
import com.wzw.his.mbg.model.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class BmsFeeServiceImpl implements BmsFeeService {
    @Autowired
    DmsRegistrationMapper dmsRegistrationMapper;
    @Autowired
    PmsPatientMapper pmsPatientMapper;
    @Autowired
    SmsSkdMapper smsSkdMapper;
    @Autowired
    SmsStaffMapper smsStaffMapper;
    @Autowired
    SmsRegistrationRankMapper smsRegistrationRankMapper;
    @Autowired
    SmsDeptMapper smsDeptMapper;
    /**
     * 查询当日挂号人
     * 1.传入病例号和挂号日期
     * 2.1 如果病历号为空和挂号日期为空，则显示今天的挂号记录按时间排序
     * 2.2 如果病历号为空挂号日期不为空，则根据日期查询所有挂号，并倒叙
     * 2.3 如果病历号不为空，挂号日期为空，则显示该患者所有就诊信息
     * 2.4 如果病历号和挂号日期不为空,则直接查询
     * @param medicalRecordNo
     * @param queryDate
     * @return
     */
    @Override
    public List<BmsRegistrationPatientResult> listRegisteredPatient(String medicalRecordNo, Date queryDate) {
        ArrayList<BmsRegistrationPatientResult> bmsRegistrationPatientResultList = new ArrayList<>();
        //如果病历号为空和挂号日期为空,则显示今天的挂号记录按创建时间排序
        if (medicalRecordNo == null && queryDate == null) {
            Date date = DateUtil.getDate(new Date());
            //毫秒设为0
            Date startOfDate = DateUtils.setMilliseconds(date, 0);
            DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
            dmsRegistrationExample.createCriteria().andAttendanceDateEqualTo(startOfDate);
            dmsRegistrationExample.setOrderByClause("create_time desc");
            List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
            if (!dmsRegistrationList.isEmpty()) {
                for (DmsRegistration dmsRegistration : dmsRegistrationList) {
                    BmsRegistrationPatientResult bmsRegistrationPatientResult = new BmsRegistrationPatientResult();
                    bmsRegistrationPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
                    bmsRegistrationPatientResult.setAttendanceDate(dmsRegistration.getAttendanceDate());
                    bmsRegistrationPatientResult.setRegistrationCreateDate(dmsRegistration.getCreateTime());
                    bmsRegistrationPatientResult.setNeedBook(dmsRegistration.getNeedBook());
                    bmsRegistrationPatientResult.setRegistrationId(dmsRegistration.getId());
                    //病人name,病人出生日期,病人性别,病历号
                    PmsPatient pmsPatient = pmsPatientMapper.selectByPrimaryKey(dmsRegistration.getPatientId());
                    bmsRegistrationPatientResult.setPatientName(pmsPatient.getName());
                    bmsRegistrationPatientResult.setPatientDateOfBirth(pmsPatient.getDateOfBirth());
                    bmsRegistrationPatientResult.setPatientGender(pmsPatient.getGender());
                    bmsRegistrationPatientResult.setMedicalRecordNo(pmsPatient.getMedicalRecordNo());
                    //挂号级别name,看诊医生名
                    SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());

                    SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd == null ? null : smsSkd.getStaffId());
                    SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff == null ? null : smsStaff.getRegistrationRankId());
                    bmsRegistrationPatientResult.setRegistrationRankName(smsRegistrationRank == null ? null : smsRegistrationRank.getName());
                    bmsRegistrationPatientResult.setBindDoctorName(smsStaff == null ? null : smsStaff.getName());
                    //挂号科室名
                    SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
                    bmsRegistrationPatientResult.setDeptName(smsDept == null ? null : smsDept.getName());
                    bmsRegistrationPatientResultList.add(bmsRegistrationPatientResult);

                }
            }
            return bmsRegistrationPatientResultList;
            //如果病病历号为空挂号日期不为空，则根据日期查询所有挂号，并排序
        } else if (medicalRecordNo == null && queryDate != null) {
            Date date = DateUtil.getDate(queryDate);
            Date startOfDate = DateUtil.setMilliSecond(date, 0);
            DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
            dmsRegistrationExample.createCriteria().andAttendanceDateEqualTo(startOfDate);
            dmsRegistrationExample.setOrderByClause("create_time desc");
            List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
            if (!dmsRegistrationList.isEmpty()) {
                for (DmsRegistration dmsRegistration : dmsRegistrationList) {
                    BmsRegistrationPatientResult bmsRegistrationPatientResult = new BmsRegistrationPatientResult();
                    bmsRegistrationPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
                    bmsRegistrationPatientResult.setAttendanceDate(dmsRegistration.getAttendanceDate());
                    bmsRegistrationPatientResult.setRegistrationCreateDate(dmsRegistration.getCreateTime());
                    bmsRegistrationPatientResult.setNeedBook(dmsRegistration.getNeedBook());
                    bmsRegistrationPatientResult.setRegistrationId(dmsRegistration.getId());
                    //病人name，病人出生日期，病人性别,病历号
                    PmsPatient pmsPatient = pmsPatientMapper.selectByPrimaryKey(dmsRegistration.getPatientId());
                    bmsRegistrationPatientResult.setPatientName(pmsPatient.getName());
                    bmsRegistrationPatientResult.setPatientDateOfBirth(pmsPatient.getDateOfBirth());
                    bmsRegistrationPatientResult.setPatientGender(pmsPatient.getGender());
                    bmsRegistrationPatientResult.setMedicalRecordNo(pmsPatient.getMedicalRecordNo());
                    //挂号级别name,看诊医生名
                    SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());
                    SmsStaff smsStaff = null;
                    SmsRegistrationRank smsRegistrationRank = null;
                    if (null != smsSkd)
                        smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
                    if (smsStaff != null)
                        smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
                    if (smsRegistrationRank != null)
                        bmsRegistrationPatientResult.setRegistrationRankName(smsRegistrationRank.getName());
                    if (smsStaff != null)
                        bmsRegistrationPatientResult.setBindDoctorName(smsStaff.getName());
                    //挂号科室名
                    SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
                    if (null != smsDept)
                        bmsRegistrationPatientResult.setDeptName(smsDept.getName());

                    bmsRegistrationPatientResultList.add(bmsRegistrationPatientResult);

                }
            }
            return bmsRegistrationPatientResultList;

        } else if (medicalRecordNo != null && queryDate == null) {//如果病历号不为空，挂号日期为空，则显示该患者所有就诊信息
            PmsPatientExample pmsPatientExample = new PmsPatientExample();
            pmsPatientExample.createCriteria().andMedicalRecordNoEqualTo(medicalRecordNo);
            List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
            //如果数据正常，则一个病历号只会存在一个病人

            if (!CollectionUtil.isEmpty(pmsPatientList)) {//如果有对应的患者记录
                PmsPatient pmsPatient = pmsPatientList.get(0);

                //查询该患者的所有就诊记录并按时间倒序
                DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
                dmsRegistrationExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId());
                dmsRegistrationExample.setOrderByClause("create_time desc");
                List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
                for (DmsRegistration dmsRegistration : dmsRegistrationList) {
                    BmsRegistrationPatientResult bmsRegistrationPatientResult = new BmsRegistrationPatientResult();
                    bmsRegistrationPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
                    bmsRegistrationPatientResult.setAttendanceDate(dmsRegistration.getAttendanceDate());
                    bmsRegistrationPatientResult.setRegistrationCreateDate(dmsRegistration.getCreateTime());
                    bmsRegistrationPatientResult.setNeedBook(dmsRegistration.getNeedBook());
                    bmsRegistrationPatientResult.setRegistrationId(dmsRegistration.getId());
                    //病人name，病人出生日期，病人性别,病历号
                    bmsRegistrationPatientResult.setPatientName(pmsPatient.getName());
                    bmsRegistrationPatientResult.setPatientDateOfBirth(pmsPatient.getDateOfBirth());
                    bmsRegistrationPatientResult.setPatientGender(pmsPatient.getGender());
                    bmsRegistrationPatientResult.setMedicalRecordNo(pmsPatient.getMedicalRecordNo());
                    //挂号级别name
                    SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());
                    SmsStaff smsStaff = null;
                    SmsRegistrationRank smsRegistrationRank = null;

                    if (null != smsSkd)
                        smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
                    //挂号级别name
                    if (null != smsStaff)
                        smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
                    //挂号级别名
                    if (null != smsRegistrationRank)
                        bmsRegistrationPatientResult.setRegistrationRankName(smsRegistrationRank.getName());
                    // 医生名
                    if (null != smsStaff)
                        bmsRegistrationPatientResult.setBindDoctorName(smsStaff.getName());
                    //挂号科室名
                    SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
                    if (null != smsDept)
                        bmsRegistrationPatientResult.setDeptName(smsDept.getName());


                    bmsRegistrationPatientResultList.add(bmsRegistrationPatientResult);
                }
                return bmsRegistrationPatientResultList;
            } else {//如果没有对应的患者记录
                return null;
            }
        }
        else {//如果病历号为和挂号日期都不为空，则直接查询
            Date date = DateUtil.getDate(queryDate);
            Date startOfDate = DateUtil.setMilliSecond(date,0);
            PmsPatientExample pmsPatientExample = new PmsPatientExample();
            pmsPatientExample.createCriteria().andMedicalRecordNoEqualTo(medicalRecordNo);
            List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(pmsPatientExample);
            if (!pmsPatientList.isEmpty()){//如果有对应的患者记录
                PmsPatient pmsPatient = pmsPatientList.get(0);
                DmsRegistrationExample dmsRegistrationExample = new DmsRegistrationExample();
                dmsRegistrationExample.createCriteria().andPatientIdEqualTo(pmsPatient.getId()).andAttendanceDateEqualTo(startOfDate);
                dmsRegistrationExample.setOrderByClause("create_time desc");
                List<DmsRegistration> dmsRegistrationList = dmsRegistrationMapper.selectByExample(dmsRegistrationExample);
                for (DmsRegistration dmsRegistration:dmsRegistrationList) {
                    BmsRegistrationPatientResult bmsRegistrationPatientResult = new BmsRegistrationPatientResult();
                    bmsRegistrationPatientResult.setRegistrationStatus(dmsRegistration.getStatus());
                    bmsRegistrationPatientResult.setAttendanceDate(dmsRegistration.getAttendanceDate());
                    bmsRegistrationPatientResult.setRegistrationCreateDate(dmsRegistration.getCreateTime());
                    bmsRegistrationPatientResult.setNeedBook(dmsRegistration.getNeedBook());
                    bmsRegistrationPatientResult.setRegistrationId(dmsRegistration.getId());
                    //病人name，病人出生日期，病人性别,病历号
                    bmsRegistrationPatientResult.setPatientName(pmsPatient.getName());
                    bmsRegistrationPatientResult.setPatientDateOfBirth(pmsPatient.getDateOfBirth());
                    bmsRegistrationPatientResult.setPatientGender(pmsPatient.getGender());
                    bmsRegistrationPatientResult.setMedicalRecordNo(pmsPatient.getMedicalRecordNo());
                    //挂号级别name,看诊医生名
                    SmsSkd smsSkd = smsSkdMapper.selectByPrimaryKey(dmsRegistration.getSkdId());
                    SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
                    SmsRegistrationRank smsRegistrationRank = smsRegistrationRankMapper.selectByPrimaryKey(smsStaff.getRegistrationRankId());
                    if(null!=smsRegistrationRank)
                        bmsRegistrationPatientResult.setRegistrationRankName(smsRegistrationRank.getName());
                    if(null != smsStaff)
                        bmsRegistrationPatientResult.setBindDoctorName(smsStaff.getName());
                    //挂号科室名
                    SmsDept smsDept = smsDeptMapper.selectByPrimaryKey(dmsRegistration.getDeptId());
                    if(null != smsDept)
                        bmsRegistrationPatientResult.setDeptName(smsDept.getName());

                    bmsRegistrationPatientResultList.add(bmsRegistrationPatientResult);
                }
                return bmsRegistrationPatientResultList;
            }
            else{//如果没有对应的患者记录
                return null;
            }
        }
    }

    @Override
    public List<BmsChargeResult> listChargeByRegistrationId(Long registrationId) {
        return null;
    }

    @Override
    public int charge(List<BmsChargeParam> bmsChargeParamList) {
        return 0;
    }

    @Override
    public List<BmsRefundChargeResult> listRefundByRegistrationId(Long registrationId) {
        return null;
    }

    @Override
    public int refundCharge(List<BmsRefundChargeParam> bmsRefundChargeParamList) {
        return 0;
    }

    @Override
    public int refundRegistrationCharge(BmsRefundRegChargeParam bmsRefundRegChargeParam) {
        return 0;
    }
}
