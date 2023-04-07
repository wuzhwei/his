package com.wzw.his.api.controller.dms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.dms.DmsDiseParam;
import com.wzw.his.common.dto.dms.DmsDiseResult;
import com.wzw.his.dms.service.DmsDiseService;
import com.wzw.his.mbg.model.DmsDise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //根据ids删除诊断   逻辑删除  将status设置为1->0
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = dmsDiseService.delete(ids);
        if (count >= 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();

    }
    //更新诊断信息
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,@RequestBody DmsDiseParam dmsDiseParam,BindingResult result){
        int count = dmsDiseService.update(id, dmsDiseParam);
        if (count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    //模糊查询诊断,且分页
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<DmsDiseResult>> list(DmsDiseParam queryParam,
                                                        @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<DmsDiseResult> list = dmsDiseService.select(queryParam, pageSize, pageNum);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));

    }
    //查询所有诊断
    @RequestMapping(value = "/listAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<DmsDiseResult>> listAll(){
        return CommonResult.success(dmsDiseService.selectAll());

    }
    //根据id串获取诊断简单对象list
    @RequestMapping(value = "/parseList",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<DmsDiseResult>> parseList(@RequestParam(value = "idsStr") String idsStr){
        return CommonResult.success(dmsDiseService.parseList(idsStr));
    }


}
