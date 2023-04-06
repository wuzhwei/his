package com.wzw.his.mbg.mapper;


import com.wzw.his.mbg.model.BmsInvoiceRecord;
import com.wzw.his.mbg.model.BmsInvoiceRecordExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BmsInvoiceRecordMapper {
    int countByExample(BmsInvoiceRecordExample example);

    int deleteByExample(BmsInvoiceRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BmsInvoiceRecord record);

    int insertSelective(BmsInvoiceRecord record);

    List<BmsInvoiceRecord> selectByExample(BmsInvoiceRecordExample example);

    BmsInvoiceRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BmsInvoiceRecord record, @Param("example") BmsInvoiceRecordExample example);

    int updateByExample(@Param("record") BmsInvoiceRecord record, @Param("example") BmsInvoiceRecordExample example);

    int updateByPrimaryKeySelective(BmsInvoiceRecord record);

    int updateByPrimaryKey(BmsInvoiceRecord record);
}