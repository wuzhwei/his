package com.wzw.his.api.controller.sms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsSkdRuleParam;
import com.wzw.his.sms.SmsSkdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/skd")
public class SmsSkdController {
    @Autowired
    private SmsSkdService smsSkdService;

    /**
     * 描述:创建排版规则
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

}
