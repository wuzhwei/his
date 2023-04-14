package com.wzw.his.dms.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.wzw.his.common.dto.dms.DmsNonDrugParam;
import com.wzw.his.common.dto.dms.DmsNonDrugResult;
import com.wzw.his.common.util.RedisUtil;
import com.wzw.his.dms.service.DmsNonDrugService;
import com.wzw.his.mbg.mapper.DmsNonDrugMapper;
import com.wzw.his.mbg.mapper.SmsDeptMapper;
import com.wzw.his.mbg.model.DmsNonDrug;
import com.wzw.his.mbg.model.DmsNonDrugExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class DmsNonDrugServiceImpl implements DmsNonDrugService {
    @Autowired
    DmsNonDrugMapper dmsNonDrugMapper;
    @Autowired
    SmsDeptMapper smsDeptMapper;
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
        DmsNonDrug dmsNonDrug = new DmsNonDrug();
        BeanUtils.copyProperties(dmsNonDrugParam,dmsNonDrug);
        //dmsNonDrug.setId(id)
        DmsNonDrugExample example = new DmsNonDrugExample();
        example.createCriteria().andIdEqualTo(id);
        //更新成功,在redis修改flag
        redisUtil.setObj("nonDrugChangeStatus","1");
        return dmsNonDrugMapper.updateByExampleSelective(dmsNonDrug,example);
    }

    /**
     * 描述：模糊查询非药品,且分页
     * @param dmsNonDrugParam
     * @return
     */

    @Override
    public List<DmsNonDrugResult> select(DmsNonDrugParam dmsNonDrugParam) {
        DmsNonDrugExample example = new DmsNonDrugExample();
        DmsNonDrugExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(dmsNonDrugParam.getCode())){
            criteria.andCodeLike("%"+dmsNonDrugParam.getCode()+"%");
        }
        if (!StringUtils.isEmpty(dmsNonDrugParam.getName())){
            criteria.andNameLike("%"+dmsNonDrugParam.getName()+"%");

        }
        if (!StringUtils.isEmpty(dmsNonDrugParam.getMnemonicCode())){
            criteria.andMnemonicCodeLike("%"+dmsNonDrugParam.getMnemonicCode()+"%");
        }
        //执行科室和类型属性精确查找
        if (!StringUtils.isEmpty(dmsNonDrugParam.getRecordType())){
            criteria.andRecordTypeEqualTo(dmsNonDrugParam.getRecordType());
        }
        if (!StringUtils.isEmpty(dmsNonDrugParam.getDeptId())){
            criteria.andDeptIdEqualTo(dmsNonDrugParam.getDeptId());
        }
        criteria.andStatusNotEqualTo(0);
        //按id降序
        example.setOrderByClause("id desc");
        List<DmsNonDrugResult> dmsNonDrugResultList = new ArrayList<>();
        List<DmsNonDrug> dmsNonDrugList = dmsNonDrugMapper.selectByExample(example);
        System.out.println(dmsNonDrugList);
        for (DmsNonDrug dmsNonDrug : dmsNonDrugList) {
            DmsNonDrugResult dmsNonDrugResult = new DmsNonDrugResult();
            BeanUtils.copyProperties(dmsNonDrug,dmsNonDrugResult);
            if (!StringUtils.isEmpty(dmsNonDrug.getDeptId())){
                //封装科室名称
                dmsNonDrugResult.setDeptName(smsDeptMapper.selectByPrimaryKey(dmsNonDrug.getDeptId()).getName());
            }
            dmsNonDrugResultList.add(dmsNonDrugResult);
        }
        return dmsNonDrugResultList;
    }

    @Override
    public List<DmsNonDrugResult> selectAll() {
        //先在redis中查询是否存在
        String status = (String) redisUtil.getObj("nonDrugChangeStatus");
        if (status != null && status.equals("0")){
            List<DmsNonDrugResult> resultList = (List<DmsNonDrugResult>) redisUtil.getObj("allNonDrug");
            if (resultList != null){
                return resultList;
            }
        }
        //在数据库中查找
        List<DmsNonDrugResult> list = new ArrayList<>();
        DmsNonDrugExample example = new DmsNonDrugExample();
        example.createCriteria().andStatusNotEqualTo(0);
        for (DmsNonDrug dmsNonDrug : dmsNonDrugMapper.selectByExample(example)) {
            DmsNonDrugResult dmsNonDrugResult = new DmsNonDrugResult();
            BeanUtils.copyProperties(dmsNonDrug,dmsNonDrugResult);
            list.add(dmsNonDrugResult);

        }
        //向redis添加
        redisUtil.setObj("allNonDrug",list);
        redisUtil.setObj("nonDrugChangeStatus","0");
        return list;
    }
}
