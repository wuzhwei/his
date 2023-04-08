package com.wzw.his.api.controller.sms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsSkdParam;
import com.wzw.his.common.dto.sms.SmsSkdResult;
import com.wzw.his.common.dto.sms.SmsSkdRuleParam;
import com.wzw.his.mbg.model.SmsSkdRuleResult;
import com.wzw.his.sms.SmsSkdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/skd")
public class SmsSkdController {
    @Autowired
    private SmsSkdService smsSkdService;

    /**
     * 描述:创建排班规则
     * @param smsSkdRuleParam
     * @param result
     * @return
     */
    @RequestMapping(value = "/createRule",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createRule(@RequestBody SmsSkdRuleParam smsSkdRuleParam, BindingResult result){
        int count = smsSkdService.createRule(smsSkdRuleParam);
        if (count > 0){
            return CommonResult.success(count,"新增排班规则成功");
        }
        return CommonResult.failed("新增排班规则失败");
    }

    /**
     * 删除排班规则
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteRule",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteRule(@RequestParam("ids") List<Long> ids){
        int count = smsSkdService.deleteRule(ids);
        if (count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    /**
     * 更新排班规则
     * @param id
     * @param smsSkdRuleParam
     * @param result
     * @return
     */

    @RequestMapping(value = "/updateRule/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRule(@PathVariable Long id,@RequestBody SmsSkdRuleParam smsSkdRuleParam,BindingResult result){
        int count = smsSkdService.updateRule(id, smsSkdRuleParam);
        if (count > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");

    }

    /**
     * 描述:根据部门id筛选排班规则基本信息、分页
     * 修改查询不到科室排班规则500错误,并返回创建人name
     * @param deptId
     * @param pageSize
     * @param pageNum
     * @return
     */

    @RequestMapping(value = "/listRule",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSkdRuleResult>> listRule(@RequestParam("deptId") Long deptId,
                                                               @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                               @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsSkdRuleResult> list = smsSkdService.selectRuleByDept(deptId);
        if (CollectionUtils.isEmpty(list)){
            return CommonResult.success(null,"不存在排班规则");
        }
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    //根据规则id查询一条规则详情
    @RequestMapping(value = "/getRuleDetail",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsSkdRuleResult> getRuleDetail(@RequestParam("ruleId")Long ruleId){
        SmsSkdRuleResult smsSkdRuleResult = smsSkdService.getRuleDetail(ruleId);
        return CommonResult.success(smsSkdRuleResult);
    }

    /**
     * 描述:根据多条件排班规则生成skd
     * @param ruleIds
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/generateSkd",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult generateSkd(@RequestParam("ruleIds") List<Long> ruleIds, @RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MMM-dd")String startDate,@RequestParam("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd")String endDate) throws ParseException {
        SimpleDateFormat simdate = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = simdate.parse(startDate);
        Date edate = simdate.parse(endDate);
        int count = smsSkdService.generateSkd(ruleIds, sdate, edate);
        if (count > 0 ){
            return CommonResult.success(count,"生成成功");
        }
        return CommonResult.failed("生成失败");

    }


    @RequestMapping(value = "/listSkd",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsSkdResult>> listSkd(SmsSkdParam queryParam,
                                                          @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                          @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsSkdResult> smsSkdResultList = smsSkdService.listSkd(queryParam);
        long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(smsSkdResultList,pageTotal));
    }

}
