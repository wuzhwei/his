package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsHerbalItemRecord;
import com.wzw.his.mbg.model.DmsHerbalItemRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsHerbalItemRecordMapper {
    int countByExample(DmsHerbalItemRecordExample example);

    int deleteByExample(DmsHerbalItemRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsHerbalItemRecord record);

    int insertSelective(DmsHerbalItemRecord record);

    List<DmsHerbalItemRecord> selectByExample(DmsHerbalItemRecordExample example);

    DmsHerbalItemRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsHerbalItemRecord record, @Param("example") DmsHerbalItemRecordExample example);

    int updateByExample(@Param("record") DmsHerbalItemRecord record, @Param("example") DmsHerbalItemRecordExample example);

    int updateByPrimaryKeySelective(DmsHerbalItemRecord record);

    int updateByPrimaryKey(DmsHerbalItemRecord record);
}