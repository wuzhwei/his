package com.wzw.his.dms.service.Impl;

import cn.hutool.core.collection.CollectionUtil;
import com.wzw.his.common.dto.dms.DmsDiseParam;
import com.wzw.his.common.dto.dms.DmsDiseResult;
import com.wzw.his.dms.service.DmsDiseService;
import com.wzw.his.mbg.mapper.DmsDiseMapper;
import com.wzw.his.mbg.model.DmsDise;
import com.wzw.his.mbg.model.DmsDiseExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DmsDiseServiceImpl implements DmsDiseService {

    @Autowired
    DmsDiseMapper dmsDiseMapper;


    @Override
    public int create(DmsDiseParam dmsDiseParam) {
        DmsDiseExample example = new DmsDiseExample();
        example.createCriteria().andNameEqualTo(dmsDiseParam.getName()).andStatusEqualTo(dmsDiseParam.getStatus());
        List<DmsDise> dmsDiseList = dmsDiseMapper.selectByExample(example);
        if (dmsDiseList.size() > 0){
            return  0;

        }
        DmsDise dmsDise = new DmsDise();
        BeanUtils.copyProperties(dmsDiseParam,dmsDise);
        dmsDiseMapper.insertSelective(dmsDise);
        return 1;
    }

    /**
     * 逻辑上的删除   将status设置为0  代表删除
     * @param ids
     * @return
     */
    @Override
    public int delete(List<Long> ids) {
        //得到要删除的数量
        int count = ids == null ? 0 : ids.size();
        if (!CollectionUtils.isEmpty(ids)){
            for (Long id:
                 ids) {
                DmsDise dmsDise = new DmsDise();
                dmsDise.setStatus(0);
                DmsDiseExample example = new DmsDiseExample();
                example.createCriteria().andIdEqualTo(id);
                dmsDiseMapper.updateByExampleSelective(dmsDise,example);
            }
        }
        return count;
    }

    @Override
    public int update(Long id, DmsDiseParam dmsDiseParam) {
        DmsDise dmsDise = new DmsDise();
        BeanUtils.copyProperties(dmsDiseParam,dmsDise);
        System.err.println("dmsDiseParam--->"+dmsDiseParam);
        System.err.println("dmsDise--->"+dmsDiseParam);
        DmsDiseExample example = new DmsDiseExample();
        example.createCriteria().andIdEqualTo(id);
        return dmsDiseMapper.updateByExampleSelective(dmsDise,example);
    }

    /**
     * 查询诊断
     * @param dmsDiseParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public List<DmsDiseResult> select(DmsDiseParam dmsDiseParam, Integer pageSize, Integer pageNum) {
        DmsDiseExample example = new DmsDiseExample();
        DmsDiseExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(dmsDiseParam.getCatId())){
            criteria.andCatIdEqualTo(dmsDiseParam.getCatId());
        }
        if (!StringUtils.isEmpty(dmsDiseParam.getCode())){
            criteria.andCodeEqualTo(dmsDiseParam.getCode());
        }
        if (!StringUtils.isEmpty(dmsDiseParam.getName())){
            criteria.andNameLike("%"+dmsDiseParam.getName()+"%");
        }
        if (!StringUtils.isEmpty(dmsDiseParam.getIcd())){
            criteria.andIcdEqualTo(dmsDiseParam.getIcd());
        }
        if (!StringUtils.isEmpty(dmsDiseParam.getStatus())){
            criteria.andStatusEqualTo(dmsDiseParam.getStatus());
        }
        //状态不为0
        criteria.andStatusNotEqualTo(0);
        //按id降序
        example.setOrderByClause("id desc");
        List<DmsDiseResult> list = new ArrayList<>();
        for (DmsDise dmsDise:
             dmsDiseMapper.selectByExample(example)) {
            DmsDiseResult dmsDiseResult = new DmsDiseResult();
            BeanUtils.copyProperties(dmsDise,dmsDiseResult);
            list.add(dmsDiseResult);
        }

        return list;
    }

    @Override
    public List<DmsDiseResult> selectAll() {
        ArrayList<DmsDiseResult> list = new ArrayList<>();
        DmsDiseExample example = new DmsDiseExample();
        example.createCriteria().andStatusNotEqualTo(0);
        for (DmsDise dmsDise:
             dmsDiseMapper.selectByExample(example)) {
            DmsDiseResult dmsDiseResult = new DmsDiseResult();
            BeanUtils.copyProperties(dmsDise,dmsDiseResult);
            list.add(dmsDiseResult);
        }
        return list;
    }

    @Override
    public List<DmsDiseResult> parseList(String idsStr) {
        //解析ids->list<Id>
        List<Long> idList = strToList(idsStr);
        //根据list<id>查询并封装诊断简单对象
        DmsDiseExample example = new DmsDiseExample();
        example.createCriteria().andIdIn(idList);
        List<DmsDise> dmsDiseList = dmsDiseMapper.selectByExample(example);
        List<DmsDiseResult> dmsDiseResultList = new ArrayList<>();
        if (CollectionUtil.isEmpty(dmsDiseList)){
            return null;
        }
        //封装dto
        for (DmsDise dmsDise:
             dmsDiseList) {
            DmsDiseResult dmsDiseResult = new DmsDiseResult();
            BeanUtils.copyProperties(dmsDise,dmsDiseResult);
            dmsDiseResultList.add(dmsDiseResult);
        }
        return dmsDiseResultList;
    }
    //将字符串解析为Long类型id数组
    private List<Long> strToList(String idsStr) {
        List<Long> idList = new ArrayList<>();
        if (idsStr == null || idsStr.length() <= 0){
            return idList;
        }
        String[] idArray = idsStr.split(",");
        for (int i = 0; i < idArray.length; i++) {
            idList.add(new Long(idArray[i]));
        }
        return idList;
    }
}
