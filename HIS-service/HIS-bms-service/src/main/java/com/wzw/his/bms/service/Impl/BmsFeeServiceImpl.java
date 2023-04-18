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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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
    @Autowired
    DmsNonDrugItemRecordMapper dmsNonDrugItemRecordMapper;
    @Autowired
    BmsBillsRecordMapper bmsBillsRecordMapper;
    @Autowired
    DmsHerbalPrescriptionRecordMapper dmsHerbalPrescriptionRecordMapper;
    @Autowired
    DmsMedicinePrescriptionRecordMapper dmsMedicinePrescriptionRecordMapper;
    @Autowired
    BmsInvoiceRecordMapper bmsInvoiceRecordMapper;
    @Autowired
    DmsNonDrugMapper dmsNonDrugMapper;
    @Autowired
    DmsHerbalItemRecordMapper dmsHerbalItemRecordMapper;
    @Autowired
    DmsDrugMapper dmsDrugMapper;
    @Autowired
    DmsMedicineItemRecordMapper dmsMedicineItemRecordMapper;
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

    //收费
    //1.传入收费项目ids和对应type (0 挂号 1 检查 2.检验 3.处置  4.草药 5.成药) 发票号 ,操作人id ，便利并判断项目状态是否为1，并修改状态为2 顺便查出挂号id等字段
    //2.根据挂号id查出账单记录,并关联项目(更新串)
    //3.插入发票

    @Override
    public int charge(List<BmsChargeParam> bmsChargeParamList) {
        if (bmsChargeParamList.isEmpty()){
            return 0;
        }else {
            //这些属性插入发票的时候要用
            Long billId = 0l;//账单id
            BigDecimal totalAmount = new BigDecimal(0.0);//总金额   每一项叠加
            String itemList = "";//项目列表串  叠加
            for (BmsChargeParam bmsChargeParam : bmsChargeParamList) {
                Integer type = bmsChargeParam.getType();
                if (type == 1 || type == 2 || type == 3){ //1.检查   2.检验   3.处置
                    DmsNonDrugItemRecord dmsNonDrugItemRecord = dmsNonDrugItemRecordMapper.selectByPrimaryKey(bmsChargeParam.getChargeItemId());
                    if (dmsNonDrugItemRecord.getStatus() == 1){
                        dmsNonDrugItemRecord.setStatus(2);
                        dmsNonDrugItemRecordMapper.updateByPrimaryKeySelective(dmsNonDrugItemRecord);
                    }
                    //根据挂号id查出账单记录,并关联项目(更新串)
                    Long registrationId = dmsNonDrugItemRecord.getRegistrationId();
                    BmsBillsRecordExample bmsBillsRecordExample = new BmsBillsRecordExample();
                    bmsBillsRecordExample.createCriteria().andRegistrationIdEqualTo(registrationId);
                    List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(bmsBillsRecordExample);
                    if (!bmsBillsRecordList.isEmpty()){
                        BmsBillsRecord bmsBillsRecord = bmsBillsRecordList.get(0);
                        String recordList = bmsBillsRecord.getRecordList();
                        if (recordList == null){
                            recordList = "";
                        }
                        String insertRecord = dmsNonDrugItemRecord.getId() + "," + type + "><";
                        recordList = recordList + insertRecord;
                        bmsBillsRecord.setRecordList(recordList);
                        bmsBillsRecordMapper.updateByExampleSelective(bmsBillsRecord,bmsBillsRecordExample);

                        billId = bmsBillsRecord.getId();  //账单id:插入发票的时候要用
                        itemList = itemList + dmsNonDrugItemRecord.getId() + "," + type + "," + bmsChargeParam.getAmount() + "><"; //项目列表串：插入发票的时候要用
                        totalAmount = totalAmount.add(bmsChargeParam.getAmount());//金额累加
                    }
                }else if (type == 4){//4草药
                    DmsHerbalPrescriptionRecord dmsHerbalPrescriptionRecord = dmsHerbalPrescriptionRecordMapper.selectByPrimaryKey(bmsChargeParam.getChargeItemId());
                    if (dmsHerbalPrescriptionRecord.getStatus() == 1){
                        dmsHerbalPrescriptionRecord.setStatus(2);
                        dmsHerbalPrescriptionRecordMapper.updateByPrimaryKeySelective(dmsHerbalPrescriptionRecord);
                    }
                    //根据挂号id查出账单记录，并关联项目(更新串)
                    Long registrationId = dmsHerbalPrescriptionRecord.getRegistrationId();
                    BmsBillsRecordExample bmsBillsRecordExample = new BmsBillsRecordExample();
                    bmsBillsRecordExample.createCriteria().andRegistrationIdEqualTo(registrationId);
                    List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(bmsBillsRecordExample);
                    if (!bmsBillsRecordList.isEmpty()){
                        BmsBillsRecord bmsBillsRecord = bmsBillsRecordList.get(0);
                        String recordList = bmsBillsRecord.getRecordList();
                        if (recordList == null){
                            recordList = "";
                        }
                        String insertRecord = dmsHerbalPrescriptionRecord.getId() + "," + type + "><";
                        recordList = recordList + insertRecord;
                        bmsBillsRecord.setRecordList(recordList);
                        bmsBillsRecordMapper.updateByExampleSelective(bmsBillsRecord,bmsBillsRecordExample);

                        billId = bmsBillsRecord.getId();//账单id：插入发票的时候要用
                        itemList = itemList + dmsHerbalPrescriptionRecord.getId() + "," + type + "," + bmsChargeParam.getAmount() + "><"; //项目列表串:插入发票的时候要用
                        totalAmount = totalAmount.add(bmsChargeParam.getAmount());//金额累加
                    }

                }
                else if (type == 5){ //5.成药
                    DmsMedicinePrescriptionRecord dmsMedicinePrescriptionRecord = dmsMedicinePrescriptionRecordMapper.selectByPrimaryKey(bmsChargeParam.getChargeItemId());
                    if (dmsMedicinePrescriptionRecord.getStatus() == 1){
                        dmsMedicinePrescriptionRecord.setStatus(2);
                        dmsMedicinePrescriptionRecordMapper.updateByPrimaryKeySelective(dmsMedicinePrescriptionRecord);
                    }
                    //根据挂号id查出账单记录,并关联项目(更新串)
                    Long registrationId = dmsMedicinePrescriptionRecord.getRegistrationId();
                    BmsBillsRecordExample bmsBillsRecordExample = new BmsBillsRecordExample();
                    bmsBillsRecordExample.createCriteria().andRegistrationIdEqualTo(registrationId);
                    List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(bmsBillsRecordExample);
                    if (!bmsBillsRecordList.isEmpty()){
                        BmsBillsRecord bmsBillsRecord = bmsBillsRecordList.get(0);
                        String recordList = bmsBillsRecord.getRecordList();
                        if (recordList == null){
                            recordList = "";
                        }
                        String insertRecord = dmsMedicinePrescriptionRecord.getId() + "," + type + "><";
                        recordList = recordList + insertRecord;
                        bmsBillsRecord.setRecordList(recordList);
                        bmsBillsRecordMapper.updateByExampleSelective(bmsBillsRecord,bmsBillsRecordExample);

                        billId = bmsBillsRecord.getId();//账单id:插入发票的时候要用
                        itemList = itemList + dmsMedicinePrescriptionRecord.getId() + "," + type + "," + bmsChargeParam.getAmount() + "><";
                        totalAmount = totalAmount.add(bmsChargeParam.getAmount());//金额累加
                    }

                }
            }
            //插入发票
            BmsInvoiceRecord bmsInvoiceRecord = new BmsInvoiceRecord();
            bmsInvoiceRecord.setCreateTime(new Date());
            bmsInvoiceRecord.setInvoiceNo(bmsChargeParamList.get(0).getInvoiceNo());
            bmsInvoiceRecord.setBillId(billId);
            bmsInvoiceRecord.setAmount(totalAmount);
            bmsInvoiceRecord.setOperatorId(bmsChargeParamList.get(0).getOperatorId());
            bmsInvoiceRecord.setSettlementCatId(bmsChargeParamList.get(0).getSettlementCatId());
            bmsInvoiceRecord.setItemList(itemList);
            bmsInvoiceRecord.setType(1);
            bmsInvoiceRecordMapper.insertSelective(bmsInvoiceRecord);
            return 1;
        }
    }

    //退费查询：根据挂号id查找出所有发票
    //1.传入挂号id，根据挂号id查找账单id
    //2.根据账单id查询所有发票， type为(1  正常)
    //3.解析器，并封装成对象(筛选非药品 (status = 2 未登记 (已缴纳))，药品(便利药品项，封装可退处方项和处方)、计算退款金额)

    @Override
    public List<BmsRefundChargeResult> listRefundByRegistrationId(Long registrationId) {
        List<BmsRefundChargeResult> bmsRefundChargeResultList = new ArrayList<>();
        BmsBillsRecordExample bmsBillsRecordExample = new BmsBillsRecordExample();
        bmsBillsRecordExample.createCriteria().andRegistrationIdEqualTo(registrationId);
        List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(bmsBillsRecordExample);
        if (!bmsBillsRecordList.isEmpty()){
            //根据账单id查询所有发票,type为(1   正常)
            BmsBillsRecord bmsBillsRecord = bmsBillsRecordList.get(0);
            Long billId = bmsBillsRecord.getId();
            BmsInvoiceRecordExample bmsInvoiceRecordExample = new BmsInvoiceRecordExample();
            bmsInvoiceRecordExample.createCriteria().andBillIdEqualTo(billId).andTypeEqualTo(1);
            List<BmsInvoiceRecord> bmsInvoiceRecordList = bmsInvoiceRecordMapper.selectByExample(bmsInvoiceRecordExample);
            for (BmsInvoiceRecord bmsInvoiceRecord : bmsInvoiceRecordList) {
                String itemList = bmsInvoiceRecord.getItemList();
                String[][] resolveItemList = resolveItemList(itemList);
                if (resolveItemList.length != 0){
                    for (int x = 0;x <resolveItemList.length;x++){
                        Long id = Long.valueOf(resolveItemList[x][0]);
                        Integer type = Integer.valueOf(resolveItemList[x][1]);
                        BigDecimal amount = new BigDecimal(resolveItemList[x][2]);
                        System.err.println(id+" "+type+" " +amount);

                        //非药品
                        if (type == 1 || type ==2 || type == 3){
                            DmsNonDrugItemRecord dmsNonDrugItemRecord = dmsNonDrugItemRecordMapper.selectByPrimaryKey(id);
                            if (dmsNonDrugItemRecord.getStatus() == 2){//status =2 未登记(已缴费)
                                BmsRefundChargeResult bmsRefundChargeResult = new BmsRefundChargeResult();
                                bmsRefundChargeResult.setInvoiceIdfNo(bmsInvoiceRecord.getInvoiceNo());
                                bmsRefundChargeResult.setId(dmsNonDrugItemRecord.getId());
                                //退款项名称
                                DmsNonDrug dmsNonDrug = dmsNonDrugMapper.selectByPrimaryKey(dmsNonDrugItemRecord.getNoDrugId());
                                bmsRefundChargeResult.setName(dmsNonDrug.getName());

                                bmsRefundChargeResult.setAmount(dmsNonDrugItemRecord.getAmount());
                                bmsRefundChargeResult.setType(type);
                                bmsRefundChargeResult.setStatus(dmsNonDrugItemRecord.getStatus());
                                bmsRefundChargeResult.setCreateTime(dmsNonDrugItemRecord.getCreateTime());
                                bmsRefundChargeResult.setNum(1l);

                                bmsRefundChargeResultList.add(bmsRefundChargeResult);
                            }
                        }
                        //草药
                        if (type == 4){
                            DmsHerbalItemRecordExample dmsHerbalItemRecordExample = new DmsHerbalItemRecordExample();
                            dmsHerbalItemRecordExample.createCriteria().andPrescriptionIdEqualTo(id);
                            List<DmsHerbalItemRecord> dmsHerbalItemRecordList = dmsHerbalItemRecordMapper.selectByExample(dmsHerbalItemRecordExample);
                            if (!dmsHerbalItemRecordList.isEmpty()){
                                for (DmsHerbalItemRecord dmsHerbalItemRecord : dmsHerbalItemRecordList) {
                                    if ((dmsHerbalItemRecord.getStatus() == 3 || dmsHerbalItemRecord.getStatus() == 1) && dmsHerbalItemRecord.getCurrentNum() != 0l){//3.退药、未发药
                                        BmsRefundChargeResult bmsRefundChargeResult = new BmsRefundChargeResult();
                                        bmsRefundChargeResult.setInvoiceIdfNo(bmsInvoiceRecord.getInvoiceNo());
                                        bmsRefundChargeResult.setId(dmsHerbalItemRecord.getId());
                                        //退款项名称
                                        DmsDrug dmsDrug = dmsDrugMapper.selectByPrimaryKey(dmsHerbalItemRecord.getDrugId());
                                        bmsRefundChargeResult.setName(dmsDrug.getName());
                                        if (dmsHerbalItemRecord.getStatus() == 3){
                                            bmsRefundChargeResult.setAmount(dmsDrug.getPrice().multiply(new BigDecimal(dmsHerbalItemRecord.getCurrentNum())));

                                        }
                                        else{
                                            bmsRefundChargeResult.setAmount(dmsDrug.getPrice().multiply(new BigDecimal(dmsHerbalItemRecord.getTotalNum())));
                                        }
                                        bmsRefundChargeResult.setType(type);
                                        bmsRefundChargeResult.setStatus(dmsHerbalItemRecord.getStatus());
                                        //开立时间
                                        DmsHerbalPrescriptionRecord dmsHerbalPrescriptionRecord = dmsHerbalPrescriptionRecordMapper.selectByPrimaryKey(dmsHerbalItemRecord.getPrescriptionId());
                                        bmsRefundChargeResult.setCreateTime(dmsHerbalPrescriptionRecord.getCreateTime());

                                        bmsRefundChargeResult.setNum(dmsHerbalItemRecord.getCurrentNum());

                                        bmsRefundChargeResultList.add(bmsRefundChargeResult);
                                    }

                                }
                            }
                        }
                        //成药
                        if (type == 5){
                            DmsMedicineItemRecordExample dmsMedicineItemRecordExample = new DmsMedicineItemRecordExample();
                            dmsMedicineItemRecordExample.createCriteria().andPrescriptionIdEqualTo(id);
                            List<DmsMedicineItemRecord> dmsMedicineItemRecordList = dmsMedicineItemRecordMapper.selectByExample(dmsMedicineItemRecordExample);
                            if (!dmsMedicineItemRecordList.isEmpty()){
                                for (DmsMedicineItemRecord dmsMedicineItemRecord : dmsMedicineItemRecordList) {
                                    //退药、未发药(现在数量不为0)
                                    if ((dmsMedicineItemRecord.getStatus() == 3 && dmsMedicineItemRecord.getRefundNum() != 0) || (dmsMedicineItemRecord.getStatus() == 1 && dmsMedicineItemRecord.getCurrentNum() != 0)){
                                        BmsRefundChargeResult bmsRefundChargeResult = new BmsRefundChargeResult();
                                        bmsRefundChargeResult.setInvoiceIdfNo(bmsInvoiceRecord.getInvoiceNo());
                                        bmsRefundChargeResult.setId(dmsMedicineItemRecord.getId());
                                        //退款项名称
                                        DmsDrug dmsDrug = dmsDrugMapper.selectByPrimaryKey(dmsMedicineItemRecord.getDrugId());
                                        bmsRefundChargeResult.setName(dmsDrug.getName());
                                        if (dmsMedicineItemRecord.getStatus() == 3){
                                            bmsRefundChargeResult.setAmount(dmsDrug.getPrice().multiply(new BigDecimal(dmsMedicineItemRecord.getRefundNum())));
                                        }
                                        else{
                                            bmsRefundChargeResult.setAmount(dmsDrug.getPrice().multiply(new BigDecimal(dmsMedicineItemRecord.getNum())));

                                        }
                                        bmsRefundChargeResult.setType(type);
                                        bmsRefundChargeResult.setStatus(dmsMedicineItemRecord.getStatus());
                                        //开立时间
                                        DmsMedicinePrescriptionRecord dmsMedicinePrescriptionRecord = dmsMedicinePrescriptionRecordMapper.selectByPrimaryKey(id);
                                        bmsRefundChargeResult.setCreateTime(dmsMedicinePrescriptionRecord.getCreateTime());

                                        bmsRefundChargeResult.setNum(dmsMedicineItemRecord.getCurrentNum());

                                        bmsRefundChargeResultList.add(bmsRefundChargeResult);
                                    }

                                }
                            }
                        }
                    }
                }
            }
            bmsRefundChargeResultList.sort(new Comparator<BmsRefundChargeResult>() {
                @Override
                public int compare(BmsRefundChargeResult o1, BmsRefundChargeResult o2) {
                    return o1.getInvoiceIdfNo().compareTo(o2.getInvoiceIdfNo());//升序
//                    return o2.getInvoiceIdfNo().compareTo(o1.getInvoiceIdfNo()); //降序
                }
            });
            return bmsRefundChargeResultList;
        }
        return null;
    }

    private String[][] resolveItemList(String itemList) {
        String[] prescription = itemList.split("><");
        String[][] result = new String[prescription.length][3];
        for (int i = 0; i < prescription.length; i++) {
            String[] item = prescription[i].split(",");
            for (int j = 0; j < item.length; j++) {
                result[i][j] = item[j];
            }
        }
        return result;
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
