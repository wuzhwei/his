package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsNonDrugParam;
import com.wzw.his.dms.service.DmsNonDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/DmsNonDrug")
public class DmsNonDrugController {
    @Autowired
    DmsNonDrugService dmsNonDrugService;

    /**
     * 描述:新增非药品
     * @param dmsNonDrugParam
     * @param result
     * @return
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody DmsNonDrugParam dmsNonDrugParam, BindingResult result){
        CommonResult commonResult;
        int count = dmsNonDrugService.create(dmsNonDrugParam);
        if (count == 1){
            commonResult = CommonResult.success(count);
        }else{
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    /**
     * 根据ids删除非药品
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids")List<Long> ids){
        int count = dmsNonDrugService.delete(ids);
        if (count >= 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
