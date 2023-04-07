package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.DmsDise;
import com.wzw.his.mbg.model.DmsDiseExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsDiseMapper {
    List<DmsDise> selectByExample(DmsDiseExample example);

    int insertSelective(DmsDise dmsDise);

     int updateByExampleSelective(@Param("record") DmsDise dmsDise,@Param("example") DmsDiseExample example);
}
