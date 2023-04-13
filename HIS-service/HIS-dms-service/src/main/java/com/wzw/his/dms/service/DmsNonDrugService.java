package com.wzw.his.dms.service;

import com.wzw.his.common.dto.dms.DmsNonDrugParam;
import com.wzw.his.common.dto.dms.DmsNonDrugResult;

import java.util.List;

/**
 * 非药品
 */
public interface DmsNonDrugService {
    /**
     * 描述：新增一个非药品
     */
    int create(DmsNonDrugParam dmsNonDrugParam);
    /**
     * 描述：根据ids删除非药品
     */
    int delete(List<Long> ids);
    /**
     * 描述：更新
     */
    int update(Long id, DmsNonDrugParam dmsNonDrugParam);
    /**
     * 描述：模糊查询非药品、且分页
     */
    List<DmsNonDrugResult> select(DmsNonDrugParam dmsNonDrugParam);
    /**
     * 描述：查询所有非药品
     */
    List<DmsNonDrugResult> selectAll();
}
