package com.wzw.his.api.controller.sms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wzw.his.common.api.CommonPage;
import com.wzw.his.common.api.CommonResult;
import com.wzw.his.common.dto.sms.SmsRegistrationRankParam;
import com.wzw.his.common.dto.sms.SmsRegistrationRankResult;
import com.wzw.his.sms.SmsRegistrationRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/registerRank")
public class SmsRegistrationRankController {
    @Autowired
    private SmsRegistrationRankService smsRegistrationRankService;

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody SmsRegistrationRankParam smsRegistrationRankParam, BindingResult result){
        int count = smsRegistrationRankService.create(smsRegistrationRankParam);
        if (count > 0){
            return CommonResult.success(count,"添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    /**
     * 描述:删除
     * @param ids
     * @return
     */
    @RequestMapping(value ="/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = smsRegistrationRankService.delete(ids);
        if (count > 0){
            return CommonResult.success(count,"删除成功");
        }
        return CommonResult.failed("删除失败");
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable("id")Long id,@RequestBody SmsRegistrationRankParam smsRegistrationRankParam,BindingResult result){
        int count = smsRegistrationRankService.update(id, smsRegistrationRankParam);
        if (count > 0){
            return CommonResult.success(count,"更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    /**
     * 描述:模糊查询挂号级别、且分页
     * @param queryParam
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/select",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<SmsRegistrationRankResult>> list(@RequestBody SmsRegistrationRankParam queryParam,
                                                                    @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
                                                                    @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum){
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<SmsRegistrationRankResult> list = smsRegistrationRankService.select(queryParam);
        Long pageTotal = page.getTotal();
        return  CommonResult.success(CommonPage.restPage(list,pageTotal));
    }

    @RequestMapping(value = "/selectAll",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsRegistrationRankResult>> listAll(){
        List<SmsRegistrationRankResult> list = smsRegistrationRankService.selectAll();
        return CommonResult.success(list);
    }

}
