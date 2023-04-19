package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsMedicinePrescriptionRecordResult;
import com.wzw.his.dms.service.DmsMedicinePrescriptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/DmsMedicinePrescriptionRecord")
public class DmsMedicinePrescriptionRecordController {
    @Autowired
    private DmsMedicinePrescriptionRecordService dmsMedicinePrescriptionRecordService;
    
    @RequestMapping(value = "/listByIds",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DmsMedicinePrescriptionRecordResult>> listByIds(@RequestParam("ids")List<Long> ids){
        List<DmsMedicinePrescriptionRecordResult> list = dmsMedicinePrescriptionRecordService.listByIds(ids);
        return CommonResult.success(list);
    }
}
