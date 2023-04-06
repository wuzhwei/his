package com.wzw.his.mbg.mapper;

import com.wzw.his.mbg.model.DmsDise;
import com.wzw.his.mbg.model.DmsDiseExample;

import java.util.List;

public interface DmsDiseMapper {
    List<DmsDise> selectByExample(DmsDiseExample example);

    int insertSelective(DmsDise dmsDise);
}
