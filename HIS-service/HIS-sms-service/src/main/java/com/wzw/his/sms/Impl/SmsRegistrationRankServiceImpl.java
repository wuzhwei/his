package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsRegistrationRankParam;
import com.wzw.his.common.dto.sms.SmsRegistrationRankResult;
import com.wzw.his.common.util.RedisUtil;
import com.wzw.his.mbg.mapper.SmsRegistrationRankMapper;
import com.wzw.his.mbg.model.SmsRegistrationRank;
import com.wzw.his.mbg.model.SmsRegistrationRankExample;
import com.wzw.his.sms.SmsRegistrationRankService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
@Service
public class SmsRegistrationRankServiceImpl implements SmsRegistrationRankService {
    @Autowired
    SmsRegistrationRankMapper smsRegistrationRankMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public int create(SmsRegistrationRankParam smsRegistrationRankParam) {
        SmsRegistrationRank smsRegistrationRank = new SmsRegistrationRank();
        BeanUtils.copyProperties(smsRegistrationRankParam,smsRegistrationRank);
        smsRegistrationRank.setStatus(1);
        //查询是偶有状态非0且名字相同的挂号级别
        SmsRegistrationRankExample example = new SmsRegistrationRankExample();
        example.createCriteria().andNameEqualTo(smsRegistrationRank.getName()).andStatusNotEqualTo(0);
        List<SmsRegistrationRank> lists = smsRegistrationRankMapper.selectByExample(example);
        if (lists.size() > 0){
            return 0;
        }
        //插入成功,在redis修改flag
        redisUtil.setObj("registrationRankChangeStatus","1");
        //没有则插入数据
        return smsRegistrationRankMapper.insert(smsRegistrationRank);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsRegistrationRank smsRegistrationRank = new SmsRegistrationRank();
        smsRegistrationRank.setStatus(0);
        SmsRegistrationRankExample example = new SmsRegistrationRankExample();
        example.createCriteria().andIdIn(ids);
        //删除成功，在redis修改flag
        redisUtil.setObj("registrationRankChangStatus","1");
        return smsRegistrationRankMapper.updateByExampleSelective(smsRegistrationRank,example);
    }

    @Override
    public int update(Long id, SmsRegistrationRankParam smsRegistrationRankParam) {
        SmsRegistrationRank smsRegistrationRank = new SmsRegistrationRank();
        BeanUtils.copyProperties(smsRegistrationRankParam,smsRegistrationRank);
        smsRegistrationRank.setId(id);
        //更新成功,在redis修改flag
        redisUtil.setObj("registrationRankChangeStatus","1");
        return smsRegistrationRankMapper.updateByPrimaryKeySelective(smsRegistrationRank);
    }

    @Override
    public List<SmsRegistrationRankResult> select(SmsRegistrationRankParam smsRegistrationRankParam) {
        SmsRegistrationRankExample example = new SmsRegistrationRankExample();
        SmsRegistrationRankExample.Criteria criteria = example.createCriteria();
        //如果没有指明state，返回不为0的
        if (smsRegistrationRankParam.getStatus() == null){
            criteria.andStatusNotEqualTo(0);
        }
        //是否按编码,名称,价格,显示顺序号查询
        if (!StringUtils.isEmpty(smsRegistrationRankParam.getCode())){
            criteria.andCodeLike("%"+smsRegistrationRankParam.getCode()+"%");
        }
        if (!StringUtils.isEmpty(smsRegistrationRankParam.getName())){
            criteria.andNameLike("%"+smsRegistrationRankParam.getName() +"%");
        }
        if (smsRegistrationRankParam.getPrice() != null){
            criteria.andPriceEqualTo(smsRegistrationRankParam.getPrice());
        }
        if (smsRegistrationRankParam.getSeqNo() != null){
            criteria.andSeqNoEqualTo(smsRegistrationRankParam.getSeqNo());
        }
        //返回数据包装成Result
        example.setOrderByClause("id desc");
        List<SmsRegistrationRank> smsRegistrationRanks = smsRegistrationRankMapper.selectByExample(example);
        List<SmsRegistrationRankResult> returnList = new ArrayList<>();
        for (SmsRegistrationRank s : smsRegistrationRanks) {
            SmsRegistrationRankResult r = new SmsRegistrationRankResult();
            BeanUtils.copyProperties(s,r);
            returnList.add(r);

        }
        return returnList;
    }

    @Override
    public List<SmsRegistrationRankResult> selectAll() {
        //先在redis中查询是否存在
        String status = (String) redisUtil.getObj("registrationRankChangeStatus");
        if (status!= null && status.equals("0")){
            List<SmsRegistrationRankResult> resultList = (List<SmsRegistrationRankResult>) redisUtil.getObj("allRegistrationRank");
            if (CollectionUtils.isEmpty(resultList)){
                return  resultList;
            }
        }
        //在数据库中查找
        SmsRegistrationRankExample example = new SmsRegistrationRankExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回包装成Result
        List<SmsRegistrationRank> smsRegistrationRanks = smsRegistrationRankMapper.selectByExample(example);
        List<SmsRegistrationRankResult> returnList = new ArrayList<>();
        for (SmsRegistrationRank s : smsRegistrationRanks) {
            SmsRegistrationRankResult r = new SmsRegistrationRankResult();
            BeanUtils.copyProperties(s,r);
            returnList.add(r);
        }
        //向redis添加
        redisUtil.setObj("allRegistrationRank",returnList);
        redisUtil.setObj("registrationRankChangeStatus","0");


        return returnList;
    }
}
