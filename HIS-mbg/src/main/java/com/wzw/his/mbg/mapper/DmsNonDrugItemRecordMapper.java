package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsNonDrugItemRecord;
import com.wzw.his.mbg.model.DmsNonDrugItemRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsNonDrugItemRecordMapper {
    int countByExample(DmsNonDrugItemRecordExample example);

    int deleteByExample(DmsNonDrugItemRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsNonDrugItemRecord record);

    int insertSelective(DmsNonDrugItemRecord record);

    List<DmsNonDrugItemRecord> selectByExample(DmsNonDrugItemRecordExample example);

    DmsNonDrugItemRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsNonDrugItemRecord record, @Param("example") DmsNonDrugItemRecordExample example);

    int updateByExample(@Param("record") DmsNonDrugItemRecord record, @Param("example") DmsNonDrugItemRecordExample example);

    int updateByPrimaryKeySelective(DmsNonDrugItemRecord record);

    int updateByPrimaryKey(DmsNonDrugItemRecord record);
}