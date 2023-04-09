package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.SmsRegistrationRank;
import com.wzw.his.mbg.model.SmsRegistrationRankExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsRegistrationRankMapper {
    int countByExample(SmsRegistrationRankExample example);

    int deleteByExample(SmsRegistrationRankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsRegistrationRank record);

    int insertSelective(SmsRegistrationRank record);

    List<SmsRegistrationRank> selectByExample(SmsRegistrationRankExample example);

    SmsRegistrationRank selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsRegistrationRank record, @Param("example") SmsRegistrationRankExample example);

    int updateByExample(@Param("record") SmsRegistrationRank record, @Param("example") SmsRegistrationRankExample example);

    int updateByPrimaryKeySelective(SmsRegistrationRank record);

    int updateByPrimaryKey(SmsRegistrationRank record);
}