package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsDeptParam;
import com.wzw.his.common.dto.sms.SmsDeptResult;
import com.wzw.his.common.util.RedisUtil;
import com.wzw.his.mbg.mapper.SmsDeptMapper;
import com.wzw.his.mbg.model.SmsDept;
import com.wzw.his.mbg.model.SmsDeptExample;
import com.wzw.his.sms.SmsDeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class SmsDeptServiceImpl implements SmsDeptService {
    @Autowired
    SmsDeptMapper smsDeptMapper;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 描述:1.调用SmsDeptDao根据code查询科室是否存在
     * 2.1如果不存在则向SmsDeptDao插入数据，并返回1
     * 2.2如果存在则返回0
     * @param smsDeptParam
     * @return
     */
    @Override
    public int create(SmsDeptParam smsDeptParam) {
        SmsDept smsDept = new SmsDept();
        BeanUtils.copyProperties(smsDeptParam,smsDept);
        smsDept.setStatus(1);
        //查询是否有同code科室
        SmsDeptExample example = new SmsDeptExample();
        example.createCriteria().andNameEqualTo(smsDept.getName()).andStatusEqualTo(0);
        List<SmsDept> smsDeptList = smsDeptMapper.selectByExample(example);
        if (smsDeptList.size() > 0){
            return 0;
        }
        //插入成功,在redis修改flag
        redisUtil.setObj("deptChangeStatus","1");
        //没有则插入数据
        return smsDeptMapper.insert(smsDept);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsDept smsDept = new SmsDept();
        smsDept.setStatus(0);
        SmsDeptExample example = new SmsDeptExample();
        example.createCriteria().andIdIn(ids);
        //删除成功,在redis修改flag
        redisUtil.setObj("deptChangeStatus","1");
        return  smsDeptMapper.updateByExampleSelective(smsDept,example);
    }

    @Override
    public int update(Long id, SmsDeptParam smsDeptParam) {
        SmsDept smsDept = new SmsDept();
        BeanUtils.copyProperties(smsDeptParam,smsDept);
        smsDept.setId(id);
        //修改成功,在redis修改flag
        redisUtil.setObj("deptChangeStatus","1");

        return smsDeptMapper.updateByPrimaryKeySelective(smsDept);
    }

    @Override
    public List<SmsDeptResult> select(SmsDeptParam smsDeptQueryParam) {
        SmsDeptExample example = new SmsDeptExample();
        SmsDeptExample.Criteria criteria = example.createCriteria();
        //如果没有指明state，返回不为0的
        if (smsDeptQueryParam.getStatus() == null){
            criteria.andStatusNotEqualTo(0);
        }
        //是否按编码、名称、类型查询
        if (!StringUtils.isEmpty(smsDeptQueryParam.getCode())){
            criteria.andCodeLike("%"+smsDeptQueryParam.getCode() +"%");
        }
        if (!StringUtils.isEmpty(smsDeptQueryParam.getName())){
            criteria.andNameLike("%"+smsDeptQueryParam.getName()+"%");
        }
        if (smsDeptQueryParam.getCatId() != null){
            criteria.andCatIdEqualTo(smsDeptQueryParam.getCatId());
        }
        if (smsDeptQueryParam.getType() != null){
            criteria.andTypeEqualTo(smsDeptQueryParam.getType());
        }
        //返回数据包装成Result
        example.setOrderByClause("id desc");
        List<SmsDept> smsDepts = smsDeptMapper.selectByExample(example);
        List<SmsDeptResult> smsDeptResults = new ArrayList<>();
        for (SmsDept s : smsDepts) {
            SmsDeptResult r = new SmsDeptResult();
            BeanUtils.copyProperties(s,r);
            smsDeptResults.add(r);
        }

        return smsDeptResults;
    }

    @Override
    public List<SmsDeptResult> selectAll() {
        //先在redis中查询是否存在
        String status = (String) redisUtil.getObj("deptChangeStatus");
        if (status != null && status.equals("0")){
            List<SmsDeptResult> resultList = (List<SmsDeptResult>) redisUtil.getObj("allDept");
            if (resultList != null){
                System.out.println("从redis中取出全部科室数据信息");
                return resultList;
            }
        }else {
            System.err.println("从mysql取出全部科室数据");
        }
        //数据库查询
        SmsDeptExample example = new SmsDeptExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回数据包装成Result
        List<SmsDept> smsDepts = smsDeptMapper.selectByExample(example);
        List smsDeptResults = new ArrayList<>();
        for (SmsDept s : smsDepts) {
            SmsDeptResult r = new SmsDeptResult();
            BeanUtils.copyProperties(s,r);
            smsDeptResults.add(r);

        }
        //向redis中添加
        redisUtil.setObj("allDept",smsDeptResults);
        redisUtil.setObj("deptChangeStatus","0");
        return smsDeptResults;
    }
}
