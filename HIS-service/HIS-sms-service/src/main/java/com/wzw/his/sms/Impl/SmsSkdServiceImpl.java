package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsSkdDocParam;
import com.wzw.his.common.dto.sms.SmsSkdDocResult;
import com.wzw.his.common.dto.sms.SmsSkdRuleItemParam;
import com.wzw.his.common.dto.sms.SmsSkdRuleParam;
import com.wzw.his.mbg.mapper.SmsSkdMapper;
import com.wzw.his.mbg.mapper.SmsSkdRuleItemMapper;
import com.wzw.his.mbg.mapper.SmsSkdRuleMapper;
import com.wzw.his.mbg.mapper.SmsStaffMapper;
import com.wzw.his.mbg.model.*;
import com.wzw.his.sms.SmsSkdService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class SmsSkdServiceImpl implements SmsSkdService {
    @Autowired
    SmsStaffMapper smsStaffMapper;
    @Autowired
    SmsSkdMapper smsSkdMapper;
    @Autowired
    SmsSkdRuleMapper smsSkdRuleMapper;
    @Autowired
    SmsSkdRuleItemMapper smsSkdRuleItemMapper;


    @Override
    public int createRule(SmsSkdRuleParam smsSkdRuleParam) {
        SmsSkdRule smsSkdRule = new SmsSkdRule();
        BeanUtils.copyProperties(smsSkdRuleParam,smsSkdRule);
        smsSkdRule.setStatus(1);
        smsSkdRule.setOperateTime(new Date());
        int skdRuleCount = smsSkdRuleMapper.insert(smsSkdRule);
        //根据操作员id和操作时间查询排版规则的id
        SmsSkdRuleExample ruleExample = new SmsSkdRuleExample();
        ruleExample.createCriteria().andOperatorIdEqualTo(smsSkdRuleParam.getOperatorId());
        ruleExample.setOrderByClause("operate_time desc");
        List<SmsSkdRule> smsSkdRuleList = smsSkdRuleMapper.selectByExample(ruleExample);
        if (smsSkdRuleList.size() <= 0){
            return 0;
        }
        Long skdRuleId = smsSkdRuleList.get(0).getId();
        //遍历排版项并全部插入
        int sumOfRuleItem = 0;
        for (SmsSkdRuleItemParam smsSkdRuleItemParam:
             smsSkdRuleParam.getSmsSkdRuleItemParamList()) {
            SmsSkdRuleItem smsSkdRuleItem = new SmsSkdRuleItem();
            BeanUtils.copyProperties(smsSkdRuleItemParam,smsSkdRuleItem);
            smsSkdRuleItem.setStatus(1);
            smsSkdRuleItem.setSkRuleId(skdRuleId);
            sumOfRuleItem += smsSkdRuleItemMapper.insert(smsSkdRuleItem);
        }

        return skdRuleCount + sumOfRuleItem;
    }

    @Override
    public List<SmsSkdDocResult> listDocBySkd(SmsSkdDocParam smsSkdDocParam) {
        SmsSkdExample smsSkdExample = new SmsSkdExample();
        SmsSkdExample.Criteria criteria = smsSkdExample.createCriteria();
        criteria.andStatusEqualTo(1).andRemainGreaterThan(new Long(0));
        if (smsSkdDocParam.getDate() != null){
            criteria.andDateEqualTo(smsSkdDocParam.getDate());
        }
        if (smsSkdDocParam.getNoon() != null){
            criteria.andNoonEqualTo(smsSkdDocParam.getNoon());
        }
        if (smsSkdDocParam.getDeptId() != null){
            criteria.andDeptIdEqualTo(smsSkdDocParam.getDeptId());
        }
        if (smsSkdDocParam.getRegistrationRankId() != null){
            SmsStaffExample smsStaffExample = new SmsStaffExample();
            SmsStaffExample.Criteria staffCriteria = smsStaffExample.createCriteria();
            staffCriteria.andStatusEqualTo(1);
            if (smsSkdDocParam.getDeptId() != null){
                staffCriteria.andDeptIdEqualTo(smsSkdDocParam.getDeptId());
            }
            staffCriteria.andRegistrationRankIdEqualTo(smsSkdDocParam.getRegistrationRankId());
            List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(smsStaffExample);
            List<Long> smsStaffIdList = new ArrayList<>();
            for (SmsStaff smsStaff :
                    smsStaffList) {
                smsStaffIdList.add(smsStaff.getId());
            }
            criteria.andStaffIdIn(smsStaffIdList);
        }
        //返回数据包装成Result
        List<SmsSkd> smsSkdList = smsSkdMapper.selectByExample(smsSkdExample);
        List<SmsSkdDocResult> smsSkdResultList = new ArrayList<>();
        for (SmsSkd smsSkd :
                smsSkdList) {
            SmsSkdDocResult smsSkdDocResult = new SmsSkdDocResult();
            smsSkdDocResult.setSkdId(smsSkd.getId());
            //封装医生名字和挂号费用
            SmsStaff smsStaff = smsStaffMapper.selectByPrimaryKey(smsSkd.getStaffId());
            smsSkdDocResult.setName(smsStaff.getName());


            smsSkdResultList.add(smsSkdDocResult);
        }

        return smsSkdResultList;
    }
}
