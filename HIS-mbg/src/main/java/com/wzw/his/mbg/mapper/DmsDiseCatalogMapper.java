package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsDiseCatalog;
import com.wzw.his.mbg.model.DmsDiseCatalogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsDiseCatalogMapper {
    int countByExample(DmsDiseCatalogExample example);

    int deleteByExample(DmsDiseCatalogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DmsDiseCatalog record);

    int insertSelective(DmsDiseCatalog record);

    List<DmsDiseCatalog> selectByExample(DmsDiseCatalogExample example);

    DmsDiseCatalog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DmsDiseCatalog record, @Param("example") DmsDiseCatalogExample example);

    int updateByExample(@Param("record") DmsDiseCatalog record, @Param("example") DmsDiseCatalogExample example);

    int updateByPrimaryKeySelective(DmsDiseCatalog record);

    int updateByPrimaryKey(DmsDiseCatalog record);
}