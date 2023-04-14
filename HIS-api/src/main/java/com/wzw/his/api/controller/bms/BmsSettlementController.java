package com.wzw.his.api.controller.bms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.bms.service.BmsSettlementCatService;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.bms.BmsSettlementCatParam;
import com.wzw.his.common.dto.bms.BmsSettlementCatResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/settleCat")
public class BmsSettlementController {
    @Autowired
    private BmsSettlementCatService bmsSettlementCatService;

    /**
     * 描述:新增一个结算类别
     *
     * @param bmsSettlementCatParam
     * @param result
     * @return
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody BmsSettlementCatParam bmsSettlementCatParam, BindingResult result){
        int count = bmsSettlementCatService.create(bmsSettlementCatParam);
        if (count > 0){
            return CommonResult.success(count,"添加成功");
        }
        return CommonResult.failed("添加失败");

    }

    /**
     * 描述:根据ids删除结算类别
     * @param ids
     * @return
     */

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids")List<Long> ids){
        int count = bmsSettlementCatService.delete(ids);
        if (count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");

    }

    /**
     * 描述:更新一个结算类别的全部信息
     * @param id
     * @param bmsSettlementCatParam
     * @param result
     * @return
     */


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,@RequestBody BmsSettlementCatParam bmsSettlementCatParam,BindingResult result){
        int count = bmsSettlementCatService.update(id, bmsSettlementCatParam);
        if (count > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    /**
     *描述:模糊查询结算类别、且分页
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */

    @RequestMapping(value = "/select",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<BmsSettlementCatResult>> list(BmsSettlementCatParam queryParam,
                                                                 @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                                 @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<BmsSettlementCatResult> list = bmsSettlementCatService.select(queryParam);
        Long pageTotal = page.getTotal();
        return CommonResult.success(CommonPage.restPage(list,pageTotal));
    }


    @RequestMapping(value = "/selectAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<BmsSettlementCatResult>> listAll(){
        List<BmsSettlementCatResult> list = bmsSettlementCatService.selectAll();
        return CommonResult.success(list);
    }





}
