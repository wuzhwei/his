package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsRegistrationParam;
import com.wzw.his.common.util.DateUtil;
import com.wzw.his.dms.service.DmsRegistrationService;
import com.wzw.his.mbg.mapper.DmsRegistrationMapper;
import com.wzw.his.mbg.mapper.PmsPatientMapper;
import com.wzw.his.mbg.model.PmsPatient;
import com.wzw.his.mbg.model.PmsPatientExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DmsRegistrationServiceImpl implements DmsRegistrationService {
    @Autowired
    DmsRegistrationMapper dmsRegistrationMapper;
    @Autowired
    PmsPatientMapper pmsPatientMapper;

    //1.调用PmsPatientDao根据省份证号查询是否存在
    //2.如果不存在,则向PmsPatient表中插入数据,返回id
    //3.判断是否为专家号
    //3.1如果为专家号,则根据skd_id判断remain是否为>0,如果大于0，则向dms_registration插入信息,并且绑定医生(sku_id,bind_status=1),，并修改sms_skd中的排班限额(-1),否则挂号失败
    //3.2如果为非专家号,则向dms_registration插入信息,bin_status=0
    //4.向bms_bills_record中插入账单记录
    //5.通过门诊号最近时间返回账单id
    //6.插入发票记录

    @Override
    public int createRegistration(DmsRegistrationParam dmsRegistrationParam) {
        PmsPatientExample example = new PmsPatientExample();
        example.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatientList = pmsPatientMapper.selectByExample(example);
        if (pmsPatientList.size() == 0){   //如果不存在，则向PmsPatient表中插入数据
            PmsPatient pmsPatient = new PmsPatient();
            BeanUtils.copyProperties(dmsRegistrationParam,pmsPatient);
            //生成病例号
            String medicalRecordNo = generateMedicalRecordNo(dmsRegistrationParam.getIdentificationNo());

        }
        

        return 0;
    }
    //生成病历号
    private String generateMedicalRecordNo(String identificationNo) {
        Date date = new Date();
        DateUtil.getDateStr(date);//年月日字符串


    }
}
