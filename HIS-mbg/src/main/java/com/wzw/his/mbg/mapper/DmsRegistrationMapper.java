package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.DmsRegistration;
import com.wzw.his.mbg.model.DmsRegistrationExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DmsRegistrationMapper {
//    int countByExample(DmsRegistrationExample example);
//
//    int deleteByExample(DmsRegistrationExample example);
//
//    int deleteByPrimaryKey(Long id);
//
//    int insert(DmsRegistration record);
//
    int insertSelective(DmsRegistration record);

    List<DmsRegistration> selectByExample(DmsRegistrationExample example);

//    DmsRegistration selectByPrimaryKey(Long id);
//
//    int updateByExampleSelective(@Param("record") DmsRegistration record, @Param("example") DmsRegistrationExample example);
//
//    int updateByExample(@Param("record") DmsRegistration record, @Param("example") DmsRegistrationExample example);
//
//    int updateByPrimaryKeySelective(DmsRegistration record);
//
//    int updateByPrimaryKey(DmsRegistration record);
}