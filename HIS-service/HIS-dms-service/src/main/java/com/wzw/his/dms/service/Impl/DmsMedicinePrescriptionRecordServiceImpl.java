package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsMedicineItemRecordResult;
import com.wzw.his.common.dto.dms.DmsMedicinePrescriptionRecordParam;
import com.wzw.his.common.dto.dms.DmsMedicinePrescriptionRecordResult;
import com.wzw.his.dms.service.DmsMedicinePrescriptionRecordService;
import com.wzw.his.mbg.mapper.DmsDrugMapper;
import com.wzw.his.mbg.mapper.DmsMedicineItemRecordMapper;
import com.wzw.his.mbg.mapper.DmsMedicinePrescriptionRecordMapper;
import com.wzw.his.mbg.mapper.SmsStaffMapper;
import com.wzw.his.mbg.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DmsMedicinePrescriptionRecordServiceImpl implements DmsMedicinePrescriptionRecordService {
    @Autowired
    private DmsMedicinePrescriptionRecordMapper dmsMedicinePrescriptionRecordMapper;
    @Autowired
    SmsStaffMapper smsStaffMapper;
    @Autowired
    DmsMedicineItemRecordMapper dmsMedicineItemRecordMapper;
    @Autowired
    DmsDrugMapper dmsDrugMapper;
    @Override
    public Long apply(DmsMedicinePrescriptionRecordParam dmsMedicinePrescriptionRecordParam) {
        return null;
    }

    @Override
    public int invalid(List<Long> ids) {
        return 0;
    }

    @Override
    public List<DmsMedicinePrescriptionRecordResult> listByReg(Long registrationId) {
        return null;
    }

    @Override
    public List<DmsMedicinePrescriptionRecordResult> listByIds(List<Long> ids) {
        List<DmsMedicinePrescriptionRecord> dmsMedicinePrescriptionRecordList =new ArrayList<>();
        List<DmsMedicinePrescriptionRecordResult> dmsMedicinePrescriptionRecordResultList=new ArrayList<>();
        DmsMedicinePrescriptionRecordExample dmsMedicinePrescriptionRecordExample = new DmsMedicinePrescriptionRecordExample();
        dmsMedicinePrescriptionRecordExample.createCriteria().andIdIn(ids);
        dmsMedicinePrescriptionRecordList = dmsMedicinePrescriptionRecordMapper.selectByExample(dmsMedicinePrescriptionRecordExample);
        //封装订单
        for (DmsMedicinePrescriptionRecord dmsMedicinePrescriptionRecord : dmsMedicinePrescriptionRecordList) {
            DmsMedicinePrescriptionRecordResult dmsMedicinePrescriptionRecordResult=new DmsMedicinePrescriptionRecordResult();
            BeanUtils.copyProperties(dmsMedicinePrescriptionRecord,dmsMedicinePrescriptionRecordResult);
            //需要单独封装createStaffName
            SmsStaff smsStaff =smsStaffMapper.selectByPrimaryKey(dmsMedicinePrescriptionRecord.getCreateStaffId());
            if(null!=smsStaff){
                dmsMedicinePrescriptionRecordResult.setCreateStaffName(smsStaff.getName());
            }
            //封装处方项
            //将DmsMedicineItemRecord封装为DmsMedicineItemRecordResult
            List<DmsMedicineItemRecordResult> dmsMedicineItemRecordResultList=new ArrayList<>();
            DmsMedicineItemRecordExample itemRecordExample = new DmsMedicineItemRecordExample();
            itemRecordExample.createCriteria().andPrescriptionIdEqualTo(dmsMedicinePrescriptionRecord.getId());
            List<DmsMedicineItemRecord> itemRecordList = dmsMedicineItemRecordMapper.selectByExample(itemRecordExample);
            for (DmsMedicineItemRecord dmsMedicineItemRecord : itemRecordList) {
                DmsMedicineItemRecordResult dmsMedicineItemRecordResult=new DmsMedicineItemRecordResult();
                BeanUtils.copyProperties(dmsMedicineItemRecord,dmsMedicineItemRecordResult);
                BigDecimal price = dmsDrugMapper.selectByPrimaryKey(dmsMedicineItemRecord.getDrugId()).getPrice();
                String drugName=dmsDrugMapper.selectByPrimaryKey(dmsMedicineItemRecord.getDrugId()).getName();
                dmsMedicineItemRecordResult.setDrugName(drugName);
                dmsMedicineItemRecordResult.setPrice(price);
                dmsMedicineItemRecordResultList.add(dmsMedicineItemRecordResult);
            }
            dmsMedicinePrescriptionRecordResult.setDmsMedicineItemRecordResultList(dmsMedicineItemRecordResultList);//向处方中加入处方项列表
            dmsMedicinePrescriptionRecordResultList.add(dmsMedicinePrescriptionRecordResult); // 向处方列表中加入处方
        }
        return dmsMedicinePrescriptionRecordResultList;
    }
}
