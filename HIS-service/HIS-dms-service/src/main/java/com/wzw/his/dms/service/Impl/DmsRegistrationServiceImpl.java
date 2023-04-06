package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsRegistrationParam;
import com.wzw.his.common.util.AgeStrUtil;
import com.wzw.his.common.util.DateUtil;
import com.wzw.his.dms.service.DmsRegistrationService;
import com.wzw.his.mbg.mapper.*;
import com.wzw.his.mbg.model.*;
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

    @Autowired
    SmsSkdMapper smsSkdMapper;
    @Autowired
    BmsBillsRecordMapper bmsBillsRecordMapper;
    @Autowired
    BmsInvoiceRecordMapper bmsInvoiceRecordMapper;

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
            pmsPatient.setMedicalRecordNo(medicalRecordNo);
            pmsPatientMapper.insertSelective(pmsPatient);
        }
        //通过省份证号查询并返回病人id
        PmsPatientExample example1 = new PmsPatientExample();
        example1.createCriteria().andIdentificationNoEqualTo(dmsRegistrationParam.getIdentificationNo());
        List<PmsPatient> pmsPatientList1 = pmsPatientMapper.selectByExample(example1);
        if (!pmsPatientList1.isEmpty()){
            Long patientId = pmsPatientList1.get(0).getId();
            //创建要插入的DmsRegistration对象
            DmsRegistration dmsRegistration = new DmsRegistration();
            BeanUtils.copyProperties(dmsRegistrationParam,dmsRegistration);
            dmsRegistration.setPatientId(patientId);
            Date createDate = new Date();//获取当前时间,并在之后通过该时间与病人id获取挂号id
            dmsRegistration.setCreateTime(createDate);
            dmsRegistration.setStatus(1);//挂号转态为待诊1
            //处理年龄
            Date dateOfBirth = dmsRegistrationParam.getDateOfBirth();
            String age = AgeStrUtil.getAgeStr(dateOfBirth);
            dmsRegistration.setPatientAgeStr(age);
            Long registrationId;  //用于存储插入的挂号信息的id
            //判断挂号类型
            if (dmsRegistrationParam.getSkdId() == null){
                System.out.println("不是专家号所做的操作");
                //非专家号
                dmsRegistration.setBindStatus(0);//非专家号绑定转态为0
                dmsRegistrationMapper.insertSelective(dmsRegistration);//向dms_registration插入信息
                registrationId  = dmsRegistration.getId();
            }else {
                //专家号
                System.out.println("专家号，做以下操作");
                SmsSkdExample example2 = new SmsSkdExample();
                example2.createCriteria().andIdEqualTo(dmsRegistrationParam.getSkdId());
                List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(example2);
                if (!smsSkdList.isEmpty()){
                    //如果还有挂号限额
                    if (smsSkdList.get(0).getRemain() > 0){
                        //专家号绑定转态为1
                        dmsRegistration.setBindStatus(1);
                        //向dms_registration插入信息
                        dmsRegistrationMapper.insertSelective(dmsRegistration);
                        registrationId = dmsRegistration.getId();
                        System.out.println(registrationId);
                        SmsSkd smsSkd = smsSkdList.get(0);
                        smsSkd.setRemain(smsSkdList.get(0).getRemain() - 1);
//                        smsSkdList.get(0).setRemain(smsSkdList.get(0).getRemain() - 1);  //上面两行代码缩减为一行
                        smsSkdMapper.updateByPrimaryKey(smsSkd);//修改sms_skd中的排班限额(-1)
                        return 1;
                    }else {

                        //挂号限额已用完,挂号失败
                        return 0;
                    }
                }else{
                    //没有该排班信息,挂号失败
                    return 0;
                }

            }
            //保存除了发票号以外的所有属性
            BmsBillsRecord bmsBillsRecord = new BmsBillsRecord();
            //生成账单号
            String billNo = generateBillNo(registrationId);
            bmsBillsRecord.setBillNo(billNo);
            //获取当前时间,并在之后通过该时间与病人id获取账单id
            Date billCreateDate = new Date();
            bmsBillsRecord.setCreateTime(billCreateDate);
            bmsBillsRecord.setStatus(1);//1为正常
            bmsBillsRecord.setInvoiceNum(1);//挂号只有一张发票
            bmsBillsRecord.setRegistrationId(registrationId);
            //recordList怎么用字符串表示,例如挂号id，0><草药id,4><成药,3
            bmsBillsRecord.setRecordList(registrationId+","+0+"><");
            //向bms_bills_record中插入账单记录
            bmsBillsRecordMapper.insertSelective(bmsBillsRecord);
            //通过挂号id和最近时间返回账单id
            BmsBillsRecordExample example5 = new BmsBillsRecordExample();
            //通过挂号id和账单号查询刚插入账单的id
            example5.createCriteria().andRegistrationIdEqualTo(registrationId).andBillNoEqualTo(billNo);
            List<BmsBillsRecord> bmsBillsRecordList = bmsBillsRecordMapper.selectByExample(example5);
            //账单id
            Long billId = bmsBillsRecordList.get(0).getId();
            //插入发票记录
            BmsInvoiceRecord bmsInvoiceRecord = new BmsInvoiceRecord();
            //1.表示挂号
            bmsInvoiceRecord.setType(1);
            Date invoiceCreateTime = new Date();
            bmsInvoiceRecord.setCreateTime(invoiceCreateTime);
            bmsInvoiceRecord.setInvoiceNo(dmsRegistrationParam.getInvoiceNo());
            bmsInvoiceRecord.setBillId(billId);
            bmsInvoiceRecord.setAmount(dmsRegistrationParam.getAmount());
//            bmsInvoiceRecord.setFreezeStatus(1);//冻结转态    为是1正常
            bmsInvoiceRecord.setOperatorId(dmsRegistrationParam.getOpratorId());
            bmsInvoiceRecord.setSettlementCatId(dmsRegistrationParam.getSettlementCatId());
            //挂号id,0,amount><
            bmsInvoiceRecord.setItemList(registrationId + "," + 0 +","+dmsRegistrationParam.getAmount()+"><");
            bmsInvoiceRecordMapper.insertSelective(bmsInvoiceRecord);
            return 1;
        }
        return 0;
    }
    //生成病历号
    private String generateMedicalRecordNo(String identificationNo) {
        Date date = new Date();
        String yyyymmdd = DateUtil.getDateStr(date);//年月日字符串
        String hhmm = DateUtil.getTimeStr(date, 4);//时分字符串
        int length = identificationNo.length();
        String lastFour;
        if (length > 4){
            lastFour = identificationNo.substring(length-4,length);
            return yyyymmdd + hhmm + lastFour;
        }
        return null;
    }

    //生成订单号
    public String generateBillNo(Long registrationId){
        Date date = new Date();
        String yyyymmdd = DateUtil.getDateStr(date);//年月日字符串
        String hhmm = DateUtil.getTimeStr(date, 4);
        int length = registrationId.toString().length();
        String lastFour;
        if (length >4 ){
            Long lastFourNo = registrationId % 10000;
            lastFour = lastFourNo.toString();


        }else if (length == 3){
            lastFour = "0"+registrationId.toString();

        }else if (length == 2){
            lastFour = "00"+registrationId.toString();
        }else{
            lastFour = "000" +registrationId.toString();
        }
        return yyyymmdd + hhmm + lastFour;
    }
}
