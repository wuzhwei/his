package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.SmsFrequentUsed;
import com.wzw.his.mbg.model.SmsFrequentUsedExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsFrequentUsedMapper {
    int countByExample(SmsFrequentUsedExample example);

    int deleteByExample(SmsFrequentUsedExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFrequentUsed record);

    int insertSelective(SmsFrequentUsed record);

    List<SmsFrequentUsed> selectByExample(SmsFrequentUsedExample example);

    SmsFrequentUsed selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsFrequentUsed record, @Param("example") SmsFrequentUsedExample example);

    int updateByExample(@Param("record") SmsFrequentUsed record, @Param("example") SmsFrequentUsedExample example);

    int updateByPrimaryKeySelective(SmsFrequentUsed record);

    int updateByPrimaryKey(SmsFrequentUsed record);
}