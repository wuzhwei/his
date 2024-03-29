package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsDrug;
import com.wzw.his.mbg.model.DmsDrugExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsDrugMapper {
    int countByExample(DmsDrugExample example);

    int deleteByExample(DmsDrugExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDrug record);

    int insertSelective(DmsDrug record);

    List<DmsDrug> selectByExample(DmsDrugExample example);

    DmsDrug selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsDrug record, @Param("example") DmsDrugExample example);

    int updateByExample(@Param("record") DmsDrug record, @Param("example") DmsDrugExample example);

    int updateByPrimaryKeySelective(DmsDrug record);

    int updateByPrimaryKey(DmsDrug record);
}