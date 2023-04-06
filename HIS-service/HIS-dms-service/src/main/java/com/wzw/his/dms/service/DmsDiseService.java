package com.wzw.his.dms.service;

import com.wzw.his.common.dto.dms.DmsDiseParam;

/**
 * 诊断
 */
public interface DmsDiseService {
    /**
     * 描述： 新增一个诊断
     * @param dmsDiseParam
     * @return
     */

    int create(DmsDiseParam dmsDiseParam);
}
