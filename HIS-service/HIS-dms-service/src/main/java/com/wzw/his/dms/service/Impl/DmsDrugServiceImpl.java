package com.wzw.his.dms.service.Impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.druid.util.StringUtils;
import com.wzw.his.common.dto.dms.DmsDosageResult;
import com.wzw.his.common.dto.dms.DmsDrugParam;
import com.wzw.his.common.dto.dms.DmsDrugResult;
import com.wzw.his.dms.service.DmsDrugService;
import com.wzw.his.mbg.mapper.DmsDosageMapper;
import com.wzw.his.mbg.mapper.DmsDrugMapper;
import com.wzw.his.mbg.model.DmsDosage;
import com.wzw.his.mbg.model.DmsDosageExample;
import com.wzw.his.mbg.model.DmsDrug;
import com.wzw.his.mbg.model.DmsDrugExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class DmsDrugServiceImpl implements DmsDrugService {
    @Autowired
    private DmsDrugMapper dmsDrugMapper;
    @Autowired
    private DmsDosageMapper dmsDosageMapper;
    @Override
    public int createDrug(DmsDrugParam dmsDrugParam) {
        DmsDrug dmsDrug = new DmsDrug();
        BeanUtils.copyProperties(dmsDrugParam,dmsDrug);
        dmsDrug.setStatus(1);
        //查询是否有相同code药品,且status为1的药品
        DmsDrugExample example = new DmsDrugExample();
        example.createCriteria().andCodeEqualTo(dmsDrug.getCode());
        List<DmsDrug> dmsDrugList = dmsDrugMapper.selectByExample(example);
        if (dmsDrugList.size() <= 0){
            //没有则插入数据
            return dmsDrugMapper.insert(dmsDrug);
        }
        //有则判断status是否为0
        DmsDrug oldDrug = dmsDrugList.get(0);
        if (oldDrug.getStatus() == 1){
            return 0;
        }else{
            //status不为0，先删除,在插入
            dmsDrugMapper.deleteByExample(example);
            return dmsDrugMapper.insert(dmsDrug);
        }
    }

    /**
     * 删除药品
     * @param ids
     * @return
     */
    @Override
    public int deleteDrug(List<Long> ids) {
        DmsDrug dmsDrug = new DmsDrug();
        dmsDrug.setStatus(0);
        DmsDrugExample example = new DmsDrugExample();
        example.createCriteria().andIdIn(ids);
        return dmsDrugMapper.updateByExampleSelective(dmsDrug,example);
    }

    /**
     * 描述:更新
     * @param id
     * @param dmsDrugParam
     * @return
     */
    @Override
    public int updateDrug(Long id, DmsDrugParam dmsDrugParam) {
        DmsDrug dmsDrug = new DmsDrug();
        BeanUtils.copyProperties(dmsDrugParam,dmsDrug);
        dmsDrug.setId(id);
        return dmsDrugMapper.updateByPrimaryKeySelective(dmsDrug);
    }

    @Override
    public List<DmsDrugResult> selectDrug(DmsDrugParam dmsDrugParam, Integer pageSize, Integer pageNum) {
        DmsDrugExample example = new DmsDrugExample();
        DmsDrugExample.Criteria criteria = example.createCriteria();
        //如果没有指明state，返回不为0的
        if (dmsDrugParam.getStatus() == null){
            criteria.andStatusNotEqualTo(0);
        }
        //是否按编码、名称、规格、单价查询
        if (!StringUtils.isEmpty(dmsDrugParam.getCode())){
            criteria.andCodeLike("%"+dmsDrugParam.getCode()+"%");
        }
        if (!StringUtils.isEmpty(dmsDrugParam.getName())){
            criteria.andNameLike("%"+dmsDrugParam.getName()+"%");
        }
        if (!StringUtils.isEmpty(dmsDrugParam.getFormat())){
            criteria.andFormatLike("%"+dmsDrugParam.getFormat()+"%");
        }
        if (dmsDrugParam.getPrice() != null){
            criteria.andPriceEqualTo(dmsDrugParam.getPrice());
        }
        //是否按包装单位,生产厂家,药品剂型,药品类型查询
        if (!StringUtils.isEmpty(dmsDrugParam.getUnit())){
            criteria.andUnitLike("%"+dmsDrugParam.getUnit()+"%");
        }
        if (!StringUtils.isEmpty(dmsDrugParam.getManufacturer())){
            criteria.andManufacturerLike("%"+dmsDrugParam.getManufacturer()+"%");
        }
        if (dmsDrugParam.getDosageId() != null){
            criteria.andDosageIdEqualTo(dmsDrugParam.getDosageId());
        }
        if (dmsDrugParam.getTypeId() != null){
            criteria.andTypeIdEqualTo(dmsDrugParam.getTypeId());
        }
        //是否按拼音助记码、库存、通用名查询
        if (!StringUtils.isEmpty(dmsDrugParam.getMnemonicCode())){
            criteria.andMnemonicCodeLike("%"+dmsDrugParam.getMnemonicCode()+"%");
        }
        if (dmsDrugParam.getStock() != null){
            criteria.andStockEqualTo(dmsDrugParam.getStock());
        }
        if (!StringUtils.isEmpty(dmsDrugParam.getGenericName())){
            criteria.andGenericNameLike("%"+dmsDrugParam.getGenericName()+"%");
        }
        //返回数据包装成Result
        example.setOrderByClause("id desc");
        List<DmsDrug> dmsDrugList = dmsDrugMapper.selectByExample(example);
        List<DmsDrugResult> smsDrugResultList = new ArrayList<>();
        for (DmsDrug dmsDrug : dmsDrugList) {
            DmsDrugResult r = new DmsDrugResult();
            BeanUtils.copyProperties(dmsDrug,r);
            DmsDosageExample dmsDosageExample = new DmsDosageExample();
            dmsDosageExample.createCriteria().andIdEqualTo(dmsDrug.getDosageId());
            List<DmsDosage> dmsDosageList = dmsDosageMapper.selectByExample(dmsDosageExample);
            if (dmsDosageList.size() <= 0){
                return  null;
            }
            r.setDosage(dmsDosageList.get(0));
            smsDrugResultList.add(r);
        }
        return smsDrugResultList;
    }

    /**
     * 描述:调用DmsDrugDao查询所有记录(不包括status = 0)
     * @return
     */

    @Override
    public List<DmsDrugResult> selectAllDrug() {
        DmsDrugExample example = new DmsDrugExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回数据包装成Result
        List<DmsDrug> dmsDrugs = dmsDrugMapper.selectByExample(example);
        List<DmsDrugResult> dmsDrugResultList = new ArrayList<>();
        for (DmsDrug dmsDrug : dmsDrugs) {
            DmsDrugResult dmsDrugResult = new DmsDrugResult();
            BeanUtils.copyProperties(dmsDrug,dmsDrugResult);
            DmsDosageExample dmsDosageExample = new DmsDosageExample();
            //分装剂型
            dmsDosageExample.createCriteria().andIdEqualTo(dmsDrug.getDosageId());
            List<DmsDosage> dmsDosageList = dmsDosageMapper.selectByExample(dmsDosageExample);
            if (!CollectionUtil.isEmpty(dmsDosageList)){
                dmsDrugResult.setDosage(dmsDosageList.get(0));
            }
            dmsDrugResultList.add(dmsDrugResult);
        }
        return dmsDrugResultList;
    }

    @Override
    public List<DmsDosageResult> selectAllDosage() {
        DmsDosageExample example = new DmsDosageExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回数据包装成Result
        List<DmsDosage> dmsDosages = dmsDosageMapper.selectByExample(example);
        List<DmsDosageResult> dosageResults = new ArrayList<>();
        for (DmsDosage s : dmsDosages) {
            DmsDosageResult r = new DmsDosageResult();
            BeanUtils.copyProperties(s,r);
            dosageResults.add(r);

        }
        return dosageResults;
    }

    @Override
    public DmsDrugResult selectById(Long id) {
        return null;
    }
}
