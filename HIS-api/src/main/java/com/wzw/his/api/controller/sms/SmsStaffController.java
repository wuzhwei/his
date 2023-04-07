package com.wzw.his.api.controller.sms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsSkdDocParam;
import com.wzw.his.common.dto.sms.SmsSkdDocResult;
import com.wzw.his.sms.SmsSkdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class SmsStaffController {
    @Autowired
    SmsSkdService smsSkdService;

    //查询上班医生
    @RequestMapping(value = "/listDocBySkd",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<SmsSkdDocResult>> listDocBySkd(@RequestBody SmsSkdDocParam smsSkdDocParam,
                                                            BindingResult result){
        List<SmsSkdDocResult> smsSkdDocResultList;
        smsSkdDocResultList = smsSkdService.listDocBySkd(smsSkdDocParam);
        return CommonResult.success(smsSkdDocResultList);

    }

}
