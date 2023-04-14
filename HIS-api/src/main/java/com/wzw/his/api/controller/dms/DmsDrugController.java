package com.wzw.his.api.controller.dms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsDosageResult;
import com.wzw.his.common.dto.dms.DmsDrugParam;
import com.wzw.his.common.dto.dms.DmsDrugResult;
import com.wzw.his.dms.service.DmsDrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/drug")
public class DmsDrugController {
    @Autowired
    private DmsDrugService dmsDrugService;

    @RequestMapping(value = "/createDrug",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult createDrug(@RequestBody DmsDrugParam dmsDrugParam, BindingResult result){
        int count = dmsDrugService.createDrug(dmsDrugParam);
        if (count > 0){
            return CommonResult.success(count,"添加科室成功");
        }
        return CommonResult.failed("添加科室失败");
    }


    @RequestMapping(value = "/deleteDrug",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteDrug(@RequestParam("ids") List<Long> ids){
        int count = dmsDrugService.deleteDrug(ids);
        if (count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");
    }


    @RequestMapping(value = "/updateDrug/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateDrug(@PathVariable Long id,@RequestBody DmsDrugParam dmsDrugParam,BindingResult result){
        int count = dmsDrugService.updateDrug(id, dmsDrugParam);
        if (id > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");
    }


    @RequestMapping(value = "/selectDrug",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<DmsDrugResult>> listDrug(DmsDrugParam queryParam,
                                                            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DmsDrugResult> list = dmsDrugService.selectDrug(queryParam, pageSize, pageNum);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    @RequestMapping(value = "/selectAllDrug",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DmsDrugResult>> listAllDrug(){
        List<DmsDrugResult> list = dmsDrugService.selectAllDrug();
        return CommonResult.success(list);
    }


    @RequestMapping(value = "selectAllDosage",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DmsDosageResult>> listAllDrugDosage(){
        List<DmsDosageResult> list = dmsDrugService.selectAllDosage();
        return CommonResult.success(list);
    }






}
