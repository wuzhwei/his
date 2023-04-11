package com.wzw.his.sms.Impl;

import com.wzw.his.common.dto.sms.SmsStaffParam;
import com.wzw.his.common.dto.sms.SmsStaffResult;
import com.wzw.his.common.util.JwtTokenUtil;
import com.wzw.his.common.util.RedisUtil;
import com.wzw.his.mbg.mapper.SmsDeptMapper;
import com.wzw.his.mbg.mapper.SmsRoleMapper;
import com.wzw.his.mbg.mapper.SmsSkdRuleMapper;
import com.wzw.his.mbg.mapper.SmsStaffMapper;
import com.wzw.his.mbg.model.*;
import com.wzw.his.sms.SmsStaffService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SmsStaffServiceImpl implements SmsStaffService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsStaffServiceImpl.class);

    @Autowired
    SmsStaffMapper smsStaffMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SmsSkdRuleMapper smsSkdRuleMapper;
    @Autowired
    SmsDeptMapper smsDeptMapper;

    @Autowired
    SmsRoleMapper smsRoleMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public int create(SmsStaffParam smsStaffParam) {
        return 0;
    }

    @Override
    public int delete(List<Long> ids) {
        SmsStaff smsStaff = new SmsStaff();
        smsStaff.setStatus(0);
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andIdIn(ids);
        int deleteCount = smsStaffMapper.updateByExampleSelective(smsStaff, example);
        //删除排班规则表中与ids相关行
        SmsSkdRuleExample smsSkdRuleExample = new SmsSkdRuleExample();
        smsSkdRuleExample.createCriteria().andIdIn(ids);
        int deleteSkdCount = smsSkdRuleMapper.deleteByExample(smsSkdRuleExample);
        //删除排班表中与ids相关行？
        //TODO: 2019/5/30

        //删除成功，在redis修改flag
        redisUtil.setObj("staffChangeStatus","1");
        return deleteCount;
    }

    @Override
    public int update(Long id, SmsStaffParam smsStaffParam) {
        SmsStaff smsStaff = new SmsStaff();
        BeanUtils.copyProperties(smsStaffParam,smsStaff);
        smsStaff.setId(id);

        //删除成功,在redis修改flag
        redisUtil.setObj("staffChangeStatus","1");

        return smsStaffMapper.updateByPrimaryKeySelective(smsStaff);
    }

    @Override
    public List<SmsStaffResult> select(SmsStaffParam smsStaffQueryParam, Integer pageSize, Integer pageNum) {
        SmsStaffExample example = new SmsStaffExample();
        SmsStaffExample.Criteria criteria = example.createCriteria();
        //如果没有指明state,返回不为0的
        if (smsStaffQueryParam.getStatus() == null){
            criteria.andStatusNotEqualTo(0);
        }
        //是否按登录名，真实姓名、科室、性别、是否参与排班、职称、角色、挂号级别查询
        if (!StringUtils.isEmpty(smsStaffQueryParam.getUsername())){
            criteria.andUsernameLike("%" + smsStaffQueryParam.getUsername() + "%");
        }
        if (!StringUtils.isEmpty(smsStaffQueryParam.getName())){
            criteria.andNameEqualTo("%" + smsStaffQueryParam.getName() +"%");
        }
        if (smsStaffQueryParam.getDeptId() != null){
            criteria.andDeptIdEqualTo(smsStaffQueryParam.getDeptId());
        }
        if (smsStaffQueryParam.getGender() != null){
            criteria.andGenderEqualTo(smsStaffQueryParam.getGender());
        }
        if(smsStaffQueryParam.getSkdFlag() != null){
            criteria.andSkdFlagEqualTo(smsStaffQueryParam.getSkdFlag());
        }
        if(!StringUtils.isEmpty(smsStaffQueryParam.getTitle())){
            criteria.andTitleEqualTo("%" + smsStaffQueryParam.getTitle() + "%");
        }
        if(smsStaffQueryParam.getRoleId() != null){
            criteria.andRoleIdEqualTo(smsStaffQueryParam.getRoleId());
        }
        if(smsStaffQueryParam.getRegistrationRankId() != null){
            criteria.andRegistrationRankIdEqualTo(smsStaffQueryParam.getRegistrationRankId());
        }
        //返回数据包装成Result
        example.setOrderByClause("id desc");
        List<SmsStaff> results = smsStaffMapper.selectByExample(example);
        List<SmsStaffResult> returnList = new ArrayList<>();
        for (SmsStaff smsStaff:
             results) {
            SmsStaffResult smsStaffResult = new SmsStaffResult();
            BeanUtils.copyProperties(smsStaff,smsStaffResult);

            //从科室表中找出对应的科室信息
            Long deptId = new Long(smsStaff.getDeptId());
            SmsDeptExample deptExample = new SmsDeptExample();
            deptExample.createCriteria().andIdEqualTo(deptId);
            List<SmsDept> dept = smsDeptMapper.selectByExample(deptExample);
            //如果有科室信息则加入，否则加入的是null
            if (dept.size() > 0){
                smsStaffResult.setDept(dept.get(0));
            }
            //从角色表中找到对应角色信息
            Long roleId = new Long(smsStaff.getRoleId());
            SmsRoleExample roleExample = new SmsRoleExample();
            roleExample.createCriteria().andIdEqualTo(roleId);
            List<SmsRole> role = smsRoleMapper.selectByExample(roleExample);
            //如果有角色信息则加入，否则加入的是null
            if (role.size() > 0){
                smsStaffResult.setRole(role.get(0));
            }
            returnList.add(smsStaffResult);
        }
        return returnList;
    }

    @Override
    public List<SmsStaffResult> selectAll() {
        //先在redis中查询是否存在
        String status = (String) redisUtil.getObj("staffChangeStatus");
        if (status != null && status.equals("0")){
            List<SmsStaffResult> resultList = (List<SmsStaffResult>) redisUtil.getObj("allStaff");
            if (CollectionUtils.isEmpty(resultList)){
                return resultList;
            }
        }
        //从数据库中读取
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andStatusNotEqualTo(0);
        //返回数据包装成Result
        List<SmsStaff> results = smsStaffMapper.selectByExample(example);
        List<SmsStaffResult> returnList = new ArrayList<>();
        for (SmsStaff s :
                results) {
            SmsStaffResult r = new SmsStaffResult();
            BeanUtils.copyProperties(s,r);
            //从科室表中找到对应的科室信息
            Long deptId = new Long(s.getDeptId());
            SmsDeptExample deptExample = new SmsDeptExample();
            deptExample.createCriteria().andIdEqualTo(deptId);
            List<SmsDept> dept = smsDeptMapper.selectByExample(deptExample);
            //如果有科室信息则加入，否则加入的是null
            if (dept.size() > 0){
                r.setDept(dept.get(0));
            }
            //从角色表中找到对应角色信息
            Long roleId = new Long(s.getRoleId());
            SmsRoleExample roleExample = new SmsRoleExample();
            roleExample.createCriteria().andIdEqualTo(roleId);
            List<SmsRole> role = smsRoleMapper.selectByExample(roleExample);
            //如果有角色信息则加入，否则加入的是null
            if (role.size() > 0){
                r.setRole(role.get(0));

            }
            returnList.add(r);
        }
        //向redis添加
        redisUtil.setObj("allStaff",returnList);
        redisUtil.setObj("staffChangeStatus","0");
        return returnList;

    }

    @Override
    public SmsStaff selectByUserName(String username) {
        SmsStaffExample example = new SmsStaffExample();
        //查找名字相同的且状态不为0的用户
        example.createCriteria().andUsernameEqualTo(username).andStatusNotEqualTo(0);
        List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(example);
        if (smsStaffList != null && smsStaffList.size() > 0){
            return smsStaffList.get(0);
        }
        return null;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            //返回的是一个userDetails的实现类AdminUserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //password是从前端传过来未经过编译的,而userDetails.getPassword()是从数据库中出来经过编译的
            if (!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            //创建一个新的token
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            //在securityContext中添加该验证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            //updateLoginTimeByUsername(username)
            //insertLogin(username)
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}",e.getMessage());
        }
        return token;
    }

    @Override
    public SmsStaff getStaffByPwd(String username, String password) {
        SmsStaffExample example = new SmsStaffExample();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())){
            String encodePassword = userDetails.getPassword();
            example.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(encodePassword).andStatusEqualTo(1);
            List<SmsStaff> smsStaffList = smsStaffMapper.selectByExample(example);
            if (smsStaffList != null && smsStaffList.size() > 0){
                return smsStaffList.get(0);
            }
        }
//        String encodePassword = passwordEncoder.encode(password);


        return null;
    }

    @Override
    public List<SmsStaffResult> getStaffByDept(Long deptId) {
        return null;
    }

    @Override
    public SmsStaff register(SmsStaffParam smsStaffParam) {
        SmsStaff smsStaff = new SmsStaff();
        BeanUtils.copyProperties(smsStaffParam,smsStaff);
        //封装dto中没有传来的属性
        smsStaff.setCreateTime(new Date());
        smsStaff.setStatus(1);
        //查询是否有相同用户名的用户
        SmsStaffExample example = new SmsStaffExample();
        example.createCriteria().andUsernameEqualTo(smsStaff.getUsername());//spring MVC必须保证Username不为null
        List<SmsStaff> staffList = smsStaffMapper.selectByExample(example);
        //存在，通过此接口进行创建,则重复数量只可能为1
        if (staffList.size() > 0 ){
            return null;
        }
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(smsStaff.getPassword());
        smsStaff.setPassword(encodePassword);
        smsStaffMapper.insert(smsStaff);
        //插入成功,在redis修改flag
        redisUtil.setObj("staffChangeStatus","1");
        //返回对象   密码没有加密
        return smsStaff;
    }

    @Override
    public List<SmsStaff> selectSkdStaff(Long deptId, Long registrationRankId) {
        return null;
    }
}
