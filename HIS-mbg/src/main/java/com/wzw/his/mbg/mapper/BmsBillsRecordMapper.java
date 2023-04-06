package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.BmsBillsRecord;
import com.wzw.his.mbg.model.BmsBillsRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BmsBillsRecordMapper {
    int countByExample(BmsBillsRecordExample example);

    int deleteByExample(BmsBillsRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsBillsRecord record);

    int insertSelective(BmsBillsRecord record);

    List<BmsBillsRecord> selectByExample(BmsBillsRecordExample example);

    BmsBillsRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsBillsRecord record, @Param("example") BmsBillsRecordExample example);

    int updateByExample(@Param("record") BmsBillsRecord record, @Param("example") BmsBillsRecordExample example);

    int updateByPrimaryKeySelective(BmsBillsRecord record);

    int updateByPrimaryKey(BmsBillsRecord record);
}