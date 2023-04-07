package com.wzw.his.api.controller.dms;

import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsDiseCatalogParam;
import com.wzw.his.common.dto.dms.DmsDiseCatalogResult;
import com.wzw.his.dms.service.DmsDiseCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/DmsDiseCatalog")
public class DmsDiseCatalogController {

    @Autowired
    DmsDiseCatalogService dmsDiseCatalogService;
    //新增一个诊断目录
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody DmsDiseCatalogParam dmsDiseCatalogParam, BindingResult result){
        CommonResult commonResult;
        int count = dmsDiseCatalogService.create(dmsDiseCatalogParam);
        if (count == 1){
            commonResult = CommonResult.success(count);
        }else{
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }
    //根据ids删除诊断记录
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = dmsDiseCatalogService.delete(ids);
        if (count >= 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,@RequestBody DmsDiseCatalogParam dmsDiseCatalogParam,BindingResult result){
        int count = dmsDiseCatalogService.update(id, dmsDiseCatalogParam);
        if (count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    //查询所有诊断目录
    @RequestMapping(value ="/listAll",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DmsDiseCatalogResult>> listAll(){
        return CommonResult.success(dmsDiseCatalogService.selectAll());
    }


}
