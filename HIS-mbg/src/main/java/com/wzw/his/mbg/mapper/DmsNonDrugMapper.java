package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.DmsNonDrug;
import com.wzw.his.mbg.model.DmsNonDrugExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsNonDrugMapper {
    int countByExample(DmsNonDrugExample example);

    int deleteByExample(DmsNonDrugExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsNonDrug record);

    int insertSelective(DmsNonDrug record);

    List<DmsNonDrug> selectByExample(DmsNonDrugExample example);

    DmsNonDrug selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsNonDrug record, @Param("example") DmsNonDrugExample example);

    int updateByExample(@Param("record") DmsNonDrug record, @Param("example") DmsNonDrugExample example);

    int updateByPrimaryKeySelective(DmsNonDrug record);

    int updateByPrimaryKey(DmsNonDrug record);
}