package com.wzw.his.api.controller.sms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.sms.SmsFrequentUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/frequentUsed")
public class SmsFrequentUsedController {
    @Autowired
    private SmsFrequentUsedService smsFrequentUsedService;
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addFrequent(@RequestParam("staffId")Long staffId,
                                    @RequestParam("addType")int addType,
                                    @RequestParam("addId")Long addId){
        int count = smsFrequentUsedService.addFrequent(staffId, addType, addId);
        if (count > 0){
            return CommonResult.success(count,"添加常用项成功");
        }
        return CommonResult.failed("添加常用项失败");

    }
}
