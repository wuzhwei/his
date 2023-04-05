package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.PmsPatient;
import com.wzw.his.mbg.model.PmsPatientExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsPatientMapper {
    int countByExample(PmsPatientExample example);

    int deleteByExample(PmsPatientExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsPatient record);

    int insertSelective(PmsPatient record);

    List<PmsPatient> selectByExample(PmsPatientExample example);

    PmsPatient selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsPatient record, @Param("example") PmsPatientExample example);

    int updateByExample(@Param("record") PmsPatient record, @Param("example") PmsPatientExample example);

    int updateByPrimaryKeySelective(PmsPatient record);

    int updateByPrimaryKey(PmsPatient record);
}