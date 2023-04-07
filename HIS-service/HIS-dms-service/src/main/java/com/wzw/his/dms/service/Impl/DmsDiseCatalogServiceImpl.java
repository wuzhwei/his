package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsDiseCatalogParam;
import com.wzw.his.common.dto.dms.DmsDiseCatalogResult;
import com.wzw.his.dms.service.DmsDiseCatalogService;
import com.wzw.his.mbg.mapper.DmsDiseCatalogMapper;
import com.wzw.his.mbg.mapper.DmsDiseMapper;
import com.wzw.his.mbg.model.DmsDise;
import com.wzw.his.mbg.model.DmsDiseCatalog;
import com.wzw.his.mbg.model.DmsDiseCatalogExample;
import com.wzw.his.mbg.model.DmsDiseExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DmsDiseCatalogServiceImpl implements DmsDiseCatalogService {
    @Autowired
    DmsDiseCatalogMapper dmsDiseCatalogMapper;

    @Autowired
    DmsDiseMapper dmsDiseMapper;

    @Override
    public int create(DmsDiseCatalogParam dmsDiseCatalogParam) {
        DmsDiseCatalogExample example = new DmsDiseCatalogExample();
        //根据name查询插入的是否已存在
        example.createCriteria().andNameEqualTo(dmsDiseCatalogParam.getName());
        List<DmsDiseCatalog> dmsDiseCatalogList = dmsDiseCatalogMapper.selectByExample(example);
        //如果存在相同的name，则插入失败
        if (dmsDiseCatalogList.size() > 0){
            return 0 ;
        }
        DmsDiseCatalog dmsDiseCatalog = new DmsDiseCatalog();
        BeanUtils.copyProperties(dmsDiseCatalogParam,dmsDiseCatalog);
        dmsDiseCatalogMapper.insertSelective(dmsDiseCatalog);
        return 1;
    }

    /**
     * 描述:根据ids删除诊断目录
     *   删除目录并删除目录中的诊断
     * @param ids
     * @return
     */

    @Override
    public int delete(List<Long> ids) {
        int count = ids == null ? 0 : ids.size();
        if (!CollectionUtils.isEmpty(ids)){
            for (Long id:
                 ids) {
                DmsDiseCatalog dmsDiseCatalog = new DmsDiseCatalog();
                dmsDiseCatalog.setStatus(0);
                DmsDiseCatalogExample example = new DmsDiseCatalogExample();
                example.createCriteria().andIdEqualTo(id);
                dmsDiseCatalogMapper.updateByExampleSelective(dmsDiseCatalog,example);
                //删除诊断
                for (Long catalogId:
                     ids) {
                    DmsDiseExample dmsDiseExample = new DmsDiseExample();
                    dmsDiseExample.createCriteria().andCatIdEqualTo(catalogId);
                    DmsDise dmsDise = new DmsDise();
                    dmsDise.setStatus(0);
                    dmsDiseMapper.updateByExampleSelective(dmsDise,dmsDiseExample);
                }
            }
        }
        return count;
    }

    /**
     * 根据id更新诊断目录信息
     * @param id
     * @param dmsDiseCatalogParam
     * @return
     */

    @Override
    public int update(Long id, DmsDiseCatalogParam dmsDiseCatalogParam) {
        DmsDiseCatalog dmsDiseCatalog = new DmsDiseCatalog();
        BeanUtils.copyProperties(dmsDiseCatalogParam,dmsDiseCatalog);
        DmsDiseCatalogExample example = new DmsDiseCatalogExample();
        example.createCriteria().andIdEqualTo(id);
        return dmsDiseCatalogMapper.updateByExampleSelective(dmsDiseCatalog,example);
    }

    /**
     * 描述:查询所有诊断目录
     * 列出目录也需要倒叙
     * @return
     */
    @Override
    public List<DmsDiseCatalogResult> selectAll() {
        List<DmsDiseCatalogResult> list = new ArrayList<>();
        DmsDiseCatalogExample example = new DmsDiseCatalogExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //按id倒序
        example.setOrderByClause("id desc");
        for (DmsDiseCatalog dmsDiseCatalog:
             dmsDiseCatalogMapper.selectByExample(example)) {
            DmsDiseCatalogResult dmsDiseCatalogResult = new DmsDiseCatalogResult();
            BeanUtils.copyProperties(dmsDiseCatalog,dmsDiseCatalogResult);
            list.add(dmsDiseCatalogResult);
        }
        return list;
    }
}
