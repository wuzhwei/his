package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.SmsSkd;
import com.wzw.his.mbg.model.SmsSkdExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsSkdMapper {
    int countByExample(SmsSkdExample example);

    int deleteByExample(SmsSkdExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsSkd record);

    int insertSelective(SmsSkd record);

    List<SmsSkd> selectByExample(SmsSkdExample example);

    SmsSkd selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsSkd record, @Param("example") SmsSkdExample example);

    int updateByExample(@Param("record") SmsSkd record, @Param("example") SmsSkdExample example);

    int updateByPrimaryKeySelective(SmsSkd record);

    int updateByPrimaryKey(SmsSkd record);
}