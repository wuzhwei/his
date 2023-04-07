package com.wzw.his.dms.service;

import com.wzw.his.common.dto.dms.DmsDiseParam;
import com.wzw.his.common.dto.dms.DmsDiseResult;

import java.util.List;

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

    /**
     * 描述：根据ids删除诊断
     * @param ids
     * @return
     */
    int delete(List<Long> ids);

    /**
     * 描述:根据id更新诊断
     * @param id
     * @param dmsDiseParam
     * @return
     */

    int update(Long id, DmsDiseParam dmsDiseParam);

    /**
     * 描述:查询诊断
     * @param dmsDiseParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<DmsDiseResult> select(DmsDiseParam dmsDiseParam, Integer pageSize, Integer pageNum);

    /**
     * 描述: 查询所有诊断
     * @return
     */
    List<DmsDiseResult> selectAll();

    /**
     * 描述:根据id串获取诊断简单对象list
     * @param idsStr
     * @return
     */
    List<DmsDiseResult> parseList(String idsStr);
}
