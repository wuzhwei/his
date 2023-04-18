package com.wzw.his.api.controller.bms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.bms.service.BmsFeeService;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.bms.BmsChargeParam;
import com.wzw.his.common.dto.bms.BmsRegistrationPatientResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/fee")
public class BmsFeeController {
    @Autowired
    BmsFeeService bmsFeeService;
    @RequestMapping(value = "/listRegisteredPatient",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<BmsRegistrationPatientResult>> listRegisteredPatient(@RequestParam(required = false,name = "medicalRecordNo") String medicalRecordNo,
                                                                                        @RequestParam(required = false,name = "queryDate")@DateTimeFormat(pattern = "yyyy-MM-dd")Date queryDate,
                                                                                        @RequestParam(value = "pageSize",defaultValue ="5")Integer pageSize,
                                                                                        @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<BmsRegistrationPatientResult> list = bmsFeeService.listRegisteredPatient(medicalRecordNo, queryDate);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }


    @RequestMapping(value = "/charge",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult charge(@RequestBody List<BmsChargeParam> bmsChargeParamList){
        int result = bmsFeeService.charge(bmsChargeParamList);
        if (result == 1){
            return  CommonResult.success(result);
        }else {
            return CommonResult.failed();
        }

    }

}









