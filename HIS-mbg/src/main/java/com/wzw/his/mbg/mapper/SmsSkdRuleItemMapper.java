package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.SmsSkdRuleItem;
import com.wzw.his.mbg.model.SmsSkdRuleItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSkdRuleItemMapper {
    int countByExample(SmsSkdRuleItemExample example);

    int deleteByExample(SmsSkdRuleItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSkdRuleItem record);

    int insertSelective(SmsSkdRuleItem record);

    List<SmsSkdRuleItem> selectByExample(SmsSkdRuleItemExample example);

    SmsSkdRuleItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSkdRuleItem record, @Param("example") SmsSkdRuleItemExample example);

    int updateByExample(@Param("record") SmsSkdRuleItem record, @Param("example") SmsSkdRuleItemExample example);

    int updateByPrimaryKeySelective(SmsSkdRuleItem record);

    int updateByPrimaryKey(SmsSkdRuleItem record);
}