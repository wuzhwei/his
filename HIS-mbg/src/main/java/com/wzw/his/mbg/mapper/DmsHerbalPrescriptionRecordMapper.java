package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsHerbalPrescriptionRecord;
import com.wzw.his.mbg.model.DmsHerbalPrescriptionRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsHerbalPrescriptionRecordMapper {
    int countByExample(DmsHerbalPrescriptionRecordExample example);

    int deleteByExample(DmsHerbalPrescriptionRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsHerbalPrescriptionRecord record);

    int insertSelective(DmsHerbalPrescriptionRecord record);

    List<DmsHerbalPrescriptionRecord> selectByExample(DmsHerbalPrescriptionRecordExample example);

    DmsHerbalPrescriptionRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsHerbalPrescriptionRecord record, @Param("example") DmsHerbalPrescriptionRecordExample example);

    int updateByExample(@Param("record") DmsHerbalPrescriptionRecord record, @Param("example") DmsHerbalPrescriptionRecordExample example);

    int updateByPrimaryKeySelective(DmsHerbalPrescriptionRecord record);

    int updateByPrimaryKey(DmsHerbalPrescriptionRecord record);
}