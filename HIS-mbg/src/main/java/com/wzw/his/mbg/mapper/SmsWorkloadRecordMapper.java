package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.SmsWorkloadRecord;
import com.wzw.his.mbg.model.SmsWorkloadRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsWorkloadRecordMapper {
    int countByExample(SmsWorkloadRecordExample example);

    int deleteByExample(SmsWorkloadRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsWorkloadRecord record);

    int insertSelective(SmsWorkloadRecord record);

    List<SmsWorkloadRecord> selectByExample(SmsWorkloadRecordExample example);

    SmsWorkloadRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsWorkloadRecord record, @Param("example") SmsWorkloadRecordExample example);

    int updateByExample(@Param("record") SmsWorkloadRecord record, @Param("example") SmsWorkloadRecordExample example);

    int updateByPrimaryKeySelective(SmsWorkloadRecord record);

    int updateByPrimaryKey(SmsWorkloadRecord record);
}