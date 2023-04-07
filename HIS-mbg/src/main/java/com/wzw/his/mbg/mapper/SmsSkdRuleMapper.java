package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.SmsSkdRule;
import com.wzw.his.mbg.model.SmsSkdRuleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSkdRuleMapper {
    int countByExample(SmsSkdRuleExample example);

    int deleteByExample(SmsSkdRuleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSkdRule record);

    int insertSelective(SmsSkdRule record);

    List<SmsSkdRule> selectByExample(SmsSkdRuleExample example);

    SmsSkdRule selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSkdRule record, @Param("example") SmsSkdRuleExample example);

    int updateByExample(@Param("record") SmsSkdRule record, @Param("example") SmsSkdRuleExample example);

    int updateByPrimaryKeySelective(SmsSkdRule record);

    int updateByPrimaryKey(SmsSkdRule record);
}