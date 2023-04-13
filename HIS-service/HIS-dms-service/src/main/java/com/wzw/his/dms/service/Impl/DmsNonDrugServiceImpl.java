package com.wzw.his.dms.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.wzw.his.common.dto.dms.DmsNonDrugParam;
import com.wzw.his.common.dto.dms.DmsNonDrugResult;
import com.wzw.his.common.util.RedisUtil;
import com.wzw.his.dms.service.DmsNonDrugService;
import com.wzw.his.mbg.mapper.DmsNonDrugMapper;
import com.wzw.his.mbg.model.DmsNonDrug;
import com.wzw.his.mbg.model.DmsNonDrugExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
@Service
public class DmsNonDrugServiceImpl implements DmsNonDrugService {
    @Autowired
    DmsNonDrugMapper dmsNonDrugMapper;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 描述:新增一个非药品
     * @param dmsNonDrugParam
     * @return
     */
    @Override
    public int create(DmsNonDrugParam dmsNonDrugParam) {
        //根据code查询非药品是否存在
        DmsNonDrugExample example = new DmsNonDrugExample();
        example.createCriteria().andCodeEqualTo(dmsNonDrugParam.getCode());
        List<DmsNonDrug> dmsNonDrugList = dmsNonDrugMapper.selectByExample(example);
        //如果存在相同的code,则插入失败
        if (dmsNonDrugList.size() > 0){
            return 0;
        }
        //插入
        DmsNonDrug dmsNonDrug = new DmsNonDrug();
        BeanUtil.copyProperties(dmsNonDrugParam,dmsNonDrug);
        dmsNonDrug.setStatus(1);
        dmsNonDrug.setCreateDate(new Date());
        dmsNonDrugMapper.insertSelective(dmsNonDrug);
        //差人成功,在redis修改flag
        redisUtil.setObj("nonDrugChangeStatus","1");
        return 1;
    }

    @Override
    public int delete(List<Long> ids) {
        int count = ids == null ? 0 : ids.size();
        if (!CollectionUtils.isEmpty(ids)){
            for (Long id : ids) {
                DmsNonDrug dmsNonDrug = new DmsNonDrug();
                dmsNonDrug.setStatus(0);
                DmsNonDrugExample example = new DmsNonDrugExample();
                example.createCriteria().andIdEqualTo(id);
                dmsNonDrugMapper.updateByExampleSelective(dmsNonDrug,example);
            }
        }
        //删除成功,在redis修改flag
        redisUtil.setObj("nonDrugChangeStatus","1");

        return count;
    }

    @Override
    public int update(Long id, DmsNonDrugParam dmsNonDrugParam) {
        return 0;
    }

    @Override
    public List<DmsNonDrugResult> select(DmsNonDrugParam dmsNonDrugParam) {
        return null;
    }

    @Override
    public List<DmsNonDrugResult> selectAll() {
        return null;
    }
}
