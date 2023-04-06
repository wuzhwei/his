package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsRegistrationParam;
import com.wzw.his.dms.service.DmsRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/registration")
public class DmsRegistrationController {
    @Autowired
    DmsRegistrationService dmsRegistrationService;
    //挂号
    //1.调用DmsRegistrationService的createRegistration
    @RequestMapping(value = "/createRegistration",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createRegistration(@RequestBody DmsRegistrationParam dmsRegistrationParam, BindingResult result){
        int returnResult = dmsRegistrationService.createRegistration(dmsRegistrationParam);
        if (returnResult == 1){
            return CommonResult.success(returnResult);
        }else{
            return CommonResult.failed();
        }
    }



}
