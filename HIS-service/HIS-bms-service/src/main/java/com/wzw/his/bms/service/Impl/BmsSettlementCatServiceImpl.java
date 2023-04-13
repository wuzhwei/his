package com.wzw.his.bms.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.wzw.his.bms.service.BmsSettlementCatService;
import com.wzw.his.common.dto.bms.BmsSettlementCatParam;
import com.wzw.his.common.dto.bms.BmsSettlementCatResult;
import com.wzw.his.mbg.mapper.BmsSettlementCatMapper;
import com.wzw.his.mbg.model.BmsSettlementCat;
import com.wzw.his.mbg.model.BmsSettlementCatExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class BmsSettlementCatServiceImpl implements BmsSettlementCatService {
    @Autowired
    BmsSettlementCatMapper bmsSettlementCatMapper;
    @Override
    public int create(BmsSettlementCatParam bmsSettlementCatParam) {
        BmsSettlementCat bmsSettlementCat = new BmsSettlementCat();
        BeanUtils.copyProperties(bmsSettlementCatParam,bmsSettlementCat);
        bmsSettlementCat.setStatus(1);
        //查询是否有同name结算类别
        BmsSettlementCatExample example = new BmsSettlementCatExample();
        example.createCriteria().andNameEqualTo(bmsSettlementCat.getName());
        List<BmsSettlementCat> lists = bmsSettlementCatMapper.selectByExample(example);
        if (lists.size() > 0){
            return 0;
        }
        //没有则插入数据
        return bmsSettlementCatMapper.insert(bmsSettlementCat);
    }

    @Override
    public int delete(List<Long> ids) {
        BmsSettlementCat bmsSettlementCat = new BmsSettlementCat();
        bmsSettlementCat.setStatus(0);
        BmsSettlementCatExample example = new BmsSettlementCatExample();
        example.createCriteria().andIdIn(ids);
        return bmsSettlementCatMapper.updateByExampleSelective(bmsSettlementCat,example);
    }

    @Override
    public int update(Long id, BmsSettlementCatParam bmsSettlementCatParam) {
        BmsSettlementCat bmsSettlementCat = new BmsSettlementCat();
        BeanUtils.copyProperties(bmsSettlementCatParam,bmsSettlementCat);
        bmsSettlementCat.setId(id);
        return bmsSettlementCatMapper.updateByPrimaryKeySelective(bmsSettlementCat);
    }

    @Override
    public List<BmsSettlementCatResult> select(BmsSettlementCatParam bmsSettlementCatParam) {
        BmsSettlementCatExample example = new BmsSettlementCatExample();
        BmsSettlementCatExample.Criteria criteria = example.createCriteria();
        //如果没有指明state,返回不为0的
        if (bmsSettlementCatParam.getStatus() == null){
            criteria.andStatusNotEqualTo(0);
        }
        //是否按结算类别名,比例查询
        if (!StringUtils.isEmpty(bmsSettlementCatParam.getName())){
            criteria.andNameLike("%"+bmsSettlementCatParam.getName()+"%");
        }
        //返回数据包装成Result
        List<BmsSettlementCat> bsclist = bmsSettlementCatMapper.selectByExample(example);
        List<BmsSettlementCatResult> bscResults = new ArrayList<>();
        for (BmsSettlementCat b : bsclist) {
            BmsSettlementCatResult r = new BmsSettlementCatResult();
            BeanUtils.copyProperties(b,r);
            bscResults.add(r);
        }
        return bscResults;
    }

    @Override
    public List<BmsSettlementCatResult> selectAll() {
        BmsSettlementCatExample example = new BmsSettlementCatExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回数据包装成Result
        List<BmsSettlementCat> bsclist = bmsSettlementCatMapper.selectByExample(example);
        ArrayList<BmsSettlementCatResult> bscResults = new ArrayList<>();
        for (BmsSettlementCat b : bsclist) {
            BmsSettlementCatResult r = new BmsSettlementCatResult();
            BeanUtils.copyProperties(b,r);
            bscResults.add(r);
        }
        return bscResults;
    }
}
