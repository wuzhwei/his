package com.wzw.his.dms.service.Impl;

import com.wzw.his.common.dto.dms.DmsDiseParam;
import com.wzw.his.dms.service.DmsDiseService;
import com.wzw.his.mbg.mapper.DmsDiseMapper;
import com.wzw.his.mbg.model.DmsDise;
import com.wzw.his.mbg.model.DmsDiseExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
