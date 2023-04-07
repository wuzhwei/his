package com.wzw.his.dms.service;

import com.wzw.his.common.dto.dms.DmsDiseCatalogParam;
import com.wzw.his.common.dto.dms.DmsDiseCatalogResult;

import java.util.List;

public interface DmsDiseCatalogService {
    /**
     * 描述: 新增一个诊断目录
     * @param dmsDiseCatalogParam
     * @return
     */
    int create(DmsDiseCatalogParam dmsDiseCatalogParam);

    /**
     * 描述:根据ids删除诊断目录
     * @param ids
     * @return
     */
    int delete(List<Long> ids);

    /**
     * 描述:根据id更新诊断目录
     * @param id
     * @param dmsDiseCatalogParam
     * @return
     */
    int update(Long id, DmsDiseCatalogParam dmsDiseCatalogParam);

    /**
     * 描述:查询所有诊断目录
     * @return
     */
    List<DmsDiseCatalogResult> selectAll();
}
