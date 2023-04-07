package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.SmsStaff;
import com.wzw.his.mbg.model.SmsStaffExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsStaffMapper {
    int countByExample(SmsStaffExample example);

    int deleteByExample(SmsStaffExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsStaff record);

    int insertSelective(SmsStaff record);

    List<SmsStaff> selectByExample(SmsStaffExample example);

    SmsStaff selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsStaff record, @Param("example") SmsStaffExample example);

    int updateByExample(@Param("record") SmsStaff record, @Param("example") SmsStaffExample example);

    int updateByPrimaryKeySelective(SmsStaff record);

    int updateByPrimaryKey(SmsStaff record);
}