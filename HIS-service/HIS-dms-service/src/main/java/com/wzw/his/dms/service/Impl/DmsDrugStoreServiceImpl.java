package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsRefundDrugListParam;
import com.wzw.his.common.dto.pms.PmsDrugStorePatientListResult;
import com.wzw.his.common.dto.pms.PmsDrugStorePatientResult;
import com.wzw.his.dms.service.DmsDrugStoreService;
import com.wzw.his.mbg.mapper.DmsHerbalPrescriptionRecordMapper;
import com.wzw.his.mbg.mapper.DmsMedicinePrescriptionRecordMapper;
import com.wzw.his.mbg.mapper.DmsRegistrationMapper;
import com.wzw.his.mbg.mapper.PmsPatientMapper;
import com.wzw.his.mbg.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class DmsDrugStoreServiceImpl implements DmsDrugStoreService {
    @Autowired
    DmsHerbalPrescriptionRecordMapper dmsHerbalPrescriptionRecordMapper;
    @Autowired
    DmsRegistrationMapper dmsRegistrationMapper;
    @Autowired
    PmsPatientMapper pmsPatientMapper;
    @Autowired
    DmsMedicinePrescriptionRecordMapper dmsMedicinePrescriptionRecordMapper;
    @Override
    public PmsDrugStorePatientListResult listPatient(Date date, String medicalRecordNo, Integer type) {
        //根据type(4 草药, 5成药)判断处方类型
        if (type == 4){
            return listWhenHerbal(date,medicalRecordNo);
        }else if (type == 5){
            return listWhenMedicine(date,medicalRecordNo);
        }
        return null;
    }
        //type = 5 时查找成药处方
    private PmsDrugStorePatientListResult listWhenMedicine(Date date, String medicalRecordNo) {
        System.err.println("正在查询西药处方");
        List<PmsDrugStorePatientResult> undoPatientList = new ArrayList<>();
        List<PmsDrugStorePatientResult> donePatientList = new ArrayList<>();
        //在处方表中根据时间查询
        List<Date> dateList = createDate(date);
        DmsMedicinePrescriptionRecordExample recordExample = new DmsMedicinePrescriptionRecordExample();
        recordExample.createCriteria().andCreateTimeBetween(dateList.get(0),dateList.get(1));
        List<DmsMedicinePrescriptionRecord> recordList = dmsMedicinePrescriptionRecordMapper.selectByExample(recordExample);
        System.err.println("在时间范围内的西药处方条数："+recordList.size());
        //判断处方项状态为2(未领药)\3（已领药）分类，查询挂号Id并去重
        Map<Long, List<Long>> undoPatientToRecord = new Hashtable();
        Map<Long, List<Long>> donePatientToRecord = new Hashtable();
        for(DmsMedicinePrescriptionRecord record : recordList){
            if(record.getStatus() == 2){  //未领药
                if(undoPatientToRecord.get(record.getRegistrationId()) == null){
                    List<Long> longList = new ArrayList<>();
                    longList.add(record.getId());
                    undoPatientToRecord.put(record.getRegistrationId(),longList);
                }else{
                    List<Long> longList = undoPatientToRecord.get(record.getRegistrationId());
                    longList.add(record.getId());
                    undoPatientToRecord.put(record.getRegistrationId(),longList);
                }
            }else if(record.getStatus() == 3){ //已领药
                if(donePatientToRecord.get(record.getRegistrationId()) == null){
                    List<Long> longList = new ArrayList<>();
                    longList.add(record.getId());
                    donePatientToRecord.put(record.getRegistrationId(),longList);
                }else{
                    List<Long> longList = donePatientToRecord.get(record.getRegistrationId());
                    longList.add(record.getId());
                    donePatientToRecord.put(record.getRegistrationId(),longList);
                }
            }
        }
        //根据病人id再在患者表中封装病人信息，如果传入患者病历号，则筛选患者
        for(Long key : undoPatientToRecord.keySet()){
            PmsDrugStorePatientResult result = new PmsDrugStorePatientResult();
            //查挂号表
            DmsRegistration registration = dmsRegistrationMapper.selectByPrimaryKey(key);
            if(registration == null){
                continue;
            }
            //查患者表，封装信息
            PmsPatient patient = pmsPatientMapper.selectByPrimaryKey(registration.getPatientId());
            if(patient == null){
                continue;
            }
            if(medicalRecordNo != null){
                if(!patient.getMedicalRecordNo().equals(medicalRecordNo)){
                    continue;
                }
            }
            result.setPatientId(patient.getId());
            result.setPatientName(patient.getName());
            result.setMedicalRecordNo(patient.getMedicalRecordNo());
            result.setPrescriptionIdList(undoPatientToRecord.get(key));
            undoPatientList.add(result);
        }
        for(Long key : donePatientToRecord.keySet()){
            PmsDrugStorePatientResult result = new PmsDrugStorePatientResult();
            //查挂号表
            DmsRegistration registration = dmsRegistrationMapper.selectByPrimaryKey(key);
            if(registration == null){
                continue;
            }
            //查患者表，封装信息
            PmsPatient patient = pmsPatientMapper.selectByPrimaryKey(registration.getPatientId());
            if(patient == null){
                continue;
            }
            if(medicalRecordNo != null){
                if(!patient.getMedicalRecordNo().equals(medicalRecordNo)){
                    continue;
                }
            }
            result.setPatientId(patient.getId());
            result.setPatientName(patient.getName());
            result.setMedicalRecordNo(patient.getMedicalRecordNo());
            result.setPrescriptionIdList(donePatientToRecord.get(key));
            donePatientList.add(result);
        }


        PmsDrugStorePatientListResult result = new PmsDrugStorePatientListResult();
        result.setUndoPatientList(undoPatientList);
        result.setDonePatientList(donePatientList);
        return result;
    }

    private PmsDrugStorePatientListResult listWhenHerbal(Date date, String medicalRecordNo) {
        System.err.println("正在查询草药处方");
        List<PmsDrugStorePatientResult> undoPatientList = new ArrayList<>();
        List<PmsDrugStorePatientResult> donePatientList = new ArrayList<>();
        //在处方表中根据时间查询
        List<Date> dateList = createDate(date);
        DmsHerbalPrescriptionRecordExample recordExample = new DmsHerbalPrescriptionRecordExample();
        recordExample.createCriteria().andCreateTimeBetween(dateList.get(0),dateList.get(1));//今天之内
        List<DmsHerbalPrescriptionRecord> recordList = dmsHerbalPrescriptionRecordMapper.selectByExample(recordExample);
        //判断处方项状态为2(未领药)、3(已领药)分类,查询挂号id并去重
        Hashtable<Long, List<Long>> undoPatientToRecord = new Hashtable<>();
        Hashtable<Long, List<Long>> donePatientToRecord = new Hashtable<>();
        for (DmsHerbalPrescriptionRecord record : recordList) {
            if (record.getStatus() == 2){
                if (undoPatientToRecord.get(record.getRegistrationId()) == null){
                    List<Long> longList = new ArrayList<>();
                    longList.add(record.getId());
                    undoPatientToRecord.put(record.getRegistrationId(),longList);
                }else{
                    List<Long> longList = undoPatientToRecord.get(record.getRegistrationId());
                    longList.add(record.getId());
                    undoPatientToRecord.put(record.getRegistrationId(),longList);
                }

            }else if(record.getStatus() == 3){
                if (donePatientToRecord.get(record.getRegistrationId()) == null){
                    List<Long> longList = new ArrayList<>();
                    longList.add(record.getId());
                    donePatientToRecord.put(record.getRegistrationId(),longList);
                }else{
                    List<Long> longList = donePatientToRecord.get(record.getRegistrationId());
                    longList.add(record.getId());
                    donePatientToRecord.put(record.getRegistrationId(),longList);
                }
            }

        }
        //根据病人id再在患者表中封装病人信息,如果传入患者病历号,则筛选患者g
        for (Long key : undoPatientToRecord.keySet()) {
            PmsDrugStorePatientResult result = new PmsDrugStorePatientResult();
            //查挂号表
            DmsRegistration registration = dmsRegistrationMapper.selectByPrimaryKey(key);
            if (registration == null){
                continue;
            }
            //查找患者表,封装信息
            PmsPatient patient = pmsPatientMapper.selectByPrimaryKey(registration.getPatientId());
            if (patient == null){
                continue;
            }
            if (medicalRecordNo != null){
                if (!patient.getMedicalRecordNo().equals(medicalRecordNo)){
                    continue;
                }
            }
            result.setPatientId(patient.getId());
            result.setPatientName(patient.getName());
            result.setMedicalRecordNo(patient.getMedicalRecordNo());
            result.setPrescriptionIdList(donePatientToRecord.get(key));
            donePatientList.add(result);
        }
        PmsDrugStorePatientListResult result = new PmsDrugStorePatientListResult();
        result.setUndoPatientList(undoPatientList);
        result.setDonePatientList(donePatientList);
        return result;


    }
    //获取给定时间的时间段
    private List<Date> createDate(Date date) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //设置时分秒毫秒为0:0:0
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        dateList.add(calendar.getTime());
        //设置时分秒毫秒为23:59:59:999
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        dateList.add(calendar.getTime());
        return dateList;
    }


    @Override
    public int releaseDrug(Long prescriptionId, Integer type) {
        return 0;
    }

    @Override
    public int refundDrug(DmsRefundDrugListParam dmsRefundDrugListParam) {
        return 0;
    }
}
