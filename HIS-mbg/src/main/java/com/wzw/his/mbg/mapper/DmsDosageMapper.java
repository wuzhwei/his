package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsDosage;
import com.wzw.his.mbg.model.DmsDosageExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsDosageMapper {
    int countByExample(DmsDosageExample example);

    int deleteByExample(DmsDosageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDosage record);

    int insertSelective(DmsDosage record);

    List<DmsDosage> selectByExample(DmsDosageExample example);

    DmsDosage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsDosage record, @Param("example") DmsDosageExample example);

    int updateByExample(@Param("record") DmsDosage record, @Param("example") DmsDosageExample example);

    int updateByPrimaryKeySelective(DmsDosage record);

    int updateByPrimaryKey(DmsDosage record);
}