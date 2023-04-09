package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.SmsRole;
import com.wzw.his.mbg.model.SmsRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsRoleMapper {
    int countByExample(SmsRoleExample example);

    int deleteByExample(SmsRoleExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsRole record);

    int insertSelective(SmsRole record);

    List<SmsRole> selectByExample(SmsRoleExample example);

    SmsRole selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SmsRole record, @Param("example") SmsRoleExample example);

    int updateByExample(@Param("record") SmsRole record, @Param("example") SmsRoleExample example);

    int updateByPrimaryKeySelective(SmsRole record);

    int updateByPrimaryKey(SmsRole record);
}