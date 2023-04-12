package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsFrequentUsedResult;
import com.wzw.his.mbg.mapper.SmsFrequentUsedMapper;
import com.wzw.his.mbg.model.SmsFrequentUsed;
import com.wzw.his.mbg.model.SmsFrequentUsedExample;
import com.wzw.his.sms.SmsFrequentUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SmsFrequentUsedServiceImpl implements SmsFrequentUsedService {
    @Autowired
    SmsFrequentUsedMapper smsFrequentUsedMapper;
    @Override
    public int addFrequent(Long staffId, int addType, Long addId) {
        SmsFrequentUsedExample example = new SmsFrequentUsedExample();
        example.createCriteria().andStaffIdEqualTo(staffId);
        List<SmsFrequentUsed> frequentUsedList = smsFrequentUsedMapper.selectByExample(example);
        SmsFrequentUsed frequentUsed;
        if (frequentUsedList.size() <= 0){
            //第一次创建
            frequentUsed = new SmsFrequentUsed();
            frequentUsed.setStaffId(staffId);
            if (smsFrequentUsedMapper.insert(frequentUsed) <= 0){
                return 0;
            }
        }else{
            frequentUsed = frequentUsedList.get(0);
        }
        boolean addBoolean = false;
        switch (addType){
            case 1:
                String checkIdStr = frequentUsed.getCheckIdList();
                Set<Long> checkSet = strToSet(checkIdStr);
                addBoolean = checkSet.add(addId);
                frequentUsed.setCheckIdList(setToStr(checkSet));
                break;
            case 2:
                String medicineDiseIdStr = frequentUsed.getMedicineDiseIdList();
                Set<Long> medicineDiseIdSet = strToSet(medicineDiseIdStr);
                addBoolean = medicineDiseIdSet.add(addId);
                frequentUsed.setMedicineDiseIdList(setToStr(medicineDiseIdSet));
                break;
            case 3:
                String dispositionIdStr = frequentUsed.getDispositionIdList();
                Set<Long> dispositionIdSet = strToSet(dispositionIdStr);
                addBoolean = dispositionIdSet.add(addId);
                frequentUsed.setDispositionIdList(setToStr(dispositionIdSet));
                break;
            case 4:
                String testIdStr = frequentUsed.getTestIdList();
                Set<Long> testIdSet = strToSet(testIdStr);
                addBoolean = testIdSet.add(addId);
                frequentUsed.setTestIdList(setToStr(testIdSet));
                break;
            case 5:
                String herbalDiseIdStr = frequentUsed.getHerbalDiseIdList();
                Set<Long> herbalDiseIdSet = strToSet(herbalDiseIdStr);
                addBoolean = herbalDiseIdSet.add(addId);
                frequentUsed.setHerbalDiseIdList(setToStr(herbalDiseIdSet));
                break;
            case 6:
                String drugIdStr = frequentUsed.getDrugIdList();
                Set<Long> drugIdSet = strToSet(drugIdStr);
                addBoolean = drugIdSet.add(addId);
                frequentUsed.setDrugIdList(setToStr(drugIdSet));
                break;
            case 7:
                String checkModelIdStr = frequentUsed.getCheckModelIdList();
                Set<Long> checkModelIdSet = strToSet(checkModelIdStr);
                addBoolean = checkModelIdSet.add(addId);
                frequentUsed.setCheckModelIdList(setToStr(checkModelIdSet));
                break;
            case 8:
                String dispositionModelIdStr = frequentUsed.getDispositionModelIdList();
                Set<Long> dispositionModelIdSet = strToSet(dispositionModelIdStr);
                addBoolean = dispositionModelIdSet.add(addId);
                break;
            case 9:
                String testModelIdStr = frequentUsed.getTestModelIdList();
                Set<Long> testModelIdSet = strToSet(testModelIdStr);
                addBoolean = testModelIdSet.add(addId);
                frequentUsed.setTestModelIdList(setToStr(testModelIdSet));
                break;
            case 0:
                String drugModelIdStr = frequentUsed.getDrugModelIdList();
                Set<Long> drugModelIdSet = strToSet(drugModelIdStr);
                addBoolean = drugModelIdSet.add(addId);
                frequentUsed.setDrugModelIdList(setToStr(drugModelIdSet));
                break;
            default:
                return 0;
        }
        if (addBoolean){
            return smsFrequentUsedMapper.updateByPrimaryKeySelective(frequentUsed);
        }else{
            return 0;
        }
    }

    private String setToStr(Set<Long> itemSet) {
        String itemListStr = "";
        if (itemSet == null || itemSet.isEmpty()){
            return itemListStr;
        }
        for (Long itemId : itemSet) {
            itemListStr += (itemId+"");
            itemListStr += ",";

        }
        return  itemListStr.substring(0,itemListStr.length() - 1);

    }

    private Set<Long> strToSet(String itemListStr) {
        HashSet<Long> idSet = new HashSet<>();
        if (itemListStr == null || itemListStr.length() <= 0){
            return idSet;
        }
        String[] idArray = itemListStr.split(",");
        for (int i=0;i < idArray.length ;i++){
            idSet.add(new Long(idArray[i]));
        }
        return idSet;
    }

    @Override
    public int deleteFrequent(Long staffId, int deleteType, Long deleteId) {
        return 0;
    }

    @Override
    public SmsFrequentUsedResult selectFrequent(Long staffId, int selectType) {
        return null;
    }
}
