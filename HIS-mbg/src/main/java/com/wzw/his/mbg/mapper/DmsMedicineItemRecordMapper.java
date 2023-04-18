package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsMedicineItemRecord;
import com.wzw.his.mbg.model.DmsMedicineItemRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsMedicineItemRecordMapper {
    int countByExample(DmsMedicineItemRecordExample example);

    int deleteByExample(DmsMedicineItemRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsMedicineItemRecord record);

    int insertSelective(DmsMedicineItemRecord record);

    List<DmsMedicineItemRecord> selectByExample(DmsMedicineItemRecordExample example);

    DmsMedicineItemRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsMedicineItemRecord record, @Param("example") DmsMedicineItemRecordExample example);

    int updateByExample(@Param("record") DmsMedicineItemRecord record, @Param("example") DmsMedicineItemRecordExample example);

    int updateByPrimaryKeySelective(DmsMedicineItemRecord record);

    int updateByPrimaryKey(DmsMedicineItemRecord record);
}