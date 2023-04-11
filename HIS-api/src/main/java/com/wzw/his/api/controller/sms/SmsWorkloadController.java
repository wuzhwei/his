package com.wzw.his.api.controller.sms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsWorkloadResult;
import com.wzw.his.sms.SmsWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/workload")
public class SmsWorkloadController {

    @Autowired
    private SmsWorkloadService smsWorkloadService;

    @RequestMapping(value = "/queryPersonal",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<SmsWorkloadResult> queryPersonalWorkload(@RequestParam("staffId")Long staffId,
                                                                 @RequestParam("startDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date startDatetime,
                                                                 @RequestParam("endDatetime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")Date endDatetime){
        return CommonResult.success(smsWorkloadService.queryPersonalWorkloadPeriod(staffId,startDatetime,endDatetime));
    }
}
