package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.pms.PmsDrugStorePatientListResult;
import com.wzw.his.dms.service.DmsDrugStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/drugStore")
public class DmsDrugStoreController {
    @Autowired
    DmsDrugStoreService dmsDrugStoreService;
    @RequestMapping(value = "/listPatient",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<PmsDrugStorePatientListResult> listPatient(@RequestParam(value = "medicalRecordNo",required = false)String medicalRecordNo,
                                                                   @RequestParam("queryDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date queryDate,
                                                                   @RequestParam("type")Integer type){
        PmsDrugStorePatientListResult result = dmsDrugStoreService.listPatient(queryDate, medicalRecordNo, type);
        return CommonResult.success(result);
    }
}
