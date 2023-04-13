package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.BmsSettlementCat;
import com.wzw.his.mbg.model.BmsSettlementCatExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BmsSettlementCatMapper {
    int countByExample(BmsSettlementCatExample example);

    int deleteByExample(BmsSettlementCatExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsSettlementCat record);

    int insertSelective(BmsSettlementCat record);

    List<BmsSettlementCat> selectByExample(BmsSettlementCatExample example);

    BmsSettlementCat selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsSettlementCat record, @Param("example") BmsSettlementCatExample example);

    int updateByExample(@Param("record") BmsSettlementCat record, @Param("example") BmsSettlementCatExample example);

    int updateByPrimaryKeySelective(BmsSettlementCat record);

    int updateByPrimaryKey(BmsSettlementCat record);
}