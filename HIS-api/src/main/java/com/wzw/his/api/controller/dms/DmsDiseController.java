package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsDiseParam;
import com.wzw.his.dms.service.DmsDiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/DmsDise")
public class DmsDiseController {
    @Autowired
    DmsDiseService dmsDiseService;
    //新增诊断
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody DmsDiseParam dmsDiseParam, BindingResult result){
        CommonResult commonResult;
        int count = dmsDiseService.create(dmsDiseParam);
        if (count == 1){
            commonResult = CommonResult.success(count);
        }else{
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }





}
